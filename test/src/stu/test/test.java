package stu.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.print(getUserHome());
		
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("one", "1");
		m.put("one", "2");
		
		System.out.print(m.get("one"));
		
		
		// TODO Auto-generated method stub
		// System.out.print("I love you");
		// System.out.println( System.getProperty("user.home"));
		// System.out.println(93&-8);
		// System.out.println(0&1);
		// System.out.println(0&0);
		// System.out.println(1&1);
		// System.out.println(Integer.toBinaryString(-1));
		// System.out.println(Integer.toBinaryString(1));
		//
		// int a = 5;
		// a = 10;
		// double d = 1.00001;
		// String tmpValue;
		// System.out.print("d: = " + d + "\n");
		// DecimalFormat df = new DecimalFormat("001");
		// tmpValue =
		// df.format(Double.parseDouble(String.valueOf("6.73400000")));

		// System.out.print("\n tmpValue: = " + tmpValue);

		// System.out.print(pad("0.001" , 10, "@", false) );

		// System.out.print(null == null );
		// System.out.print("".equals(""));

		Set<String> set = new HashSet<String>();
		String a = "abc";
		String b = "abc";
		set.add(a);
		System.out.print(set.contains(b));

	}
	/**
	 * 获得用户主机
	 * @return
	 */
	public static String getUserHome() {
		return System.getProperty("user.home");
	}


	private static String pad(String inStr, int len, String filStr,
			boolean filway) {
		if (inStr == null)
			inStr = "";
		while (inStr.getBytes().length < len && filStr != null
				&& !"".equals(filStr)) {
			if (filway) {
				inStr = filStr + inStr;
			} else {
				inStr += filStr;
			}
		}
		return inStr;
	}

	public void oracle() {
		
		try {
			String url = "jdbc:oracle:thin:@IP:1521:orcl"; // orcl为数据库的SID
			String user = "oracle";
			String password = "oracle";
			StringBuffer sql = new StringBuffer();
			sql.append("insert into ex_log (EX_LOG_ID,EX_LOG_DATE) values (?,?)");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = (Connection) DriverManager.getConnection(url,user, password);
			// 关闭事务自动提交
			con.setAutoCommit(false);
			final int batchSize = 10000;
			int count = 0;
			Long startTime = System.currentTimeMillis();
			PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql.toString());
			for (int i = 0; i < 3; i++) {
				pst.setString(1, "1");
				pst.setString(2, "2");
				// 把一个SQL命令加入命令列表
				pst.addBatch();
				if (++count % batchSize == 0) {
					pst.executeBatch();
					con.commit();
					pst.clearBatch();
					count = 0;
				}
			}
			// 执行批量更新
			pst.executeBatch();
			// 语句执行完毕，提交本事务
			con.commit();
			Long endTime = System.currentTimeMillis();
			System.out.println("用时：" + (endTime - startTime));
			pst.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
