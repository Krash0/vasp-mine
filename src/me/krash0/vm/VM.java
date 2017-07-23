package me.krash0.vm;

import org.bukkit.plugin.java.JavaPlugin;

import me.krash0.vm.commands.VMCommands;
import me.krash0.vm.listeners.PlayerEvents;
import me.krash0.vm.managers.LanguageManager;
import me.krash0.vm.managers.MineManager;

public class VM extends JavaPlugin {
	private static VM instance;
	private LanguageManager languageManager;
	private MineManager mineManager;
	
	@Override
	public void onEnable(){
		getServer().getConsoleSender().sendMessage("§8[VaspMine] - Plugin ativado.");
		instance = this;
		languageManager = new LanguageManager();
		mineManager = new MineManager();
		
		getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
		getCommand("vm").setExecutor(new VMCommands());
	}
	
	@Override
	public void onDisable(){
	    getServer().getScheduler().cancelTasks(this);
		getServer().getConsoleSender().sendMessage("§8[VaspMine] - Plugin desativado.");
	}
	
	public LanguageManager getLanguage() {
		return languageManager;
	}
	
	public MineManager getMineManager() {
		return mineManager;
	}
	
	public static VM getInstance() {
		return instance;
	}
}