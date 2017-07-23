package me.krash0.vm.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.krash0.vm.VM;
import me.krash0.vm.datatype.SelectPos;

public class PlayerEvents implements Listener {
	private VM plugin;
	
	public PlayerEvents() {
		plugin = VM.getInstance();
	}
	
	@EventHandler
	void playerInteract(PlayerInteractEvent e){
		if(e.getPlayer().getItemInHand().getType() == Material.BLAZE_ROD){
			SelectPos selectPos = plugin.getMineManager().getSelectPos().get(e.getPlayer().getName());
			if(selectPos == null) {return;}
			
			if(e.getAction() == Action.LEFT_CLICK_BLOCK){
				selectPos.pos1 = e.getClickedBlock().getLocation();
				e.getPlayer().sendMessage("§bPos 1 setada com sucesso.");
				e.setCancelled(true);
				return;
			}
			
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
				selectPos.pos2 = e.getClickedBlock().getLocation();
				e.getPlayer().sendMessage("§bPos 2 setada com sucesso.");
				e.setCancelled(true);
				return;
			}
		}
	}
}
