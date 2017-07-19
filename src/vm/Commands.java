package vm;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
		if(command.getName().equalsIgnoreCase("VM")){
			if(!(sender instanceof Player)){
				return false;
			}
			
			Player p = (Player) sender;
			
			if(!p.isOp()  && !p.hasPermission("Vasp.Op")){
				p.sendMessage("§6[MINA] §7Você não tem permissão para fazer isso ^^.");
				return false;
			}
			
			
			if(args.length < 1){
				p.sendMessage("§6[MINA] - Comandos:");
				p.sendMessage(" §3/VM create [name] §b- criar uma mina.");
				p.sendMessage(" §3/VM setpos [name] §b- entrar/sair no modo de setar posições.");
				p.sendMessage(" §3/VM addblock [name] [porcentage] §b- adicionar o bloco a arena.");
				p.sendMessage(" §3/VM setIntervalReset [name] [interval] §b- setar intervalo para a mina resetar.");
				p.sendMessage(" §3/VM enable [name] §b- ativar uma mina.");
				return false;
			}
			
			if(args[0].equalsIgnoreCase("create")){
				if(args.length == 2){
					String name = args[1];

					if(Main.pos1 == null || Main.pos2 == null){
						sender.sendMessage("§6[MINA] §7Você precisa setar as posições primeiro!, use: /vm pos1 e /vm pos2.");
						return false;
					}

					if(Utilities.getInstancia().createArena(name, Main.pos1, Main.pos2)){
						sender.sendMessage("§6[MINA] §bA arena " + name + " foi criada com sucesso.");
						return true;
					}

					sender.sendMessage("§6[MINA] §7A arena " + name + " já existe!");
					return false;
				}
				sender.sendMessage("§6[MINA] §7Use: /vm create [name].");
				return false;
			}

			if(args[0].equalsIgnoreCase("setpos")){
				if(Main.setPos.contains(p.getUniqueId().toString())){
					Main.setPos.remove(p.getUniqueId().toString());
					sender.sendMessage("§6[MINA] §bVocê saiu do modo de setar posições.");
					return true;
				}
				Main.setPos.add(p.getUniqueId().toString());
				p.getInventory().addItem(new ItemStack(Material.BLAZE_ROD));
				sender.sendMessage("§6[MINA] §bVocê entrou no modo de setar posições.");
				return true;
			}

			if(args[0].equalsIgnoreCase("addblock")){
				if(args.length == 3){
					String name = args[1].toLowerCase();
					Integer porcentage = Integer.parseInt(args[2]);

					ItemStack itemStack = p.getItemInHand();

					if(Utilities.getInstancia().addBlock(name.toLowerCase(), itemStack, porcentage)){
						sender.sendMessage("§6[MINA] §bO bloco " + itemStack.getType().name() + " foi setado com sucesso.");
						return true;
					}

					sender.sendMessage("§6[MINA] §7A arena " + name + " não existe!");
					return false;
				}
				sender.sendMessage("§6[MINA] §7Use: /vm addblock [arena] [porcentage].");
				return false;
			}

			if(args[0].equalsIgnoreCase("setIntervalReset")){
				if(args.length == 3){
					String name = args[1];
					Integer interval = Integer.parseInt(args[2]);
					if(Utilities.getInstancia().setIntervalReset(name.toLowerCase(), interval)){
						sender.sendMessage("§6[MINA] §bA arena " + name + " resetará acada " + interval + " segundos.");
						return true;
					}

					sender.sendMessage("§6[MINA] §7A arena " + name + " não existe!");
					return false;
				}
				sender.sendMessage("§6[MINA] §7Use: /vm setIntervalReset [arena] [interval].");
				return false;
			}

			if(args[0].equalsIgnoreCase("enable")){
				if(args.length == 2){
					String name = args[1];
					if(Utilities.getInstancia().setEnable(name.toLowerCase(), true)){
						sender.sendMessage("§6[MINA] §bA arena " + name + " foi ativada com sucesso.");
						return true;
					}
					sender.sendMessage("§6[MINA] §7A arena " + name + " não existe!");
					return false;
				}
			}
			return false;
		}
		return false;
	}
}
