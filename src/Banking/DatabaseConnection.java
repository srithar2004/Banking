package Banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	private static final String url="jdbc:mysql://localhost:3306/userdb";
	private static final String Name ="root";
	private static final String Password ="Srithar@2004";
	
	private static Connection connection =null;
	
	private DatabaseConnection() {};
	
	public static Connection getConnection() {
		
		if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, Name, Password);
                System.out.println("✅ Connected to MySQL Database");
            } catch (ClassNotFoundException e) {
                System.out.println("❌ MySQL Driver not found!");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("❌ Database Connection Failed!");
                e.printStackTrace();
            }
        }
        return connection;
	}
}
