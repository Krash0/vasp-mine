package me.krash0.vm.managers;

import org.bukkit.ChatColor;

import me.krash0.vm.VM;
import me.krash0.vm.utils.YAMLCore;

public class LanguageManager extends YAMLCore {
	public LanguageManager() {
		super(VM.getInstance(), "language.yml");
	}
	
	public String getMsg(String path) {
		return ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix") + " " + getConfig().getString(path));
	}
}
