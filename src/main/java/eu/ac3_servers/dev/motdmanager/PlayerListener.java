/**
 * 
 */
package eu.ac3_servers.dev.motdmanager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import syam.motdmanager.MotdManager;

public class PlayerListener implements Listener {
	
	private StorageConfig config;
	private MotdManager plugin;

	/**
	 * 
	 */
	public PlayerListener(MotdManager plugin, StorageConfig config) {
		
		this.plugin = plugin;
		this.config = config;
		
	}
	
	@EventHandler
	public void onPostLogin(PlayerJoinEvent e) {
		
		Player player = e.getPlayer();
		String address = player.getAddress().getAddress().getHostAddress();
		plugin.debug("Player joined: " + player.getName() + " | " + address);
		address = "ips." + (address.replaceFirst("/", "").replaceAll("\\.", "-"));
		plugin.debug("Storing the username to: \"" + address + "\"");
		this.config.getConfig().set(address, player.getName());
		this.config.saveConfig();
		
	}

}
