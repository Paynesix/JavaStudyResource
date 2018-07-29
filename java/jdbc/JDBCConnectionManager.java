 
import com.ebills.util.EbillsException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnectionManager extends ConnectionManager {
	protected Connection openConnection() throws EbillsException {
		Connection conn = null;
		try {
			String jdbcDriver = getProperty().getProperty("jdbc.driver",
					"oracle.jdbc.driver.OracleDriver");
			String jdbcUrl = getProperty().getProperty("jdbc.url",
					"jdbc:oracle:thin:@10.0.0.251:1521:ORCL");
			String jdbcUname = getProperty().getProperty("jdbc.username",
					"test1");
			String jdbcPwd = getProperty()
					.getProperty("jdbc.password", "test1");

			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, jdbcUname, jdbcPwd);
			if (conn == null)
				throw new Exception("do not catch Connection!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new EbillsException(e, "", super.getClass().getName(), 1,
					null, null);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new EbillsException(e, "", super.getClass().getName(), 2,
					null, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EbillsException(e, "", super.getClass().getName(), 3,
					null, null);
		}

		return conn;
	}

	protected void closeConnection(Connection conn) throws EbillsException {
		if (!(isAutoTransaction()))
			return;
		try {
			conn.close();
		} catch (SQLException e) {
			throw new EbillsException(e, "", super.getClass().getName(), 4,
					null, null);
		}
	}

	private String getHomeDir() {
		String hdir = null;
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
		}
		return hdir;
	}

	private Properties getProperty() {
		Properties pr = new Properties();
		try {
			String fpath = getHomeDir() + "jdbc.properties";
			pr.load(new BufferedInputStream(new FileInputStream(fpath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pr;
	}
}