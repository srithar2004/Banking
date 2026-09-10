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
	
	public String TransferAmount(long Ac, long Accrec, long amount) throws SQLException {

	    // Basic validation
	    if (amount <= 0) {
	        return "Invalid Transfer Amount!";
	    }

	    // Prevent transferring to the same account
	    if (Ac == Accrec) {
	        return "Sender and Receiver Account Cannot Be Same!";
	    }

	    // Set transaction isolation level
	    con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

	    // Start transaction
	    con.setAutoCommit(false);

	    try {

	        // Check sender account and get balance
	        String selectQuery =
	                "SELECT balance FROM Banking WHERE AccountNumber = ?";

	        PreparedStatement ps = con.prepareStatement(selectQuery);
	        ps.setLong(1, Ac);

	        ResultSet rs = ps.executeQuery();

	        if (!rs.next()) {
	            con.rollback();
	            con.setAutoCommit(true);
	            return "Sender Account Not Found!";
	        }

	        int currentBalance = rs.getInt("balance");

	        // Check sufficient balance
	        if (currentBalance < amount) {
	            con.rollback();
	            con.setAutoCommit(true);
	            return "Insufficient Balance!";
	        }

	        // Check receiver account exists
	        String receiverQuery =
	                "SELECT AccountNumber FROM Banking WHERE AccountNumber = ?";

	        PreparedStatement receiverPs =
	                con.prepareStatement(receiverQuery);

	        receiverPs.setLong(1, Accrec);

	        ResultSet receiverRs = receiverPs.executeQuery();

	        if (!receiverRs.next()) {
	            con.rollback();
	            con.setAutoCommit(true);
	            return "Receiver Account Not Found!";
	        }

	        // Debit sender
	        String debitQuery =
	                "UPDATE Banking SET balance = balance - ? " +
	                "WHERE AccountNumber = ?";

	        PreparedStatement debitPs =
	                con.prepareStatement(debitQuery);

	        debitPs.setLong(1, amount);
	        debitPs.setLong(2, Ac);

	        int debitResult = debitPs.executeUpdate();

	        if (debitResult != 1) {
	            con.rollback();
	            con.setAutoCommit(true);
	            return "Debit Failed!";
	        }

	        // Credit receiver
	        String creditQuery =
	                "UPDATE Banking SET balance = balance + ? " +
	                "WHERE AccountNumber = ?";

	        PreparedStatement creditPs =
	                con.prepareStatement(creditQuery);

	        creditPs.setLong(1, amount);
	        creditPs.setLong(2, Accrec);

	        int creditResult = creditPs.executeUpdate();

	        if (creditResult != 1) {
	            con.rollback();
	            con.setAutoCommit(true);
	            return "Credit Failed! Transaction Rolled Back!";
	        }

	        // Both operations successful
	        con.commit();

	        // Restore default JDBC behavior
	        con.setAutoCommit(true);

	        return "Amount Transferred Successfully!";

	    } catch (SQLException e) {

	        // Something went wrong
	        con.rollback();

	        con.setAutoCommit(true);

	        System.out.println("Transaction Failed. Rolled Back.");

	        e.printStackTrace();

	        return "Transfer Failed! Transaction Rolled Back!";
	    }
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