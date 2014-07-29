/**
 * MotdManager - Package: syam.motdmanager
 * Created: 2012/11/02 6:02:34
 */
package syam.motdmanager;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;

import syam.motdmanager.util.Util;

/**
 * ConfigurationManager (ConfigurationManager.java)
 * @author syam(syamn)
 */
public class ConfigurationManager {
    /* Current config.yml file version! */
    private final int latestVersion = 1;

    // Logger
    private static final Logger log = MotdManager.log;
    private static final String logPrefix = MotdManager.logPrefix;
    @SuppressWarnings("unused")
	private static final String msgPrefix = MotdManager.msgPrefix;

    private MotdManager plugin;
    @SuppressWarnings("unused")
	private FileConfiguration conf;

    private static File pluginDir = new File("plugins", "MotdManager");

    // デフォルトの設定定数
    @SuppressWarnings("serial")
    private final List<String> defMotdList = new ArrayList<String>() {{add("&cThis is default MOTD!");}};

    // 設定項目
    /* Basic Configs */
    private List<String> motdList = defMotdList;
    private boolean useMaxPlayers = false;
    private int fakeMaxPlayers = 20;

    private boolean debug = false;

    /**
     * コンストラクタ
     * @param plugin
     */
    public ConfigurationManager(final MotdManager plugin){
        this.plugin = plugin;
        pluginDir = this.plugin.getDataFolder();
    }

    /**
     * 設定をファイルから読み込む
     * @param initialLoad 初回ロードかどうか
     */
    public void loadConfig(final boolean initialLoad) throws Exception{
        // ディレクトリ作成
        createDirs();

        // 設定ファイルパス取得
        File file = new File(pluginDir, "config.yml");
        // 無ければデフォルトコピー
        if (!file.exists()){
            extractResource(System.getProperty("file.separator") + "config.yml", pluginDir, false, true);
            log.info(logPrefix+ "config.yml is not found! Created default config.yml!");
        }

        plugin.reloadConfig();

        // 先にバージョンチェック
        checkver(plugin.getConfig().getInt("ConfigVersion", 1));

        /* Basic Configs */
        if (plugin.getConfig().get("MotdList") != null){
            motdList = plugin.getConfig().getStringList("MotdList");
        }else{
            motdList = defMotdList;
        }
        String fakeMaxPlayersStr = plugin.getConfig().getString("FakeMaxPlayer", "disable");
        if (Util.isInteger(fakeMaxPlayersStr)){
            fakeMaxPlayers = Integer.parseInt(fakeMaxPlayersStr);
            useMaxPlayers = true;
        }else{
            useMaxPlayers = false;
        }

        /* Debug Configs */
        debug = plugin.getConfig().getBoolean("Debug", false);

    }

    // 設定 getter/setter ここから
    /* Basic Configs */
    public List<String> getMotdList(){
        return this.motdList;
    }
    public List<String> addMotdList(final String motd){
        this.motdList.add(motd);
        return this.getMotdList();
    }
    public String removeMotdList(final int index){
        if (index < 0 || index >= motdList.size()){
            return null;
        }
        return this.motdList.remove(index);
    }

    public int getFakeMaxPlayers(){
        return this.fakeMaxPlayers;
    }
    public void setFakeMaxPlayers(final int count){
        this.fakeMaxPlayers = count;
    }

    public boolean getUseFakeMaxPlayers(){
        return useMaxPlayers;
    }
    public void setUseFakeMaxPlayers(final boolean use){
        this.useMaxPlayers = use;
    }

    /* Debug Configs */
    public boolean isDebug(){
        return this.debug;
    }
    // 設定 getter/setter ここまで

