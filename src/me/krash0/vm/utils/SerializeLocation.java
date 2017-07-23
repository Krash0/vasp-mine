package me.krash0.vm.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class SerializeLocation {
	public static String serializeLocation(Location loc, boolean isPlayer){
		String world = loc.getWorld().getName();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		
		String _temp = null;
		if(isPlayer){
			float yaw = loc.getYaw();
			float pitch = loc.getPitch();
			_temp = world + " " + x + " " +  y + " " +  z + " " + yaw+ " " + pitch;
		}else{
			_temp = world + " " + x + " " +  y + " " +  z;
		}
		return _temp;
	}
	
	public static Location deserializeLocation(String s, boolean isPlayer){
		String[] _split = s.split(" ");
		World world = Bukkit.getWorld(_split[0]);
		double x = Double.valueOf(_split[1]);
		double y = Double.valueOf(_split[2]);
		double z = Double.valueOf(_split[3]);
		Location _temp = null;
		if(isPlayer){
			float yaw = Float.valueOf(_split[4]);
			float pitch = Float.valueOf(_split[5]);
			_temp = new Location(world, x, y, z, yaw, pitch);
		}else{
			_temp = new Location(world, x, y, z);
		}
		return _temp;
	}
}
