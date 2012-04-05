package me.NerdsWBNerds.Empires;

import org.bukkit.entity.Player;

public class FileHandling {
	public Empires plugin;
	
	public FileHandling(Empires p){
		plugin = p;
	}
	
	public void save(){
		saveEmpires();
		saveKings();
		saveCitizens();
		plugin.saveConfig();
	}
	
	public void saveCitizens(){
		int i = 1;
		int ii = 1;
		
		for(Empire e : plugin.empires){
			for(Citizen c : e.people){
				plugin.getConfig().set("Empires." + i + ".Citizen", ii);
				plugin.getConfig().set("Empires." + i + ".Citizen." + ii + ".Name", c.getName());
				plugin.getConfig().set("Empires." + i + ".Citizen." + ii + ".Rank", c.title);
				ii++;
			}
			i++;
		}
	}
	
	public void saveKings(){
		int i = 1;
		for(Empire e : plugin.empires){
			String kingName = "";
			if(e.nextKing!= null){
				kingName = e.nextKing;
			}

			plugin.getConfig().set("Empires." + i + ".King", e.getKingName());
			plugin.getConfig().set("Empires." + i + ".NextKing", kingName);
			i++;
		}
	}
	
	public void saveEmpires(){
		int i = 1;
		
		for(Empire e : plugin.empires){
			plugin.getConfig().set("Empires." + i + ".Name", e.name);
			i++;
		}
	}
	
	public void load(){
		int i = 1;
		
		boolean go = true;
		while(go){
			if(plugin.getConfig().get("Empires." + i + ".Name") != null){
				plugin.empires.add(new Empire(plugin.getConfig().getString("Empires." + i + ".Name"), (String)plugin.getConfig().get("Empires." + i + ".King")));
					int ii = 1;
				
					boolean goo = true;
					while(goo){
						if(plugin.getConfig().getString("Empires." + i + ".Citizen." + ii + ".Name") != null){
							plugin.empires.get(i - 1).people.add(new Citizen((String)plugin.getConfig().get("Empires." + i + ".Citizen." + ii + ".Name"), (String)plugin.getConfig().get("Empires." + i + ".Citizen." + ii + ".Rank")));
							ii++;
						}else{
							goo = false;;
						}
					}
				i++;
			}else{
				go = false;
			}
		}
	}
}
