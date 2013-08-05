package com.etriacraft.probending;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.etriacraft.probending.mysql.Database;
import com.etriacraft.probending.mysql.MySQLConnection;
import java.sql.DatabaseMetaData;

public final class DBConnection {

	public static Database sql;

	public static String engine;
	public static String host;
	public static int port;
	public static String db;
	public static String user;
	public static String pass;

	public static void init() {
		if (engine.equalsIgnoreCase("mysql")) {
			sql = new MySQLConnection(Probending.log, "[Probending] Establishing Database Connection...", host, port, user, pass, db);
			((MySQLConnection) sql).open();
			Probending.log.info("[Probending] Etablishing Database Connection...");

			if (!sql.tableExists("probending_players")) {
				Probending.log.info("Creating probending_players table.");
				String query = "CREATE TABLE IF NOT EXISTS `probending_players` ("
						+ "`id` int(32) NOT NULL AUTO_INCREMENT,"
						+ "`player` varchar(32),"
						+ "`team` varchar(15),"
						+ " PRIMARY KEY (id));";
				sql.modifyQuery(query);
			}
			
			if (!sql.tableExists("probending_teams")) {
				Probending.log.info("Creating probending_teams table.");
				String query = "CREATE TABLE IF NOT EXISTS `probending_teams` ("
						+ "`id` int(32) NOT NULL AUTO_INCREMENT,"
						+ "`team` varchar(32),"
						+ "`owner` varchar(32),"
						+ "`Air` varchar(32),"
						+ "`Water` varchar(32),"
						+ "`Earth` varchar(32),"
						+ "`Fire` varchar(32),"
						+ "`Chi` varchar(32),"
						+ "`wins` int(32),"
						+ "`losses` int(32),"
						+ " PRIMARY KEY (id));";
				sql.modifyQuery(query);
			}
			
			DatabaseMetaData md;
			try {
				md = sql.getConnection().getMetaData();
				ResultSet rs = md.getColumns(null, null, "probending_teams", "points");
				if (!rs.next()) {
					sql.modifyQuery("ALTER TABLE probending_teams ADD points int(32)");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}