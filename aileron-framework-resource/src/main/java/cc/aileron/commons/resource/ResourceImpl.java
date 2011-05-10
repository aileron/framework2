/**
 * Copyright (C) 2008 aileron.cc
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
 * 
 */
package cc.aileron.commons.resource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;

/**
 * classpath-resource
 * 
 * @author Aileron
 * 
 */
public class ResourceImpl implements Resource
{
    /**
     * @return byte[]
     * @throws IOException
     */
    @Override
    public byte[] toBytes() throws IOException
    {
        final BufferedInputStream in = new BufferedInputStream(content.getInputStream());
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final DataOutputStream os = new DataOutputStream(baos);

        byte[] buffer = new byte[8192];
        while (in.read(buffer) != -1)
        {
            os.write(buffer);
            buffer = new byte[8192];
        }

        return baos.toByteArray();
    }

    @Override
    public FileContent toFileContent()
    {
        return content;
    }

    @Override
    public FileObject toFileObject()
    {
        return this.content.getFile();
    }

    @Override
    public Properties toProperties() throws IOException
    {
        final Properties properties = new Properties();
        final InputStream stream = this.content.getInputStream();
        properties.load(stream);
        stream.close();

        return properties;
    }

    @Override
    public Properties toPropertiesNoneException()
    {
        try
        {
            return toProperties();
        }
        catch (final IOException e)
        {
            return new Properties();
        }
    }

    @Override
    public String toString()
    {
        try
        {
            final InputStreamReader ir = new InputStreamReader(content.getInputStream());
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(new BufferedWriter(sw));
            final BufferedReader br = new BufferedReader(ir);

            String line = br.readLine();
            while (line != null)
            {
                pw.println(line);
                line = br.readLine();
            }
            br.close();
            ir.close();
            pw.flush();
            pw.close();
            return sw.toString();
        }
        catch (final Exception e)
        {
            return "";
        }
    }

    @Override
    public URL toURL()
    {
        try
        {
            return content.getFile().getURL();
        }
        catch (final Exception e)
        {
            return null;
        }
    }

    /**
     * @param content
     * @throws ResourceNotFoundException
     */
    public ResourceImpl(final FileContent content)
            throws ResourceNotFoundException
    {
        this.content = content;
        this.type = null;
    }

    /**
     * @param content
     * @param type
     */
    public ResourceImpl(final FileContent content,
            final ResourceLoaders.Type type)
    {
        this.content = content;
        this.type = type;
    }

    /**
     * type
     */
    public final ResourceLoaders.Type type;

    /**
     * resource
     */
    private final FileContent content;
}
