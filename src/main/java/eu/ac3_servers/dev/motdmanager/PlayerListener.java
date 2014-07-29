/**
 * 
 */
package eu.ac3_servers.dev.motdmanager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
	
	private StorageConfig config;

	/**
	 * 
	 */
	public PlayerListener(StorageConfig config) {
		
		this.config = config;
		
	}
	
	@EventHandler
	public void onPostLogin(PlayerJoinEvent e) {
		
		Player player = e.getPlayer();
		String address = player.getAddress().toString();
		this.config.getConfig().set("ips." + address, player.getName());
		this.config.saveConfig();
		
	}

}
