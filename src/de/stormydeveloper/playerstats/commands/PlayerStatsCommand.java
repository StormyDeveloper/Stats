package de.stormydeveloper.playerstats.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.stormydeveloper.playerstats.Main;
import de.stormydeveloper.playerstats.utils.DataManager;
import de.stormydeveloper.playerstats.utils.UUIDFetcher;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerStatsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("PlayerStats")) {
			if (sender instanceof Player) {
				final Player p = (Player) sender;

				if (args.length == 0) {
					if (p.hasPermission("playerstats.self") || p.hasPermission("playerstats.other")) {
						final DataManager dm = new DataManager(p.getUniqueId());

						p.sendMessage(Main.getPlugin().getMessageFromFile("StatsMessages_Online")
								.replace("%Player%", p.getName()).replace("%Kills%", "" + dm.getKills())
								.replace("%Deaths%", "" + dm.getDeaths()).replace("%FirstJoin%", dm.getFirstJoin())
								.replace("%OnlineTime%", dm.getOnlineTime()));
					} else {
						p.sendMessage(Main.getPlugin().getMessageFromFile("NoPerms"));
					}
				} else if (args.length == 1) {
					if (p.hasPermission("playerstats.other")) {
						if (args[0].equals(p.getName())) {
							final DataManager dm = new DataManager(p.getUniqueId());

							p.sendMessage(Main.getPlugin().getMessageFromFile("StatsMessages_Online")
									.replace("%Player%", args[0]).replace("%Kills%", "" + dm.getKills())
									.replace("%Deaths%", "" + dm.getDeaths()).replace("%FirstJoin%", dm.getFirstJoin())
									.replace("%OnlineTime%", dm.getOnlineTime()));
						} else {
							if (Bukkit.getPlayer(args[0]) != null) {
								final Player t = Bukkit.getPlayer(args[0]);

								final DataManager dm = new DataManager(t.getUniqueId());

								p.sendMessage(Main.getPlugin().getMessageFromFile("StatsMessages_Online")
										.replace("%Player%", args[0]).replace("%Kills%", "" + dm.getKills())
										.replace("%Deaths%", "" + dm.getDeaths())
										.replace("%FirstJoin%", dm.getFirstJoin())
										.replace("%OnlineTime%", dm.getOnlineTime()));
							} else {
								if (args[0].length() > 2 && args[0].length() < 17) {
									if (UUIDFetcher.getUUID(args[0]) != null) {
										final DataManager dm = new DataManager(UUIDFetcher.getUUID(args[0]));

										if (dm.isUserExists()) {
											p.sendMessage(Main.getPlugin().getMessageFromFile("StatsMessages_Offline")
													.replace("%Player%", args[0]).replace("%Kills%", "" + dm.getKills())
													.replace("%Deaths%", "" + dm.getDeaths())
													.replace("%FirstJoin%", dm.getFirstJoin())
													.replace("%OnlineTime%", dm.getOnlineTime()).replace("%LastOnline%", dm.getLastOffline()));
										} else {
											p.sendMessage(
													Main.getPlugin().getMessageFromFile("ThePlayerWasNotOnTheServer")
															.replace("%Player%", args[0]));
										}

									} else {
										p.sendMessage(Main.getPlugin().getMessageFromFile("ThePlayerDoesntExists")
												.replace("%Player%", args[0]));
									}
								} else {
									p.sendMessage(Main.getPlugin().getMessageFromFile("ThePlayerDoesntExists")
											.replace("%Player%", args[0]));
								}

							}
						}
					} else {
						p.sendMessage(Main.getPlugin().getMessageFromFile("NoPerms"));
					}
				} else {
					if (p.hasPermission("playerstats.other") && p.hasPermission("playerstats.self")) {
						final TextComponent tc = new TextComponent(
								Main.getPlugin().getMessageFromFile("SyntaxErrorPlayer_other_1"));
						tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pstats"));
						tc.setHoverEvent(
								new HoverEvent(Action.SHOW_TEXT,
										new ComponentBuilder(
												Main.getPlugin().getMessageFromFile("SyntaxErrorPlayer_self_hover"))
														.create()));
						p.spigot().sendMessage(tc);

						p.sendMessage(Main.getPlugin().getMessageFromFile("SyntaxErrorPlayer_other_2"));
					} else if (p.hasPermission("playerstats.self")) {
						final TextComponent tc = new TextComponent(
								Main.getPlugin().getMessageFromFile("SyntaxErrorPlayer_self"));
						tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pstats"));
						tc.setHoverEvent(
								new HoverEvent(Action.SHOW_TEXT,
										new ComponentBuilder(
												Main.getPlugin().getMessageFromFile("SyntaxErrorPlayer_self_hover"))
														.create()));
						p.spigot().sendMessage(tc);

					} else {
						p.sendMessage(Main.getPlugin().getMessageFromFile("NoPerms"));
					}
				}
			} else {
				if (args.length == 1) {
					if (Bukkit.getPlayer(args[0]) != null) {
						final Player p = Bukkit.getPlayer(args[0]);
						final DataManager dm = new DataManager(p.getUniqueId());

						Bukkit.getConsoleSender().sendMessage(Main.getPlugin()
								.getMessageFromFile("StatsMessages_Online_console").replace("%Player%", args[0])
								.replace("%Kills%", "" + dm.getKills()).replace("%Deaths%", "" + dm.getDeaths())
								.replace("%FirstJoin%", dm.getFirstJoin()).replace("%OnlineTime%", dm.getOnlineTime()));
					} else {
						if (args[0].length() > 2 && args[0].length() < 17) {
							if (UUIDFetcher.getUUID(args[0]) != null) {
								final DataManager dm = new DataManager(UUIDFetcher.getUUID(args[0]));

								if (dm.isUserExists()) {
									Bukkit.getConsoleSender()
											.sendMessage(Main.getPlugin()
													.getMessageFromFile("StatsMessages_Offline_console")
													.replace("%Player%", args[0]).replace("%Kills%", "" + dm.getKills())
													.replace("%Deaths%", "" + dm.getDeaths())
													.replace("%FirstJoin%", dm.getFirstJoin())
													.replace("%OnlineTime%", dm.getOnlineTime()).replace("%LastOnline%", dm.getLastOffline()));
								} else {
									Bukkit.getConsoleSender()
											.sendMessage(Main.getPlugin()
													.getMessageFromFile("ThePlayerWasNotOnTheServer_console")
													.replace("%Player%", args[0]));
								}
							} else {
								Bukkit.getConsoleSender().sendMessage(
										Main.getPlugin().getMessageFromFile("ThePlayerDoesntExists_console")
												.replace("%Player%", args[0]));
							}
						} else {
							Bukkit.getConsoleSender().sendMessage(Main.getPlugin()
									.getMessageFromFile("ThePlayerDoesntExists_console").replace("%Player%", args[0]));
						}

					}
				} else {
					Bukkit.getConsoleSender().sendMessage(Main.getPlugin().getMessageFromFile("SyntaxErrorConsole"));
				}
			}
		}
		return false;
	}

}
