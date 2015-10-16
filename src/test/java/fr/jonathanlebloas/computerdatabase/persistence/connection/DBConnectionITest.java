package fr.jonathanlebloas.computerdatabase.persistence.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class DBConnectionITest {

	private static String url = "jdbc:mysql://localhost:3306/computer-database-db-test?zeroDateTimeBehavior=convertToNull";

	// TODO export logins in properties
	private static String user = "admincdbtests";

	private static String passwd = "qwerty1234";

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			fail("Driver not founded");
		}
	}

	private DBConnectionITest() {
		super();
	}

	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(url, user, passwd);
		} catch (SQLException e) {
			fail("Error when connecting to the database");
		}
		return null;
	}

	public static void closeConnection(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			fail("Error when closing the conneciton");
		}
	}

}
