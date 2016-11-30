package com.projectkorra.probending.libraries.flatfile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class FileManager {

	protected JavaPlugin _plugin;
	protected File dFile;
	protected FileConfiguration cFile;

	public FileManager(JavaPlugin plugin, String path, String fileName) {
		try {
			_plugin = plugin;
			File folder = new File(plugin.getDataFolder().getCanonicalPath() + File.separator + path.replace(".", File.separator));
			folder.mkdirs();
			dFile = new File(folder.getCanonicalPath() + File.separator + fileName + ".yml");
			if (!dFile.exists()) {
				dFile.createNewFile();
			}

			cFile = YamlConfiguration.loadConfiguration(dFile);
		}
		catch (Exception e) {
			return;
		}
		;
	}

	public FileConfiguration getFile() {
		return cFile;
	}

	public Boolean getBoolean(String path) {
		return cFile.getBoolean(path);
	}

	public String getString(String path) {
		return cFile.getString(path);
	}

	public Long getLong(String path) {
		return cFile.getLong(path);
	}

	public Integer getInteger(String path) {
		return cFile.getInt(path);
	}

	public Double getDouble(String path) {
		return cFile.getDouble(path);
	}

	public List<String> getStringList(String path) {
		return cFile.getStringList(path);
	}

	public ConfigurationSection getConfigurationSection(String path) {
		return cFile.getConfigurationSection(path);
	}

	public void set(String path, Object value, boolean autosave) {
		cFile.set(path, value);
		if (autosave) {
			save();
		}
	}

	public void save() {
		Bukkit.getScheduler().runTaskAsynchronously(_plugin, new Runnable() {
			public void run() {
				try {
					cFile.save(dFile);
				}
				catch (IOException e) {
					System.out.println("Error saving " + dFile.getName());
				}
			}
		});
	}
}
