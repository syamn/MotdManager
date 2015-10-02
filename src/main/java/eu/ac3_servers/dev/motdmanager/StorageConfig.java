package eu.ac3_servers.dev.motdmanager;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class StorageConfig {
	
	private Plugin plugin;

	public StorageConfig(Plugin plugin) {
		this.plugin = plugin;
	}
	
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	
	public void reloadConfig() {
	    if (customConfigFile == null) {
	    customConfigFile = new File(this.plugin.getDataFolder(), "ips.yml");
	    }
	    customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
	 
	    // Look for defaults in the jar
	    try {
	    	Reader defConfigStream = new InputStreamReader(this.plugin.getResource("ips.yml"), "UTF8");
			if (defConfigStream != null) {
		        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		        customConfig.setDefaults(defConfig);
		    }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			this.plugin.getLogger().severe("Couldn't reload custom storage.");
		}
	}
	
	public FileConfiguration getConfig() {
	    if (customConfig == null) {
	        reloadConfig();
	    }
	    return customConfig;
	}

	public void saveConfig() {
	    if (customConfig == null || customConfigFile == null) {
	        return;
	    }
	    try {
	        getConfig().save(customConfigFile);
	    } catch (IOException ex) {
	        this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
	    }
	}
	
	public void saveDefaultConfig() {
	    if (customConfigFile == null) {
	        customConfigFile = new File(this.plugin.getDataFolder(), "ips.yml");
	    }
	    if (!customConfigFile.exists()) {            
	         plugin.saveResource("ips.yml", false);
	     }
	}
	
}
