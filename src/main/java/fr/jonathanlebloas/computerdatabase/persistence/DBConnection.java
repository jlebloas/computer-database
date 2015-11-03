package fr.jonathanlebloas.computerdatabase.persistence;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public enum DBConnection {
	INSTANCE;

	private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

	private static final Logger LOGGER = LoggerFactory.getLogger(DBConnection.class);

	private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

	private BoneCP connectionPool = null;

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
		} catch (Exception e) {
			throw new RuntimeException("Error while loading config.properties", e);
		}

		// Configure the database connection pool
		try {
			BoneCPConfig config = new BoneCPConfig(properties);
			connectionPool = new BoneCP(config);
		} catch (Exception e) {
			throw new RuntimeException("Error while configuring the database connection pool", e);
		}
	}

	public Connection getConnection() {
		LOGGER.trace("Getting a connection to the database");
		try {
			Connection connect = threadLocal.get();
			if (connect == null || connect.isClosed()) {
				connect = connectionPool.getConnection();
				threadLocal.set(connect);
				return connect;
			} else {
				return connect;
			}
		} catch (SQLException e) {
			LOGGER.error("Error while getting a connection", e);
			throw new RuntimeException("Error while getting a connection", e);
		}
	}

	/**
	 * Close the connection of the current Thread
	 */
	public void rollbackConnection() {
		LOGGER.trace("Rollback the transaction");
		try {
			threadLocal.get().rollback();
		} catch (SQLException e) {
			throw new RuntimeException("Error while rollback", e);
		}
	}

	/**
	 * Close the connection of the current Thread
	 */
	public void closeConnection() {
		LOGGER.trace("Closing the database connection");
		try {
			Connection connect = threadLocal.get();
			if (connect != null && !connect.isClosed()) {
				connect.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error while closing the connection", e);
		}
	}

}
