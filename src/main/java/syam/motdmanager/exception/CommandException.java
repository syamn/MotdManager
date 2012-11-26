/**
 * MotdManager - Package: syam.motdmanager.exception
 * Created: 2012/11/02 6:13:18
 */
package syam.motdmanager.exception;

/**
 * CommandException (CommandException.java)
 * @author syam(syamn)
 */
public class CommandException extends Exception{
    private static final long serialVersionUID = 756337721456853035L;

    public CommandException(String message){
        super(message);
    }

    public CommandException(Throwable cause){
        super(cause);
    }

    public CommandException(String message, Throwable cause){
        super(message, cause);
    }
}
