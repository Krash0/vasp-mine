package me.krash0.vm.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.krash0.vm.VM;
import me.krash0.vm.datatype.Mine;
import me.krash0.vm.datatype.SelectPos;
import me.krash0.vm.utils.SerializeLocation;

public class MineManager {
	private VM plugin;
	
	private HashMap<String, Mine> mines;
	private HashMap<String, SelectPos> setPos;
	
	public MineManager() {
		plugin = VM.getInstance();
		mines = new HashMap<>();
		setPos = new HashMap<>();
		
		loadMines();
	}
	
	public HashMap<String, Mine> getMines(){
		return mines;
	}
	
	public HashMap<String, SelectPos> getSelectPos(){
		return setPos;
	}
	
	/* LOAD ALL MINES OF THE FILES */
	public void loadMines(){
		if(plugin.getConfig().getString("mines") != null){
			Set<String> listArenas = plugin.getConfig().getConfigurationSection("mines").getKeys(false);
			plugin.getServer().getConsoleSender().sendMessage("§8[Vasp Mine] - Carregando " + listArenas.size() + " mina(s).");
			int loadeadArenas = 0;
			for(String arenaName : listArenas){
				if(plugin.getConfig().getString("mines." + arenaName) != null){
					boolean enabled = plugin.getConfig().getBoolean("mines." + arenaName + ".enabled");
					String pos1 = plugin.getConfig().getString("mines." + arenaName + ".area.pos1");
					String pos2 = plugin.getConfig().getString("mines." + arenaName + ".area.pos2");
					int interval = plugin.getConfig().getInt("mines." + arenaName + ".reset_interval");
					
					Mine mine = new Mine(arenaName, enabled, SerializeLocation.deserializeLocation(pos1, false), SerializeLocation.deserializeLocation(pos2, false));
					loadBlocks(mine);
					
					mine.setResetInterval(interval);
					mines.put(arenaName.toLowerCase(), mine);
					
					plugin.getServer().getConsoleSender().sendMessage("§8 *Mina '" + arenaName + "' carregada com sucesso.*");
					loadeadArenas++;
				}
			}
			plugin.getServer().getConsoleSender().sendMessage("§8[Vasp Mine] - " + loadeadArenas + " mina(s) carregada(s) com sucesso.");
		}
		
		/* START THE COUNTDOWN */
		startCountDown();
	}
	
	/* LOAD ALL BLOCKS OF THE FILE CONFIG */
	private void loadBlocks(Mine mine){
		Map<Material, Integer> blocks = new HashMap<>();
		if(plugin.getConfig().getString("mines." + mine.getName() + ".blocks") != null){
			for(String block_name : plugin.getConfig().getConfigurationSection("mines." + mine.getName() + ".blocks").getKeys(false)){
				if(!block_name.equals("")){
					if(plugin.getConfig().getString("mines." + mine.getName() + ".blocks." + block_name) != null){
						int porcentage = plugin.getConfig().getInt("mines." + mine.getName() + ".blocks." + block_name + ".percentage");
						blocks.put(Material.getMaterial(block_name), porcentage);
					}
				}
			}
		}
		mine.setBlocks(blocks);
	}
	
	/* CREATE A MINE */
	public boolean createMine(String name, SelectPos selectPos){
		if(mines.containsKey(name.toLowerCase())) {
			return false;
		}

		plugin.getConfig().set("mines." + name + ".enabled", false);
		plugin.getConfig().set("mines." + name + ".area.pos1", SerializeLocation.serializeLocation(selectPos.pos1, false));
		plugin.getConfig().set("mines." + name + ".area.pos2", SerializeLocation.serializeLocation(selectPos.pos2, false));
		plugin.getConfig().set("mines." + name + ".reset_interval" , 1);
		plugin.saveConfig();

		Mine mine = new Mine(name, false, selectPos.pos1, selectPos.pos2);
		mine.setResetInterval(1);
		mines.put(name.toLowerCase(), mine);
		return true;
	}
	
	/* DELETE A MINE */
	public boolean deleteMine(String mineName) {
		Mine mine = mines.get(mineName.toLowerCase());
		if(mine == null){
			return false;
		}
		
		plugin.getConfig().set("mines." + mine.getName(), null);
		plugin.saveConfig();
		
		mines.remove(mineName.toLowerCase());
		return true;
	}
	
	/* SET THE INTERVAL TO RESET A MINE */
	public boolean setIntervalReset(String mineName, Integer interval){
		Mine mine = mines.get(mineName.toLowerCase());
		if(mine == null){
			return false;
		}
		
		mine.setResetInterval(interval);

		plugin.getConfig().set("mines." + mine.getName() + ".reset_interval" , interval);
		plugin.saveConfig();
		return true;
	}
	
	/* CHANGE ENABLE MODE */
	public boolean setEnable(String mineName, boolean enabled){
		Mine mine = mines.get(mineName.toLowerCase());
		
		if(mine == null){
			return false;
		}

		plugin.getConfig().set("mines." + mine.getName() + ".enabled" , enabled);
		plugin.saveConfig();
		
		mine.setEnabled(enabled);
		
		if(enabled){
			mine.resetMine();
		}
		return true;
	}
	
	/* ADD A BLOCK TO A MINE */
	public int addBlock(String mineName, ItemStack material, Integer percentage){
		Mine mine = mines.get(mineName.toLowerCase());
		
		if(mine == null){
			return 0;
		}
		if(mine.getBlocks().containsKey(material.getType())) {
			return 1;
		}
		mine.addBlock(material.getType(), percentage);

		plugin.getConfig().set("mines." + mine.getName() + ".blocks." + material.getType().name() + ".percentage", percentage);
		plugin.saveConfig();
		return 2;
	}
	
	/* REMOVE A BLOCK OF A MINE */
	public int removeBlock(String mineName, ItemStack material){
		Mine mine = mines.get(mineName.toLowerCase());
		
		if(mine == null){
			return 0;
		}
		
		if(!mine.getBlocks().containsKey(material.getType())) {
			return 1;
		}
		
		mine.removeBlock(material.getType());
		plugin.getConfig().set("mines." + mine.getName() + ".blocks." + material.getType().name(), null);
		plugin.saveConfig();
		return 2;
	}
	
	/* RESET A MINE BY NAME */
	public boolean resetArenaByName(String mineName) {
		Mine mine = mines.get(mineName.toLowerCase());
		
		if(mine == null){
			return false;
		}
		
		mine.resetMine();
		return true;
	}
	
	/* START THE COUNTDOWN */
	void startCountDown(){
		new BukkitRunnable() {
			@Override
			public void run() {
				for(Mine mine : mines.values()) {
					if(mine.isEnabled()) {
						mine.resetMine();
					}
				}
			}
		}.runTaskTimer(plugin, 60 * 20L, 60 * 20L);
	}
}
