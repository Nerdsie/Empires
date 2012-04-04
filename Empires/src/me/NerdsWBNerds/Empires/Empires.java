package me.NerdsWBNerds.Empires;


import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class Empires extends JavaPlugin{
	private EListener Listener = new EListener(this);
	public Logger log;
	String base = null;
	
	public void onEnable(){
		log = this.getLogger();
		base = "[" + this.getDescription().getName() + " v" + this.getDescription().getVersion() + "] ";
		log.info(base + "enabled.");

		getServer().getPluginManager().registerEvents(Listener, this);
		for(int i = 0; i < getServer().getOnlinePlayers().length; i++){
			Listener.cuffs.put(getServer().getOnlinePlayers()[i], 3);
		}
	}
	
	public void onDisable(){
		log.info(base + "disabled.");
	}
}
