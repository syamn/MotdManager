/**
 * MotdManager - Package: syam.motdmanager.command
 * Created: 2012/11/02 6:32:33
 */
package syam.motdmanager.command;

import syam.motdmanager.Perms;
import syam.motdmanager.exception.CommandException;
import syam.motdmanager.util.Actions;
import syam.motdmanager.util.Util;

/**
 * AddCommand (AddCommand.java)
 * @author syam(syamn)
 */
public class AddCommand extends BaseCommand{
	public AddCommand(){
		bePlayer = false;
		name = "add";
		argLength = 1;
		usage = "<motd> <- add to motd list";
	}

	@Override
	public void execute() throws CommandException {
		final String motd = Util.join(args, " ");
		config.addMotdList(motd);
		if (!config.save()){
			throw new CommandException("&cFailed to save configuration file!");
		}
		Actions.message(sender, "&aAdded MOTD: &f" + motd);
	}

	@Override
	public boolean permission() {
		return Perms.ADD.has(sender);
	}
}
