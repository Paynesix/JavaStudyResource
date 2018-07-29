package stu.collection;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;



public class CommonUtil {

	private static CommonUtil m_CommonUtil;
	public final static String FORMAT_DATE = "yyyy-MM-dd";
	
	public static synchronized CommonUtil getInstance() throws Exception {
		if(m_CommonUtil == null ){
			m_CommonUtil = new CommonUtil();
		}
		return m_CommonUtil;
	}
	/**
	 * map to JsonString
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static String MapToJson(Map result ) throws Exception{
		return handleJavaObjToJsonSting(result);
	}
	
	/**
	 * List to JsonString
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static String ListToJson(List result ) throws Exception{
		return handleJavaObjToJsonSting(result);
	}
	
	/**
	 * 将java集合对象转换成Json字符串
	 * @param object
	 * @return
	 */
	private static String handleJavaObjToJsonSting(Object object) {
		ValueFilter filter = new ValueFilter() {
			public Object process(Object source, String name, Object value) {
				if (value == null) {
					return "";
				}
				return value;
			}

		};
		SerializeWriter out = new SerializeWriter();
		JSONSerializer serializer = new JSONSerializer(out);
		serializer.getValueFilters().add(filter);
		serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
		serializer.config(SerializerFeature.WriteMapNullValue, true);
		serializer.config(SerializerFeature.WriteNullListAsEmpty, true);
		serializer.write(object);
		return out.toString();
	}
	
	/**
	 * json格式化为map
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static Map JsonToMap(String json ) throws Exception{
		json = json.replaceAll("\"null\"", "\\\"\\\"").replaceAll("null", "\\\"\\\"");
		return (Map)JSONObject.parseObject(json,Map.class);
	}
	
	/**
	 * josn格式化为list
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static Object[] JsonToList(String json ) throws Exception{
		json = json.replaceAll("\"null\"", "\\\"\\\"").replaceAll("null", "\\\"\\\"");
		JSONArray jsonArray = JSONArray.parseArray(json);
		
		return jsonArray.toArray();
	}

  /**
   * map中的对象格式化为字符串类型
   * @param mInfo
   * @throws Exception
   */
	public static void valueToString(Map<String,Object> mInfo) throws Exception{
		if(null == mInfo || mInfo.isEmpty())return ;
		for (Iterator<String> iterator = mInfo.keySet().iterator(); iterator.hasNext();) {
			String name = iterator.next() ;
			Object objVal = mInfo.get(name);
			if(null == objVal || objVal instanceof String)continue;
			mInfo.put(name, objVal.toString());
		}
	}
	/**
	 * map所有值赋值为空
	 * @param mDesc
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map cleanMapValue(Map mDesc ) throws Exception{
		if(null == mDesc || mDesc.isEmpty() )return null;
		Map mRtn = new HashMap();
		for (Iterator iterator = mDesc.keySet().iterator(); iterator.hasNext();) {
			String name = (String)iterator.next() ;
			mRtn.put(name, "");
		}
		return mRtn;
	}
	
	/**map转化为json
	 * @deprecated
	 * @param result
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void MapToJson1(Map result ) throws Exception{
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		boolean flag = false;
		for (Iterator iterator = result.keySet().iterator(); iterator.hasNext();) {
				String fieldName = (String) iterator.next();
				if (flag)
					buf.append(",");
				else
					flag = true;
				buf.append("\"").append(fieldName).append("\":");
				Object value = result.get(fieldName);
				if(null != value && value instanceof java.lang.String){
					buf.append("\"").append(value).append("\"");
				}else if(null != value && value instanceof java.util.Date){
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					buf.append("\"").append(format.format(value)).append("\"");
				}else{
					buf.append(value);
					
				}
				
		}
		buf.append("}");
	}
	
  /**
   * 根据默认格式-格式化日期
   * @param sDate
   * @return
   * @throws Exception
   */
	public static Date formatDate(String sDate) throws Exception {
		return CommonUtil.formatDate(sDate, FORMAT_DATE);
	}
	
