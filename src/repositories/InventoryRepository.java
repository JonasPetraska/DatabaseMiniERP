package repositories;
import java.sql.*;
import database.Database;
import java.util.*;
import app.*;

public class InventoryRepository implements IRepository
{
	private Connection _connection;
	private HashMap<String, PreparedStatement> _preparedStatements = new HashMap<String, PreparedStatement>();
	
	public InventoryRepository(Database database) throws SQLException
	{
		_connection = database.getConnection();
		InitializePreparedStatements();
	}
	
	private void InitializePreparedStatements() throws SQLException
	{
		_preparedStatements.put(AppConfig.PreparedStatementInsertItem, 
				_connection.prepareStatement("INSERT INTO jope4327.Preke VALUES (DEFAULT, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS));
	}
	
	public ResultSet getItems() throws SQLException
	{
        Statement stmt = _connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM jope4327.Preke;");
        if (result.isBeforeFirst())
            return result;
        else
            return null;
	}
	
	public void insertItem(String name, String type, String description, double price) throws SQLException
	{
		PreparedStatement stmt = _preparedStatements.get(AppConfig.PreparedStatementInsertItem);
		stmt.setString(1, name);
		stmt.setString(2, type);
		stmt.setString(3, description);
		stmt.setDouble(4, price);
		stmt.executeUpdate();
	}
	
    public void closePreparedStatements() throws SQLException
    {    
        for(PreparedStatement stmt : _preparedStatements.values()) 
        	stmt.close();
    }
}
