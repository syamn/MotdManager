/**
 * MotdManager - Package: syam.motdmanager.util
 * Created: 2012/11/02 6:10:39
 */
package syam.motdmanager.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * TextFileHandler (TextFileHandler.java)
 * @author syam(syamn)
 */
public class TextFileHandler {
    private final String p;

    public TextFileHandler(String path) {
        p = path;
        if(!new File(p).exists()) {
            try {
                // 無ければ作る
                new File(p).createNewFile();
            } catch (IOException ex) {}
        }
    }

    /**
     * ファイルを行ごとに全取得
     * @return 行ごとのList<String>
     * @throws FileNotFoundException
     * @throws IOException
     */
    public List<String> readLines() throws FileNotFoundException, IOException {
        BufferedReader inputStream = null;
        List<String> data = new ArrayList<String>();
        try {
            inputStream = new BufferedReader(new FileReader(p));
            String l;

            while ((l = inputStream.readLine()) != null) {
                data.add(l);
            }
            /* 最終処理 */
        } finally {
            if(inputStream != null) {
                inputStream.close();
            }
        }
        return data;
    }

    /**
     * ファイルを書き込み
     * @param data
     * @throws IOException
     */
    public void writeLines(List<String> data) throws IOException {
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new FileWriter(p));
            while(!data.isEmpty()) {
                outputStream.println(data.remove(0));
            }
            /* 最終処理 */
        } finally {
            if(outputStream != null) {
                outputStream.close();
            }
        }
    }

    /**
     * ファイルの最終行に追記
     * @param line
     * @throws IOException
     */
    public void appendLine(String line) throws IOException  {
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new FileWriter(p, true));
            outputStream.println(line);
            /* 最終処理 */
        } finally {
            if(outputStream != null) {
                outputStream.close();
            }
        }
    }
}
