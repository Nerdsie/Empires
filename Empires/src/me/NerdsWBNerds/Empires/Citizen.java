package me.NerdsWBNerds.Empires;

import org.bukkit.entity.Player;

public class Citizen {
	public String player;
	public String title;
	
	public Citizen(String p, String t){
		player = p;
		title = t;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getName(){
		return player;
	}
}
