package repositories;
import java.sql.*;

public interface IRepository 
{
	void closePreparedStatements() throws SQLException;
}
