package vm;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Events implements Listener {
	@EventHandler
	void playerInteract(PlayerInteractEvent e){
		if(e.getPlayer().getItemInHand().getType() == Material.BLAZE_ROD){
			if(Main.setPos.contains(e.getPlayer().getUniqueId().toString())){
				if(e.getAction() == Action.LEFT_CLICK_BLOCK){
					Main.pos1 = e.getClickedBlock().getLocation();
					e.getPlayer().sendMessage("§bPos 1 setada com sucesso.");
					e.setCancelled(true);
					return;
				}
				if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
					Main.pos2 = e.getClickedBlock().getLocation();
					e.getPlayer().sendMessage("§bPos 2 setada com sucesso.");
					e.setCancelled(true);
					return;
				}
			}
		}
	}
}
