/**
 * MotdManager - Package: syam.motdmanager.command
 * Created: 2012/11/02 7:06:11
 */
package syam.motdmanager.command;

import java.util.List;

import syam.motdmanager.Perms;
import syam.motdmanager.exception.CommandException;
import syam.motdmanager.util.Actions;

/**
 * ListCommand (ListCommand.java)
 * @author syam(syamn)
 */
public class ListCommand extends BaseCommand{
	public ListCommand(){
		bePlayer = false;
		name = "list";
		argLength = 0;
		usage = "<- see motd list";
	}

	@Override
	public void execute() throws CommandException {
		final List<String> motds = config.getMotdList();
		if (motds.size() == 0){
			throw new CommandException("&cMotd list is empty! Add motd to /" + this.command + " add <motd>");
		}

		Actions.message(sender, "&e-- MotdManager : MotdList (" + motds.size() + ") --");
		for (int i = 0, n = motds.size(); i < n; i++){
			Actions.message(sender, "&7" + (i + 1) + ". &f" + plugin.formatting(motds.get(i)));
		}
	}

	@Override
	public boolean permission() {
		return Perms.LIST.has(sender);
	}
}
