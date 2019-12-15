package de.stormydeveloper.playerstats.utils;

import java.io.File;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import de.stormydeveloper.playerstats.Main;
import de.stormydeveloper.playerstats.sql.MySQL;

public class DataManager {
	private MySQL mysql;
	private String uuid;
	private File file;
	private YamlConfiguration cfg;
	
	
	private void checkDatatTpe() {
		if(Main.getPlugin().isMysqlEnabled) {
			mysql = MySQL.getMysql();
		}else {
			file = new File(Main.getPlugin().getDataFolder() + "/data/"+uuid+".yml");
		    cfg = YamlConfiguration.loadConfiguration(file);
		}
	}
	
	private boolean isMySQLExists() {
		if(mysql == null) {
			return false;
		}else {
			return true;
		}
	}
	
	public DataManager(final String uuid) {
		this.uuid = uuid;
		
		checkDatatTpe();
	}
	
	public DataManager(final UUID uuid) {
		this.uuid = uuid.toString();
		
		checkDatatTpe();
	}
	
	public boolean isUserExists() {
		if(isMySQLExists()) {
			ResultSet rs = mysql.getResult("SELECT * FROM Stats WHERE UUID='"+uuid+"'");
			try {
				return rs.next();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			return file.exists();
		}
		return false;
	}
	
	public void createUser() {
		if(isMySQLExists()) {
			mysql.execute("INSERT INTO Stats (UUID,Kills,Deaths,FirstJoin,OnlineTime) VALUES ('"+uuid+"','0','0',NOW(),'0')");
		}else {
			cfg.set("Kills", 0);
			cfg.set("Deaths", 0);
			
			final GregorianCalendar gregorianCalendar = new GregorianCalendar();
			final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
			final DateFormat timeFormat= DateFormat.getTimeInstance(DateFormat.MEDIUM);
			
			cfg.set("FirstJoin", dateFormat.format(gregorianCalendar.getTime()) + " - " +  timeFormat.format(gregorianCalendar.getTime()));
			cfg.set("OnlineTime", 0);
			
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setLastOnline() {
		if(isMySQLExists()) {
			mysql.update("UPDATE Stats SET LastOnline =NOW() WHERE UUID='"+uuid+"'");
		}else {

			final GregorianCalendar gregorianCalendar = new GregorianCalendar();
			final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
			final DateFormat timeFormat= DateFormat.getTimeInstance(DateFormat.MEDIUM);
			
			cfg.set("LastOnline", dateFormat.format(gregorianCalendar.getTime()) + " - " +  timeFormat.format(gregorianCalendar.getTime()));
			
			
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void updateKills(final int killstoAdd) {
		if(isMySQLExists()) {			
			final int kills = getKills();
			final int killsInDB = kills + killstoAdd;
			mysql.update("UPDATE Stats SET Kills='"+killsInDB+"' WHERE UUID='"+uuid+"'");

		}else {
			cfg.set("Kills", cfg.getInt("Kills") + killstoAdd);
			
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void updateDeaths(final int deathstoAdd) {
		if (isMySQLExists()) {

			final int deaths = getDeaths();
			final int deathsInDB = deaths + deathstoAdd;
			mysql.update("UPDATE Stats SET Deaths='" + deathsInDB + "' WHERE UUID='" + uuid + "'");

		} else {
			cfg.set("Deaths", cfg.getInt("Deaths") + deathstoAdd);

			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void updateOnlineTime(final long millisToAdd) {
		if(isMySQLExists()) {
			final ResultSet rs = mysql.getResult("SELECT * FROM Stats WHERE UUID='"+uuid+"'");
			
			try {
				if(rs.next()) {
					final long millisFromDB = rs.getLong("OnlineTime");
					final long millisToInsert = millisFromDB + millisToAdd;
					mysql.update("UPDATE Stats SET OnlineTime='"+millisToInsert+"' WHERE UUID='"+uuid+"'");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			cfg.set("OnlineTime", cfg.getLong("OnlineTime") + millisToAdd);
			
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getKills() {
		if(isMySQLExists()) {
			final ResultSet rs = mysql.getResult("SELECT * FROM Stats WHERE UUID='"+uuid+"'");
			
			try {
				if(rs.next()) {
					return rs.getInt("Kills");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}else {
			return cfg.getInt("Kills");
		}
		
		return -1;
	}
	
	public int getDeaths() {
		if(isMySQLExists()) {
			final ResultSet rs = mysql.getResult("SELECT * FROM Stats WHERE UUID='"+uuid+"'");
			
			try {
				if(rs.next()) {
					return rs.getInt("Deaths");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}else {
			return cfg.getInt("Deaths");
		}
		
		return -1;
	}
	
	public String getFirstJoin() {
		if(Main.getPlugin().americanDateStyle) {
			if(isMySQLExists()) {
				final ResultSet rs = mysql.getResult("SELECT * FROM Stats WHERE UUID='"+uuid+"'");
				

				try {
					if(rs.next()) {
						String date = rs.getDate("FirstJoin").toString().replace("-", ".");
						String time = rs.getTime("FirstJoin").toString();
						
						String[] split_date = date.split("\\.");
						String year = split_date[0];
						String month = split_date[1];
						String day = split_date[2];
						return month+"."+day+"."+year+" - "+time;
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else {
				String date_and_time = cfg.getString("FirstJoin");
				String[] split_time = date_and_time.split("-");
				String time = split_time[1];
				String[] split_date = split_time[0].split("\\.");
				String year = split_date[2];
				String month = split_date[1];
				String day = split_date[0];
				
				return month+"."+day+"."+year+ "-"+time;
			}
			
			
		}else {
			if(isMySQLExists()) {
				final ResultSet rs = mysql.getResult("SELECT * FROM Stats WHERE UUID='"+uuid+"'");
				

				try {
					if(rs.next()) {
						String date = rs.getDate("FirstJoin").toString();
						String time = rs.getTime("FirstJoin").toString();
						
						
						String date_and_time = date;
						String[] split_date = date_and_time.split("-");
						String year = split_date[0];
						String month = split_date[1];
						String day = split_date[2];
						
						return day+"."+month+"."+year+" - "+time;
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else {
				return cfg.getString("FirstJoin");
	
			}
		}
		
		return "";
	}
	
	public String getOnlineTime() {
		if(Bukkit.getPlayer(UUID.fromString(uuid)) != null) {
			final Long millisFromHashMap = Main.getPlugin().online_time.get(Bukkit.getPlayer(UUID.fromString(uuid)));
			
			if(isMySQLExists()) {
				final ResultSet rs = mysql.getResult("SELECT * FROM Stats WHERE UUID='"+uuid+"'");
				
				try {
					if(rs.next()) {
						long millis = rs.getLong("OnlineTime") + (System.currentTimeMillis() - millisFromHashMap);
						
						long seconds = millis / 1000;
						long minutes = seconds / 60;
						long hours = minutes / 60;
						long days = hours / 24;
						
						return "§6Tage §e-> §b"+days+" §6Stunden §e-> §b"+hours % 24+" §6Minuten §e-> §b"+minutes % 60 + " §6Sekunden §e-> §b"+seconds % 60;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else {
				long millis = cfg.getLong("OnlineTime") + (System.currentTimeMillis() - millisFromHashMap);
				
				long seconds = millis / 1000;
				long minutes = seconds / 60;
				long hours = minutes / 60;
				long days = hours / 24;
				
				return "§6Tage §e-> §b"+days+" §6Stunden §e-> §b"+hours % 24+" §6Minuten §e-> §b"+minutes % 60 + " §6Sekunden §e-> §b"+seconds % 60;
			}
		}else {
			if(isMySQLExists()) {
				final ResultSet rs = mysql.getResult("SELECT * FROM Stats WHERE UUID='"+uuid+"'");
				
				try {
					if(rs.next()) {
						long millis = rs.getLong("OnlineTime");
						
						long seconds = millis / 1000;
						long minutes = seconds / 60;
						long hours = minutes / 60;
						long days = hours / 24;
						
						return "§6Tage §e-> §b"+days+" §6Stunden §e-> §b"+hours % 24+" §6Minuten §e-> §b"+minutes % 60 + " §6Sekunden §e-> §b"+seconds % 60;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else {
				long millis = cfg.getLong("OnlineTime");
				
				long seconds = millis / 1000;
				long minutes = seconds / 60;
				long hours = minutes / 60;
				long days = hours / 24;
				
				return "§6Tage §e-> §b"+days+" §6Stunden §e-> §b"+hours % 24+" §6Minuten §e-> §b"+minutes % 60 + " §6Sekunden §e-> §b"+seconds % 60;
			}
			
		}
		
		
		
		return "";
	}
	
	public String getLastOffline() {
		if(Main.getPlugin().americanDateStyle) {
			if(isMySQLExists()) {
				final ResultSet rs = mysql.getResult("SELECT * FROM Stats WHERE UUID='"+uuid+"'");
				

				try {
					if(rs.next()) {
						String date = rs.getDate("LastOnline").toString().replace("-", ".");
						String time = rs.getTime("LastOnline").toString();
						
						String[] split_date = date.split("\\.");
						String year = split_date[0];
						String month = split_date[1];
						String day = split_date[2];
						return month+"."+day+"."+year+" - "+time;
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else {
				String date_and_time = cfg.getString("LastOnline");
				String[] split_time = date_and_time.split("-");
				String time = split_time[1];
				String[] split_date = split_time[0].split("\\.");
				String year = split_date[2];
				String month = split_date[1];
				String day = split_date[0];
				
				return month+"."+day+"."+year+ "-"+time;
			}
			
			
		}else {
			if(isMySQLExists()) {
				final ResultSet rs = mysql.getResult("SELECT * FROM Stats WHERE UUID='"+uuid+"'");
				

				try {
					if(rs.next()) {
						String date = rs.getDate("LastOnline").toString();
						String time = rs.getTime("LastOnline").toString();
						
						
						String date_and_time = date;
						String[] split_date = date_and_time.split("-");
						String year = split_date[0];
						String month = split_date[1];
						String day = split_date[2];
						
						return day+"."+month+"."+year+" - "+time;
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else {
				return cfg.getString("LastOnline");
	
			}
		}
		return "";
	}
	
	

}
