package me.krash0.vm.datatype;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Mine {
	private String name;
	private int resetInterval = 0;
	private int resetClock = 0;
	private boolean enabled;
	private Location pos1, pos2;
	private Map<Material, Integer> blocks;
	
	public Mine(String name, Boolean enabled, Location pos1, Location pos2) {
		this.name = name;
		this.enabled = enabled;
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.blocks = new LinkedHashMap<>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setResetInterval(int resetInterval) {
		this.resetInterval = resetInterval;
		this.resetClock = this.resetInterval;
	}
	
	public Integer getResetInterval() {
		return resetInterval;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setBlocks(Map<Material, Integer> blocks) {
		this.blocks = sortHashMap(blocks);
	}
	
	public Map<Material, Integer> getBlocks(){
		return blocks;
	}
	
	public void addBlock(Material material, int percentage) {
		Map<Material, Integer> new_map = new LinkedHashMap<>(this.blocks);
		new_map.put(material, percentage);
		this.blocks = sortHashMap(new_map);
	}
	
	public void removeBlock(Material material) {
		this.blocks.remove(material);
	}
	
	public void count() {
		if(resetInterval <= 0) {
			return;
		}
		if(resetClock > 0) {
			resetClock--;
		}
		
		if(resetClock == 0) {
			resetMine();
            resetClock = resetInterval;
            return;
		}
	}
	
	public void resetMine(){
		int X1 = (int) Math.min(pos1.getX(), pos2.getX());
		int X2 = (int) Math.max(pos1.getX(), pos2.getX()) + 1;

		int Y1 = (int) Math.min(pos1.getY(), pos2.getY());
		int Y2 = (int) Math.max(pos1.getY(), pos2.getY()) + 1;

		int Z1 = (int) Math.min(pos1.getZ(), pos2.getZ());
		int Z2 = (int) Math.max(pos1.getZ(), pos2.getZ()) + 1;
		
		World world = pos1.getWorld();

		Random random = new Random();
		
		Location loc;
		Integer porce;
		Material materialBlock;
		
		for(int x = X1; x < X2; x++){
			for(int y = Y1; y < Y2; y++){
				for(int z = Z1; z < Z2; z++){
					loc = new Location(world, x, y, z);
					porce = random.nextInt(100);
					int cumulativeProbability = 0;
					materialBlock = Material.AIR;
					
					for(Entry<Material, Integer> block : blocks.entrySet()){
						cumulativeProbability += block.getValue();
						if(cumulativeProbability >= porce){
							materialBlock = block.getKey();
							break;
						}
					}
					loc.getBlock().setType(materialBlock);
				}
			}
		}
		
		/* TELEPORT ALL PLAYERS IN MINE TO SURFACE */
		for(Player _currentPlayer : world.getPlayers()){
			if(X2-1 >= _currentPlayer.getLocation().getBlockX() && Y2-1 >= _currentPlayer.getLocation().getBlockY() && Z2-1 >= _currentPlayer.getLocation().getBlockZ()){
				if(X1 <= _currentPlayer.getLocation().getBlockX() && Y1 <= _currentPlayer.getLocation().getBlockY() && Z1 <= _currentPlayer.getLocation().getBlockZ()){
					_currentPlayer.teleport(new Location(world, _currentPlayer.getLocation().getX(), (Y2 + 1), _currentPlayer.getLocation().getZ(), _currentPlayer.getLocation().getYaw(), _currentPlayer.getLocation().getPitch()));
				}
			}
		}
	}
	
	/* SORT THE HASHMAP */
	private LinkedHashMap<Material, Integer> sortHashMap(Map<Material, Integer> map) {
		 return map.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
	}
}
