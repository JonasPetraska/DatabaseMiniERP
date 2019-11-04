package repositories;

import java.sql.*;
import database.Database;
import java.util.*;
import app.*;

public class ClientRepository implements IRepository
{

	private Connection _connection;
	private HashMap<String, PreparedStatement> _preparedStatements = new HashMap<String, PreparedStatement>();
	
	public ClientRepository(Database database) throws SQLException
	{
		_connection = database.getConnection();
		InitializePreparedStatements();
	}
	
	private void InitializePreparedStatements() throws SQLException
	{
		_preparedStatements.put(AppConfig.PreparedStatementInsertClient, 
				_connection.prepareStatement("INSERT INTO jope4327.Klientas VALUES (DEFAULT, ?, ?)", Statement.RETURN_GENERATED_KEYS));
		_preparedStatements.put(AppConfig.PreparedStatementInsertCompanyClient, 
				_connection.prepareStatement("INSERT INTO jope4327.JuridinisKlientas VALUES (?, ?, ?)"));
		_preparedStatements.put(AppConfig.PreparedStatementInsertPhysicalClient, 
				_connection.prepareStatement("INSERT INTO jope4327.FizinisKlientas VALUES (?, ?, ?, ?)"));
		_preparedStatements.put(AppConfig.PreparedStatementDeleteClient, 
				_connection.prepareStatement("DELETE FROM jope4327.Klientas WHERE Nr = ?"));
		
		_preparedStatements.put(AppConfig.PreparedStatementGetClientByAK, 
				_connection.prepareStatement("SELECT Asmens_Kodas AS AK, Vardas, Pavarde "
											+ "FROM jope4327.FizinisKlientas "
											+ "WHERE Asmens_Kodas = ? "));
		
		_preparedStatements.put(AppConfig.PreparedStatementGetClientByCN, 
				_connection.prepareStatement("SELECT Imones_Nr, Imones_Pavadinimas "
											+ "FROM jope4327.JuridinisKlientas "
											+ "WHERE Imones_Nr = ? "));
		
		_preparedStatements.put(AppConfig.PreparedStatementInsertAddressForClient, 
				_connection.prepareStatement("INSERT INTO jope4327.Adresas VALUES (?, ?, ?, ?, ?, ?)"));
		
		_preparedStatements.put(AppConfig.PreparedStatementUpdateClientsMobileNumber, 
				_connection.prepareStatement("UPDATE jope4327.Klientas SET Tel_Nr = ? WHERE Nr = ?"));
		
		_preparedStatements.put(AppConfig.PreparedStatementUpdateClientsEmail, 
				_connection.prepareStatement("UPDATE jope4327.Klientas SET ElPasto_Adresas = ? WHERE Nr = ?"));
	}
	
	public ResultSet getClients() throws SQLException
	{
        Statement stmt = _connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM jope4327.VisiKlientai;");
        if (result.isBeforeFirst())
            return result;
        else
            return null;
	}
	
	public ResultSet getCompanyClients() throws SQLException
	{
        Statement stmt = _connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM jope4327.JuridinisKlientas;");
        if (result.isBeforeFirst())
            return result;
        else 
            return null;
	}
	
	public ResultSet getPhysicalClients() throws SQLException
	{
        Statement stmt = _connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM jope4327.FizinisKlientas;");
        if (result.isBeforeFirst()) 
            return result;
        else
            return null;
	}
	
	public ResultSet getClientByAK(String ak) throws SQLException
	{
		PreparedStatement stmt = _preparedStatements.get(AppConfig.PreparedStatementGetClientByAK);
		stmt.setString(1, ak);
		ResultSet result = stmt.executeQuery();
		if(result.isBeforeFirst())
			return result;
		else
			return null;
	}
	
