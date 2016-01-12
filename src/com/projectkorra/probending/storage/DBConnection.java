package com.projectkorra.probending.storage;

import com.projectkorra.probending.Probending;

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
			sql = new MySQLConnection(Probending.log, "Establishing Database Connection...", host, port, user, pass, db);
			((MySQLConnection) sql).open();
			Probending.log.info("Etablishing Database Connection...");

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

			if (!sql.tableExists("probending_arenas")) {
				Probending.log.info("Creating probending_arenas table.");
				String query = "CREATE TABLE IF NOT EXISTS `probending_arenas` ("
						+ "`id` int(32) NOT NULL AUTO_INCREMENT,"
						+ "`name` varchar(255),"
						+ "`world` varchar(255),"
						+ "`spectatorX` int(32),"
						+ "`spectatorY` int(32),"
						+ "`spectatorZ` int(32),"
						+ "`teamOneX` int(32),"
						+ "`teamOneY` int(32),"
						+ "`teamOneZ` int(32),"
						+ "`teamTwoX` int(32),"
						+ "`teamTwoY` int(32),"
						+ "`teamTwoZ` int(32),"
						+ "`field` varchar(255)"
						+ "`divider` varchar(255),"
						+ "`teamOneZoneOne` varchar(255),"
						+ "`teamOneZoneTwo` varchar(255),"
						+ "`teamOneZoneThree` varchar(255),"
						+ "`teamTwoZoneOne` varchar(255),"
						+ "`teamTwoZoneTwo` varchar(255),"
						+ "`teamTwoZoneThree` varchar(255),"
						+ "`teamOneColor` varchar(255),"
						+ "`teamTwoColor` varchar(255),"
						+ " PRIMARY KEY (id));";
				sql.modifyQuery(query);
			}

		} else {
			/*
			 * Using SQLite
			 */
			sql = new SQLite(Probending.log, "Establishing SQLite Connection.", "probending.db", Probending.plugin.getDataFolder().getAbsolutePath());
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

			if (!sql.tableExists("probending_arenas")) {
				Probending.log.info("Creating probending_arenas table.");
				String query = "CREATE TABLE IF NOT EXISTS `probending_arenas` ("
						+ "`name` TEXT(255),"
						+ "`world` TEXT(255),"
						+ "`spectatorX` INTEGER(32),"
						+ "`spectatorY` INTEGER(32),"
						+ "`spectatorZ` INTEGER(32),"
						+ "`teamOneX` INTEGER(32),"
						+ "`teamOneY` INTEGER(32),"
						+ "`teamOneZ` INTEGER(32),"
						+ "`teamTwoX` INTEGER(32),"
						+ "`teamTwoY` INTEGER(32),"
						+ "`teamTwoZ` INTEGER(32),"
						+ "`divider` TEXT(255),"
						+ "`field` TEXT(255),"
						+ "`teamOneZoneOne` TEXT(255),"
						+ "`teamOneZoneTwo` TEXT(255),"
						+ "`teamOneZoneThree` TEXT(255),"
						+ "`teamTwoZoneOne` TEXT(255),"
						+ "`teamTwoZoneTwo` TEXT(255),"
						+ "`teamTwoZoneThree` TEXT(255),"
						+ "`teamOneColor` TEXT(255),"
						+ "`teamTwoColor` TEXT(255));";
				sql.modifyQuery(query);
			}
		}
	}
}