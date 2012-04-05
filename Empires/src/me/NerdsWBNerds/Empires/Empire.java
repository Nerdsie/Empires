package me.NerdsWBNerds.Empires;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Empire {
	public String name;
	public String King, nextKing;
	public ArrayList<Citizen> people = new ArrayList<Citizen>();
	public ArrayList<String> invites = new ArrayList<String>();
	
	public Empire(String n, String king){
		King = king;
		name = n;
	}
	public Empire(String n, Player king){
		King = king.getName();
		name = n;
	}

	public void add(String player){
		if(!Empires.inEmpire(player)){
			people.add(new Citizen(player, "Citizen"));
		}
	}

	public boolean invited(Player player){
		if(invites.contains(player.getName())){
			return true;
		}
		
		return false;
	}
	
	public void changePos(Player player, String newPos){
		this.getPlayer(player).title = newPos;
	}
	
	public void add(Player player){
		if(!Empires.inEmpire(player)){
			people.add(new Citizen(player.getName(), "Citizen"));
		}
	}

	public void invite(Player player){
		if(!invites.contains(player.getName())){
			invites.add(player.getName());
			Empires.server.getPlayer(King).sendMessage(ChatColor.AQUA + player.getName() + ChatColor.GREEN + " invited to Empire!");
			player.sendMessage(ChatColor.GREEN + "You have been invited to the " + ChatColor.AQUA + name + ChatColor.GREEN+ " Empire! Type " + ChatColor.AQUA + "/empire join " + name + ChatColor.GREEN + " to join.");
		}
	}

	public void invite(String player){
		if(!invites.contains(player)){
			invites.add(player);
			Player p = Empires.server.getPlayer(player);
			Empires.server.getPlayer(King).sendMessage(ChatColor.AQUA + player + ChatColor.GREEN + " invited to Empire!");
			if(p.isOnline()){		
				p.sendMessage(ChatColor.GREEN + "You have been invited to the " + ChatColor.AQUA + name + ChatColor.GREEN+ " Empire! Type " + ChatColor.AQUA + "/empire join " + name + ChatColor.GREEN + " to join.");
			}
		}
	}
	
	public void remove(String player){
		if(people.contains(getPlayer(player))){
			people.remove(getPlayer(player));
		}
	}
	
	public void kingKilledPvP(Player newKing){
		remove(newKing.getName());
		remove(King);
		King = newKing.getName();
	}
	
	public void kingKilledOther(){
		add(King);
		King = nextKing;
	}
	
	public Citizen getPlayer(Player player){
		for(Citizen c : people){
			if(c.player.equalsIgnoreCase(player.getName())){
				return c;
			}
		}
		
		return null;
	}
	
	public Player getKing(){
		return Empires.server.getPlayer(getKingName());
	}
	
	public String getKingName(){
		return King;
	}
	
	public Citizen getPlayer(String player){
		for(Citizen c : people){
			if(c.player.equalsIgnoreCase(player)){
				return c;
			}
		}
		
		return null;
	}
	
	public Citizen getPlayer(int i){		
		return people.get(i);
	}
	
	public String getTitle(Player player){
		for(Citizen c: people){
			if(c.player.equalsIgnoreCase(player.getName())){
				return c.title;
			}
		}
		
		return null;
	}
	
	public String getTitle(String player){
		for(Citizen c: people){
			if(c.player.equalsIgnoreCase(player)){
				return c.title;
			}
		}
		
		return null;
	}
}
