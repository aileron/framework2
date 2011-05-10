/**
 * Copyright (C) 2009 aileron.cc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.aileron.wsgi.request;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import cc.aileron.generic.util.Cast;
import cc.aileron.generic.util.SkipList;
import cc.aileron.workflow.container.WorkflowRequestParameter;
import cc.aileron.workflow.environment.WorkflowEnvironment;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * 実装
 * 
 * @author Aileron
 */
@Singleton
public class WsgiRequestParameterFactoryImpl implements
        WsgiRequestParameterFactory
{
    /*
     * (非 Javadoc)
     * 
     * @see
     * cc.aileron.wsgi.request.WsgiRequestParameterFactory#create(javax.servlet
     * .http.HttpServletRequest)
     */
    @Override
    public WorkflowRequestParameter create(final HttpServletRequest request)
            throws FileUploadException
    {
        try
        {
            request.setCharacterEncoding(characterEncodingName);
        }
        catch (final UnsupportedEncodingException e)
        {
        }
        if (ServletFileUpload.isMultipartContent(request))
        {
            final DiskFileItemFactory factory = new DiskFileItemFactory();
            final ServletFileUpload upload = new ServletFileUpload(factory);
            factory.setSizeThreshold(1024);
            upload.setSizeMax(-1);
            upload.setHeaderEncoding(characterEncodingName);
            return new RequestMultipart(characterEncoding, upload, request);
        }
        return new RequestUrlencoded(request);
    }

    /**
     * @param environment
     */
    @Inject
    public WsgiRequestParameterFactoryImpl(final WorkflowEnvironment environment)
    {
        this.characterEncoding = environment.getEncode();
        this.characterEncodingName = environment.getEncode().name();
    }

    /**
     * characterEncoding
     */
    private final Charset characterEncoding;

    /**
     * characterEncodingName
     */
    private final String characterEncodingName;
}

class RequestMultipart implements WorkflowRequestParameter
{
    private static Charset rawEncoding = Charset.forName("iso-8859-1");

    @Override
    public Object get(final String name)
    {
        return map.get(name);
    }

    @Override
    public Set<String> getKeys()
    {
        return keyset;
    }

    @Override
    public Iterator<Entry<String, Object>> iterator()
    {
        return map.entrySet().iterator();
    }

    @Override
    public String toString()
    {
        return new ReflectionToStringBuilder(this).toString();
    }

    /**
     * @param request
     * @param conv
     * @param upload
     * @return map
     * @throws FileUploadException
     */
    private HashMap<String, Object> initMap(final Charset characterEncoding,
            final HttpServletRequest request, final ServletFileUpload upload)
            throws FileUploadException
    {
        final List<FileItem> params = Cast.<List<FileItem>> cast(upload.parseRequest(request));
        final HashMap<String, Object> result = new HashMap<String, Object>();
        final HashMap<String, List<Object>> tmp = new HashMap<String, List<Object>>();
        for (final FileItem item : params)
        {
            final String key = item.getFieldName();
            final List<Object> vals;
            {
                List<Object> t = tmp.get(key);
                if (t == null)
                {
                    t = new SkipList<Object>();
                    tmp.put(key, t);
                }
                vals = t;
            }
            if (item.isFormField())
            {
                vals.add(new String(item.getString().getBytes(rawEncoding),
                        characterEncoding));
            }
            else
            {
                vals.add(item);
            }
        }
        for (final Entry<String, List<Object>> e : tmp.entrySet())
        {
            final String key = e.getKey();
            final List<Object> vals = e.getValue();
            result.put(key, vals.size() == 1 ? vals.get(0) : vals);
        }
        return result;
    }

    /**
     * constractor
     * @param characterEncoding 
     * 
     * @param upload
     * @param request
     * @throws FileUploadException
     */
    public RequestMultipart(final Charset characterEncoding,
            final ServletFileUpload upload, final HttpServletRequest request)
            throws FileUploadException
    {
        this.map = initMap(characterEncoding, request, upload);
        this.keyset = map.keySet();
    }

    private final Set<String> keyset;

    private final HashMap<String, Object> map;

}

/**
 * 
 * application/x-www-form-urlencoded 形式の Formパラメータ
 * 
 * @author Aileron
 * 
 */
class RequestUrlencoded implements WorkflowRequestParameter
{
    @Override
    public Object get(final String key)
    {
        return map.get(key);
    }

    @Override
    public Set<String> getKeys()
    {
        return map.keySet();
    }

    @Override
    public Iterator<Entry<String, Object>> iterator()
    {
        return map.entrySet().iterator();
    }

    @Override
    public String toString()
    {
        return new ReflectionToStringBuilder(this).toString();
    }

    private Map<String, Object> initMap(final HttpServletRequest request)
    {
        final ArrayList<String> keys = new ArrayList<String>();
        final Enumeration<?> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements())
        {
            keys.add(enumeration.nextElement().toString());
        }
        final HashMap<String, Object> map = new HashMap<String, Object>();
        for (final String key : keys)
        {
            final String[] vals = request.getParameterValues(key);
            final Object value;
            if (vals == null)
            {
                value = null;
            }
            else
            {
                value = vals.length == 1 ? vals[0] : new SkipList<String>(vals);
            }
            map.put(key, value);
        }
        return map;
    }

    /**
     * constractor
     * 
     * @param request
     */
    public RequestUrlencoded(final HttpServletRequest request)
    {
        this.map = initMap(request);
    }

    /**
     * map
     */
    private final Map<String, Object> map;
}