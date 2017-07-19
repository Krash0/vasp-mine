package vm;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Utilities {
	public static Utilities instancia;

	private Utilities() {}

	public static Utilities getInstancia(){
		if(instancia == null){
			instancia = new Utilities();
		}
		return instancia;
	}

	/* LOAD ALL MINES OF THE FILES */
	void loadArenas(){
		if(Main.pl.getConfig().getString("Arenas") != null){
			Set<String> listArenas = Main.pl.getConfig().getConfigurationSection("Arenas").getKeys(false);
			Main.pl.getServer().getConsoleSender().sendMessage("§8[Vasp Mine] - Carregando " + listArenas.size() + " mina(s).");
			int loadeadArenas = 0;
			for(String arenaName : listArenas){
				if(Main.pl.getConfig().getString("Arenas." + arenaName) != null){
					String name = Main.pl.getConfig().getString("Arenas." + arenaName + ".Name");
					Boolean enable = Main.pl.getConfig().getBoolean("Arenas." + name.toLowerCase() + ".Enable");
					String pos1 = Main.pl.getConfig().getString("Arenas." + name.toLowerCase() + ".Area.Pos1");
					String pos2 = Main.pl.getConfig().getString("Arenas." + name.toLowerCase() + ".Area.Pos2");
					Integer interval = Main.pl.getConfig().getInt("Arenas." + name.toLowerCase() + ".IntervalReset");
					Arena arena = new Arena(name, enable, SerializeLocation.deserializeLocation(pos1, false), SerializeLocation.deserializeLocation(pos2, false));
					arena.blocks = loadBlocks(arenaName);
					arena.intervalReset = interval;
					Main.arenas.put(arenaName, arena);
					if(!Main.intervalReset.contains(arenaName)){
						Main.intervalReset.add(arenaName);
						startCountDown(arenaName);
					}
					Main.pl.getServer().getConsoleSender().sendMessage("§8 *Mina '" + name + "' carregada com sucesso.*");
					loadeadArenas++;
				}
			}
			Main.pl.getServer().getConsoleSender().sendMessage("§8[Vasp Mine] - " + loadeadArenas + " mina(s) carregada(s) com sucesso.");
		}
	}

	/* CREATE A MINE */
	boolean createArena(String name, Location pos1, Location pos2){
		if(Main.pl.getConfig().getString("Arenas." + name.toLowerCase()) != null){
			return false;
		}

		Main.pl.getConfig().set("Arenas." + name.toLowerCase() + ".Name", name);
		Main.pl.getConfig().set("Arenas." + name.toLowerCase() + ".Enable", false);
		Main.pl.getConfig().set("Arenas." + name.toLowerCase() + ".Area.Pos1", SerializeLocation.serializeLocation(pos1, false));
		Main.pl.getConfig().set("Arenas." + name.toLowerCase() + ".Area.Pos2", SerializeLocation.serializeLocation(pos2, false));
		Main.pl.getConfig().set("Arenas." + name.toLowerCase() + ".IntervalReset" , 60);
		Main.pl.saveConfig();

		Main.arenas.put(name.toLowerCase(), new Arena(name, false, pos1, pos2));
		return true;
	}

	/* SET THE INTERVAL TO RESET A MINE */
	boolean setIntervalReset(String arena, Integer interval){
		if(Main.pl.getConfig().getString("Arenas." + arena) == null){
			return false;
		}

		if(Main.arenas.containsKey(arena)){
			Main.arenas.get(arena).intervalReset = interval;
		}

		Main.pl.getConfig().set("Arenas." + arena + ".IntervalReset" , interval);
		Main.pl.saveConfig();
		return true;
	}

	/* CHANGE ENABLE MODE */
	boolean setEnable(String arena, boolean Enable){
		if(Main.pl.getConfig().getString("Arenas." + arena) == null){
			return false;
		}

		if(Main.arenas.containsKey(arena)){
			Main.arenas.get(arena).enable = Enable;
		}

		Main.pl.getConfig().set("Arenas." + arena + ".Enable" , Enable);
		Main.pl.saveConfig();
		if(Enable){
			if(!Main.intervalReset.contains(arena)){
				Main.intervalReset.add(arena);
				startCountDown(arena);
			}
			resetArena(Main.arenas.get(arena));
		}
		return true;
	}

	/* ADD A BLOCK TO A MINE */
	boolean addBlock(String arena, ItemStack material, Integer porcentage){
		if(Main.pl.getConfig().getString("Arenas." + arena) == null){
			return false;
		}

		if(Main.arenas.containsKey(arena)){
			Main.arenas.get(arena).blocks.put(porcentage, material.getType().name());
		}

		Main.pl.getConfig().set("Arenas." + arena + ".Porcentages." + porcentage + ".Block", material.getType().name());
		Main.pl.saveConfig();
		return true;
	}

	/* START THE COUNTDOWN */
	void startCountDown(final String arena){
		if(Main.arenas.containsKey(arena)){
			new BukkitRunnable() {
				@Override
				public void run() {
					resetArena(Main.arenas.get(arena));
				}
			}.runTaskTimer(Main.pl, Main.arenas.get(arena).intervalReset * 20L, Main.arenas.get(arena).intervalReset * 20L);
		}
	}

	/* LOAD ALL BLOCKS OF THE FILE CONFIG */
	Map<Integer, String> loadBlocks(String arena){
		Map<Integer, String> blocks = new TreeMap<Integer, String>();
		if(Main.pl.getConfig().getString("Arenas." + arena + ".Porcentages") != null){
			for(String info : Main.pl.getConfig().getConfigurationSection("Arenas." + arena + ".Porcentages").getKeys(false)){
				if(!info.equals("")){
					if(Main.pl.getConfig().getString("Arenas." + arena + ".Porcentages." + info) != null){
						String Material = Main.pl.getConfig().getString("Arenas." + arena + ".Porcentages." + info + ".Block");
						blocks.put(Integer.parseInt(info), Material);
					}
				}
			}
		}
		return blocks;
	}

	/* RESET A MINE */
	public void resetArena(Arena arena){
		int X1 = (int) Math.min(arena.pos1.getX(), arena.pos2.getX());
		int X2 = (int) Math.max(arena.pos1.getX(), arena.pos2.getX()) + 1;

		int Y1 = (int) Math.min(arena.pos1.getY(), arena.pos2.getY());
		int Y2 = (int) Math.max(arena.pos1.getY(), arena.pos2.getY()) + 1;

		int Z1 = (int) Math.min(arena.pos1.getZ(), arena.pos2.getZ());
		int Z2 = (int) Math.max(arena.pos1.getZ(), arena.pos2.getZ()) + 1;

		Random random = new Random();

		for(int x = X1; x < X2; x++){
			for(int y = Y1; y < Y2; y++){
				for(int z = Z1; z < Z2; z++){
					Location loc = new Location(arena.pos1.getWorld(), x, y, z);
					Integer porce = random.nextInt(100);

					Boolean set = false;
					int fix = 0;
					for(Integer value : arena.blocks.keySet()){
						String block = arena.blocks.get(value);
						if((value + fix) > porce){
							loc.getBlock().setType(Material.getMaterial(block));
							set = true;
							break;

						}
						else
						{
							fix = fix + value;
						}
					}
					if(!set){
						loc.getBlock().setType(Material.AIR);
					}
				}
			}
		}
		
		/* TELEPORT ALL PLAYERS ON MINE TO SURFACE */
		for(Player _currentPlayer : arena.pos1.getWorld().getPlayers()){
			if(X2-1 >= _currentPlayer.getLocation().getBlockX() && Y2-1 >= _currentPlayer.getLocation().getBlockY() && Z2-1 >= _currentPlayer.getLocation().getBlockZ()){
				if(X1 <= _currentPlayer.getLocation().getBlockX() && Y1 <= _currentPlayer.getLocation().getBlockY() && Z1 <= _currentPlayer.getLocation().getBlockZ()){
					_currentPlayer.teleport(new Location(arena.pos1.getWorld(), _currentPlayer.getLocation().getX(), (Y2 + 1), _currentPlayer.getLocation().getZ(), _currentPlayer.getLocation().getYaw(), _currentPlayer.getLocation().getPitch()));
				}
			}
		}
	}
}

