package Banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Bank {
//	public static  HashMap<Long,Integer> AccDetails  = new HashMap<>();
//	public static HashMap<Long,String> Account = new HashMap<>();
//	Bank(){
//		
//	}
	private Connection con = DatabaseConnection.getConnection();
	public String SearchAccNo(long accno) throws SQLException {
		String selectquery = "select balance from Banking where AccountNumber =?";
		PreparedStatement ps = con.prepareStatement(selectquery) ;
		
		ps.setLong(1,accno);
		ResultSet rs = ps.executeQuery();
		
		if(!rs.next()) {
			return "Account Not Found!";
		}
		
		return "Account";
		
		
	}
	
	public String DisplayAccDetails(long Accno) throws SQLException {
		String DisplayQuery = "Select * from Banking where AccountNumber =?";
		PreparedStatement ps = con.prepareStatement(DisplayQuery);
		
		ps.setLong(1, Accno);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			String name =rs.getString("AccountHolderName");
			long accno =rs.getLong("AccountNumber");
			String acctype =rs.getString("AccountType");
			long bal =rs.getLong("balance");
			
			return "Account Details\n"+
				"---------------------\n"+
				"AccountHolderName: "+name+"\nAccountNumber: "+accno
				+"\nAccount Type: "+acctype+"\nBalance: "+bal;
		}
		return "Account not Found!";
	}
	
	public String TransferAmount(long Ac,long Accrec,long amount) throws SQLException {
		String selectquery = "select balance from Banking where AccountNumber =?";
		PreparedStatement ps = con.prepareStatement(selectquery) ;
		
		ps.setLong(1,Ac);
		ResultSet rs = ps.executeQuery();
		
		if(!rs.next()) {
			return "Account Not Found!";
		}
		
		
		int currentbalance = rs.getInt("balance");
		
		if(currentbalance<amount) {
			return "Insufficient Balance!";
		}
		
		String Updatequery = "Update Banking set balance=balance-? where AccountNumber = ?";
		PreparedStatement ps1 = con.prepareStatement(Updatequery);
		
		ps1.setLong(1,amount);
		ps1.setLong(2, Ac);
		ps1.executeUpdate();
		
		String TransferQuery = "update Banking set balance=balance+? where AccountNumber=?";
		PreparedStatement ps2= con.prepareStatement(TransferQuery);
		
		ps2.setLong(1, amount);
		ps2.setLong(2,Accrec);
		ps2.executeUpdate();
		return "Amount Transfered Successfully!";
		
		
	}
	
	
}


//1. BankAccount (Entity)
//
//Fields: accountNumber, accountHolderName, accountType, balance.
//
//Methods: deposit(amount), withdraw(amount), toString().
//
//Constraints:
//
//balance ≥ 0
//
//accountType can only be "Savings" or "Current"
//

//2. Bank (Service)
//
//Manages a collection of BankAccount objects.
//
//Methods:
//
//addAccount() → add a new account
//
//getAccount() → search by account number
//
//updateAccount() → update balance after deposit/withdraw
//
//transfer() → move money from one account to another
//
//getAllAccounts() → list all accounts
//
//3. Main (Application)
//
//Handle user interactions using Scanner.
//
//Menu:
//
//Create account
//
//Deposit money
//
//Withdraw money
//
//Transfer money
//
//Show all accounts
//
//Exit