package me.NerdsWBNerds.Empires;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

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
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class EListener implements Listener{
	public Empires plugin;
	public Logger log;
	public ChatColor Blue = ChatColor.BLUE;
	public ChatColor Green = ChatColor.GREEN;
	public ChatColor Red = ChatColor.RED;
	public Server server;
	
	public EListener(Empires hc) {
		plugin = hc;
	}

	@EventHandler
	public void onChat(PlayerChatEvent e){
		e.setCancelled(true);
		
		Player player = e.getPlayer();
		
		String form = "";
		if(Empires.inEmpire(player)){
			form += "[ " + Empires.getEmpire(player).name + " | " + Empires.getTitle(player) + " ]";
		}
		
		form += "<" + player.getDisplayName() + "> " + e.getMessage();
		
		for(Player p: player.getWorld().getPlayers()){
			if(p.getLocation().distance(player.getLocation()) <= 45){
				p.sendMessage(form);
			}
		}
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e){
		Player player = e.getPlayer();
		String[] split = e.getMessage().split(" ");
		
		if(split[0].equalsIgnoreCase("/empire")){
			e.setCancelled(true);
			
			if(split[1].equalsIgnoreCase("create") && !Empires.inEmpire(player)){
				Empires.empires.add(new Empire(split[2], player));
			}
			if(split[1].equalsIgnoreCase("rank") && Empires.inEmpire(player)){
				if(Empires.isKing(player)){
					Empire emp = Empires.getEmpire(player);
					if(Empires.getEmpire(server.getPlayer(split[2])) == emp){
						
					}else{
						player.sendMessage(Red + "This player is not in your empire, you cannot change their rank.");
					}
				}else{
					player.sendMessage(Red + "You must be the King of an Empire to do this!");
				}
			}
		}
		
		if(split[0].equalsIgnoreCase("/shout")){
			String form = "";
			if(Empires.inEmpire(player)){
				form += "[ " + Empires.getEmpire(player).name + " | " + Empires.getTitle(player) + " ]";
			}
			
			form += "<" + player.getDisplayName() + "> " + e.getMessage().replaceFirst(split[0] + " ", "");
			
			for(Player p: player.getWorld().getPlayers()){
				if(p.getLocation().distance(player.getLocation()) <= 120){
					p.sendMessage(form);
				}
			}
		}
		
		if(split[0].equalsIgnoreCase("/tell")){
			Player toTell = server.getPlayer(split[1]);
			
			if(player.getLocation().distance(toTell.getLocation()) >= 4){
				player.sendMessage(Red + "You must be closer to him to do this!");
				return;
			}
			
			String form1 = "";
			if(Empires.inEmpire(player)){
				form1 += "" + Empires.getEmpire(player).name + " | " + Empires.getTitle(player) + " ]";
			}
			form1 += " " + player.getDisplayName();
			
			String form2 = "";
			if(Empires.inEmpire(toTell)){
				form2 += "[" + Empires.getEmpire(toTell).name + " | " + Empires.getTitle(toTell) + " ]";
			}
			form2 += " " + toTell.getDisplayName();

			String m1 = "[me -> " + form2 + "] " + e.getMessage().replaceFirst(split[0] + " " + split[1] + " ", "");
			String m2 = "[" + form1 + " -> me] " + e.getMessage().replaceFirst(split[0] + " " + split[1] + " ", "");
			
			player.sendMessage(m1);
			toTell.sendMessage(m2);
		}
	}
}
