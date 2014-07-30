/**
 * MotdManager - Package: syam.motdmanager
 * Created: 2012/11/02 6:00:01
 */
package syam.motdmanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import eu.ac3_servers.dev.motdmanager.PlayerListener;
import eu.ac3_servers.dev.motdmanager.StorageConfig;
import syam.motdmanager.command.AddCommand;
import syam.motdmanager.command.BaseCommand;
import syam.motdmanager.command.HelpCommand;
import syam.motdmanager.command.ListCommand;
import syam.motdmanager.command.MaxplayerCommand;
import syam.motdmanager.command.ReloadCommand;
import syam.motdmanager.command.RemoveCommand;
import syam.motdmanager.listener.ServerListener;
import syam.motdmanager.util.Actions;
import syam.motdmanager.util.Metrics;

/**
 * MotdManager (MotdManager.java)
 * @author syam(syamn)
 */
public class MotdManager extends JavaPlugin{
    // ** Logger **
    public final static Logger log = Logger.getLogger("Minecraft");
    public final static String logPrefix = "[MotdManager] ";
    public final static String msgPrefix = "&6[MotdManager] &f";

    // ** Listener **
    ServerListener serverListener = new ServerListener(this);

    // ** Commands **
    private List<BaseCommand> commands = new ArrayList<BaseCommand>();

    // ** Private Classes **
    private ConfigurationManager config;

    // ** Replaces **
    private String mcVersion = "";
	private StorageConfig storage;

    // ** Instance **
    private static MotdManager instance;

    /**
     * プラグイン起動処理
     */
    @Override
    public void onEnable(){
        instance  = this;

        PluginManager pm = getServer().getPluginManager();
        config = new ConfigurationManager(this);

        // loadconfig
        try{
            config.loadConfig(true);
        }catch (Exception ex){
            log.warning(logPrefix+"an error occured while trying to load the config file.");
            ex.printStackTrace();
        }

        // プラグインを無効にした場合進まないようにする
        if (!pm.isPluginEnabled(this)){
            return;
        }

        // Regist Listeners
        pm.registerEvents(serverListener, this);

        // コマンド登録
        registerCommands();

        // Building replaces
        try {
            buildReplaces();
        }catch (Exception ex){
            log.warning("Could not build replace strings! (Check plugin update!)");
            ex.printStackTrace();
        }
        
        this.storage = new StorageConfig(this);
        if(!(new File(getDataFolder(), "ips.yml")).exists()) 
        	this.storage.saveDefaultConfig();
        
        pm.registerEvents(new PlayerListener(this, this.storage), this);
        log.info("[MOTDManager mod] Player listener registered!");
        
        // メッセージ表示
        PluginDescriptionFile pdfFile=this.getDescription();
        log.info("["+pdfFile.getName()+"] version "+pdfFile.getVersion()+" is enabled!");

        setupMetrics(); // mcstats
    }

    /**
     * プラグイン停止処理
     */
    @Override
    public void onDisable(){
        // メッセージ表示
        PluginDescriptionFile pdfFile=this.getDescription();
        this.storage.saveConfig();
        log.info("["+pdfFile.getName()+"] version "+pdfFile.getVersion()+" is disabled!");
    }

    public void buildReplaces(){
        // mcVersion
        final Matcher matcher = Pattern.compile("\\(MC: (.+)\\)").matcher(Bukkit.getVersion());
        this.mcVersion = (matcher.find()) ? matcher.group(1) : "Unknown";
    }

    /**
     * コマンドを登録
     */
    private void registerCommands(){
        // Intro Commands
        commands.add(new HelpCommand());

        // Motd Commands
        commands.add(new AddCommand());
        commands.add(new RemoveCommand());
        commands.add(new ListCommand());

        // Other Commands
        commands.add(new MaxplayerCommand());

        // Admin Commands
        commands.add(new ReloadCommand());
    }

    /**
     * Metricsセットアップ
     */
    private void setupMetrics(){
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException ex) {
            log.warning(logPrefix+"cant send metrics data!");
            ex.printStackTrace();
        }
    }

    /**
     * コマンドが呼ばれた
     */
    @SuppressWarnings("unused")
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]){
        if (cmd.getName().equalsIgnoreCase("motdmanager")){
            if(args.length == 0){
                // 引数ゼロはヘルプ表示
                args = new String[]{"help"};
            }

            outer:
                for (BaseCommand command : commands.toArray(new BaseCommand[0])){
                    String[] cmds = command.getName().split(" ");
                    for (int i = 0; i < cmds.length; i++){
                        if (i >= args.length || !cmds[i].equalsIgnoreCase(args[i])){
                            continue outer;
                        }
                        // 実行
                        return command.run(this, sender, args, commandLabel);
                    }
                }
            // 有効コマンドなし ヘルプ表示
            new HelpCommand().run(this, sender, args, commandLabel);
            return true;
        }
        return false;
    }

    public void debug(final String msg){
        if (config.isDebug()){
            log.info(logPrefix+ "[DEBUG]" + msg);
        }
    }

    public String formatting(String string, String address){
        debug("Formatting..:  " + string);
        string = replacing(string, address);
        debug("Replaced:  " + string);
        string = Actions.coloring(string);
        debug("Formatted:  " + string);
        return string;
    }

    /**
     * 変数の置換を行う
     * @param motd
     * @param address 
     * @return
     */
    private String replacing(final String motd, String address){
        if (motd == null) return null;
        return motd
                .replaceAll("%ver", mcVersion)
                .replaceAll("%players", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replaceAll("%player", getUserFromAddress(address.replaceFirst("/", null).replaceAll(".", "-")))
                ;
    }

    private String getUserFromAddress(String address) {
		String username = this.storage.getConfig().getString("ips." + address);
		debug(username);
		if(username == null || address == "127.0.0.1") return "user";
		return username;
	}

	/* getter */
    /**
     * コマンドを返す
     * @return List<BaseCommand>
     */
    public List<BaseCommand> getCommands(){
        return commands;
    }

    /**
     * 設定マネージャを返す
     * @return ConfigurationManager
     */
    public ConfigurationManager getConfigs() {
        return config;
    }

    /**
     * インスタンスを返す
     * @return MotdManagerインスタンス
     */
    public static MotdManager getInstance(){
        return instance;
    }
}
