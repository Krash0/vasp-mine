package vm;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Location;

public class Arena {
	Arena(String name, Boolean enable, Location pos1,Location pos2) {
		this.name = name;
		this.enable = enable;
		this.pos1 = pos1;
		this.pos2 = pos2;
	}
	Integer intervalReset = 0;
	String name;
	Boolean enable;
	Location pos1, pos2;
	Map<Integer, String> blocks = new TreeMap<Integer, String>();
}
