 

import com.ebills.util.EbillsException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public abstract class ConnectionManager {
	private String className;
	private ThreadLocal<Boolean> eapTransaction;
	private ThreadLocal<ArrayList<Boolean>> lastTransaction;
	private ThreadLocal<ArrayList<ConnectionRefCount>> connList;

	public ConnectionManager() {
		this.className = ConnectionManager.class.getName();
		this.eapTransaction = new ThreadLocal();
		this.lastTransaction = new ThreadLocal();
		this.connList = new ThreadLocal();
	}

	public boolean isAutoTransaction() {
		if (isEapTransaction())
			return true;
		List list = (List) this.connList.get();

		return (null == list);
	}

	public Connection getConnection() throws EbillsException {
		Connection conn = null;
		if (isAutoTransaction()) {
			conn = openConnection();
		} else {
			List list = (List) this.connList.get();
			ConnectionRefCount crfc = (ConnectionRefCount) list
					.get(list.size() - 1);
			conn = crfc.con;
		}
		return conn;
	}

	public void releaseConnection(Connection con) throws EbillsException {
		closeConnection(con);
	}

	public void startTransaction(boolean requestNew) throws EbillsException {
		Logger.getLogger(ConnectionManager.class.getName()).debug(
				"startTransaction================================new");
		setLastTransaction(isEapTransaction());
		setEapTransaction(false);
		List list = (List) this.connList.get();

		if (requestNew) {
			if (null == list) {
				this.connList.set(new ArrayList());
				list = (List) this.connList.get();
			}

			ConnectionRefCount crfc = new ConnectionRefCount();
			crfc.con = openConnection();
			try {
				crfc.con.setAutoCommit(false);
			} catch (SQLException e) {
				new EbillsException(e, null, this.className, 3, null, null);
			}
			list.add(crfc);
		} else if ((list != null) && (!(list.isEmpty()))) {
			ConnectionRefCount crfc = (ConnectionRefCount) list
					.get(list.size() - 1);

			crfc.refCount += 1;
		}
	}

	public void commit() throws EbillsException {
		Logger.getLogger(ConnectionManager.class.getName()).debug(
				"startTransaction================================end");
		if (isAutoTransaction()) {
			return;
		}
		List list = (List) this.connList.get();

		ConnectionRefCount crfc = (ConnectionRefCount) list
				.get(list.size() - 1);

		if (crfc.refCount == 0) {
			Connection conn = crfc.con;
			boolean rollback = crfc.rollback;
			list.remove(list.size() - 1);
			if (list.isEmpty()) {
				this.connList.set(null);
			}
			try {
				if (rollback)
					conn.rollback();
				else
					conn.commit();
			} catch (SQLException e) {
				new EbillsException(e, null, this.className, 1, null, null);
			}
			try {
				conn.close();
			} catch (SQLException e) {
				new EbillsException(e, null, this.className, 2, null, null);
			}
		} else {
			crfc.refCount -= 1;
		}
		setEapTransaction(getLastTransaction());
	}

	public void setRollbackOnly() throws EbillsException {
		if (isAutoTransaction()) {
			return;
		}
		List list = (List) this.connList.get();

		if ((list == null) || (list.size() < 1)) {
			return;
		}

		ConnectionRefCount crfc = (ConnectionRefCount) list
				.get(list.size() - 1);

		crfc.rollback = true;
	}

	protected abstract Connection openConnection() throws EbillsException;

	protected abstract void closeConnection(Connection paramConnection)
			throws EbillsException;

	public boolean isEapTransaction() {
		return ((this.eapTransaction.get() != null) && (((Boolean) this.eapTransaction
				.get()).booleanValue()));
	}

	public void setEapTransaction(boolean eapTransaction) {
		this.eapTransaction.set(Boolean.valueOf(eapTransaction));
	}

	public boolean getLastTransaction() {
		ArrayList list = (ArrayList) this.lastTransaction.get();
		if ((list == null) || (list.size() < 1)) {
			return false;
		}
		Boolean is = (Boolean) list.get(list.size() - 1);
		list.remove(list.size() - 1);
		return is.booleanValue();
	}

	public void setLastTransaction(boolean aLastTransaction) {
		ArrayList list = (ArrayList) this.lastTransaction.get();
		if (list == null) {
			this.lastTransaction.set(new ArrayList());
		}
		((ArrayList) this.lastTransaction.get()).add(Boolean
				.valueOf(aLastTransaction));
	}
}