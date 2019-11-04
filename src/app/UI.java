package app;
import database.Database;
import java.sql.*;
import java.util.Scanner;
import java.sql.*;
import repositories.*;

public class UI 
{
	private final Database _database;
	private final ClientRepository _clients;
	private final InventoryRepository _items;
	private final OrderRepository _orders;
	private final InvoiceRepository _invoices;
	
	public UI(Database database) throws SQLException
	{
		_database = database;
		_clients = new ClientRepository(_database);
		_items = new InventoryRepository(_database);
		_orders = new OrderRepository(_database);
		_invoices = new InvoiceRepository(_database);
		initialize();
	}
	
	public void initialize() throws SQLException
	{
		int option = -1;

		Scanner sin = new Scanner(System.in);
        sin.useDelimiter("\n");

        ResultSet result = null;
        int client = 0;
        while (option != 0) 
        {
        	showMenu();
        	try 
            {
        		System.out.print(">");
        		option = sin.nextInt();
                switch (option) 
                {
                	case 1:
                        System.out.print("El. pasto adresas: ");
                        String email = sin.next();
                        
                        System.out.print("Mobilusis tel.nr.: ");
                        String mobileNumber = sin.next();
                		
                		System.out.print("Fizinis ar juridinis ?");
                		String tipas = sin.next();
                		
                		if(tipas.toLowerCase().equals("fizinis"))
                		{
                            System.out.print("Asmens kodas (11 skaitmenu): ");
                            String ak = sin.next();
                            
                            System.out.print("Vardas: ");
                            String firstName = sin.next();
                            
                            System.out.print("Pavarde: ");
                            String lastName = sin.next();
                            
                            _clients.insertClient(email, mobileNumber, ak, firstName, lastName);
                            System.out.println("Klientas (" + ak + ") sekmingai uzregistruotas.");
                		}
                		else if(tipas.toLowerCase().equals("juridinis")) 
                		{
                            System.out.print("Imones kodas (7-9 skaitmenys): ");
                            String companyNo = sin.next();
                            
                            System.out.print("Imones pavadinimas: ");
                            String companyName = sin.next();
                            
                            _clients.insertClient(email, mobileNumber, companyNo, companyName);
                            System.out.println("Klientas (" + companyNo + ") sekmingai uzregistruotas.");
                		}
                		else 
                		{
                			System.out.print("Neteisingai ivestas tipas.");
                			break;
                		}
                		break;
                	case 2:
                		result = _clients.getClients();
                		displayResultSet(result);
                		break;
                	case 3:
                        System.out.print("Asmens kodas: ");
                        String ak = sin.next();
                		result = _clients.getClientByAK(ak);
                		if(result != null)
                			displayResultSet(result);
                		else
                			System.out.println("Klientas su asmens kodu '" + ak + "' nerastas.");
                		break;
                	case 4:
                        System.out.print("Imones Nr.: ");
                        String companyNo = sin.next();
                		result = _clients.getClientByCN(companyNo);
                		if(result != null)
                			displayResultSet(result);
                		else
                			System.out.println("Klientas su imones numeriu '" + companyNo + "' nerastas.");
                		break;
                	case 5:
                        result = _clients.getClients();
                        if (result != null) {
                        	System.out.print("Galimi klientai: \n");
                            displayResultSet(result);
                        } else {
                            System.out.print("Klientu nera!");
                            break;
                        }
                        
                        System.out.print("Kliento Nr.: ");
                        client = sin.nextInt();
                        
                        System.out.print("Salis: ");
                        String country = sin.next();
                        
                        System.out.print("Miestas: ");
                        String city = sin.next();

                        System.out.print("Gatve: ");
                        String street = sin.next();                     
                        
                        System.out.print("Namo Nr.: ");
                        int streetNo = sin.nextInt();   
                        
                        System.out.print("Buto Nr.: ");
                        int flatNo = sin.nextInt();
                        
                        _clients.insertAddressForClient(country, city, flatNo, street, streetNo, client);
                        System.out.println("Adresas klientui (" + client + ") sekmingai pridetas.");
                        
                		break;
                	case 6:
                		result = _clients.getClientAddresses();
                		displayResultSet(result);
                		break;
                	case 7:
                        result = _clients.getClients();
                        if (result != null) {
                        	System.out.print("Galimi klientai: \n");
                            displayResultSet(result);
                        } else {
                            System.out.print("Klientu nera!");
                            break;
                        }
                        
                        System.out.print("Kliento Nr.: ");
                        client = sin.nextInt();
                        
                        System.out.print("Naujas telefono numeris: ");
                        String mobNo = sin.next();
                        
                        if(mobNo.isEmpty()) 
                        {
                        	System.out.println("Iveskite telefono numeri.");
                        	break;
                        }
                        
                        if(client <= 0)
                        {
                        	System.out.println("Kliento numeris turi buti >= 0.");
                        }
                        else 
                        {
                        	_clients.updateClientsMobileNumber(mobNo, client);
                        	System.out.println("Telefono numeris klientui (" + client + ") sekmingai pakeistas.");
                        }
                        break;
                	case 8:
                        result = _clients.getClients();
                        if (result != null) {
                        	System.out.print("Galimi klientai: \n");
                            displayResultSet(result);
                        } else {
                            System.out.print("Klientu nera!");
                            break;
                        }
                        
                        System.out.print("Kliento Nr.: ");
                        client = sin.nextInt();
                        
                        System.out.print("Naujas el.pasto adresas: ");
                        String emailAddress = sin.next();
                        
                        if(emailAddress.isEmpty()) 
                        {
                        	System.out.println("Iveskite el. pasto adresa.");
                        	break;
                        }
                        
                        if(client <= 0)
                        {
                        	System.out.println("Kliento numeris turi buti >= 0.");
                        }
                        else 
                        {
                        	_clients.updateClientsEmail(emailAddress, client);
                        	System.out.println("El.pasto adresas klientui (" + client + ") sekmingai pakeistas.");
                        }
                        break;
                	case 9:
                        result = _clients.getClients();
                        if (result != null) {
                        	System.out.print("Galimi klientai: \n");
                            displayResultSet(result);
                        } else {
                            System.out.print("Klientu nera!");
                            break;
                        }
                        
                        System.out.print("Kliento nr.: ");
                        client = sin.nextInt();
                		_clients.deleteClient(client);
                		System.out.println("Klientas (" + client + ") sekmingai isregistruotas.");
                		break;
                	case 10:
                		result = _items.getItems();
                		displayResultSet(result);
                		break;
                	case 11:
                        System.out.print("Pavadinimas: ");
                        String name = sin.next();
                        
                        System.out.print("Tipas (Maisto, Kosmetikos, Statybine, Elektronikos, Higienos): ");
                        String type = sin.next();
                		
                		System.out.print("Aprasymas: ");
                		String description = sin.next();
                		
                        System.out.print("Kaina: ");
                        double price = sin.nextDouble();
   
                        _items.insertItem(name, type, description, price);
                        System.out.println("Preke (" + name + ") sekmingai prideta.");
                		break;
                	/*case 11:
                        result = _clients.getClients();
                        if (result != null) {
                        	System.out.print("Galimi klientai: \n");
                            displayResultSet(result);
                        } else {
                            System.out.print("Klientu nera!");
                            break;
                        }
                        
                        System.out.print("Kliento nr.: ");
                        client = sin.nextInt();
                        
                        _invoices.insertInvoice(client);
                        System.out.println("Saskaita faktura klientui (" + client + ") sekmingai uzregistruotas.");
                        
                		break;*/
                	case 12:
                        result = _clients.getClients();
                        if (result != null) {
                        	System.out.print("Galimi klientai: \n");
                            displayResultSet(result);
                        } else {
                            System.out.print("Klientu nera!");
                            break;
                        }
                        
                        System.out.print("Kliento nr.: ");
                        client = sin.nextInt();
                        
                        System.out.print("Data: ");
                        String date = sin.next();
                		
                        try {
                            Date dateCheck = Date.valueOf(date);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Data neteisingo formato!");
                            break;
                        }
                        
                        _orders.insertOrder(date, client);
                        System.out.println("Pirkimo uzsakymas klientui (" + client + ") sekmingai uzregistruotas.");
                		break;
                	case 13:
                		result = _orders.getOrders();
                		displayResultSet(result);
                		break;
                    case 0:
                        break;
                    default:
                        System.out.println("Tokio pasirinkimo nera!");
                        break;
                }
            }
        	catch (java.util.InputMismatchException ine) 
        	{
            	System.out.println("Klaidingai ivesti duomenys arba pasirinkimas. Meginkite is naujo!");
            	sin.next();
            	sin.reset();
            }
        	catch(SQLException ex) 
        	{
        		final String message = ex.getMessage();
        		if(message.contains("tinkamasimonesnr")) 
        		{
        			System.out.println("Imones kodas per trumpas.");
        		}
        		else if(message.contains("tinkamasasmenskodas")) 
        		{
        			System.out.println("Asmens kodas per trumpas.");
        		}else {
        			System.out.println(message);
        		}
            	sin.next();
            	sin.reset();
        	}
        	catch(Exception ex) 
        	{
            	System.out.println(ex.getMessage());
            	sin.next();
            	sin.reset();
        	}
            pause(sin);
        }
        sin.close();	
		_clients.closePreparedStatements();
		_orders.closePreparedStatements();
		_items.closePreparedStatements();
		_invoices.closePreparedStatements();
	}
	
