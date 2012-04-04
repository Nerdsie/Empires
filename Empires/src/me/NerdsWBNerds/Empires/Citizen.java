package me.NerdsWBNerds.Empires;

import org.bukkit.entity.Player;

public class Citizen {
	public Player player;
	public String title;
	
	public Citizen(Player p, String t){
		player = p;
		title = t;
	}
}
