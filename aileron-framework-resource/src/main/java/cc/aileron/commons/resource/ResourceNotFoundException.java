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
 */
package cc.aileron.commons.resource;

/**
 * @author Aileron
 * 
 */
public class ResourceNotFoundException extends Exception
{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public ResourceNotFoundException()
    {
    }

    /**
     * @param path
     */
    public ResourceNotFoundException(final String path)
    {
        super("resource not found [" + path + "]");
    }

    /**
     * @param path
     * @param cause
     */
    public ResourceNotFoundException(final String path, final Throwable cause)
    {
        super("resource not found [" + path + "]", cause);
    }

    /**
     * @param cause
     */
    public ResourceNotFoundException(final Throwable cause)
    {
        super(cause);
    }

}
