package com.projectkorra.probending.libraries.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SQLiteDatabase extends AbstractDatabase
{
	private Connection _connection;

	public SQLiteDatabase(Logger log, String databaseFile, String folder)
	{
		super(log, "SQLite");

		File fileFolder = new File(folder);

		if (!fileFolder.exists())
		{
			fileFolder.mkdirs();
		}

		File sqlFile = new File(fileFolder.getAbsolutePath() + File.separator + databaseFile);

		try
		{
			Class.forName("org.sqlite.JDBC");
			_connection = DriverManager.getConnection("jdbc:sqlite:" + sqlFile.getAbsolutePath());
			printInfo("Connection established!");
		}
		catch(ClassNotFoundException e)
		{
			printErr("JDBC driver not found!", true);
		}
		catch(SQLException e)
		{
			printErr("SQL exception during connection.", true);
		}
	}

	@Override
	public Connection getConnection()
	{
		return _connection;
	}
	
	@Override
	public void close()
	{
		try
		{
			_connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public ResultSet executeQuery(String query, Object... values)
	{
		try (PreparedStatement statement = getConnection().prepareStatement(query))
		{
			for (int i = 0; i < values.length; i++)
			{
				statement.setObject(i + 1, values[i]);
			}
			
			try (ResultSet result = statement.executeQuery())
			{
				return result;
			}
		}
		catch (SQLException exception)
		{
			exception.printStackTrace();
			return null;
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			return null;
		}
	}

	@Override
	public void executeQuery(String query, Callback<ResultSet> callback, Object... values)
	{
		ResultSet rs = executeQuery(query, values);
		if (callback != null)
		{
			callback.run(rs);
		}
	}
}