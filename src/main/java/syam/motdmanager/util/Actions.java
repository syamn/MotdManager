/**
 * MotdManager - Package: syam.motdmanager.util
 * Created: 2012/11/02 6:10:12
 */
package syam.motdmanager.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import syam.motdmanager.MotdManager;

/**
 * Actions (Actions.java)
 * @author syam(syamn)
 */
public class Actions {
    // Logger
    private static final Logger log = MotdManager.log;
    private static final String logPrefix = MotdManager.logPrefix;
    private static final String msgPrefix = MotdManager.msgPrefix;

    private final MotdManager plugin;

    private static final List<String> colors = new ArrayList<String>(16){
        private static final long serialVersionUID = -4095100564568189453L;
        {
            add("\u00A70");
            add("\u00A71");
            add("\u00A72");
            add("\u00A73");
            add("\u00A74");
            add("\u00A75");
            add("\u00A76");
            add("\u00A77");
            add("\u00A78");
            add("\u00A79");
            add("\u00A7a");
            add("\u00A7b");
            add("\u00A7c");
            add("\u00A7d");
            add("\u00A7e");
            add("\u00A7f");
        }};

        public Actions(MotdManager plugin){
            this.plugin = plugin;
        }

        /****************************************/
        // メッセージ送信系関数
        /****************************************/
        /**
         * メッセージをユニキャスト
         * @param message メッセージ
         */
        public static void message(CommandSender sender, String message){
            if (sender != null && message != null){
                sender.sendMessage(message.replaceAll("&([0-9a-fk-or])", "\u00A7$1"));
            }
        }

        /**
         * メッセージをブロードキャスト
         * @param message メッセージ
         */
        public static void broadcastMessage(String message){
            if (message != null){
                message = message.replaceAll("&([0-9a-fk-or])", "\u00A7$1");
                //debug(message);//debug
                Bukkit.broadcastMessage(message);
            }
        }
        /**
         * メッセージをワールドキャスト
         * @param world
         * @param message
         */
        public static void worldcastMessage(World world, String message){
            if (world != null && message != null){
                message = message.replaceAll("&([0-9a-fk-or])", "\u00A7$1");
                for(Player player: world.getPlayers()){
                    player.sendMessage(message);
                }
                log.info("[Worldcast]["+world.getName()+"]: " + message);
            }
        }
        /**
         * メッセージをパーミッションキャスト(指定した権限ユーザにのみ送信)
         * @param permission 受信するための権限ノード
         * @param message メッセージ
         */
        public static void permcastMessage(String permission, String message){
            // OK
            int i = 0;
            for (Player player : Bukkit.getServer().getOnlinePlayers()){
                if (player.hasPermission(permission)){
                    Actions.message(player, message);
                    i++;
                }
            }

            log.info("Received "+i+"players: "+message);
        }

        /****************************************/
        // ユーティリティ
        /****************************************/
        /**
         * 文字配列をまとめる
         * @param s つなげるString配列
         * @param glue 区切り文字 通常は半角スペース
         * @return
         */
        public static String combine(String[] s, String glue)
        {
            int k = s.length;
            if (k == 0){ return null; }
            StringBuilder out = new StringBuilder();
            out.append(s[0]);
            for (int x = 1; x < k; x++){
                out.append(glue).append(s[x]);
            }
            return out.toString();
        }
        /**
         * コマンドをコンソールから実行する
         * @param command
         */
        public static void executeCommandOnConsole(String command){
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }
        /**
         * 文字列の中に全角文字が含まれているか判定
         * @param s 判定する文字列
         * @return 1文字でも全角文字が含まれていればtrue 含まれていなければfalse
         * @throws UnsupportedEncodingException
         */
        public static boolean containsZen(String s)
                throws UnsupportedEncodingException {
            for (int i = 0; i < s.length(); i++) {
                String s1 = s.substring(i, i + 1);
                if (URLEncoder.encode(s1,"MS932").length() >= 4) {
                    return true;
                }
            }
            return false;
        }
        /**
         * 現在の日時を yyyy-MM-dd HH:mm:ss 形式の文字列で返す
         * @return
         */
        public static String getDatetime(){

            Date date = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return df.format(date);
        }
        /**
         * 座標データを ワールド名:x, y, z の形式の文字列にして返す
         * @param loc
         * @return
         */
        public static String getLocationString(Location loc){
            return loc.getWorld().getName()+":"+loc.getX()+","+loc.getY()+","+loc.getZ();
        }
        public static String getBlockLocationString(Location loc){
            return loc.getWorld().getName()+":"+loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ();
        }
        /**
         * デバッグ用 syamnがオンラインならメッセージを送る
         * @param msg
         */
        public static void debug(String msg){
            OfflinePlayer syamn = Bukkit.getServer().getOfflinePlayer("syamn");
            if (syamn.isOnline()){
                Actions.message((Player) syamn, msg);
            }
        }
        /**
         * 文字列の&(char)をカラーコードに変換して返す
         * @param string 文字列
         * @return 変換後の文字列
         */
        public static String coloring(String string){
            if (string == null) return null;
            string = string.replaceAll("&([0-9a-fA-Fk-oK-OrR])", "\u00A7$1");

            // don't touch above replace method. keep backward compatibility.
            string = string.replaceAll("\u00A7P", "\u00A7p");

            // (\u00A7)p roop
            while (string.contains("\u00A7p")){
                // without random
                int i = string.indexOf("\u00A7p") + 2;
                String sub = string.substring(i, string.length());
                // end if other & is found
                if (sub.contains("\u00A7")) sub = sub.substring(0, sub.indexOf("\u00A7"));
                // replace
                string = string.replace(sub, coloringRandom(sub));
                string = string.replaceFirst("\u00A7p", "");
            }

            return string;
        }
        /**
         * 引数の文字列をランダムカラーリングして返す
         * @param string
         * @return
         */
        private static String coloringRandom(String string){
            if (string == null) return null;
            String ret = "";
            for (int i = 0, k = string.length(); i < k; i++){
                int x = (int)(Math.random() * colors.size());
                char ch = string.charAt(i);
                ret += colors.get(x) + Character.toString(ch);
            }
            return ret;
        }

        /****************************************/
        /* ログ操作系 */
        /****************************************/
        /**
         * ログファイルに書き込み
         * @param file ログファイル名
         * @param line ログ内容
         */
        public static void log(String filepath, String line){
            TextFileHandler r = new TextFileHandler(filepath);
            try{
                r.appendLine("[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "] " + line);
            } catch (IOException ex) {}
        }

        /****************************************/
        /* その他 */
        /****************************************/
        // プレイヤーがオンラインかチェックしてテレポートさせる
        public static void tpPlayer(Player player, Location loc){
            if (player == null || loc == null || !player.isOnline())
                return;
            player.teleport(loc);
        }

        // プレイヤーのインベントリをその場にドロップさせる
        public static void dropInventoryItems(Player player){
            if (player == null) return;

            PlayerInventory inv = player.getInventory();
            Location loc = player.getLocation();

            // インベントリアイテム
            for (ItemStack i : inv.getContents()) {
                if (i != null && i.getType() != Material.AIR) {
                    inv.remove(i);
                    player.getWorld().dropItemNaturally(loc, i);
                }
            }

            // 防具アイテム
            for (ItemStack i : inv.getArmorContents()){
                if (i != null && i.getType() != Material.AIR) {
                    inv.remove(i);
                    player.getWorld().dropItemNaturally(loc, i);
                }
            }
        }
}
