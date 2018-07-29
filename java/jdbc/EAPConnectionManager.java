 
import com.eap.resource.ResourceManager;
import com.ebills.util.EbillsCfg;
import com.ebills.util.EbillsException;
import java.sql.Connection;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.commons.bussprocess.transaction.BPTransactionDefinition;
import org.apache.commons.bussprocess.transaction.BPTransactionManager;

public class EAPConnectionManager extends ConnectionManager {
	private String className;
	public static final String EAP_PROJECT_NAME = "eap.ProjectName";
	public static final String EAP_DATASOURCE_NAME = "eap.DatasourceName";
	public static final String EAP_TRANSACTION_MANAGER_NAME = "eap.transactionManager";
	public static final String JNDI_FACTORY = "JNDIFactory";
	public static final String JNDI_PROVIDER = "JNDIProvider";
	public static final String JNDI_NAME = "JNDIName";

	public EAPConnectionManager() {
		this.className = EAPConnectionManager.class.getName();
	}

	protected Connection openConnection() throws EbillsException {
		Connection conn = null;

		if (isAutoTransaction()) {
			try {
				BPTransactionManager transactionManager = (BPTransactionManager) ResourceManager
						.getResource(EbillsCfg.getProperty("eap.ProjectName"),
								EbillsCfg.getProperty("eap.transactionManager"));

				transactionManager
						.getTransaction(new BPTransactionDefinition(0));

				DataSource dataSource = (DataSource) ResourceManager
						.getResource(EbillsCfg.getProperty("eap.ProjectName"),
								EbillsCfg.getProperty("eap.DatasourceName"));

				conn = transactionManager.getConnection(dataSource);
			} catch (Exception e) {
				throw new EbillsException(e, null, this.className, 1, null,
						null);
			}
		} else {
			Properties prop = new Properties();
			if ((EbillsCfg.getProperty("JNDIFactory") != null)
					&& (!("".equals(EbillsCfg.getProperty("JNDIFactory").trim())))) {
				prop.put("java.naming.factory.initial",
						EbillsCfg.getProperty("JNDIFactory"));
			}

			if ((EbillsCfg.getProperty("JNDIProvider") != null)
					&& (!("".equals(EbillsCfg.getProperty("JNDIProvider")
							.trim())))) {
				prop.put("java.naming.provider.url",
						EbillsCfg.getProperty("JNDIProvider"));
			}
			try {
				InitialContext ctx = new InitialContext(prop);
				DataSource ds = (DataSource) ctx.lookup(EbillsCfg
						.getProperty("JNDIName"));

				conn = ds.getConnection();
			} catch (Exception e) {
				throw new EbillsException(e, null, this.className, 2, null,
						null);
			}
		}

		return conn;
	}

	public Connection getConnectionNoTransaction() throws EbillsException {
		Connection conn = null;
		Properties prop = new Properties();
		if ((EbillsCfg.getProperty("JNDIFactory") != null)
				&& (!("".equals(EbillsCfg.getProperty("JNDIFactory").trim())))) {
			prop.put("java.naming.factory.initial",
					EbillsCfg.getProperty("JNDIFactory"));
		}

		if ((EbillsCfg.getProperty("JNDIProvider") != null)
				&& (!("".equals(EbillsCfg.getProperty("JNDIProvider").trim())))) {
			prop.put("java.naming.provider.url",
					EbillsCfg.getProperty("JNDIProvider"));
		}
		try {
			InitialContext ctx = new InitialContext(prop);
			DataSource ds = (DataSource) ctx.lookup(EbillsCfg
					.getProperty("JNDIName"));

			conn = ds.getConnection();
		} catch (Exception e) {
			throw new EbillsException(e, null, this.className, 2, null, null);
		}
		return conn;
	}

	protected void closeConnection(Connection conn) throws EbillsException {
	}
}