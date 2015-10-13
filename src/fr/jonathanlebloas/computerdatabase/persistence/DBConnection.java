package fr.jonathanlebloas.computerdatabase.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

	private static String url = "jdbc:mysql://localhost:3306/computer-database-db?zeroDateTimeBehavior=convertToNull";

	// TODO export logins in properties
	private static String user = "admincdb";

	private static String passwd = "qwerty1234";

	private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			logger.error("Error while registering the driver", e);
		}
	}

	private DBConnection() {
		super();
	}

	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(url, user, passwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void closeConnection(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
