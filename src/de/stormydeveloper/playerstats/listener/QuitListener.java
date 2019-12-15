package de.stormydeveloper.playerstats.listener;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.stormydeveloper.playerstats.Main;
import de.stormydeveloper.playerstats.utils.DataManager;

public class QuitListener implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		final Player p = e.getPlayer();
		
		final DataManager dm = new DataManager(p.getUniqueId());
		
		dm.setLastOnline();
		
		
		final long time = System.currentTimeMillis() - Main.getPlugin().online_time.get(p);
		Main.getPlugin().online_time.remove(p);
		
		dm.updateOnlineTime(time);
		
		

		

		
		
		
	}

}
