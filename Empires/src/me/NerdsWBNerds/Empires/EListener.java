package me.NerdsWBNerds.Empires;


import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class EListener implements Listener{
	public Empires plugin;
	public ChatColor Blue = ChatColor.BLUE;
	public ChatColor Green = ChatColor.GREEN;
	public ChatColor Red = ChatColor.RED;
	public Server server;
	
	public EListener(Empires hc) {
		plugin = hc;
		server = hc.getServer();
	}

	public ArrayList<Player> hc = new ArrayList<Player>();
	public HashMap<Player, Integer> cuffs = new HashMap<Player, Integer>();

	@EventHandler
	public void onClick(PlayerInteractEntityEvent e){
		Player player = e.getPlayer();
		
		if(isCuffed(player)){
			return;
		}
		
		if(e.getRightClicked() instanceof Player){
			Player target = (Player) e.getRightClicked();
			
			if(inHand(player, Material.STRING)){
				if(isCuffed(target)){
					player.sendMessage(Blue + target.getName() + Red + " is already handcuffed.");
				}else{
					setCuffed(target, true, player);
				}
			}
			
			if(inHand(player, Material.SHEARS)){
				if(!isCuffed(target)){
					player.sendMessage(Blue + target.getName() + Red + " is already free.");
				}else{
					target.sendMessage(Blue + player.getName() + Green + " has freed you.");
					setCuffed(target, false, player);
				}
			}
		}
	}

	@EventHandler
	public void onRun(PlayerMoveEvent e){
		Player player = e.getPlayer();
		
		if(player.getGameMode() == GameMode.CREATIVE){
			return;
		}
		
		if(player.isSprinting() && isCuffed(player)){
			player.sendMessage(Red + "You are handcuffed! You must walk!");
			player.setSprinting(false);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		Player player = e.getPlayer();
		
		if(player.getGameMode() == GameMode.CREATIVE){
			return;
		}
				
		if(isCuffed(player)){
			player.sendMessage(Red + "You are handcuffed! You cannot do this!");
			e.setCancelled(true);
		}
	}
	
	public void onDamaged(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
			Player source = (Player) e.getDamager();
			
			if(isCuffed(source)){
				source.sendMessage(Red + "You are handcuffed! You cannot do this!");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e){
		Player player = e.getPlayer();
		String split[] = e.getMessage().split(" ");
		int size = split.length;
		
		if(player.isOp()){
			if(split[0].equalsIgnoreCase("/hc")){
				e.setCancelled(true);
				
				if(split.length >= 2){
					if(split.length==3){
						Player target = plugin.getServer().getPlayer(split[2]);

						if(split[1].equalsIgnoreCase("free") && !isCuffed(target)){
							setCuffed(target, false, player);
							target.sendMessage(Green + "You are now free!");
						}
						if(split[1].equalsIgnoreCase("cuff") && !isCuffed(target)){
							setCuffed(target, true, player);
							target.sendMessage(Green + "You are now cuffed!");
						}
						return;
					}
					if(split[1].equalsIgnoreCase("free") && !isCuffed(player)){
						setCuffed(player, false, player);
						player.sendMessage(Green + "You are now free!");
					}
					if(split[1].equalsIgnoreCase("cuff") && !isCuffed(player)){
						setCuffed(player, true, player);
						player.sendMessage(Green + "You are now cuffed!");
					}
				
					if(split[1].equalsIgnoreCase("amt")){
						player.sendMessage(Blue + "You have " + Green + howMany(player) + Blue + " handcuffs left!");
					}
				}				
			}
		}
	}
	
	public void setCuffed(Player player, boolean flag, Player police){
		if(isCuffed(player) && !flag){
			hc.remove(player);
			police.sendMessage(Blue + player.getName() + Green + " is now free.");
			player.sendMessage(Blue + police.getName() + Green + " has freed you.");
		}
		
		if(flag && !isCuffed(player)){
			if(howMany(police)==0){
				police.sendMessage(Red + "You have no handcuffs on you!");
				return;
			}
			
			cuffs.put(police, cuffs.get(police) - 1);
			hc.add(player);
			police.sendMessage(Blue + player.getName() + Green + " is now cuffed.");
			police.sendMessage(Blue + "You have " + Green + howMany(police) + Blue + " handcuffs left!");
			player.sendMessage(Blue + police.getName() + Green + " has handcuffed you.");
		}
	}
	
	public boolean isCuffed(Player player){
		if(hc.contains(player)){
			return true;
		}
		
		return false;
	}
	
	public boolean inHand(Player player, Object i){
		if(player.getItemInHand().getType() == i ){
			return true;
		}
		
		return false;
	}
	
	public int howMany(Player player){
		if(cuffs.containsKey(player)){
			return cuffs.get(player);
		}
		
		return 0;
	}
}
