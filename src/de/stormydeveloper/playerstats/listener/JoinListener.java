package de.stormydeveloper.playerstats.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.stormydeveloper.playerstats.Main;
import de.stormydeveloper.playerstats.utils.DataManager;

public class JoinListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		final Player p = e.getPlayer();
		
		final DataManager dm = new DataManager(p.getUniqueId());
		
		if(!dm.isUserExists()) {dm.createUser();}
		
		Main.getPlugin().online_time.put(p, System.currentTimeMillis());
	}

}
