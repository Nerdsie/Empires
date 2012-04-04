package me.NerdsWBNerds.Empires;

import org.bukkit.entity.Player;

public class Citizen {
	public Player player;
	public String title;
	
	public Citizen(Player p, String t){
		player = p;
		title = t;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getName(){
		return player.getName();
	}
}
