package de.stormydeveloper.playerstats.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import de.stormydeveloper.playerstats.utils.DataManager;
import de.stormydeveloper.playerstats.utils.UUIDFetcher;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerStatsCommand implements CommandExecutor,TabExecutor {
	private final String Prefix_c = "§7[§6PlayerStats§7] ";

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("PlayerStats")) {
			if(sender instanceof Player) {
				final Player p = (Player) sender;
				
				if(args.length == 0) {
					if(p.hasPermission("playerstats.self") || p.hasPermission("playerstats.other")) {
						final DataManager dm = new DataManager(p.getUniqueId());
						
						p.sendMessage("\n §7===== §6| §d"+p.getName()+" §7- §aOnline §6| §7===== \n"
															+ "§6-> §bKills§4: §e"+dm.getKills()+"\n"
															+ "§6-> §bDeaths§4: §e"+dm.getDeaths()+"\n"
															+ "§6-> §bFirstJoin§4: §e"+dm.getFirstJoin()+"\n"
															+ "§6-> §bOnlineTime§4: "+dm.getOnlineTime()+"\n"
															+ "§7=====================================================");
					}else {
						p.sendMessage("§c§lDazu hast du keine Rechte!");
					}
				}else if(args.length == 1) {
					if(p.hasPermission("playerstats.other")) {
						if(args[0].equals(p.getName())) {
							final DataManager dm = new DataManager(p.getUniqueId());
							
							p.sendMessage("\n §7===== §6| §d"+p.getName()+" §7- §aOnline §6| §7===== \n"
																+ "§6-> §bKills§4: §e"+dm.getKills()+"\n"
																+ "§6-> §bDeaths§4: §e"+dm.getDeaths()+"\n"
																+ "§6-> §bFirstJoin§4: §e"+dm.getFirstJoin()+"\n"
																+ "§6-> §bOnlineTime§4: "+dm.getOnlineTime()+"\n"
																+ "§7=====================================================");
						}else {
							if(Bukkit.getPlayer(args[0]) != null) {
								final Player t = Bukkit.getPlayer(args[0]);
								
								final DataManager dm = new DataManager(p.getUniqueId());
								
								p.sendMessage("\n §7===== §6| §d"+t.getName()+" §7- §aOnline §6| §7===== \n"
										+ "§6-> §bKills§4: §e"+dm.getKills()+"\n"
										+ "§6-> §bDeaths§4: §e"+dm.getDeaths()+"\n"
										+ "§6-> §bFirstJoin§4: §e"+dm.getFirstJoin()+"\n"
										+ "§6-> §bOnlineTime§4: "+dm.getOnlineTime()+"\n"
										+ "§7=====================================================");
							}else {
								if(args[0].length() > 2 && args[0].length() > 17) {
									if(UUIDFetcher.getUUID(args[0]) != null) {
										final DataManager dm = new DataManager(UUIDFetcher.getUUID(args[0]));
										
										if(dm.isUserExists()) {
											p.sendMessage("\n §7===== §6| §d"+args[0]+" §7- §aOnline §6| §7===== \n"
													+ "§6-> §bKills§4: §e"+dm.getKills()+"\n"
													+ "§6-> §bDeaths§4: §e"+dm.getDeaths()+"\n"
													+ "§6-> §bFirstJoin§4: §e"+dm.getFirstJoin()+"\n"
													+ "§6-> §bOnlineTime§4: "+dm.getOnlineTime()+"\n"
													+ "§7=====================================================");
										}else {
											p.sendMessage(Prefix_c + "§c§lFalscher Spieler! §6Der Spieler §b"+args[0]+ "§6 war noch nie auf dem Server!");
										}
										
									
									}else {
										p.sendMessage(Prefix_c + "§c§lFalscher Spieler! §6Der Spieler §b"+args[0]+ "§6 exsistiert nicht!");
									}
								}else {
									p.sendMessage(Prefix_c + "§c§lFalscher Spieler! §6Der Spieler §b"+args[0]+ "§6 exsistiert nicht!");
								}
							
								
							}
						}
					}else {
						p.sendMessage("§c§lDazu hast du keine Rechte!");
					}
				}else {
					if(p.hasPermission("playerstats.other") && p.hasPermission("playerstats.self")) {
						
						final TextComponent tc = new TextComponent(Prefix_c+"§6Bitte benutze §7:§b /PlayerStats");
						tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pstats"));
						tc.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder("§6-> §bKlick mich um den Befehl zu benutzten.").create()));
						p.spigot().sendMessage(tc);
						
						p.sendMessage(Prefix_c+"§6Oder benutze§7:§b /PlayerStats §e<SPIELERNAME>");
					}else if(p.hasPermission("playerstats.self")) {
						final TextComponent tc = new TextComponent("§6Bitte benutze nur§7:§b /PlayerStats");
						tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pstats"));
						tc.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder("§6-> §bKlick mich um den Befehl zu benutzten.").create()));
						p.spigot().sendMessage(tc);
						
					}else {
						p.sendMessage("§c§lDazu hast du keine Rechte!");
					}
				}
			}else {
				if(args.length == 1) {
					if(Bukkit.getPlayer(args[0]) != null) {
						final Player p = Bukkit.getPlayer(args[0]);
						final DataManager dm = new DataManager(p.getUniqueId());
						
						Bukkit.getConsoleSender().sendMessage("\n §7===== §6| §d"+p.getName()+" §7- §aOnline §6| §7===== \n"
															+ "§6-> §bKills§4: §e"+dm.getKills()+"\n"
															+ "§6-> §bDeaths§4: §e"+dm.getDeaths()+"\n"
															+ "§6-> §bFirstJoin§4: §e"+dm.getFirstJoin()+"\n"
															+ "§6-> §bOnlineTime§4: "+dm.getOnlineTime()+"\n"
															+ "§7===================================================================");
					}else {
						if(args[0].length() > 2 && args[0].length() > 17) {
							if(UUIDFetcher.getUUID(args[0]) != null) {
								final DataManager dm = new DataManager(UUIDFetcher.getUUID(args[0]));
								
								if(dm.isUserExists()) {
									Bukkit.getConsoleSender().sendMessage("\n §7===== §6| §d"+args[0]+" §7- §cOffline §6| §7===== \n"
											+ "§6-> §bKills§4: §e"+dm.getKills()+"\n"
											+ "§6-> §bDeaths§4: §e"+dm.getDeaths()+"\n"
											+ "§6-> §bFirstJoin§4: §e"+dm.getFirstJoin()+"\n"
											+ "§6-> §bOnlineTime§4: "+dm.getOnlineTime()+"\n"
											+ "§6-> §bLastOnline§4:§e "+dm.getLastOffline()+"\n"
											+ "§7===================================================================");
								}else {
									Bukkit.getConsoleSender().sendMessage(Prefix_c + "§cFalscher Spieler! §6Der Spieler §b"+args[0]+ "§6 war noch nie auf dem Server!");
								}
							}else {
								Bukkit.getConsoleSender().sendMessage(Prefix_c + "§cFalscher Spieler! §6Der Spieler §b"+args[0]+ "§6 exsistiert nicht!");
							}
						}else {
							Bukkit.getConsoleSender().sendMessage(Prefix_c + "§cFalscher Spieler! §6Der Spieler §b"+args[0]+ "§6 exsistiert nicht!");
						}
						
					}
				}else {
					Bukkit.getConsoleSender().sendMessage(Prefix_c + "§cFalscher Syntax! §6/PlayerStats §e<SPIELERNAME>");
				}
			}
		}
		return false;
	}

}
