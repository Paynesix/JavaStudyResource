package stu.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import stu.db.bean.Page;

public class Dao extends CommonDao{

	/**
	 * author xianyi
	 * @param sql
	 * @param order
	 * @param inputList
	 * @param outputFields
	 * @param pageno
	 * @param pagesize
	 * @return
	 * @throws EbillsException
	 */
/*	public Page page(String sql, String order, List<Object> inputList, List<Object> outputFields, int pageno, int pagesize) 
	  {
	    if (inputList == null) {
	      inputList = new LinkedList();
	    }
	  
	    if (makeConnection() == null) {
	      throw new Exception();
	    }
	    //String dataBaseType = CommonUtil.getDataBaseType("system.DataBase");
	    StringBuffer resultSql = new StringBuffer("select * ");
	    Page page = new Page();
	    List result = new ArrayList();

	    if (dataBaseType.toLowerCase().indexOf("oracle") >= 0) {
	      if ((order != null) && (!"".equals(order)))
	        resultSql.append(" FROM ( SELECT TEAP_TABLE.*, ROW_NUMBER() OVER(" + order + ") ROWNUM_ FROM ( ");
	      else
	        resultSql.append(" FROM ( SELECT TEAP_TABLE.*, ROWNUM ROWNUM_ FROM ( ");
	    }
	    else if (dataBaseType.toLowerCase().indexOf("db2") >= 0) {
	      if ((order != null) && (!"".equals(order)))
	        resultSql.append(" FROM ( SELECT TEAP_TABLE.*, ROWNUMBER() OVER(" + order + ") ROWNUM_ FROM ( ");
	      else
	        resultSql.append(" FROM ( SELECT TEAP_TABLE.*, ROWNUMBER() OVER() ROWNUM_ FROM ( ");
	    }
	    else if (dataBaseType.toLowerCase().indexOf("mysql") >= 0) {
	      resultSql.append(" FROM ( ");
	    }

	    resultSql.append(sql);
	  if (order != null) {
	      resultSql.append(' ').append(order);
	    }
	    if (dataBaseType.toLowerCase().indexOf("oracle") >= 0) {
	      resultSql.append(")  TEAP_TABLE WHERE ROWNUM < ? ) WHERE ROWNUM_ >= ?");
	    } else if (dataBaseType.toLowerCase().indexOf("db2") >= 0) {
	      resultSql.append(")  TEAP_TABLE ) WHERE ROWNUM_ < ? AND ROWNUM_ >= ?");
	    } else if (dataBaseType.toLowerCase().indexOf("mysql") >= 0) {
	      int startIndex = (pageno - 1) * pagesize;
	      int endIndex = pagesize;
	      resultSql.append(")  TEAP_TABLE limit " + startIndex + "," + endIndex);
	    }

	    PreparedStatement statement = null;
	    ResultSet resultSet = null;
	    try {
	      statement = connection.prepareStatement(resultSql.toString());
	      IDao dao = DAOFactory.create(connection);
	      int total = dao.count(sql, (LinkedList)inputList);
	      page.setTotalNum(Integer.valueOf(total));

	      if (dataBaseType.toLowerCase().indexOf("mysql") < 0)
	      {
	        inputList.add(Integer.valueOf((pageno - 1) * pagesize + pagesize + 1));
	        inputList.add(Integer.valueOf((pageno - 1) * pagesize + 1));
	      }

	      if ((inputList != null) && (inputList.size() > 0)) {
	        SqlInOutParamter.setInParamter(statement, inputList);
	      }
	      this.log.debug(3, new String[] { resultSql.toString(), null == inputList ? "" : inputList.toString() });
	      ResultSetMetaData meta;
	      if ((pageno > 0) || ((pageno <= 0) && (page.getTotalNum().intValue() > 0))) {
	        resultSet = statement.executeQuery();
	        meta = resultSet.getMetaData();
	        while (resultSet.next()) {
	          Map map = new HashMap();
	          for (int i = 0; i < outputFields.size(); i++) {
	            int type = meta.getColumnType(i + 1);
	            String dataField = (String)outputFields.get(i);
	            Object objVal = resultSet.getObject(i + 1);
	            if (null == objVal) {
	              map.put(dataField, null);
	            }
	            else if (2004 == type) {
	              Object blob = null;
	              try {
	                blob = readObject(resultSet.getBinaryStream(i + 1));
	              } catch (Exception e) {
	                blob = new String(resultSet.getBytes(i + 1));
	              }
	              if ((blob instanceof String)) {
	                blob = ((String)blob).replaceAll(MessageInfo.swiftBegin, "").replaceAll(MessageInfo.swiftEnd, "");
	              }
	              map.put(dataField, blob);
	            } else if (2 == type) {
	              int percision = meta.getPrecision(i + 1);
	              if (percision == 0) {
	                map.put(dataField, Double.valueOf(resultSet.getDouble(i + 1)));
	              } else {
	                int ntScale = meta.getScale(i + 1);
	                if (ntScale < 0) {
	                  ntScale = 2;
	                }
	                map.put(dataField, new BigDecimal(resultSet.getDouble(i + 1)).setScale(ntScale, 4).toString());
	              }
	            }
	            else if (91 == type) {
	              String date = resultSet.getString(i + 1);
	              if (date.length() > 10) {
	                date = date.substring(0, 10);
	              }
	              map.put(dataField, date);
	            } else if (93 == type) {
	              Timestamp date = resultSet.getTimestamp(i + 1);
	              map.put(dataField, new java.util.Date(date.getTime()));
	            } else if (2005 == type) {
	              Clob clob = resultSet.getClob(i + 1);
	              String content = clobToString(clob);
	              content = content.replaceAll(MessageInfo.swiftBegin, "").replaceAll(MessageInfo.swiftEnd, "");
	              map.put(dataField, content);
	            } else if (3 == type) {
	              map.put(dataField, resultSet.getBigDecimal(i + 1).toPlainString());
	            } else {
	              map.put(dataField, resultSet.getString(i + 1));
	            }
	          }
	          result.add(map);
	        }
	      }
	      page.setList(result);
	      this.log.debug(11, new String[] { "Query results size--->" + page.getTotalNum() });
	      return page;
	    } catch (Exception ex) {
	      throw new EbillsException(ex, className, 11, null, new String[] { "query Page " + ex.getMessage() });
	    } finally {
	      cleanResources(statement);
	    }
	  }*/
	/**
	 * 
	 * @param clob
	 * @return String
	 * @throws SQLException
	 * @throws IOException
	 */
	 public String clobToString(Clob clob)
			    throws SQLException, IOException
			  {
			    String reString = "";
			    Reader is = clob.getCharacterStream();
			    BufferedReader br = new BufferedReader(is);
			    String s = br.readLine();
			    StringBuffer sb = new StringBuffer();
			    while (s != null) {
			      sb.append(s).append("\r\n");
			      s = br.readLine();
			    }
			    reString = sb.toString();
			    if (reString.endsWith("\r\n")) {
			      reString = reString.substring(0, reString.length() - 2);
			    }
			    return reString;
			  }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
