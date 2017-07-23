package me.krash0.vm.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class YAMLCore {
	JavaPlugin m = null;
	String configName;

	public YAMLCore(JavaPlugin m, String configName) {
		this.m = m;
		this.configName = configName;
		setupConfig();
	}

	public FileConfiguration userfile;
	private File userfiled;

	public Plugin getPlugin()
	{
		return m;
	}

	public void reloadConfig()
	{
		setupConfig();
		return;
	}

	public File getDataFolder()
	{
		return getPlugin().getDataFolder();
	}

	public void updateConfig() {
		saveConfig();
	}
	
	public void reloadMe()
	{
		reloadConfig();
	}

	public void setupConfig() {
		if (!m.getDataFolder().exists()) {
			m.getDataFolder().mkdir();
		}
		this.userfiled = new File(m.getDataFolder().getAbsolutePath(), configName);
				
		if (!this.userfiled.exists()) {
			try {
				if(m.getResource(configName) != null){
					m.saveResource(configName, true);
				}
				else
				{
					this.userfiled.createNewFile();
				}
			} catch (IOException e) {
				Bukkit.getConsoleSender().sendMessage("§cNão foi possível criar o " + configName + "!");
			}
		}
		this.userfile = YamlConfiguration.loadConfiguration(this.userfiled);
	}

	public FileConfiguration getConfig()
	{
		return this.userfile;
	}

	public boolean saveConfig()
	{
		try
		{
			this.userfile.save(this.userfiled);
			return true;
		}
		catch (Exception e)
		{
			Bukkit.getConsoleSender().sendMessage("§cNão foi possível salvar o " + configName + "!");
			return false;
		}
	}
}