    public void displayResultSet (ResultSet rs) throws SQLException 
    {
        if (rs != null) 
        {
            ResultSetMetaData md = rs.getMetaData ( );
            int ncols = md.getColumnCount ( );
            int nrows = 0;
            
            System.out.print ("|");
            for(int i = 1; i <= ncols; i++)
            	System.out.print(md.getColumnLabel(i) == null ? md.getColumnName(i) : md.getColumnLabel(i) + "|");
            System.out.println();
            
            while (rs.next())
            {
                ++nrows;
                System.out.print ("|");
                for (int i = 1; i <= ncols; i++)
                {
                    String s = rs.getString(i);
                    if (rs.wasNull())
                        s = "Nera";
                    System.out.print (s);
                    System.out.print("|");
                }
                System.out.println();
            }
            
            System.out.println (nrows + " irasai");
        } else {
            throw new SQLException("Tokiu duomenu nera!");
        }
    }
    
    private void showMenu() 
    {
        System.out.println("--Klientas--");
        System.out.println("1. Uzregistruoti klienta");
        System.out.println("2. Klientu sarasa");
        System.out.println("3. Ieskoti kliento pagal asmens koda");
        System.out.println("4. Ieskoti kliento pagal imones koda");
        System.out.println("5. Pridenti klientui adresa");
        System.out.println("6. Klientu adresu sarasas");
        System.out.println("7. Keisti kliento telefono numeri");
        System.out.println("8. Keisti kliento el.pasta");
        System.out.println("9. Isregistruoti klienta");
        
        System.out.println("--Preke--");
        System.out.println("10. Prekiu sarasas");
        System.out.println("11. Prideti preke");
        
        //System.out.println("--Saskaita faktura--");
        //System.out.println("11. Israsyti saskaita faktura");
        
        System.out.println("--Pirkimo uzsakymas--");
        System.out.println("12. Sukurti pirkimo uzsakyma");
        System.out.println("13. Pirkimo uzsakymu sarasas");
        //System.out.println("14. Apmoketi pirkimo uzsakyma");
        //System.out.println("15. Pristatyti pirkimo uzsakyma");
        
        //System.out.println("--Pirkimo uzsakymo eilute--");
        //System.out.println("16. Prideti pirkimo uzsakymo eilute prie pirkimo uzsakymo");   
        
        System.out.println("--Kita--");
        System.out.println("0. Baigti darba");
    }
    
    private void pause(Scanner sin)
    {
        sin.nextLine();
        System.out.println("Paspauskite bet koki mygtuka testi darbui...");
        sin.nextLine();
        sin.reset();
    }
}
