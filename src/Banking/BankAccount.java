package Banking;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankAccount {

	static Bank bank = new Bank();

	private BigDecimal balance;

	private Connection con = DatabaseConnection.getConnection();

	public void CreateAccount(long AccountNumber, String AccountHolderName, String AccountType) {
		try {

			if (!AccountHolderName.matches("[a-zA-Z ]+")) {
				throw new IllegalArgumentException("Invalid name! Only alphabets are allowed.");
			}

			if (!AccountType.equals("saving account") && !AccountType.equals("fixed deposit")) {
				throw new IllegalArgumentException("Invalid Account Type! Must be 'saving account' or 'fixed deposit'.");
			}

			System.out.println("First Name : " + AccountHolderName + "\nAccount Type: " + AccountType);
			System.out.println("Account Created Successfully!!");

		} catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
			System.out.println("Cannot Proceed Further!");
		}
	}

	public String deposit(long accno, BigDecimal amount) throws SQLException {

		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			return "Deposit amount must be positive!";
		}

		String query = "Update Banking set balance=balance+? where AccountNumber = ?";
		try (PreparedStatement ps = con.prepareStatement(query)) {
			ps.setBigDecimal(1, amount);
			ps.setLong(2, accno);
			int rows = ps.executeUpdate();
			if (rows == 0) {
				return "Account Not Found!";
			}
		}

		return "Amount Credited Successfully!";
	}

	public String withdraw(long accno, BigDecimal take) throws SQLException {

		if (take.compareTo(BigDecimal.ZERO) <= 0) {
			return "Withdrawal amount must be positive!";
		}

		String selectquery = "select balance from Banking where AccountNumber =?";
		BigDecimal currentbalance;
		try (PreparedStatement ps = con.prepareStatement(selectquery)) {
			ps.setLong(1, accno);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					return "Account Not Found!";
				}
				currentbalance = rs.getBigDecimal("balance");
			}
		}

		if (currentbalance.compareTo(take) < 0) {
			return "Insufficient Balance!";
		}

		String query = "update Banking set balance=balance-? where AccountNumber =?";
		try (PreparedStatement ps1 = con.prepareStatement(query)) {
			ps1.setBigDecimal(1, take);
			ps1.setLong(2, accno);
			ps1.executeUpdate();
		}
		return "Amount Withdrawn Successfully!";
	}
}
