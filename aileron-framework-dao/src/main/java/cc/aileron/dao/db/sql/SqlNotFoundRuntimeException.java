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
package cc.aileron.dao.db.sql;

/**
 * 
 * 操作する SQL が 存在しない時の例外
 * 
 * @author Aileron
 * 
 */
public class SqlNotFoundRuntimeException extends RuntimeException
{
    private static final long serialVersionUID = -5268896664046526941L;

    /**
     * 
     */
    public SqlNotFoundRuntimeException()
    {
    }

    /**
     * @param message
     */
    public SqlNotFoundRuntimeException(final String message)
    {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public SqlNotFoundRuntimeException(
        final String message,
        final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public SqlNotFoundRuntimeException(final Throwable cause)
    {
        super(cause);
    }

}
