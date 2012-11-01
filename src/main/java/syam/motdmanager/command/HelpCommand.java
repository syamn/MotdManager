/**
 * MotdManager - Package: syam.motdmanager.command
 * Created: 2012/11/02 6:14:29
 */
package syam.motdmanager.command;

import syam.motdmanager.util.Actions;

/**
 * HelpCommand (HelpCommand.java)
 * @author syam(syamn)
 */
public class HelpCommand extends BaseCommand{
	public HelpCommand(){
		bePlayer = false;
		name = "help";
		argLength = 0;
		usage = "<- show command help";
	}

	@Override
	public void execute() {
		Actions.message(sender, "&c===================================");
		Actions.message(sender, "&b" + plugin.getDescription().getName() + " Plugin version &3"+ plugin.getDescription().getVersion()+" &bby syamn");
		Actions.message(sender, " &b<>&f = required, &b[]&f = optional");
		// 全コマンドをループで表示
		for (BaseCommand cmd : plugin.getCommands().toArray(new BaseCommand[0])){
			cmd.sender = this.sender;
			if (cmd.permission()){
				Actions.message(sender, "&8-&7 /"+command+" &c" + cmd.name + " &7" + cmd.usage);
			}
		}
		Actions.message(sender, "&c===================================");

		return;
	}

	@Override
	public boolean permission() {
		return true;
	}
}
