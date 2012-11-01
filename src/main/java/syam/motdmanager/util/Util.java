/**
 * MotdManager - Package: syam.motdmanager.util
 * Created: 2012/11/02 6:09:32
 */
package syam.motdmanager.util;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Util (Util.java)
 * @author syam(syamn)
 */
public class Util{
	/**
	 * 文字列が整数型に変換できるか返す
	 * @param str チェックする文字列
	 * @return 変換成功ならtrue、失敗ならfalse
	 */
	public static boolean isInteger(String str) {
		try{
			Integer.parseInt(str);
		}catch (NumberFormatException e){
			return false;
		}
		return true;
	}

	/**
	 * 文字列がdouble型に変換できるか返す
	 * @param str チェックする文字列
	 * @return 変換成功ならtrue、失敗ならfalse
	 */
	public static boolean isDouble(String str) {
		try{
			Double.parseDouble(str);
		}catch (NumberFormatException e){
			return false;
		}
		return true;
	}

	/**
	 * PHPの join(array, delimiter) と同じ関数
	 * @param s 結合するコレクション
	 * @param delimiter デリミタ文字
	 * @return 結合後の文字列
	 */
	public static String join(Collection<?> s, String delimiter) {
		StringBuffer buffer = new StringBuffer();
		Iterator<?> iter = s.iterator();

		// 要素が無くなるまでループ
		while (iter.hasNext()){
			buffer.append(iter.next());
			// 次の要素があればデリミタを挟む
			if (iter.hasNext()){
				buffer.append(delimiter);
			}
		}
		// バッファ文字列を返す
		return buffer.toString();
	}

	/**
	 * ファイル名から拡張子を返します
	 * @param fileName ファイル名
	 * @return ファイルの拡張子
	 */
	public static String getSuffix(String fileName) {
		if (fileName == null)
			return null;
		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			return fileName.substring(point + 1);
		}
		return fileName;
	}

	/**
	 * Unix秒を yy/MM/dd HH:mm:ss フォーマットにして返す
	 * @param unixSec Unix秒
	 * @return yy/MM/dd HH:mm:ss
	 */
	public static String getDispTimeByUnixTime(long unixSec){
		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
		return sdf.format(new Date(unixSec * 1000));
	}

	/**
	 * 現在のUnix秒を取得する
	 * @return long unixSec
	 */
	public static Long getCurrentUnixSec(){
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * Unix秒からDateを取得して返す
	 * @return Date
	 */
	public static Date getDateByUnixTime(long unixSec){
		return new Date(unixSec * 1000);
	}

	/**
	 * 分を読みやすい時間表記にして返す
	 * @param min
	 * @return
	 */
	public static String getReadableTimeByMinute(int min){
		if (min < 0) return "0分間";
		if (min < 60) return min + "分間";
		if (min % 60 == 0) return min / 60 + "時間";

		int h = min / 60;
		int m = min % 60;
		return h + "時間" + m + "分";
	}
}