    /**
     * 設定ファイルに設定を書き込む
     * @throws Exception
     */
    public boolean save(){
        plugin.getConfig().set("MotdList", getMotdList());
        plugin.getConfig().set("FakeMaxPlayer", (useMaxPlayers) ? fakeMaxPlayers : "disable");
        try {
            plugin.getConfig().save(new File(plugin.getDataFolder() + System.getProperty("file.separator") + "config.yml"));
        } catch (IOException ex) {
            log.warning(logPrefix+ "Couldn't write config file!");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 必要なディレクトリ群を作成する
     */
    private void createDirs(){
        createDir(plugin.getDataFolder());
    }

    /**
     * 存在しないディレクトリを作成する
     * @param dir File 作成するディレクトリ
     */
    private static void createDir(final File dir) {
        // 既に存在すれば作らない
        if (dir.isDirectory()){
            return;
        }
        if (!dir.mkdir()){
            log.warning(logPrefix+ "Can't create directory: "+dir.getName());
        }
    }

    /**
     * 設定ファイルのバージョンをチェックする
     * @param ver
     */
    private void checkver(final int ver){
        // compare configuration file version
        if (ver < latestVersion){
            // first, rename old configuration
            final String destName = "oldconfig-v" + ver + ".yml";
            String srcPath = new File(pluginDir, "config.yml").getPath();
            String destPath = new File(pluginDir, destName).getPath();
            try{
                copyTransfer(srcPath, destPath);
                log.info("Copied old config.yml to "+destName+"!");
            }catch(Exception ex){
                log.warning("Failed to copy old config.yml!");
            }

            // force copy config.yml and languages
            extractResource("/config.yml", pluginDir, true, false);

            plugin.reloadConfig();
            conf = plugin.getConfig();

            log.info("Deleted existing configuration file and generate a new one!");
        }
    }

    /**
     * リソースファイルをファイルに出力する
     * @param from 出力元のファイルパス
     * @param to 出力先のファイルパス
     * @param force jarファイルの更新日時より新しいファイルが既にあっても強制的に上書きするか
     * @param checkenc 出力元のファイルを環境によって適したエンコードにするかどうか
     * @author syam
     */
    static void extractResource(String from, File to, boolean force, boolean checkenc){
        File of = to;

        // ファイル展開先がディレクトリならファイルに変換、ファイルでなければ返す
        if (to.isDirectory()){
            String filename = new File(from).getName();
            of = new File(to, filename);
        }else if(!of.isFile()){
            log.warning(logPrefix+ "not a file:" + of);
            return;
        }

        // ファイルが既に存在する場合は、forceフラグがtrueでない限り展開しない
        if (of.exists() && !force){
            return;
        }

        OutputStream out = null;
        InputStream in = null;
        InputStreamReader reader = null;
        OutputStreamWriter writer =null;
        @SuppressWarnings("unused")
		DataInputStream dis = null;
        try{
            // jar内部のリソースファイルを取得
            URL res = MotdManager.class.getResource(from);
            if (res == null){
                log.warning(logPrefix+ "Can't find "+ from +" in plugin Jar file");
                return;
            }
            URLConnection resConn = res.openConnection();
            resConn.setUseCaches(false);
            in = resConn.getInputStream();

            if (in == null){
                log.warning(logPrefix+ "Can't get input stream from " + res);
            }else{
                // 出力処理 ファイルによって出力方法を変える
                if (checkenc){
                    // 環境依存文字を含むファイルはこちら環境

                    reader = new InputStreamReader(in, "UTF-8");
                    writer = new OutputStreamWriter(new FileOutputStream(of)); // 出力ファイルのエンコードは未指定 = 自動で変わるようにする

                    int text;
                    while ((text = reader.read()) != -1){
                        writer.write(text);
                    }
                }else{
                    // そのほか

                    out = new FileOutputStream(of);
                    byte[] buf = new byte[1024]; // バッファサイズ
                    int len = 0;
                    while((len = in.read(buf)) >= 0){
                        out.write(buf, 0, len);
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally{
            // 後処理
            try{
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
                if (reader != null)
                    reader.close();
                if (writer != null)
                    writer.close();
            }catch (Exception ex){}
        }
    }

    /**
     * コピー元のパス[srcPath]から、コピー先のパス[destPath]へファイルのコピーを行います。
     * コピー処理にはFileChannel#transferToメソッドを利用します。
     * コピー処理終了後、入力・出力のチャネルをクローズします。
     * @param srcPath コピー元のパス
     * @param destPath  コピー先のパス
     * @throws IOException 何らかの入出力処理例外が発生した場合
     */
    public static void copyTransfer(String srcPath, String destPath) throws IOException {
        @SuppressWarnings("resource")
		FileChannel srcChannel = new FileInputStream(srcPath).getChannel();
        @SuppressWarnings("resource")
		FileChannel destChannel = new FileOutputStream(destPath).getChannel();
        try {
            srcChannel.transferTo(0, srcChannel.size(), destChannel);
        } finally {
            srcChannel.close();
            destChannel.close();
        }
    }

    public static File getJarFile(){
        return new File("plugins", "MotdManager.jar");
    }
}
