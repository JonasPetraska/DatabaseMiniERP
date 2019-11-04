package app;

public final class AppConfig 
{
	 public static String PreparedStatementInsertClient = "InsertClient";
	 public static String PreparedStatementInsertPhysicalClient = "InsertPhysicalClient";
	 public static String PreparedStatementInsertCompanyClient = "InsertCompany";
	 public static String PreparedStatementGetClientByAK = "GetClientByAK";
	 public static String PreparedStatementGetClientByCN = "GetClientByCompanyNumber";
	 public static String PreparedStatementInsertAddressForClient = "InsertAddressForClient";
	 public static String PreparedStatementUpdateClientsMobileNumber = "UpdateClientsMobileNumber";
	 public static String PreparedStatementUpdateClientsEmail = "UpdateClientsEmail";
	 public static String PreparedStatementDeleteClient = "DeleteClient";
	 
	 public static String PreparedStatementInsertItem = "InsertItem";
	 
	 public static String PreparedStatementInsertOrder = "InsertOrder";
	 public static String PreparedStatementInsertCreatedOrder = "InsertCreatedOrder";
	 public static String PreparedStatementPayOrder = "PayOrder";
	 public static String PreparedStatementDeliverOrder = "DeliverOrder";
	 
	 public static String PreparedStatementInsertInvoice = "InsertInvoice";
}
