/**
 * 
 */
package cc.aileron.dao;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * @author aileron
 */
public class G2DaoDataSource implements DataSource
{
    /**
     * @param driverClassName
     * @throws ClassNotFoundException 
     */
    public void driverClassName(final String driverClassName)
            throws ClassNotFoundException
    {
        Class.forName(driverClassName);
    }

    @Override
    public Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public Connection getConnection(final String arg0, final String arg1)
            throws SQLException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLoginTimeout() throws SQLException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWrapperFor(final Class<?> arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }

    /**
     * @param password
     */
    public void password(final String password)
    {
        this.password = password;

    }

    @Override
    public void setLoginTimeout(final int logintTimeout) throws SQLException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLogWriter(final PrintWriter logwriter) throws SQLException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(final Class<T> arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }

    /**
     * @param url
     */
    public void url(final String url)
    {
        this.url = url;

    }

    /**
     * @param username
     */
    public void username(final String username)
    {
        this.username = username;

    }

    private String password;

    private String url;

    private String username;
}
