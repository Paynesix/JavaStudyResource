import com.ebills.util.EbillsCfg;
import com.ebills.util.EbillsException;
import com.ebills.util.EbillsLog;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseDAO {
	private String className = BaseDAO.class.getName();

	private EbillsLog log = new EbillsLog(this.className);
	private String local;
	private ConnectionManager cm = ConnectionManagerFactory
			.getConnectionManager();

	public BaseDAO(String local) throws EbillsException {
		this.local = local;
	}

	public int sqlUpdate(String sqlStr, List<Object> params)
			throws EbillsException {
		PreparedStatement prepStmt = null;
		Connection conn = this.cm.getConnection();
		int ires = -1;
		try {
			this.log.debug(1, null, new String[]{sqlStr,
					(params == null) ? null : params.toString()});
			prepStmt = conn.prepareStatement(sqlStr);
			setParameter(prepStmt, params);
			ires = prepStmt.executeUpdate();
		} catch (EbillsException e) {
		} catch (Exception e) {
		} finally {
			if (prepStmt != null) {
				try {
					prepStmt.close();
				} catch (SQLException e) {
					this.cm.setRollbackOnly();
					new EbillsException(e, this.local, this.className, 3, null,
							null);
				}
				prepStmt = null;
			}
			this.cm.releaseConnection(conn);
		}
		return ires;
	}

	public List<Map<String, Object>> sqlQuery(String sqlStr, List<Object> params)
			throws EbillsException {
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		Connection conn = this.cm.getConnection();
		List lt = new ArrayList();
		try {
			this.log.debug(4, null, new String[]{sqlStr,
					(params == null) ? null : params.toString()});
			prepStmt = conn.prepareStatement(sqlStr);
			setParameter(prepStmt, params);
			rs = prepStmt.executeQuery();

			ResultSetMetaData rsMetaData = prepStmt.getMetaData();
			int fldcnt = rsMetaData.getColumnCount();
			List lfldname = new ArrayList();
			List lfldtype = new ArrayList();
			for (int i = 1; i <= fldcnt; ++i) {
				lfldname.add(rsMetaData.getColumnName(i).toLowerCase());
				lfldtype.add(Integer.valueOf(rsMetaData.getColumnType(i)));
			}

			while (rs.next()) {
				Map row = new HashMap();
				for (int i = 1; i <= fldcnt; ++i) {
					String sfname = (String) lfldname.get(i - 1);
					int itype = ((Integer) lfldtype.get(i - 1)).intValue();
					row.put(sfname, getRsValue(rs, i, itype));
				}
				lt.add(row);
			}
		} catch (EbillsException e) {
		} catch (Exception e) {
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					this.cm.setRollbackOnly();
					new EbillsException(e, this.local, this.className, 6, null,
							null);
				}
				rs = null;
			}
			if (prepStmt != null) {
				try {
					prepStmt.close();
				} catch (SQLException e) {
					this.cm.setRollbackOnly();
					new EbillsException(e, this.local, this.className, 7, null,
							null);
				}
				prepStmt = null;
			}
			this.cm.releaseConnection(conn);
		}
		return lt;
	}

	public void batchInsert(String tabname, List<Map<String, Object>> value)
			throws EbillsException {
		if ((value == null) || (value.size() <= 0)) {
			return;
		}
		Connection conn = this.cm.getConnection();
		PreparedStatement pst = null;
		try {
			Object[] aflds = null;

			Map m = (Map) value.get(0);

			aflds = m.keySet().toArray();
			String sfld = "";
			String sval = "";
			for (int k = 0; k < aflds.length; ++k) {
				sfld = sfld + "," + aflds[k];
				sval = sval + ",?";
			}
			sfld = sfld.substring(1);
			sval = sval.substring(1);
			String sqlStr = "insert into " + tabname + " (" + sfld
					+ ") values (" + sval + ")";

			this.log.debug(8, null, new String[]{sqlStr, value.toString()});
			pst = conn.prepareStatement(sqlStr);

			for (int i = 0; i < value.size(); ++i) {
				m = (Map) value.get(i);
				List params = new ArrayList();
				for (int k = 1; k <= aflds.length; ++k) {
					params.add(m.get(aflds[(k - 1)]));
				}
				setParameter(pst, params);
				pst.addBatch();
			}
			pst.executeBatch();
			pst.clearBatch();
		} catch (EbillsException e) {
		} catch (Exception e) {
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					this.cm.setRollbackOnly();
					new EbillsException(e, this.local, this.className, 10,
							null, null);
				}
			}
			this.cm.releaseConnection(conn);
		}
	}

	public void batchInsertWFtskchg(List<Integer> taskids, String curDateFunStr)
			throws EbillsException {
		if ((taskids == null) || (taskids.size() <= 0)) {
			return;
		}
		Connection conn = this.cm.getConnection();
		PreparedStatement pst = null;
		try {
			String sqlStr = "insert into wftskchg (taskid, readflag, crtdate) values (?, 0, "
					+ curDateFunStr + ")";
			pst = conn.prepareStatement(sqlStr);

			for (int i = 0; i < taskids.size(); ++i) {
				List params = new ArrayList();
				params.add(taskids.get(i));
				setParameter(pst, params);
				pst.addBatch();
			}
			pst.executeBatch();
			pst.clearBatch();
		} catch (EbillsException e) {
		} catch (Exception e) {
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					this.cm.setRollbackOnly();
					new EbillsException(e, this.local, this.className, 10,
							null, null);
				}
			}
			this.cm.releaseConnection(conn);
		}
	}

	public void batchUpdateWFTSK(List<Map<String, Object>> values)
			throws EbillsException {
		Connection conn = this.cm.getConnection();
		PreparedStatement pst = null;
		try {
			String sqlStr = "update wftsk set dspop = ? where taskid = ?";
			pst = conn.prepareStatement(sqlStr);

			for (int i = 0; i < values.size(); ++i) {
				Map m = (Map) values.get(i);
				List params = new ArrayList();
				params.add(m.get("dspop"));
				params.add(m.get("taskid"));

				setParameter(pst, params);
				pst.addBatch();
			}
			pst.executeBatch();
			pst.clearBatch();
		} catch (EbillsException e) {
		} catch (Exception e) {
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					this.cm.setRollbackOnly();
					new EbillsException(e, this.local, this.className, 10,
							null, null);
				}
			}
			this.cm.releaseConnection(conn);
		}
	}

	public boolean callProc(String sqlStr, List<Object> params)
			throws EbillsException {
		CallableStatement prepStmt = null;
		Connection conn = this.cm.getConnection();
		boolean bres = true;
		try {
			this.log.debug(11, null, new String[]{sqlStr,
					(params == null) ? null : params.toString()});
			prepStmt = conn.prepareCall(sqlStr);
			setParameter(prepStmt, params);
			bres = prepStmt.execute();
		} catch (EbillsException e) {
		} catch (Exception e) {
		} finally {
			if (prepStmt != null) {
				try {
					prepStmt.close();
				} catch (SQLException e) {
					this.cm.setRollbackOnly();
					new EbillsException(e, this.local, this.className, 13,
							null, null);
				}
				prepStmt = null;
			}
			this.cm.releaseConnection(conn);
		}
		return bres;
	}

	private void setParameter(PreparedStatement prepStmt, List<Object> params)
			throws EbillsException {
		try {
			if (params != null) {
				for (int i = 1; i <= params.size(); ++i) {
					Object o = params.get(i - 1);
					if (o == null)
						prepStmt.setObject(i, o);
					else if (o instanceof Date) {
						prepStmt.setTimestamp(i,
								new Timestamp(((Date) o).getTime()));
					} else if (o instanceof Integer)
						prepStmt.setInt(i, ((Integer) o).intValue());
					else if (o instanceof Double)
						prepStmt.setDouble(i, ((Double) o).doubleValue());
					else if (o instanceof BigDecimal)
						prepStmt.setBigDecimal(i, (BigDecimal) o);
					else if (o instanceof String)
						prepStmt.setString(i, (String) o);
					else if (o instanceof byte[])
						prepStmt.setBytes(i, (byte[]) (byte[]) o);
					else
						throw new EbillsException(null, this.local,
								this.className, 14, null, new String[]{o
										.getClass().getName()});
				}
			}
		} catch (EbillsException e) {
			throw e;
		} catch (Exception e) {
			throw new EbillsException(e, this.local, this.className, 15, null,
					null);
		}
	}

	private Object getRsValue(ResultSet rs, int i, int itype)
			throws EbillsException {
		Object o;
		try {
			Object o;
			if ((itype == -5) || (itype == 4) || (itype == 5) || (itype == -6)) {
				Object o;
				if (rs.getObject(i) != null)
					o = new Integer(rs.getInt(i));
				else
					o = null;
			} else {
				Object o;
				if ((itype == 3) || (itype == 8) || (itype == 6)
						|| (itype == 7) || (itype == 2)) {
					o = rs.getBigDecimal(i);
				} else if ((itype == 92) || (itype == 91) || (itype == 93)) {
					Object o = rs.getTimestamp(i, Calendar.getInstance());
					if (o != null)
						o = new Date(((Timestamp) o).getTime());
				} else {
					Object o;
					if (itype == 2004) {
						o = readObject(rs.getBinaryStream(i));
					} else {
						Object o;
						if ((itype == 1) || (itype == 12) || (itype == 2005)) {
							o = rs.getString(i);
						} else
							throw new EbillsException(null, this.local,
									this.className, 16, null, null);
					}
				}
			}
		} catch (EbillsException e) {
			throw e;
		} catch (Exception e) {
			throw new EbillsException(e, this.local, this.className, 17, null,
					null);
		}
		return o;
	}

	public Date getCurrentTimestamp() throws EbillsException {
		String dateSql = EbillsCfg.getProperty("DBCurrentTimestampSQL");
		List list = sqlQuery(dateSql, null);
		if ((null != list) && (!(list.isEmpty()))) {
			Map map = (Map) list.get(0);
			return ((Date) map.values().toArray()[0]);
		}
		throw new EbillsException(null, this.local, this.className, 18, null,
				null);
	}

	private Object readObject(InputStream inputStream) throws EbillsException {
		try {
			if (null == inputStream)
				return null;
			ObjectInputStream ois = new ObjectInputStream(inputStream);
			Object o = ois.readObject();
			ois.close();
			return o;
		} catch (Exception ex) {
			ex.printStackTrace();
			this.log.debug(11,
					new String[]{"read buffer error" + ex.getMessage()});
		}
		return null;
	}
}