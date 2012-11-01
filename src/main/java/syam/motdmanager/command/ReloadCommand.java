/**
 * MotdManager - Package: syam.motdmanager.command
 * Created: 2012/11/02 6:15:11
 */
package syam.motdmanager.command;

import syam.motdmanager.Perms;
import syam.motdmanager.util.Actions;

/**
 * ReloadCommand (ReloadCommand.java)
 * @author syam(syamn)
 */
public class ReloadCommand extends BaseCommand{
	public ReloadCommand(){
		bePlayer = false;
		name = "reload";
		argLength = 0;
		usage = "<- reload config.yml";
	}

	@Override
	public void execute() {
		try{
			plugin.getConfigs().loadConfig(false);
		}catch (Exception ex){
			log.warning(logPrefix+"an error occured while trying to load the config file.");
			ex.printStackTrace();
			return;
		}
		Actions.message(sender, "&aConfiguration reloaded!");
	}

	@Override
	public boolean permission() {
		return Perms.RELOAD.has(sender);
	}
}
