package me.NerdsWBNerds.Empires;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class Empire {
	public String name;
	public Player King, nextKing;
	public ArrayList<Citizen> people = new ArrayList<Citizen>();
	public ArrayList<Player> invites = new ArrayList<Player>();
	
	public Empire(String n, Player king){
		King = king;
		name = n;
	}
	
	public void add(Player player){
		if(!Empires.inEmpire(player)){
			people.add(new Citizen(player, "Citizen"));
		}
	}
	
	public void remove(Player player){
		if(Empires.getEmpire(player) == this){
			people.remove(player);
		}
	}
	
	public void kingKilledPvP(Player newKing){
		remove(newKing);
		add(King);
		King = newKing;
	}
	
	public void kingKilledOther(){
		add(King);
		King = nextKing;
	}
	
	public Citizen getPlayer(Player player){
		for(Citizen c : people){
			if(c.player == player){
				return c;
			}
		}
		
		return null;
	}
}