	public ResultSet getClientByCN(String cn) throws SQLException
	{
		PreparedStatement stmt = _preparedStatements.get(AppConfig.PreparedStatementGetClientByCN);
		stmt.setString(1, cn);
		ResultSet result = stmt.executeQuery();
		if(result.isBeforeFirst())
			return result;
		else
			return null;
	}
	
	public void insertClient(String email, String mobileNumber, String companyNumber, String companyName) throws SQLException
	{
		PreparedStatement stmt1 = _preparedStatements.get(AppConfig.PreparedStatementInsertClient);
		stmt1.setString(1, email);
		stmt1.setString(2, mobileNumber);
		_connection.setAutoCommit(false);
		
		stmt1.executeUpdate();
		
		ResultSet rs = stmt1.getGeneratedKeys();
		
		if(rs.next())
		{
			try
			{
				int id = rs.getInt(1);
				
				PreparedStatement stmt2 = _preparedStatements.get(AppConfig.PreparedStatementInsertCompanyClient);
				stmt2.setString(1, companyNumber);
				stmt2.setString(2, companyName);
				stmt2.setInt(3, id);
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
			throw new SQLException("Klaida pridedant klienta.");
		}
		_connection.setAutoCommit(false);
	}
	
	public void insertClient(String email, String mobileNumber, String ak, String firstName, String lastName) throws SQLException
	{
		PreparedStatement stmt1 = _preparedStatements.get(AppConfig.PreparedStatementInsertClient);
		stmt1.setString(1, email);
		stmt1.setString(2, mobileNumber);
		_connection.setAutoCommit(false);
		
		stmt1.executeUpdate();
		
		ResultSet rs = stmt1.getGeneratedKeys();
		
		if(rs.next())
		{
			try 
			{
				int id = rs.getInt(1);
				
				PreparedStatement stmt2 = _preparedStatements.get(AppConfig.PreparedStatementInsertPhysicalClient);
				stmt2.setString(1, ak);
				stmt2.setString(2, firstName);
				stmt2.setString(3, lastName);
				stmt2.setInt(4, id);
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
			throw new SQLException("Klaida uzregistruojant klienta.");
		}
		_connection.setAutoCommit(false);
	}
	
	public void deleteClient(int client) throws SQLException
	{
		PreparedStatement stmt = _preparedStatements.get(AppConfig.PreparedStatementDeleteClient);
		stmt.setInt(1, client);
		stmt.executeUpdate();	
	}
	
	public void insertAddressForClient(String country, String city, int flatNo, String street, int streetNo, int client) throws SQLException
	{
		PreparedStatement stmt1 = _preparedStatements.get(AppConfig.PreparedStatementInsertAddressForClient);
		stmt1.setString(1, country);
		stmt1.setString(2, city);
		stmt1.setInt(3, flatNo);
		stmt1.setString(4, street);
		stmt1.setInt(5, streetNo);
		stmt1.setInt(6, client);
		stmt1.executeUpdate();
	}
	
	public ResultSet getClientAddresses() throws SQLException 
	{
        Statement stmt = _connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM jope4327.Adresas;");
        if (result.isBeforeFirst()) {
            return result;
        } else {
            return null;
        }
	}
	
	public void updateClientsMobileNumber(String mobNo, int client) throws SQLException
	{
		PreparedStatement stmt1 = _preparedStatements.get(AppConfig.PreparedStatementUpdateClientsMobileNumber);
		stmt1.setString(1, mobNo);
		stmt1.setInt(2, client);
		stmt1.executeUpdate();
	}
	
	public void updateClientsEmail(String email, int client) throws SQLException
	{
		PreparedStatement stmt1 = _preparedStatements.get(AppConfig.PreparedStatementUpdateClientsEmail);
		stmt1.setString(1, email);
		stmt1.setInt(2, client);
		stmt1.executeUpdate();
	}
	
    public void closePreparedStatements() throws SQLException
    {    
        for(PreparedStatement stmt : _preparedStatements.values()) 
        	stmt.close();
    }
}
