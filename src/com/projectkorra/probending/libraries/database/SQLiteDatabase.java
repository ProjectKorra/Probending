package com.projectkorra.probending.libraries.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SQLiteDatabase extends AbstractDatabase {

	private Connection _connection;

	public SQLiteDatabase(Logger log, String databaseFile, String folder) {
		super(log, "SQLite");

		File fileFolder = new File(folder);

		if (!fileFolder.exists()) {
			fileFolder.mkdirs();
		}

		File sqlFile = new File(fileFolder.getAbsolutePath() + File.separator + databaseFile);

		try {
			Class.forName("org.sqlite.JDBC");
			_connection = DriverManager.getConnection("jdbc:sqlite:" + sqlFile.getAbsolutePath());
			printInfo("Connection established!");
		}
		catch (ClassNotFoundException e) {
			printErr("JDBC driver not found!", true);
		}
		catch (SQLException e) {
			printErr("SQL exception during connection.", true);
		}
	}

	@Override
	public Connection getConnection() {
		return _connection;
	}

	@Override
	public void close() {
		try {
			_connection.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void executeQuery(String query, Callback<ResultSet> callback, Object... values) {
		try (PreparedStatement statement = getConnection().prepareStatement(query)) {
			if (values.length > 0) {
				for (int i = 0; i < values.length; i++) {
					statement.setObject(i + 1, values[i]);
				}
			}

			try (ResultSet result = statement.executeQuery()) {
				if (callback != null) {
					callback.run(result);
				}
			}
		}
		catch (SQLException exception) {
			exception.printStackTrace();
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void executeUpdate(String query, Object... values) {
		try (PreparedStatement statement = getConnection().prepareStatement(query)) {
			if (values.length > 0) {
				for (int i = 0; i < values.length; i++) {
					statement.setObject(i + 1, values[i]);
				}
			}

			statement.executeUpdate();
		}
		catch (SQLException exception) {
			exception.printStackTrace();
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public boolean tableExists(String table) {
		try {
			DatabaseMetaData dmd = getConnection().getMetaData();
			ResultSet rs = dmd.getTables(null, null, table, null);

			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
