package de.stormydeveloper.playerstats;

import java.io.File;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
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
	private final File messages_file = new File(this.getDataFolder() + "/messages.yml");

	private YamlConfiguration messages;

	public HashMap<Player, Long> online_time = new HashMap<Player, Long>();

	public static Main getPlugin() {
		return plugin;
	}

	public YamlConfiguration getMessages() {
		return messages;
	}

	@Override
	public void onEnable() {
		plugin = this;

		this.loadConfig();
		this.loadMessages();

		if (this.getConfig().getString("Datastorage.Type").equalsIgnoreCase("MySQL")) {
			isMysqlEnabled = true;
		} else {
			isMysqlEnabled = false;
		}

		americanDateStyle = this.getConfig().getBoolean("UseAmericanDateStyle");

		if (isMysqlEnabled) {
			try {
				MySQL.getMysql().connect(this.getConfig().getString("Datastorage.MySQL.Host"),
						this.getConfig().getInt("Datastorage.MySQL.Port"),
						this.getConfig().getString("Datastorage.MySQL.Database"),
						this.getConfig().getString("Datastorage.MySQL.User"),
						this.getConfig().getString("Datastorage.MySQL.Password"));
			} catch (SQLException e) {
				Bukkit.getConsoleSender().sendMessage(getMessageFromFile("MySQLConnectError"));
			}
		}

		Bukkit.getOnlinePlayers().forEach(all -> {
			final DataManager dm = new DataManager(all.getUniqueId());

			if (!dm.isUserExists()) {
				dm.createUser();
			}

			if (!online_time.containsKey(all)) {
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

			if (online_time.containsKey(all)) {
				dm.updateOnlineTime(System.currentTimeMillis() - online_time.get(all));
				online_time.remove(all);
			}

		});

		if (isMysqlEnabled) {
			MySQL.getMysql().close();
		}
	}

	private void loadConfig() {
		this.getConfig().options().copyDefaults(true);

		this.getConfig().options()
				.header("You can switch between MySQL or File as Datastorage type. Default the Plugin use File.");
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

	private void loadMessages() {
		messages = YamlConfiguration.loadConfiguration(messages_file);

		messages.options().copyDefaults(true);
		messages.options().header(
				"Here can you change the messages of the Plugin. With %Prefix% or %Prefix_console% can you use the prefix");
		messages.options().copyHeader(true);

		messages.addDefault("Prefix", "&7[&6PlayerStats&7] ");
		messages.addDefault("Prefix_console", "&7[&6PlayerStats&7] ");
		messages.addDefault("MySQLConnectError", "%Prefix_console% &cThe MySQL can't connect!");
		messages.addDefault("ThePlayerDoesntExists", "%Prefix% &cThe player %Player% doesn't exists!");
		messages.addDefault("ThePlayerDoesntExists_console", "%Prefix_console% &cThe player %Player% doesnt exists!");
		messages.addDefault("ThePlayerWasNotOnTheServer", "%Prefix% &cThe player %Player% was not on the Server!");
		messages.addDefault("ThePlayerWasNotOnTheServer_console",
				"%Prefix_console% &cThe player %Player% was not on the Server!");
		messages.addDefault("NoPerms", "%Prefix% &cYou have no permissions to do that!");
		messages.addDefault("SyntaxErrorConsole", "%Prefix_console% &cPlease use &6/PlayerStats &e<playername>");
		messages.addDefault("SyntaxErrorPlayer_self", "%Prefix% &cPlease use only &6/PlayerStats");
		messages.addDefault("SyntaxErrorPlayer_self_hover", "%Prefix% &6-> &bClick me to use.");
		messages.addDefault("SyntaxErrorPlayer_other_1", "%Prefix% &cPlease use  &6/PlayerStats");
		messages.addDefault("SyntaxErrorPlayer_other_2", "%Prefix% &cOr use &6/PlayerStats &e<playername>");
		messages.addDefault("OnlineTimeDesign",
				"&eDays &6-> %days% &eHours &6-> %hours% &eMinutes &6-> %minutes% &eSeconds §6-> %seconds%");
		messages.addDefault("StatsMessages_Online_console",
				"\n &7===== &6| &d%Player% &7- &aOnline &6| &7===== \n" + "&6-> &bKills&4: &e%Kills%\n"
						+ "&6-> &bDeaths&4: &e%Kills%\n" + "&6-> &bFirstJoin&4: &e%FirstJoin%\n"
						+ "&6-> &bOnlineTime&4: &e%OnlineTime%\n"
						+ "&7===================================================================");
		messages.addDefault("StatsMessages_Offline",
				"\n &7===== &6| &d%Player% &7- &cOffline &6| &7===== \n" + "&6-> &bKills&4: &e%Kills%\n"
						+ "&6-> &bDeaths&4: &e%Kills%\n" + "&6-> &bFirstJoin&4: &e%FirstJoin%\n"
						+ "&6-> &bOnlineTime&4: &e%OnlineTime%\n"
						+ "&6-> &bLastOnline&4: &e%LastOnline%\n"
						+ "&7===================================================================");

		messages.addDefault("StatsMessages_Online",
				"\n &7===== &6| &d%Player% &7- &aOnline &6| &7===== \n" + "&6-> &bKills&4: &e%Kills%\n"
						+ "&6-> &bDeaths&4: &e%Kills%\n" + "&6-> &bFirstJoin&4: &e%FirstJoin%\n"
						+ "&6-> &bOnlineTime&4: &e%OnlineTime%\n"
						+ "&7===================================================================");
		messages.addDefault("StatsMessages_Offline_console",
				"\n &7===== &6| &d%Player% &7- &cOffline &6| &7===== \n" + "&6-> &bKills&4: &e%Kills%\n"
						+ "&6-> &bDeaths&4: &e%Kills%\n" + "&6-> &bFirstJoin&4: &e%FirstJoin%\n"
						+ "&6-> &bOnlineTime&4: &e%OnlineTime%\n"
						+ "&6-> &bLastOnline&4: &e%LastOnline%\n"
						+ "&7===================================================================");
		try {
			messages.save(messages_file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getMessageFromFile(final String key) {
		String a = messages.getString(key).replaceAll("%Prefix_console% ", messages.getString("Prefix_console"))
				.replaceAll("%Prefix%", messages.getString("Prefix"));
		return a.replace('&', '§');
	}

}
