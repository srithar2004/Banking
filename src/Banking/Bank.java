package Banking;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Bank {

	private Connection con = DatabaseConnection.getConnection();

	public String SearchAccNo(long accno) throws SQLException {
		String selectquery = "select balance from Banking where AccountNumber =?";
		try (PreparedStatement ps = con.prepareStatement(selectquery)) {
			ps.setLong(1, accno);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					return "Account Not Found!";
				}
				return "Balance: " + rs.getBigDecimal("balance");
			}
		}
	}

	public String DisplayAccDetails(long Accno) throws SQLException {
		String DisplayQuery = "Select * from Banking where AccountNumber =?";
		try (PreparedStatement ps = con.prepareStatement(DisplayQuery)) {
			ps.setLong(1, Accno);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String name = rs.getString("AccountHolderName");
					long accno = rs.getLong("AccountNumber");
					String acctype = rs.getString("AccountType");
					BigDecimal bal = rs.getBigDecimal("balance");

					return "Account Details\n" +
							"---------------------\n" +
							"AccountHolderName: " + name + "\nAccountNumber: " + accno
							+ "\nAccount Type: " + acctype + "\nBalance: " + bal;
				}
			}
		}
		return "Account not Found!";
	}

	public String TransferAmount(long Ac, long Accrec, BigDecimal amount) throws SQLException {

		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			return "Transfer amount must be positive!";
		}

		if (Ac == Accrec) {
			return "Cannot transfer to the same account!";
		}

		String selectquery = "select balance from Banking where AccountNumber =?";
		BigDecimal currentbalance;
		try (PreparedStatement ps = con.prepareStatement(selectquery)) {
			ps.setLong(1, Ac);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					return "Account Not Found!";
				}
				currentbalance = rs.getBigDecimal("balance");
			}
		}

		if (currentbalance.compareTo(amount) < 0) {
			return "Insufficient Balance!";
		}

		String selectRecQuery = "select balance from Banking where AccountNumber =?";
		try (PreparedStatement psRec = con.prepareStatement(selectRecQuery)) {
			psRec.setLong(1, Accrec);
			try (ResultSet rsRec = psRec.executeQuery()) {
				if (!rsRec.next()) {
					return "Receiver Account Not Found!";
				}
			}
		}

		String Updatequery = "Update Banking set balance=balance-? where AccountNumber = ?";
		try (PreparedStatement ps1 = con.prepareStatement(Updatequery)) {
			ps1.setBigDecimal(1, amount);
			ps1.setLong(2, Ac);
			ps1.executeUpdate();
		}

		String TransferQuery = "update Banking set balance=balance+? where AccountNumber=?";
		try (PreparedStatement ps2 = con.prepareStatement(TransferQuery)) {
			ps2.setBigDecimal(1, amount);
			ps2.setLong(2, Accrec);
			ps2.executeUpdate();
		}
		return "Amount Transfered Successfully!";
	}
}
