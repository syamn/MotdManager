/**
 * MotdManager - Package: syam.motdmanager
 * Created: 2012/11/02 6:14:14
 */
package syam.motdmanager;

import org.bukkit.permissions.Permissible;

/**
 * Perms (Perms.java)
 * @author syam(syamn)
 */
public enum Perms {
    /* 権限ノード */
    // Motd Commands
    ADD ("add"),
    REMOVE ("remove"),
    LIST ("list"),

    // Other Commands
    MAXPLAYER ("maxplayer"),

    // Admin Commands
    RELOAD ("reload"),

    ;

    // ノードヘッダー
    final String HEADER = "motdmanager.";
    private String node;

    /**
     * コンストラクタ
     * @param node 権限ノード
     */
    Perms(final String node){
        this.node = HEADER + node;
    }

    /**
     * 指定したプレイヤーが権限を持っているか
     * @param player Permissible. Player, CommandSender etc
     * @return boolean
     */
    public boolean has(final Permissible perm){
        if (perm == null) return false;
        return perm.hasPermission(node); // only support SuperPerms
    }
}
