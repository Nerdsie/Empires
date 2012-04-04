package me.NerdsWBNerds.Empires;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Empires extends JavaPlugin{
	private EListener Listener = new EListener(this);
	private FileHandling config = new FileHandling(this);
	public Logger log;
	String base = null;

	public static ArrayList<Empire> empires = new ArrayList<Empire>();
	
	@SuppressWarnings("unchecked")
	public void onEnable(){
		log = this.getLogger();
		base = "[" + this.getDescription().getName() + " v" + this.getDescription().getVersion() + "] ";
		log.info(base + "enabled.");

		Listener.server = this.getServer();
		Listener.log = log;
		config.load();
		
		getServer().getPluginManager().registerEvents(Listener, this);
	}
	
	public void onDisable(){
		config.save();
	}
	
	public static Empire getEmpire(Player player){
		for(Empire e : empires){
			if(e.people.contains(player) || e.King == player){
				return e;
			}
		}
		
		return null;
	}

	public static Empire getEmpire(String name){
		for(Empire e : empires){
			if(e.name.startsWith(name)){
				return e;
			}
		}
		
		return null;
	}
	
	public static boolean inEmpire(Player player){
		if(getEmpire(player)==null)
			return false;
		
		return true;			
	}
	
	public static boolean isKing(Player player){
		if(inEmpire(player) && getEmpire(player).King == player){
			return true;
		}
		
		return false;
	}
	
	public static String getTitle(Player player){
		if(isKing(player)){
			return "King";
		}
		return getEmpire(player).getPlayer(player).title;
	}

	public static int getPopulation(Empire e){
		return e.people.size() + 1;
	}
}
