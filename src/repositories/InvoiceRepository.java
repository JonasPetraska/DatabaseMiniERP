package repositories;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import app.AppConfig;
import database.Database;

public class InvoiceRepository implements IRepository
{
	private Connection _connection;
	private HashMap<String, PreparedStatement> _preparedStatements = new HashMap<String, PreparedStatement>();
	
	public InvoiceRepository(Database database) throws SQLException
	{
		_connection = database.getConnection();
		InitializePreparedStatements();
	}
	
	private void InitializePreparedStatements() throws SQLException
	{
		_preparedStatements.put(AppConfig.PreparedStatementInsertInvoice, 
				_connection.prepareStatement("INSERT INTO jope4327.SaskaitaFaktura VALUES (DEFAULT, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS));
	}
	
	public ResultSet getInvoices() throws SQLException
	{
        
		Statement stmt = _connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM jope4327.SaskaitaFaktura;");
        if (result.isBeforeFirst())
            return result;
        else
            return null;
	}
	
	public void insertInvoice(int client) throws SQLException
	{
		PreparedStatement stmt1 = _preparedStatements.get(AppConfig.PreparedStatementInsertOrder);
		stmt1.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
		stmt1.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
		stmt1.setInt(3, client);
		stmt1.executeUpdate();
		
	}
	
    public void closePreparedStatements() throws SQLException
    {    
        for(PreparedStatement stmt : _preparedStatements.values()) 
        	stmt.close();
    }
}
