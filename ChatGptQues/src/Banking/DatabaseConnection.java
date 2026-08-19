package Banking;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.io.IOException;
public class DatabaseConnection {
	public static String getPassword() {
		try {
			String password = Files.readString(Paths.get("Password.txt")).trim(); 
			return password;
			
		}
		catch(IOException e) {
			e.printStackTrace();
			return null;
		}		
	}
	private static final String url="jdbc:mysql://localhost:3306/userdb";
	private static final String Name ="root";
	private static final String Password = getPassword();
	
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
