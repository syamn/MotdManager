/**
 * MotdManager - Package: syam.motdmanager.command
 * Created: 2012/11/02 7:14:28
 */
package syam.motdmanager.command;

import syam.motdmanager.Perms;
import syam.motdmanager.exception.CommandException;
import syam.motdmanager.util.Actions;
import syam.motdmanager.util.Util;

/**
 * RemoveCommand (RemoveCommand.java)
 * @author syam(syamn)
 */
public class RemoveCommand extends BaseCommand{
    public RemoveCommand(){
        bePlayer = false;
        name = "remove";
        argLength = 1;
        usage = "<id> <- remove from motd list";
    }

    @Override
    public void execute() throws CommandException {
        final String idstr = args.get(0).trim();
        if (!Util.isInteger(idstr)){
            throw new CommandException("&cIndex must be a number: " + idstr);
        }
        final int id = Integer.parseInt(idstr);
        final String motd = config.removeMotdList(id - 1);
        if (motd == null){
            throw new CommandException("&cInvalid number: " + idstr);
        }
        if (!config.save()){
            throw new CommandException("&cFailed to save configuration file!");
        }

        Actions.message(sender, "&aRemoved MOTD: &7" + id + ". &f" + plugin.formatting(motd, "127.0.0.1.."));
    }

    @Override
    public boolean permission() {
        return Perms.REMOVE.has(sender);
    }
}
