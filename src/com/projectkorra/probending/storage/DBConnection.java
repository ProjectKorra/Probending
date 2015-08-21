package com.projectkorra.probending.storage;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.projectkorra.probending.Probending;

import java.sql.DatabaseMetaData;

public final class DBConnection {

	public static Database sql;

	public static String engine;
	public static String host;
	public static int port;
	public static String db;
	public static String user;
	public static String pass;
	
	public static boolean isOpen = false;

	public static void init() {
		if (engine.equalsIgnoreCase("mysql")) {
			sql = new MySQLConnection(Probending.log, "[Probending] Establishing Database Connection...", host, port, user, pass, db);
			((MySQLConnection) sql).open();
			Probending.log.info("[Probending] Etablishing Database Connection...");

			if (!sql.tableExists("probending_players")) {
				Probending.log.info("Creating probending_players table.");
				String query = "CREATE TABLE IF NOT EXISTS `probending_players` ("
						+ "`id` int(32) NOT NULL AUTO_INCREMENT,"
						+ "`uuid` varchar(255),"
						+ "`team` varchar(255),"
						+ " PRIMARY KEY (id));";
				sql.modifyQuery(query);
			}
			
			if (!sql.tableExists("probending_teams")) {
				Probending.log.info("Creating probending_teams table.");
				String query = "CREATE TABLE IF NOT EXISTS `probending_teams` ("
						+ "`id` int(32) NOT NULL AUTO_INCREMENT,"
						+ "`team` varchar(255),"
						+ "`owner` varchar(255),"
						+ "`Air` varchar(255),"
						+ "`Water` varchar(255),"
						+ "`Earth` varchar(255),"
						+ "`Fire` varchar(255),"
						+ "`Chi` varchar(255),"
						+ "`wins` int(32),"
						+ "`losses` int(32),"
						+ " PRIMARY KEY (id));";
				sql.modifyQuery(query);
			}

		} else {
			/*
			 * Using SQLite
			 */
			sql = new SQLite(Probending.log, "[Probending] Establishing SQLite Connection.", "probending.db", Probending.plugin.getDataFolder().getAbsolutePath());
			if (((SQLite) sql).open() == null) {
				Probending.log.severe("Disablign due to database error.");
				Probending.plugin.getServer().getPluginManager().disablePlugin(Probending.plugin);
				return;
			}
			
			isOpen = true;
			if (!sql.tableExists("probending_players")) {
				Probending.log.info("Creating probending_players table.");
				String query = "CREATE TABLE `probending_players` ("
						+ "`uuid` TEXT(255),"
						+ "`team` TEXT(255));";
				sql.modifyQuery(query);
			}
			
			if (!sql.tableExists("probending_teams")) {
				Probending.log.info("Creating probending_teams table.");
				String query = "CREATE TABLE `probending_teams` ("
						+ "`team` TEXT(255),"
						+ "`owner` TEXT(255),"
						+ "`Air` TEXT(255),"
						+ "`Water` TEXT(255),"
						+ "`Earth` TEXT(255),"
						+ "`Fire` TEXT(255),"
						+ "`Chi` TEXT(255),"
						+ "`wins` INTEGER(32),"
						+ "`losses` INTEGER(32));";
				sql.modifyQuery(query);
			}
		}
	}

}