	/**
	 * 根据指定格式-格式化日期
	 * @param sDate
	 * @param sFormat
	 * @return
	 * @throws Exception
	 */
	public static Date formatDate(String sDate,String sFormat) throws Exception {
		if(null == sDate || "".equals(sDate.trim()))return null;
		DateFormat format = new SimpleDateFormat(sFormat);
		Date dDate = null;
		try {
			dDate = format.parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dDate;
	}
	
	/**
	 * 日期格式化 不区分大小写
	 * @param Date
	 * @param sFormat
	 * @return
	 * @throws Exception
	 */
	public static String unFormatDate(Date Date,String sFormat) throws Exception {
		if(null == Date )return null;
		if(null != sFormat){
			sFormat=sFormat.replaceAll("Y", "y").replaceAll("D", "d");
		}
		DateFormat format = new SimpleDateFormat(sFormat);
		return format.format(Date);
	}
	
	/**
     * 将数据转变为给定精度的数字
     * @param x
     * @param scale
     * @return
     */
    public static double round(double x, int scale) {       
        BigDecimal a = new BigDecimal(String.valueOf(x));
        a = a.divide(new BigDecimal("1"), scale, BigDecimal.ROUND_HALF_UP);       
        return a.doubleValue();
    }
  
	/**
	 * 将转换json时，特殊类型的处理。
	 * @param mInfo
	 */
	public static void MapValueToString(Map<String,Object> mInfo ){
		
		if(null == mInfo || mInfo.isEmpty()) return ;
		
		Iterator<String> it = mInfo.keySet().iterator();
		while(it.hasNext()){
			String name = it.next();
			Object obj = mInfo.get(name);
			if ( null == obj )continue;
			if(obj instanceof java.util.Date){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				mInfo.put(name, sdf.format((java.util.Date)obj));
			}
		}
	}
 
	public static int occurTimes(String source, String target){
		return (source.length() - source.replaceAll(target, "").length())/target.length();
	}
	
	/**
	 * 判断字符串是否符合给定的日期格式
	 * @Title: checkDate
	 * @Description: TODO(判断字符串是否符合给定的日期格式)
	 * @param date 待判断是否符合给定日期格式的字符串
	 * @param format 判断的日期格式
	 * @return boolean true 匹配成功 false 匹配失败
	 */
	public static boolean checkDate(String date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		Date d = null;
		try {
			d = df.parse(date);
		} catch (Exception e) {
			// 如果不能转换,肯定是错误格式
			return false;
		}
		String s1 = df.format(d);
		// 转换后的日期再转换回String,如果不等,逻辑错误.如format为"yyyy-MM-dd",date为
		// "2006-02-31",转换为日期后再转换回字符串为"2006-03-03",说明格式虽然对,但日期
		// 逻辑上不对.
		return date.equals(s1);
	}
	
   /**
    * JSON 解析成list
    * @param messageInfoList
    * @return
    * @throws Exception
    */
	@SuppressWarnings("unchecked")
	public static List<Map<String,Object>> parseListFromJson(String messageInfoList) throws Exception{
		//[{msgType:MT700_1,sendType:0},{msgType:MT700_1,sendType:1}]
		if(StringUtils.isEmpty(messageInfoList)){
			return new ArrayList<Map<String,Object>>();
		}
		Object obj[] = CommonUtil.JsonToList(messageInfoList);
		LinkedList<Map<String,Object>> all  = new LinkedList<Map<String,Object>>();
		if(null != obj && obj.length >0){
			for(int i = 0 ; i < obj.length ; i ++){
				JSONObject json = (JSONObject)obj[i];
				Map<String,Object> oneChg = CommonUtil.JsonToMap(json.toString()) ;
				all.add(oneChg);
			}
		}
		return all;
	}
   
}
