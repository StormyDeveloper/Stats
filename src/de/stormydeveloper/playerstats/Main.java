package de.stormydeveloper.playerstats;

import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.stormydeveloper.playerstats.commands.PlayerStatsCommand;
import de.stormydeveloper.playerstats.listener.JoinListener;
import de.stormydeveloper.playerstats.listener.PlayerDeathListener;
import de.stormydeveloper.playerstats.listener.QuitListener;
import de.stormydeveloper.playerstats.sql.MySQL;
import de.stormydeveloper.playerstats.utils.DataManager;

public class Main extends JavaPlugin {
	private static Main plugin;
	public boolean isMysqlEnabled;
	public boolean americanDateStyle;
	
	public HashMap<Player, Long> online_time = new HashMap<Player, Long>();
	
	public static Main getPlugin() {
		return plugin;
	}
	
	
	@Override
	public void onEnable() {
		plugin = this;
		
		this.loadConfig();
		
		
		if(this.getConfig().getString("Datastorage.Type").equalsIgnoreCase("MySQL")) {
			isMysqlEnabled = true;
		}else {
			isMysqlEnabled = false;
		}
		
		americanDateStyle = this.getConfig().getBoolean("UseAmericanDateStyle");
		
		
		if(isMysqlEnabled) {
			try {
				MySQL.getMysql().connect(this.getConfig().getString("Datastorage.MySQL.Host"), this.getConfig().getInt("Datastorage.MySQL.Port")
				,this.getConfig().getString("Datastorage.MySQL.Database"), this.getConfig().getString("Datastorage.MySQL.User"),this.getConfig().getString("Datastorage.MySQL.Password"));
			} catch (SQLException e) {
				Bukkit.getConsoleSender().sendMessage("§7[§6PlayerStats§7] §cBei der MySQL Verbinndung ist ein Fehler aufgetreten! \n§e"+e.getMessage());
			}
		}
		
		
		
		Bukkit.getOnlinePlayers().forEach(all -> {
			final DataManager dm = new DataManager(all.getUniqueId());
			
			if(!dm.isUserExists()) {
				dm.createUser();
			}
			
			if(!online_time.containsKey(all)) {
				online_time.put(all, System.currentTimeMillis());
			}
	
		});
		
		
		
		Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new QuitListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
		
		Bukkit.getPluginCommand("PlayerStats").setExecutor(new PlayerStatsCommand());
	}
	
	@Override
	public void onDisable() {
		
		
		Bukkit.getOnlinePlayers().forEach(all -> {
			final DataManager dm = new DataManager(all.getUniqueId());
			
			if(online_time.containsKey(all)) {
				dm.updateOnlineTime(System.currentTimeMillis() - online_time.get(all));
				online_time.remove(all);
			}
			
	
		});
		
		if(isMysqlEnabled) {
			MySQL.getMysql().close();
		}
	}
	
	private void loadConfig() {
		this.getConfig().options().copyDefaults(true);
		
		this.getConfig().options().header("You can switch between MySQL or File as Datastorage type. Default the Plugin use File.");
		this.getConfig().options().copyHeader(true);
		
		
		this.getConfig().addDefault("Datastorage.Type", "File");
		this.getConfig().addDefault("Datastorage.MySQL.Host", "127.0.0.1");
		this.getConfig().addDefault("Datastorage.MySQL.Port", 3306);
		this.getConfig().addDefault("Datastorage.MySQL.Database", "PlayerStats");
		this.getConfig().addDefault("Datastorage.MySQL.User", "root");
		this.getConfig().addDefault("Datastorage.MySQL.Password", "");
		
		this.getConfig().addDefault("UseAmericanDateStyle", false);
		
		
		this.saveConfig();
	}

}
