import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesCfg {
	public static final String EBILLS_MSG_FORMAT = "[{0}][{1}]{2}";
	public static final String LOG_CONFIG_FILE = "log4j.xml";
	public static final String ENV_HOME_DIR = "company.home.dir";
	public static final String CONFIG_FILE = "company.properties";
	public static final String LOG_TODB_FILE = "log2DB.xml";
	public static final String LOG_TODB_PROPKEY = "wf.log.dbcfg";
	public static final String MSG_FILE_PREFEX = "core_";
	public static final String DEFAULT_MSG_FILE = "core_default";
	public static final String CLASS_NO_FILE = "core_classno.properties";
	private static Map<String, String> cfg = new HashMap();

	public static synchronized String getProperty(String key) {
		return ((String) cfg.get(key));
	}

	public static synchronized String getProperty(String key, String defautlVal) {
		if ((cfg != null) && (cfg.containsKey(key))) {
			return ((String) cfg.get(key));
		}
		return defautlVal;
	}

	public static synchronized String setProperty(String key, String value) {
		return ((String) cfg.put(key, value));
	}

	public static String getHomeDir() {
		String hdir = System.getProperty("company.home.dir");
		if (null == hdir) {
			File file = new File("d:\\");
			if (file.exists()) {
				return "d:\\gjyw\\";
			}
			file = new File("e:\\");
			if (file.exists()) {
				return "e:\\gjyw\\";
			}
			file = new File("/home");
			if (file.exists()) {
				return "/home/gjyw/";
			}
			hdir = "";
		} else if ((!(hdir.endsWith("/"))) && (!(hdir.endsWith("\\")))) {
			hdir = hdir + File.separator;
		}

		return hdir;
	}

	static {
		Properties pr = new Properties();
		try {
			pr.load(new BufferedInputStream(new FileInputStream(getHomeDir()
					+ "company.properties")));

			Enumeration en = pr.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				cfg.put(key, pr.getProperty(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}