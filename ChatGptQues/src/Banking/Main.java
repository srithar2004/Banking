package Banking;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

//import com.sun.jdi.connect.spi.Connection;
//
//import java.sql.*;
public class Main {
	
	
	static BankAccount  bankaccount=new BankAccount();
	static Bank bank = new Bank();
	public static void main(String[] args) throws SQLException{
		final Scanner s = new Scanner(System.in);
		
		Connection con = DatabaseConnection.getConnection();
		
		
		
		String query="";
		while(true) {
			System.out.println("1. Create the Account \n2. Deposit Money \n3. Withdraw Money \n4. Transfer Money \n5. Show all Accounts \n6. Show Balance \n8. Show Account Details \n9. Exit");
			System.out.println("Enter the Choice(1-4): ");
			int choice=s.nextInt();
			
			switch(choice) {
				case 1:
					long AccountNumber = RandomNumber();
					System.out.println(AccountNumber);
					
					System.out.println("Enter the HolderName (Full Name):");
					s.nextLine();
					
					String AccountHolderName =s.nextLine();
					long balance=0;
					
					System.out.println("Enter the Account Type: ");
					String AccountType=s.nextLine().toLowerCase();
					
					bankaccount.CreateAccount(AccountNumber,AccountHolderName,AccountType);
					query = "Insert into Banking values(?,?,?,?)";
					PreparedStatement ps = con.prepareStatement(query);
					
					ps.setString(1,AccountHolderName);
					ps.setLong(2,AccountNumber);
					ps.setString(3,AccountType);
					ps.setLong(4,0);
					
					ps.executeUpdate();
					break;
				
				case 2:
					System.out.println("Enter the Account Number: ");
					long accno = s.nextLong();
					
					System.out.println("Enter the Amount to Deposit: ");
					long amount=s.nextInt();
					
					System.out.println(bankaccount.deposit(accno,amount));
					
					break;
					
				case 3:
					System.out.println("Enter the Account Number: ");
					long accnumber =s.nextLong();
					System.out.println("Enter the Amount to Withdraw: ");
					long take = s.nextInt();
					
					System.out.println(bankaccount.withdraw(accnumber,take));
					
					
					break;
					
				case 4:
					System.out.println("Enter the Account Number: ");
					long AccNum=s.nextLong();
					
					System.out.println("Enter the Receiver Account Number: ");
					long AccRec=s.nextLong();
					
					System.out.println("Enter the Amount to Transfer: ");
					long amt=s.nextInt();
					System.out.println(bank.TransferAmount(AccNum,AccRec,amt));
					break;
					
				case 6:
					System.out.println("Enter the Account Number: ");
					long acc=s.nextLong();
					
					bank.SearchAccNo(acc);
							
					break;
				
				case 8:
					System.out.println("Enter the Account Number: ");
					long val =s.nextLong();
					System.out.println(bank.DisplayAccDetails(val));
					break;
				default:
					System.out.println("Please Enter the Correct Choice!");
					
			}
		}
	}
	
	private static long RandomNumber() {
		Random rand = new Random();
		return 1000000000L + (long)(rand.nextDouble() * 9000000000L);
	}
	
	
}