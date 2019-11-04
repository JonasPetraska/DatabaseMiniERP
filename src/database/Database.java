package database;
import java.sql.*;

public class Database {

	private Connection _connection;
	
	public Connection getConnection() 
	{
		return _connection;
	}
	
	public Database(String url, String username, String password) 
	{
        try {
            loadDriver();
            createConnection(url, username, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
	}
	
    private void loadDriver() throws Exception 
    {
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException cnfe) {
            throw new Exception("Unable to load the driver class!");
        }
    }

    private Connection createConnection(String url, String username, String password) throws Exception 
    {
        try {
            _connection = DriverManager.getConnection(url, username, password);
        }
        catch (SQLException sqle) {
            _connection = null;
            throw new Exception("Couldn’t get connection!");
        }
        return _connection;
    }
    
    public void closeConnection() throws SQLException 
    {
        _connection.close();
    }
    
    public boolean isConnected() 
    {
        if (_connection != null) {
            return true;
        }  
        return false;
    }
	
}
