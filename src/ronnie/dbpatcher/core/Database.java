package ronnie.dbpatcher.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import com.logicacmg.idt.commons.SystemException;
import com.logicacmg.idt.commons.util.Assert;


/**
 * Manages connections to the database.
 *
 * @author Ren� M. de Bloois
 * @since Apr 1, 2006 7:16:37 PM
 */
public class Database
{
	protected String driverName;
	protected String url;
	protected HashMap< String, Connection > connections = new HashMap< String, Connection >();
	protected HashMap passwords = new HashMap();
	protected String currentUser;

	/**
	 * Constructs a Database object for a specific database url. This object will manage multiple connections to this database.
	 *
	 * @param driverClassName Classname of the driver.
	 * @param url Url of the database.
	 */
	public Database( String driverClassName, String url )
	{
		this.driverName = driverClassName;
		this.url = url;
		try
		{
			Class.forName( driverClassName );
		}
		catch( ClassNotFoundException e )
		{
			throw new SystemException( e );
		}
	}

	/**
	 * Gets a new connection from the DriverManager with the given url, user and password. The returned connection has autocommit set to on.
	 *
	 * @param url The database connection url.
	 * @param user The connection user.
	 * @param password The password of the user.
	 * @return the connection.
	 */
	protected Connection getConnection( String url, String user, String password )
	{
		try
		{
			Connection connection = DriverManager.getConnection( url, user, password );
			connection.setAutoCommit( true );
			return connection;
		}
		catch( SQLException e )
		{
			throw new SystemException( e );
		}
	}

	/**
	 * Gets a cached connection for the given user. If a connection for the given user is not found in the cache, this method will
	 * request a password by calling the method {@link ProgressListener#requestPassword(String)} of {@link Patcher#callBack}. The connection is cached for later use.
	 *
	 * @param user The user name.
	 * @return the connection
	 */
	protected Connection getConnection( String user )
	{
		Connection connection = this.connections.get( user );
		if( connection == null )
		{
			String password = Patcher.callBack.requestPassword( user );
			connection = getConnection( this.url, user, password );
			this.connections.put( user, connection );
			this.passwords.put( user, password );
		}
		return connection;
	}

	/**
	 * Gets a connection for the default user from the cache. If a connection for the default user is not found in the cache, this method will
	 * request a password by calling the method {@link ProgressListener#requestPassword(String)} of {@link Patcher#callBack}. The connection is cached for later use.
	 *
	 * @return the connection.
	 * @see #getConnection(String)
	 */
	protected Connection getConnection()
	{
		Assert.notEmpty( this.currentUser, "Current user must not be empty" );
		return getConnection( this.currentUser );
	}

	/**
	 * Sets the default user.
	 *
	 * @param user
	 */
	protected void setCurrentUser( String user )
	{
		Assert.notEmpty( user, "User must not be empty" );
		this.currentUser = user;
	}

	protected void closeConnections()
	{
		for( Connection connection : this.connections.values() )
			try
			{
				connection.close();
			}
			catch( SQLException e )
			{
				throw new SystemException( e );
			}

		this.connections.clear();
	}
}
