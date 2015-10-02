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
        String motd = Util.join(args, " ");
        config.addMotdList(motd);
        if (!config.save()){
            throw new CommandException("&cFailed to save configuration file!");
        }
        motd = plugin.formatting(motd, "127.0.0.1");
        Actions.message(sender, "&aAdded MOTD: &f" + motd);
        if (motd.length() > 200){ // 閾値 0～237 文字 超えるとCommunicationError
            Actions.message(sender, "&4WARN:&c Too long strings! May this cause Communication Error! (length: " + motd.length() + ")");
        }
    }

    @Override
    public boolean permission() {
        return Perms.ADD.has(sender);
    }
}
