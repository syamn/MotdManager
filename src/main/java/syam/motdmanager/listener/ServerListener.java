/**
 * MotdManager - Package: syam.motdmanager.listener
 * Created: 2012/11/02 7:31:54
 */
package syam.motdmanager.listener;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import syam.motdmanager.MotdManager;

/**
 * ServerListener (ServerListener.java)
 * @author syam(syamn)
 */
public class ServerListener implements Listener{
    @SuppressWarnings("unused")
	private static final Logger log = MotdManager.log;
    @SuppressWarnings("unused")
	private static final String logPrefix = MotdManager.logPrefix;
    @SuppressWarnings("unused")
	private static final String msgPrefix = MotdManager.msgPrefix;

    private final MotdManager plugin;
    private final Random random;

    public ServerListener(final MotdManager plugin){
        this.plugin = plugin;
        this.random = new Random();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onServerListPing(final ServerListPingEvent event){
        String debugmsg = "Get ping from " + event.getAddress().getHostAddress() + "!";
        
        String address = event.getAddress().toString();
        
        final String motd = plugin.formatting(chooseMsg(), address);
        if (motd != null) {
            event.setMotd(motd);
            debugmsg += " Motd: '" + motd + "'";
        }
        if (plugin.getConfigs().getUseFakeMaxPlayers()){
            event.setMaxPlayers(plugin.getConfigs().getFakeMaxPlayers());
            debugmsg += " FakeMaxPlayers: '" + plugin.getConfigs().getFakeMaxPlayers() + "'";
        }

        plugin.debug(debugmsg);
    }

    /**
     * ランダムでMOTDリストから1つ選択する
     * @return
     */
    private String chooseMsg(){
        final List<String> motds = plugin.getConfigs().getMotdList();
        if (motds.size() == 0) return null;
        return motds.get(random.nextInt(motds.size()));
    }
}
