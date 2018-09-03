/*
 * Created on 2008-1-20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package stu.xml;

/**
 * @author xf
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.*;
import java.util.*;
import java.io.*;

public class Tools {
	
	/**
	 * 验证日期正确性
	 * @param datestr
	 * @param format
	 * @param yearfrom
	 * @param yearto
	 * @return
	 */
	public static boolean isValidDate(String datestr, String format, int yearfrom, int yearto) {
		DateFormat df = new SimpleDateFormat(format);
		ParsePosition pp = new ParsePosition(0); 
		df.setLenient(false);
		Date dt = df.parse(datestr, pp);
		if (pp.getIndex() != datestr.length())
			return false;
		int y = dt.getYear() + 1900;
		if (yearto < y || yearfrom > y)
			return false;
		return true;
	}
	
	/**
	 * 验证日期正确性
	 * @param datestr
	 * @param format
	 * @return
	 */
	public static boolean isValidDate(String datestr, String format) {
		return isValidDate(datestr, format, 1000, 3000);
	}
	
	/**
	 * 文件读入转化为String
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static String file2String(String filename) throws Exception {
		FileChannel fc = new FileInputStream(filename).getChannel();
		ByteBuffer buf = ByteBuffer.allocate((int) fc.size());
		fc.read(buf);
	    String res = Charset.forName(System.getProperty("file.encoding")).decode((ByteBuffer)buf.rewind()).toString();	
	    fc.close();
	    return res;
	}
	
	/**
	 * 文件读入转化为String
	 * @param str
	 * @param filename
	 * @throws Exception
	 */
	public static void string2File(String str, String filename) throws Exception {
		new FileOutputStream(filename).write(str.getBytes());
	}
	
	/**
	 * properties文件读入为String类型
	 * @param prop
	 * @return
	 * @throws Exception
	 */
	public static String prop2String(Properties prop) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		prop.store(bos, null);
		return bos.toString();
	}

	/**
	 * String文件读入为properties类型
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static Properties string2Prop(String str) throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes());
		Properties prop = new Properties();
		prop.load(bis);
		return prop;
	}
	
	/**
	 * 异常显示为String
	 * @param t
	 * @return
	 */
	public static String throwable2String(Throwable t) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bos);
		t.printStackTrace(ps);
		return bos.toString();
	}
	
	//将目录中所有的文件作为Properties对象，以文件名作key读入一个Hashtable
	public static Hashtable loadPropFromPath(String datapath) throws Exception {
		Hashtable table = new Hashtable();
		
		File dpath = new File(datapath);
		File[] list = dpath.listFiles();
		for (int i=0; i<list.length; i++) {
			if (! list[i].isFile()) 
				continue;
			Properties pr = new Properties();
			String sname = list[i].getName();
			String lname = list[i].getAbsolutePath();
			pr.load(new FileInputStream(lname));
			table.put(sname, pr);
		}
		
		return table;
	}	
	
	/**
	 * 路径下的资源读入文件写入流
	 * @param recouce
	 * @return
	 */
	public static InputStream getResource(String recouce) {
		InputStream is = null;

		try {
			if (is == null)
				is = Thread.currentThread().getContextClassLoader().getResourceAsStream(recouce);
			if (is == null)
				is = ClassLoader.getSystemResourceAsStream(recouce);
			if (is == null) {
				try {
					File file = new File(Tools.getHomeDir() + recouce);
					is = getResource(file);
				} catch (Exception eb) {
					is = null;
				}
			}
		} catch (Exception e) {
			;
		}
		return is;
	}
	
	/**
	 * 文件写入文件流
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getResource(File file) throws FileNotFoundException {
		InputStream is = new FileInputStream(file);
		return is;
	}

	/**
	 * 文件写入文件流
	 * @param url
	 * @return
	 */
	protected static InputStream getResource(URL url) {
		InputStream is = null;
		if (url != null) {
			try {
				is = url.openStream();
			} catch (Exception ex) {
			}
		}
		return is;
	}	
	//与EbillsCfg.getHomeDir一致
	public static String getHomeDir() {
		File file = new File("d:\\");
		if (file.exists()) {
			return "d:\\fcpp\\";
		}
		file = new File("e:\\");
		if (file.exists()) {
			return "e:\\fcpp\\";
		}
		file = new File("/home");
		if (file.exists()) {
			return "/home/wasadmin/fcpp/";
		}
		return "";
	}
}
