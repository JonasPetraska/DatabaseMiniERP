package repositories;
import java.sql.*;
import database.Database;
import java.util.*;
import java.sql.Date;

import app.*;

public class OrderRepository implements IRepository
{
	private Connection _connection;
	private HashMap<String, PreparedStatement> _preparedStatements = new HashMap<String, PreparedStatement>();
	
	public OrderRepository(Database database) throws SQLException
	{
		_connection = database.getConnection();
		InitializePreparedStatements();
	}
	
	private void InitializePreparedStatements() throws SQLException
	{
		_preparedStatements.put(AppConfig.PreparedStatementInsertOrder,
				_connection.prepareStatement("INSERT INTO jope4327.PirkimoUzsakymas VALUES(DEFAULT, ?)", Statement.RETURN_GENERATED_KEYS));
		_preparedStatements.put(AppConfig.PreparedStatementInsertCreatedOrder, 
				_connection.prepareStatement("INSERT INTO jope4327.PirkimoUzsakymasSukurtas VALUES (?, ?)"));
		_preparedStatements.put(AppConfig.PreparedStatementPayOrder, 
				_connection.prepareStatement("INSERT INTO jope4327.PirkimoUzsakymasApmoketas VALUES (?, ?)"));
		_preparedStatements.put(AppConfig.PreparedStatementDeliverOrder, 
				_connection.prepareStatement("INSERT INTO jope4327.PirkimoUzsakymasPristatytas VALUES (DEFAULT, ?, ?)"));
	}
	
	public ResultSet getOrders() throws SQLException
	{
		//Statement stmtRefresh = _connection.createStatement();
		//stmtRefresh.execute("REFRESH MATERIALIZED VIEW jope4327.VisiPirkimoUzsakymai;");
        
		Statement stmt = _connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM jope4327.VisiPirkimoUzsakymai;");
        if (result.isBeforeFirst())
            return result;
        else
            return null;
	}
	
	public void insertOrder(String date, int client) throws SQLException
	{
		PreparedStatement stmt1 = _preparedStatements.get(AppConfig.PreparedStatementInsertOrder);
		stmt1.setInt(1, client);
		_connection.setAutoCommit(false);
		
		stmt1.executeUpdate();
		
		ResultSet rs = stmt1.getGeneratedKeys();
		
		if(rs.next()) 
		{
			try 
			{
				int id = rs.getInt(1);
				
				PreparedStatement stmt2 = _preparedStatements.get(AppConfig.PreparedStatementInsertCreatedOrder);
				stmt2.setDate(1, Date.valueOf(date));
				stmt2.setInt(2, id);
				stmt2.executeUpdate();
				_connection.commit();
			}
			catch(SQLException sqle) 
			{
				_connection.rollback();
				throw sqle;
			}
		}else {
			_connection.rollback();
			throw new SQLException("Klaida kuriant pirkimo uzsakyma.");
		}
	}
	
	public void payOr(String date, int client) throws SQLException
	{
		PreparedStatement stmt1 = _preparedStatements.get(AppConfig.PreparedStatementInsertOrder);
		stmt1.setInt(1, client);
		_connection.setAutoCommit(false);
		
		stmt1.executeUpdate();
		
		ResultSet rs = stmt1.getGeneratedKeys();
		
		if(rs.next()) 
		{
			try 
			{
				int id = rs.getInt(1);
				
				PreparedStatement stmt2 = _preparedStatements.get(AppConfig.PreparedStatementInsertCreatedOrder);
				stmt2.setDate(1, Date.valueOf(date));
				stmt2.setInt(2, id);
				stmt2.executeUpdate();
				_connection.commit();
			}
			catch(SQLException sqle) 
			{
				_connection.rollback();
				throw sqle;
			}
		}else {
			_connection.rollback();
			throw new SQLException("Klaida kuriant pirkimo uzsakyma.");
		}
	}
	
	
    public void closePreparedStatements() throws SQLException
    {    
        for(PreparedStatement stmt : _preparedStatements.values()) 
        	stmt.close();
    }
}
