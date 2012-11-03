/**
 * MotdManager - Package: syam.motdmanager.command
 * Created: 2012/11/03 6:09:16
 */
package syam.motdmanager.command;

import syam.motdmanager.Perms;
import syam.motdmanager.exception.CommandException;
import syam.motdmanager.util.Actions;
import syam.motdmanager.util.Util;

/**
 * MaxplayerCommand (MaxplayerCommand.java)
 * @author syam(syamn)
 */
public class MaxplayerCommand extends BaseCommand{
	public MaxplayerCommand(){
		bePlayer = false;
		name = "maxplayer";
		argLength = 1;
		usage = "[num]|disable <- set fake maxplayers count";
	}

	@Override
	public void execute() throws CommandException {
		final String arg1 = args.get(0).trim();

		// check if disable feature
		if (arg1.equalsIgnoreCase("disable")){
			config.setUseFakeMaxPlayers(false);
			if (!config.save()){
				throw new CommandException("&cFailed to save configuration file!");
			}
			Actions.message(sender, "&aDisabled show fake maxplayers feature!");
			return;
		}

		// set new value and enable this feature
		if (!Util.isInteger(arg1)){
			throw new CommandException("&cPlayer count must be a number: " + arg1);
		}
		final int count = Integer.parseInt(arg1);

		config.setUseFakeMaxPlayers(true);
		config.setFakeMaxPlayers(count);
		if (!config.save()){
			throw new CommandException("&cFailed to save configuration file!");
		}
		Actions.message(sender, "&aUsing fake players count: &6" + count);
		if (count <= 0 || count >= 1000){
			Actions.message(sender, "&4WARN:&c You specified a number of deprecated range!");
		}
	}

	@Override
	public boolean permission() {
		return Perms.MAXPLAYER.has(sender);
	}
}
