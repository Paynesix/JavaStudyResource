 
import com.ebills.util.EbillsCfg;
import com.ebills.util.EbillsException;

public class ConnectionManagerFactory {
	public static final String CONNECTION_MANAGER = "ConnectionManager";
	private static ConnectionManager cm = null;

	public static ConnectionManager getConnectionManager()
			throws EbillsException {
		if (null == cm) {
			String cmName = EbillsCfg.getProperty("ConnectionManager");
			try {
				cm = (ConnectionManager) Class.forName(cmName).newInstance();
			} catch (Exception e) {
				throw new EbillsException(e, null,
						ConnectionManagerFactory.class.getName(), 1, null, null);
			}
		}

		return cm;
	}
}