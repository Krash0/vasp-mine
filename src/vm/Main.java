package vm;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	static JavaPlugin pl;
	static HashMap<String, Arena> arenas = new HashMap<String, Arena>();
	static ArrayList<String> intervalReset = new ArrayList<String>();
	static ArrayList<String> setPos = new ArrayList<String>();
	static Location pos1, pos2;
	
	@Override
	public void onEnable(){
		getServer().getConsoleSender().sendMessage("§8[Vasp Mine] - Plugin ativado.");
		pl = this;
		getServer().getPluginManager().registerEvents(new Events(), this);
		getCommand("vm").setExecutor(new Commands());
		Utilities.getInstancia().loadArenas();
	}
	@Override
	public void onDisable(){
		getServer().getConsoleSender().sendMessage("§8[Vasp Mine] - Plugin desativado.");
	}
}