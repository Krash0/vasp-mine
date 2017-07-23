package me.krash0.vm.commands;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.krash0.vm.VM;
import me.krash0.vm.datatype.SelectPos;

public class VMCommands implements CommandExecutor {
	private VM plugin;
	
	public VMCommands () {
		plugin = VM.getInstance();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
		if(command.getName().equalsIgnoreCase("VM")){
			if(!(sender instanceof Player)){
				return false;
			}
			
			Player p = (Player) sender;
			
			if(!p.isOp() && !p.hasPermission("Mine.Staff")){
				p.sendMessage(plugin.getLanguage().getMsg("no_permission"));
				return false;
			}
			
			if(args.length < 1){
				p.sendMessage("§6[MINE] - Commands:");
				p.sendMessage(" §3/VM create [mine] §b- criar uma mina.");
				p.sendMessage(" §3/VM delete [mine] §b- deletar uma mina.");
				p.sendMessage(" §3/VM setpos §b- entrar/sair no modo de setar posições.");
				p.sendMessage(" §3/VM addblock [mine] [porcentage] §b- adicionar o bloco a mina.");
				p.sendMessage(" §3/VM removeblock [mine] §b- remover o bloco da mina.");
				p.sendMessage(" §3/VM setresetinterval [mine] [interval] §b- setar intervalo para a mina resetar.");
				p.sendMessage(" §3/VM enable [mine] §b- ativar uma mina.");
				p.sendMessage(" §3/VM disable [mine] §b- desativar uma mina.");
				p.sendMessage(" §3/VM reset [mine] §b- resetar uma mina.");
				return false;
			}
			
			if(args[0].equalsIgnoreCase("create")){
				if(args.length == 2){
					String name = args[1];

					SelectPos selectPos = plugin.getMineManager().getSelectPos().get(p.getName());
					
					if(selectPos == null || selectPos.pos1 == null || selectPos.pos2 == null){
						p.sendMessage(plugin.getLanguage().getMsg("not_setted_positions"));
						return false;
					}

					if(plugin.getMineManager().createMine(name, selectPos)){
						p.sendMessage(plugin.getLanguage().getMsg("mine_created").replace("{mine}", name));
						return true;
					}

					p.sendMessage(plugin.getLanguage().getMsg("mine_exist").replace("{mine}", name));
					return false;
				}
				p.sendMessage("§7Use: /vm create [mine].");
				return false;
			}

			if(args[0].equalsIgnoreCase("delete")){
				if(args.length == 2){
					String name = args[1];
					if(plugin.getMineManager().deleteMine(name)){
						p.sendMessage(plugin.getLanguage().getMsg("mine_deleted").replace("{mine}", name));
						return true;
					}
					p.sendMessage(plugin.getLanguage().getMsg("mine_not_exist").replace("{mine}", name));
					return false;
				}
				p.sendMessage("§7Use: /vm delete [mine]");
				return false;
			}
			
			if(args[0].equalsIgnoreCase("setpos")){
				if(plugin.getMineManager().getSelectPos().containsKey(p.getName())){
					plugin.getMineManager().getSelectPos().remove(p.getName());
					p.sendMessage(plugin.getLanguage().getMsg("set_pos_disabled"));
					return true;
				}
				plugin.getMineManager().getSelectPos().put(p.getName(), new SelectPos());
				p.getInventory().addItem(new ItemStack(Material.BLAZE_ROD));
				p.sendMessage(plugin.getLanguage().getMsg("set_pos_activated"));
				return true;
			}

			if(args[0].equalsIgnoreCase("addblock")){
				if(args.length == 3){
					String name = args[1].toLowerCase();
					Integer porcentage = Integer.parseInt(args[2]);

					ItemStack itemStack = p.getItemInHand();
					
					if(itemStack == null || itemStack.getType() == Material.AIR) {
						p.sendMessage(plugin.getLanguage().getMsg("no_block_in_hand"));
						return false;
					}

					int result = plugin.getMineManager().addBlock(name, itemStack, porcentage);
					switch (result) {
					case 0:
						p.sendMessage(plugin.getLanguage().getMsg("mine_not_exist").replace("{mine}", name));
						return true;
					case 1:
						p.sendMessage(plugin.getLanguage().getMsg("block_exist").replace("{block}", itemStack.getType().name()));
						return true;
					case 2:
						p.sendMessage(plugin.getLanguage().getMsg("block_added").replace("{block}", itemStack.getType().name()));
						return true;
					}
					return false;
				}
				sender.sendMessage("§7Use: /vm addblock [mine] [porcentage].");
				return false;
			}
			
			if(args[0].equalsIgnoreCase("removeblock")){
				if(args.length == 2){
					String name = args[1].toLowerCase();
					ItemStack itemStack = p.getItemInHand();
					
					if(itemStack == null || itemStack.getType() == Material.AIR) {
						p.sendMessage(plugin.getLanguage().getMsg("no_block_in_hand"));
						return false;
					}
					
					int result = plugin.getMineManager().removeBlock(name, itemStack);

					switch (result) {
					case 0:
						p.sendMessage(plugin.getLanguage().getMsg("mine_not_exist").replace("{mine}", name));
						return true;
					case 1:
						p.sendMessage(plugin.getLanguage().getMsg("block_not_exist").replace("{block}", itemStack.getType().name()));
						return true;
					case 2:
						p.sendMessage(plugin.getLanguage().getMsg("block_removed").replace("{block}", itemStack.getType().name()));
						return true;
					}
					return false;
				}
				sender.sendMessage("§7Use: /vm removeblock [mine]");
				return false;
			}

			if(args[0].equalsIgnoreCase("setresetinterval")){
				if(args.length == 3){
					String name = args[1];
					int interval = NumberUtils.toInt(args[2]);
					if(plugin.getMineManager().setIntervalReset(name, interval)){
						p.sendMessage(plugin.getLanguage().getMsg("interval_reset_setted").replace("{mine}", name).replace("{minutes}", interval + ""));
						return true;
					}

					p.sendMessage(plugin.getLanguage().getMsg("mine_not_exist").replace("{mine}", name));
					return false;
				}
				p.sendMessage("§7Use: /vm setresetinterval [mine] [interval].");
				return false;
			}

			if(args[0].equalsIgnoreCase("enable")){
				if(args.length == 2){
					String name = args[1];
					if(plugin.getMineManager().setEnable(name, true)){
						p.sendMessage(plugin.getLanguage().getMsg("mine_enabled").replace("{mine}", name));
						return true;
					}
					p.sendMessage(plugin.getLanguage().getMsg("mine_not_exist").replace("{mine}", name));
					return false;
				}
				p.sendMessage("§7Use: /vm enable [mine]");
				return false;
			}
			
			if(args[0].equalsIgnoreCase("disable")){
				if(args.length == 2){
					String name = args[1];
					if(plugin.getMineManager().setEnable(name, false)){
						p.sendMessage(plugin.getLanguage().getMsg("mine_disabled").replace("{mine}", name));
						return true;
					}
					p.sendMessage(plugin.getLanguage().getMsg("mine_not_exist").replace("{mine}", name));
					return false;
				}
				p.sendMessage("§7Use: /vm disable [mine]");
				return false;
			}
			
			if(args[0].equalsIgnoreCase("reset")){
				if(args.length == 2){
					String name = args[1];
					if(plugin.getMineManager().resetArenaByName(name)){
						p.sendMessage(plugin.getLanguage().getMsg("mine_reseted").replace("{mine}", name));
						return true;
					}
					p.sendMessage(plugin.getLanguage().getMsg("mine_not_exist").replace("{mine}", name));
					return false;
				}
				p.sendMessage("§7Use: /vm reset [mine]");
				return false;
			}
			return false;
		}
		return false;
	}
}
