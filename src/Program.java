import java.util.Base64;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import database.Database;
import app.*;

public class Program 
{
	public static void main(String[] args) 
	{
	    String url = "";
	    String username = "";
	    String password = "";
		
		// SSH into MIF servers
		int lPort = 1231;
		int rPort = 5432;
		String host = "uosis.mif.vu.lt";
		String rHost = "pgsql3.mif";
		
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		try 
		{
			JSch jsch =  new JSch();
			Session session = jsch.getSession(username, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected to SSH");
			//session.delPortForwardingL(lPort);
			int assignedPort = session.setPortForwardingL(lPort, rHost, rPort);
			System.out.println("localhost:" + assignedPort + " -> " + rHost + ":" + rPort);
			Database db = new Database(url, username, password);
			UI ui = new UI(db);
			session.disconnect();
			db.closeConnection();
		}
		catch(Exception ex) 
		{
			System.out.println(ex.getMessage());
			System.exit(1);
		}
	}
	
}
