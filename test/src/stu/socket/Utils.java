package stu.socket;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	private static DateFormat timeGuidFormater;

	private static final char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static String quotedStr(Object str) {
		return "[" + str + "]";
	}
	/**
	 * 字符串转化为有小数点的金额 
	 * @param amt
	 * @param scale
	 * @return 金额格式的字符串
	 */
	public static String formatAmt(String amt, int scale) {
		if (isNothing(amt))
			return "";
		BigDecimal dec = new BigDecimal(amt);
		if (scale < 0)
			scale = 2;
		String format = ".";
		if (scale == 0)
			format = "";
		else
			for (int i = 0; i < scale; i++) {
				format += "0";
			}
		DecimalFormat fmt = new DecimalFormat("0" + format);
		return fmt.format(dec);
	}

	/**
	 * byte[] 转化为字符串
	 * @param values
	 * @return
	 */
	public static String encodeBytes(byte[] values) {
		StringBuffer ret = new StringBuffer();
		byte value = 0;
		for (int i = 0; i < values.length; i++) {
			value = values[i];
			int tmpValue = value & 0x00FF;
			ret.append(digit[(tmpValue >>> 4) & 0x0F]);
			ret.append(digit[(tmpValue & 0x0F)]);
		}
		return ret.toString();
	}

	/**
	 * char[] 转化为 byte[]
	 * @param inStr
	 * @return
	 */
	public static byte[] decodeBytes(char[] inStr) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		char char1;
		char char2;
		int value1;
		int value2;
		if (inStr != null)
			for (int i = 0; i < inStr.length; i = i + 2) {
				char1 = inStr[i];
				char2 = inStr[i + 1];
				if (char1 > 64)
					value1 = (char1 - 65 + 10);
				else
					value1 = (char1 - 48);
				if (char2 > 64)
					value2 = (char2 - 65 + 10);
				else
					value2 = (char2 - 48);
				value1 = (value1 << 4) | value2;
				out.write(value1);
			}
		return out.toByteArray();
	}

	/**18位随机数据*/
	public static String getTimeGUID() {
		String ret = "";
		Date d1 = new Date(System.currentTimeMillis());
		if (timeGuidFormater == null)
			timeGuidFormater = new SimpleDateFormat("yyMMddHHmmssSSS");
		ret = timeGuidFormater.format(d1);
		double ran = Math.random();
		ret += Math.round(1000 * ran);
		if (ret.length()>20){//防止超长
			ret = ret.substring(0,20);
		}
		return ret;
	}

	/**
	 * 统计以规定符分割的字符串个数
	 * @param str
	 * @param split
	 * @return
	 */
	public static int CountString(String str, char split) {
		int pos = 0;
		int count = 0;
		if (str == null)
			return count;
		pos = str.indexOf(split, pos);
		while (pos >= 0) {
			count++;
			pos++;
			pos = str.indexOf(split, pos);
		}
		return count;
	}

	/**
	 * 判断字符串为数字值
	 * @param s
	 * @return
	 */
	public static boolean isNumeric(String s) {
		if (s == null || s.trim().length() < 1)
			return false;
		String reg = "[\\d]{1,}";
		Pattern pattern = Pattern.compile(reg);
		Matcher match = pattern.matcher(s);
		if (match.matches())
			return true;
		else
			return false;
	}

	/**
	 * 是否为空字符串
	 * @param s
	 * @return
	 */
	public static boolean isNothing(String s) {
		if (s == null || s.trim().length() < 1)
			return true;
		return false;
	}

	/**
	 * 更改文件的扩展名
	 * @param file
	 * @param newext
	 * @return
	 * @throws Exception
	 */
	public static File chgExt(File file, String newext) throws Exception {
		String filename = file.getAbsolutePath();
		int index = filename.lastIndexOf('.');
		if (-1 != index)
			filename = filename.substring(0, index);
		filename = filename + '.' + newext;
		File newfile = new File(filename);
		if (newfile.exists())
			throw new Exception(newfile.getPath() + "已经存在");
		if (!file.renameTo(newfile))
			throw new Exception(file.getAbsolutePath() + "更名失败" + filename);
		return newfile;
	}

	/**
	 * 复制文件
	 * @param source
	 * @param dest
	 * @return
	 * @throws Exception
	 */
	public static boolean copyFile(File source, File dest) throws Exception {
		try {
			if (!source.exists())
				throw new Exception("源文件不存在");
			if (!source.isFile())
				throw new Exception("源文件是目录");
			if (dest.exists())
				throw new Exception("目标文件已存在");
			if (!dest.createNewFile())
				throw new Exception("不能建立文件:" + dest);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage(), e);
		}
		try {
			FileInputStream sourceStream = new FileInputStream(source);
			FileOutputStream destStream = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int len = 0;
			len = sourceStream.read(buf);
			while (len > 0) {
				destStream.write(buf, 0, len);
				len = sourceStream.read(buf);
			}
			sourceStream.close();
			destStream.flush();
			destStream.close();
		} catch (Exception e) {
			throw new Exception(e);
		}
		return true;
	}

	/**
	 * 获取文件扩展名
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String getExt(File file) throws Exception {
		String filename = file.getAbsolutePath();
		int index = filename.lastIndexOf('.');
		if (index < 0)
			return "";
		return filename.substring(index + 1, filename.length());
	}

	/**
	 * 创建路径
	 * @param path
	 * @return
	 */
	public static boolean createPath(File path) {
		if (!path.exists()) {
			return path.mkdir();
		}
		return true;
	}

	/**
	 * 获得文件系统分割符  Windows \  Linux /
	 * @return
	 */
	public static String getFileSplit() {
		return System.getProperty("file.separator");
	}

	/**
	 * 获得用户在系统中的目录
	 * @return
	 */
	public static String getUserHome() {
		return System.getProperty("user.home");
	}

	/**
	 * 获得文件夹下所有文件列表
	 * @param path
	 * @param ext
	 * @return
	 */
	public static String[] getFiles(File path, String ext) {
		return path.list(new DirFilter(ext));
	}

	/**
	 * 给字符串日期格式化为指定的日期格式
	 * @param dateStr
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static java.sql.Date parseDate(String dateStr, String format) throws Exception {
		if (dateStr == null || dateStr.trim().length() < 6)
			return null;
		if (format == null || format.trim().length() < 6)
			throw new Exception("无效的格式:" + format);
		if (dateStr.trim().length() != format.trim().length())
			throw new Exception("无效的格式:" + format);
		int start = 0;
		String tmp = null;
		start = format.indexOf("yyyy");
		if (start < 0) {
			start = format.indexOf("yy");
			if (start < 0)
				throw new Exception("无效的格式:" + format);
			else
				tmp = "20" + dateStr.substring(start, start + 2);
		} else
			tmp = dateStr.substring(start, start + 4);
		int year = Integer.parseInt(tmp);

		start = format.indexOf("mm");
		if (start < 0)
			throw new Exception("无效的格式:" + format);
		tmp = dateStr.substring(start, start + 2);
		int month = Integer.parseInt(tmp) - 1;

		start = format.indexOf("dd");
		if (start < 0)
			throw new Exception("无效的格式:" + format);
		tmp = dateStr.substring(start, start + 2);
		int date = Integer.parseInt(tmp);
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);

		return new java.sql.Date(cal.getTimeInMillis());
	}

	public static void main(String[] args) {

		System.out.println(formatAmt("69820", 2));
	}
}
