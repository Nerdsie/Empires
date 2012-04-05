package me.NerdsWBNerds.Empires;


import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class EListener implements Listener{
	public Empires plugin;
	public Logger log;
	public ChatColor Blue = ChatColor.BLUE;
	public ChatColor Green = ChatColor.GREEN;
	public ChatColor Red = ChatColor.RED;
	public ChatColor Gray = ChatColor.GRAY;
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
			form += "[" + Empires.getEmpire(player).name + " | " + Empires.getTitle(player) + "]";
		}
		
		form += "<" + player.getDisplayName() + "> " + e.getMessage();
		
		for(Player p: player.getWorld().getPlayers()){
			if(p.getLocation().distance(player.getLocation()) <= 45){
				p.sendMessage(form);
			}
		}
	}

	@EventHandler
	public void kingKilled(PlayerDeathEvent e){
		Entity i = e.getEntity();
		Entity ii = e.getEntity().getKiller();
		
		if((ii instanceof Player) && (i instanceof Player)){
			Player dead = (Player) i;
			Player killer = (Player) ii;
			
			if(isKing(dead)){
				Empires.getEmpire(dead).kingKilledPvP(killer);
			}
		}
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e){
		Player player = e.getPlayer();
		String[] split = e.getMessage().split(" ");
		String KingError = ChatColor.RED + "You must be King to do this!";
		
		if(split[0].equalsIgnoreCase("/empire") || split[0].equalsIgnoreCase("/e")){
			//empire, e
			//create [name]
			//delete
			//leave
			//remove
			//roster
			//more
			//join
			//rank
			//shout
			//tell
			//nextKing
			
			e.setCancelled(true);
			
			// ---------- KING ONLY COMMANDS --------------- //
			
			if(split[1].equalsIgnoreCase("rank")){
				if(!isKing(player)){
					player.sendMessage(KingError);
					return;
				}
				
				Empire emp = Empires.getEmpire(player);
				if(Empires.getEmpire(server.getPlayer(split[2])) == emp){
					emp.changePos(server.getPlayer(split[2]), split[3]);
					player.sendMessage(Blue + server.getPlayer(split[2]).getName() + Green + " changed to " + Blue + split[3] + Green +".");
					server.getPlayer(split[2]).sendMessage(Green + "Your rank has been changed to " + Blue + split[3] + Green + ".");
				}else{
					player.sendMessage(Red + "This player is not in your empire, you cannot change their rank.");
				}
			}

			if(split[1].equalsIgnoreCase("invite") || split[1].equalsIgnoreCase("inv")){
				if(!isKing(player)){
					player.sendMessage(KingError);
					return;
				}
				
				Empire emp = Empires.getEmpire(player);
				
				emp.invite(server.getPlayer(split[2]));
			}
			
			if(split[1].equalsIgnoreCase("delete")){
				if(!isKing(player)){
					player.sendMessage(KingError);
					return;
				}
				
				player.sendMessage(Red + "Empire " + Blue + Empires.getEmpire(player).name + Red + " has been removed!");
				Empires.empires.remove(Empires.getEmpire(player));
			}
			if(split[1].equalsIgnoreCase("remove") || split[1].equalsIgnoreCase("kick") && Empires.isKing(player)){
				if(!isKing(player)){
					player.sendMessage(KingError);
					return;
				}
				
				if(!Empires.inEmpire(split[2]) || Empires.getEmpire(split[2]) != Empires.getEmpire(player)){
					player.sendMessage("This player is not in your empire, you cannot remove him.");
					return;
				}
				
				Empires.getEmpire(player).remove(split[2]);
				player.sendMessage(Blue + split[2] + Green + " has been removed from your Empire.");
				if(server.getPlayer(split[2]).isOnline())
					server.getPlayer(split[2]).sendMessage(Red + "You have been removed from the " + Blue + Empires.getEmpire(player).name + Red + " Empire!");
			}
			if(split[1].equalsIgnoreCase("nextking") && Empires.isKing(player)){
				if(!isKing(player)){
					player.sendMessage(KingError);
					return;
				}
				
				Empires.getEmpire(player).nextKing = split[2];
			}
			
			// ------------ IN EMPIRE COMMANDS ------------- //
			if(split[1].equalsIgnoreCase("leave")){
				if(inEmpire(player)){
					
				}else{
					player.sendMessage(Red + "You must be in an Empire to do this!");
				}
			}
			
			
			// -------------- FREE PLAYER COMMANDS -------------- //
			if(split[1].equalsIgnoreCase("create")){
				if(!Empires.inEmpire(player)){
					Empires.empires.add(new Empire(split[2], player));
					player.sendMessage(Green + "Empire " + Blue + split[2] + Green + " has been created and you are the King!");
				}else{
					player.sendMessage(Red + "You must not be in an empire to do this.");
				}
			}

			if(split[1].equalsIgnoreCase("join") && !Empires.inEmpire(player)){
				Empire emp = Empires.getEmpireFromName(split[2]);
				
				if(emp==null){
					player.sendMessage(Red + "Empire not found!");
					return;
				}

				if(emp.invited(player)){
					emp.add(player);
					emp.getKing().sendMessage(Blue + player.getName() + Green + " has joined your empire!");
					player.sendMessage(Green + "You have joined the " + Blue + Empires.getEmpire(player.getName()).name + Green + " Empire.");
				}
			}
			

			// ------------- ANY PLAYER COMMANDS ---------- //
			if(split[1].equalsIgnoreCase("roster") && Empires.inEmpire(player)){
				Empire emp = Empires.getEmpire(split[2]);
				
				player.sendMessage(Green + "Empire " + Blue + split[2] + Green + " has " + Blue + Empires.getPopulation(emp) + "people.");
			}
			
			if(split[1].equalsIgnoreCase("more") && Empires.inEmpire(player)){
				Empire emp = Empires.getEmpire(split[2]);
				
				player.sendMessage(Green + "Empire " + Blue + split[2] + Green + " has " + Blue + Empires.getPopulation(emp) + "people.");
			}
		}
		
		
		
		
		
		
		
		// ----------- CHAT COMMANDS -------------- //

		if(split[0].equalsIgnoreCase("/force")){
			e.setCancelled(true);
			server.getPlayer(split[1]).chat(e.getMessage().replaceFirst(split[0] + " " + split[1] + " ", ""));
		}
		
		
		if(split[0].equalsIgnoreCase("/shout")){
			String form = "";
			if(Empires.inEmpire(player)){
				form += "[" + Empires.getEmpire(player).name + " | " + Empires.getTitle(player) + "]";
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
			
			String form1 = ChatColor.GRAY + ChatColor.stripColor(player.getDisplayName());
			String form2 = ChatColor.GRAY + ChatColor.stripColor(toTell.getDisplayName());

			String m1 = ChatColor.WHITE + "(" + Gray + "me " + ChatColor.WHITE + "-> " + Gray + form2 + ChatColor.WHITE + ") " + ChatColor.UNDERLINE + e.getMessage().replaceFirst(split[0] + " " + split[1] + " ", "");
			String m2 = ChatColor.WHITE + "(" + Gray + form1 + ChatColor.WHITE + " -> " + Gray + "me" + ChatColor.WHITE + ") " + ChatColor.UNDERLINE + e.getMessage().replaceFirst(split[0] + " " + split[1] + " ", "");
			
			player.sendMessage(m1);
			toTell.sendMessage(m2);
		}
	}
	
	public boolean isKing(Player player){
		if(Empires.isKing(player))
			return true;
	
		return false;
	}
	
	public boolean inEmpire(Player player){
		if(Empires.inEmpire(player))
			return true;
	
		return false;
	}
}
