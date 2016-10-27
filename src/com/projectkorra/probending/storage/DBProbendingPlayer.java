package com.projectkorra.probending.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.probending.libraries.database.Callback;
import com.projectkorra.probending.objects.PBPlayer;

public class DBProbendingPlayer extends DBInterpreter
{
	public DBProbendingPlayer(JavaPlugin plugin)
	{
		super(plugin);
	}
	
	public void loadPBPlayer(final UUID uuid, final Callback<PBPlayer> callback)
	{
		DatabaseHandler.getDatabase().executeQuery("SELECT * FROM pb_players WHERE uuid=?;", new Callback<ResultSet>()
		{
			public void run(ResultSet rs)
			{
				try
				{
					if (rs.next())
					{
						int id = rs.getInt("id");
						double points = rs.getDouble("points");
						
						callback.run(new PBPlayer(id, uuid, points));
					}
					else
					{
						ResultSet rs2 = DatabaseHandler.getDatabase().executeQuery("INSERT INTO pb_players (uuid, points) VALUES(?, ?);", uuid.toString(), 0D);
						rs2.next();
						int id = rs2.getInt(1);
						double points = 0D;
						callback.run(new PBPlayer(id, uuid, points));
					}
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}, uuid.toString());
	}
	
	public void loadPBPlayerAsync(final UUID uuid, final Callback<PBPlayer> callback)
	{
		runAsync(new Runnable()
		{
			public void run()
			{
				loadPBPlayer(uuid, new Callback<PBPlayer>()
				{
					public void run(PBPlayer player)
					{
						final PBPlayer pbPlayer = player;
						runSync(new Runnable()
						{
							public void run()
							{
								callback.run(pbPlayer);
							}
						});
					}
				});
			}
		});
	}
	
	public void updatePBPlayer(final PBPlayer player)
	{
		DatabaseHandler.getDatabase().executeQuery("UPDATE pb_players SET points=? WHERE id=?;", null, player.getPointsEarned(), player.getID());
	}
	
	public void updatePBPlayerAsync(final PBPlayer player)
	{
		runAsync(new Runnable()
		{
			public void run()
			{
				updatePBPlayer(player);
			}
		});
	}
}