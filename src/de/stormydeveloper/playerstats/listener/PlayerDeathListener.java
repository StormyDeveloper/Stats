package de.stormydeveloper.playerstats.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import de.stormydeveloper.playerstats.utils.DataManager;

public class PlayerDeathListener implements Listener {
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		final Player p = e.getEntity();
		
		final DataManager p_dm = new DataManager(p.getUniqueId());
		p_dm.updateDeaths(1);
		
		if(p.getKiller() instanceof Player) {
			final Player killer = p.getKiller();
			
			if(!p.getName().equals(killer.getName())) {
				final DataManager killer_dm = new DataManager(killer.getUniqueId());
				killer_dm.updateKills(1);
			}
		}
		
		
		
		
		
		
	}

}
