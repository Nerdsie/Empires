package me.NerdsWBNerds.Empires;


import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

    /**
     * READ THIS: PlayerCommandPreproccessEvent is being DEPRECATED for command parsing!
     *            Use EmpiresCommandExecutor instead!
     * @deprecated as of 1.1.  Replaced by EmpiresCommandExecutor.
     */
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e){
		Player player = e.getPlayer();
		String[] split = e.getMessage().split(" ");
		
		if(split[0].equalsIgnoreCase("/empire") || split[0].equalsIgnoreCase("/e")){
			e.setCancelled(true);
			
			if(split[1].equalsIgnoreCase("create") && !Empires.inEmpire(player)){
				Empires.empires.add(new Empire(split[2], player));
				player.sendMessage(Green + "Empire " + Blue + split[2] + Green + " has been created and you are the King!");
			}
			if(split[1].equalsIgnoreCase("delete") && Empires.isKing(player)){
				Empires.empires.remove(Empires.getEmpire(player));
				player.sendMessage(Red + "Empire " + Blue + split[2] + Red + " has been removed!");
			}
			if(split[1].equalsIgnoreCase("roster") && Empires.inEmpire(player)){
				Empire emp = Empires.getEmpire(split[2]);
				
				player.sendMessage(Green + "Empire " + Blue + split[2] + Green + " has " + Blue + Empires.getPopulation(emp) + "people.");
				int max = Math.min(16, emp.people.size() + 1);
				int maxx = Math.max(16, emp.people.size() + 1);
				
				for(int i = 0; i < max / 2; i++){
					if(i == 0 && max<=16)
						player.sendMessage(Blue + emp.King.getName() + "-King  ||  " + emp.getPlayer(i).getName()+ "-" + emp.people.get(i).getTitle());
					else
						player.sendMessage(Blue + emp.getPlayer(i).getName()+ "-" + emp.people.get(i).getTitle() + "  ||  " + emp.getPlayer(i).player.getName() + "-" + emp.people.get(i).title);						
				}
				player.sendMessage(ChatColor.GRAY + "Use /more to see more pages! Page 1 of " + maxx / 2);
			}
			if(split[1].equalsIgnoreCase("more") && Empires.inEmpire(player)){
				Empire emp = Empires.getEmpire(split[2]);
				
				player.sendMessage(Green + "Empire " + Blue + split[2] + Green + " has " + Blue + Empires.getPopulation(emp) + "people.");
				int max = Math.min(16, emp.people.size() + 1);
				int maxx = Math.max(16, emp.people.size() + 1);
				
				for(int i = 0; i < max / 2; i++){
					if(i == 0 && max<=16)
						player.sendMessage(Blue + emp.King.getName() + "-King  ||  " + emp.getPlayer(i).getName()+ "-" + emp.people.get(i).getTitle());
					else
						player.sendMessage(Blue + emp.getPlayer(i).getName()+ "-" + emp.people.get(i).getTitle() + "  ||  " + emp.getPlayer(i).player.getName() + "-" + emp.people.get(i).title);						
				}
				player.sendMessage(ChatColor.GRAY + "Use /more to see more pages! Page 1 of " + maxx / 2);
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
			
			String form1 = "";
			if(Empires.inEmpire(player)){
				form1 += "" + Empires.getEmpire(player).name + " | " + Empires.getTitle(player) + "]";
			}
			form1 += " " + ChatColor.GRAY + ChatColor.stripColor(player.getDisplayName());
			
			String form2 = "";
			if(Empires.inEmpire(toTell)){
				form2 += "[" + Empires.getEmpire(toTell).name + " | " + Empires.getTitle(toTell) + "]";
			}
			form2 += " " + ChatColor.GRAY + ChatColor.stripColor(toTell.getDisplayName());

			String m1 = ChatColor.WHITE + "(" + Gray + "me " + ChatColor.WHITE + "-> " + Gray + form2 + ChatColor.WHITE + ") " + ChatColor.UNDERLINE + e.getMessage().replaceFirst(split[0] + " " + split[1] + " ", "");
			String m2 = ChatColor.WHITE + "(" + Gray + form1 + ChatColor.WHITE + " -> " + Gray + "me" + ChatColor.WHITE + ") " + ChatColor.UNDERLINE + e.getMessage().replaceFirst(split[0] + " " + split[1] + " ", "");
			
			player.sendMessage(m1);
			toTell.sendMessage(m2);
		}
	}
}
