package Banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankAccount {
	
	static Bank bank=new Bank();
	
	private int balance;
	
	private Connection con = DatabaseConnection.getConnection();
	
	public void CreateAccount(long AccountNumber,String AccountHolderName,String AccountType) {
		try {
		    
		    if(!AccountHolderName.matches("[a-zA-Z ]+")) {
		        throw new IllegalArgumentException("Invalid name! Only alphabets are allowed.");
		    }

		    
		    
		    if(!AccountType.equals("saving account") && !AccountType.equals("fixed deposit")) {
		        throw new IllegalArgumentException("Invalid Account Type! Must be 'saving account' or 'fixed deposit'.");
		    }

		    System.out.println("First Name : "+AccountHolderName+"\nAccount Type: "+AccountType);
		    System.out.println("Account Created Successfully!!");
		   

		} catch(IllegalArgumentException e) {
		    System.out.println("Error: "+e.getMessage());
		    System.out.println("Cannot Proceed Further!");
		}

	}
	
	public String deposit(long accno,long amount) throws SQLException {
//		if(!bank.AccDetails.containsKey(accno)) {
//			return "Account not Found!";
//			
//		}
		
		String query = "Update Banking set balance=balance+? where AccountNumber  = ?";
		PreparedStatement ps = con.prepareStatement(query);
		
		
		ps.setLong(1, amount);
		ps.setLong(2, accno);
		
		ps.executeUpdate();
		
		
		return "Amount Credited Successfully!";
	}
	
	public String withdraw(long accno,long take) throws SQLException {
		
		String selectquery = "select balance from Banking where AccountNumber =?";
		PreparedStatement ps = con.prepareStatement(selectquery) ;
		
		ps.setLong(1,accno);
		ResultSet rs = ps.executeQuery();
		
		if(!rs.next()) {
			return "Account Not Found!";
		}
		
		
		int currentbalance = rs.getInt("balance");
		
		if(currentbalance<take) {
			return "Insufficient Balance!";
		}
		
		
		String query = "update Banking set balance=balance-? where AccountNumber =?";
		PreparedStatement ps1 = con.prepareStatement(query);
		ps1.setLong(1,take);
		ps1.setLong(2,accno);
		ps1.executeUpdate();
		return "Amount Withdrawn Successfully!";
	}
}