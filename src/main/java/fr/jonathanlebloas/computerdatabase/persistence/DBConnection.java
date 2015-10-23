package fr.jonathanlebloas.computerdatabase.persistence;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum DBConnection {
	INSTANCE;

	private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

	private static final Logger LOGGER = LoggerFactory.getLogger(DBConnection.class);

	private String url;

	private Properties properties;

	private DBConnection() {
		// Load the Driver
		try {
			Class.forName(DRIVER_NAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Error while loading the driver", e);
		}

		// Load the properties
		properties = new Properties();
		try {
			InputStream is = DBConnection.class.getClassLoader().getResourceAsStream("config/config.properties");
			properties.load(is);
			url = properties.getProperty("url");
		} catch (Exception e) {
			throw new RuntimeException("Error while loading config.properties", e);
		}
	}

	public Connection getConnection() {
		LOGGER.trace("Getting a connection to the database");
		try {
			return DriverManager.getConnection(url, properties);
		} catch (SQLException e) {
			LOGGER.error("Error while getting a connection", e);
		}
		return null;
	}

	public void closeConnection(Connection connection) {
		LOGGER.trace("Closing the database connection");
		try {
			connection.close();
		} catch (SQLException e) {
			LOGGER.error("Error while closing the connection", e);
		}
	}

}
