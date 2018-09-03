package stu.num;

import java.text.SimpleDateFormat;



public class EbillsUtil {
	private static String HanDigiStr[] = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

	private static String HanDiviStr[] = new String[] { "", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万",
			"拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟" };

	public static String PositiveIntegerToHanStr(String NumStr) { // 输入字符串必须正整数，只允许前导空格(必须右对齐)，不宜有前导零
		String RMBStr = "";
		boolean lastzero = false;
		boolean hasvalue = false; // 亿、万进位前有数值标记
		int len, n;
		len = NumStr.length();
		if (len > 15)
			return "数值过大!";
		for (int i = len - 1; i >= 0; i--) {
			if (NumStr.charAt(len - i - 1) == ' ')
				continue;
			n = NumStr.charAt(len - i - 1) - '0';
			if (n < 0 || n > 9)
				return "输入含非数字字符!";

			if (n != 0) {
				if (lastzero)
					RMBStr += HanDigiStr[0]; // 若干零后若跟非零值，只显示一个零
				// 除了亿万前的零不带到后面
				// if( !( n==1 && (i%4)==1 && (lastzero || i==len-1) ) ) //
				// 如十进位前有零也不发壹音用此行
				if (!(n == 1 && (i % 4) == 1 && i == len - 1)) // 十进位处于第一位不发壹音
					RMBStr += HanDigiStr[n];
				RMBStr += HanDiviStr[i]; // 非零值后加进位，个位为空
				hasvalue = true; // 置万进位前有值标记

			} else {
				if ((i % 8) == 0 || ((i % 8) == 4 && hasvalue)) // 亿万之间必须有非零值方显示万
					RMBStr += HanDiviStr[i]; // “亿”或“万”
			}
			if (i % 8 == 0)
				hasvalue = false; // 万进位前有值标记逢亿复位
			lastzero = (n == 0) && (i % 4 != 0);
		}

		if (RMBStr.length() == 0)
			return HanDigiStr[0]; // 输入空字符或"0"，返回"零"
		return RMBStr;
	}

	/** 将金额转换成大写 */
	public static String doubleToMoneyStr(double val) {
		String SignStr = "";
		String TailStr = "";
		long fraction, integer;
		int jiao, fen;

		if (val < 0) {
			val = -val;
			SignStr = "负";
		}
		if (val > 99999999999999.999 || val < -99999999999999.999)
			return "数值位数过大!";
		// 四舍五入到分
		long temp = Math.round(val * 100);
		integer = temp / 100;
		fraction = temp % 100;
		jiao = (int) fraction / 10;
		fen = (int) fraction % 10;
		if (jiao == 0 && fen == 0) {
			TailStr = "整";
		} else {
			TailStr = HanDigiStr[jiao];
			if (jiao != 0)
				TailStr += "角";
			if (integer == 0 && jiao == 0) // 零元后不写零几分
				TailStr = "";
			if (fen != 0)
				TailStr += HanDigiStr[fen] + "分";
		}

		// 下一行可用于非正规金融场合，0.03只显示“叁分”而不是“零元叁分”
		// if( !integer ) return SignStr+TailStr;

		return SignStr + PositiveIntegerToHanStr(String.valueOf(integer)) + "元" + TailStr;
	}

	//获得汇票要求的英文大写金额
	public static String draftAmtToEn(double num, String curSign) {
		String draftAmtStr = "";
		String tmpStr = arabToEn(num);
		if (curSign.equals("USD")) {
			draftAmtStr = "SAY U.S. DOLLARS ";
		} else if (curSign.equals("EUR")) {
			draftAmtStr = "SAY EUROPEAN DOLLARS ";
		} else if (curSign.equals("GBP")) {
			draftAmtStr = "SAY GREAT BRITAIN POUNDS DOLLARS ";
		} else if (curSign.equals("HKD")) {
			draftAmtStr = "SAY HONGKONG  DOLLARS ";
		} else if (curSign.equals("JPY")) {
			draftAmtStr = "SAY JAPANESE YEN DOLLARS ";
		}
		if (!draftAmtStr.equals(""))
			draftAmtStr += tmpStr + " ONLY.";
		return draftAmtStr;
	}

	public static String arabToEn(double num) {
		String strIntNum = String.valueOf(Math.round(num * 100) / 100);
		String strDecNum = String.valueOf(Math.round(num * 100) % 100);
		String[] enNumericaUnit = new String[] { "ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SERVEN",
				"EIGHT", "NINE", "TEN", "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN",
				"EIGHTEEN", "NINETEEN", "", "TWENTY", "THIRTY", "FORTY", "FIFTY", "SIXTY", "SEVENTY", "EIGHTY",
				"NINETY", "HUNDRED", "", "THOUSAND", "MILLION", "BILLION", "TRILLION", "QUADRILLION", "QUINTILLION" };
		String[] enNumerica = new String[1000];
		for (int i = 0; i < 1000; i++) {
			enNumerica[i] = i < 20 ? enNumericaUnit[i] : i < 100 ? enNumericaUnit[19 + (int) (i / 10)]
					+ (i % 10 == 0 ? "" : " " + enNumerica[i % 10]) : enNumericaUnit[(int) (i / 100)] + " "
					+ enNumericaUnit[29] + (i % 100 == 0 ? "" : " AND " + enNumerica[i % 100]);
		}
		String enText = "";
		int strIntNumLen = strIntNum.length();
		int splitLen = (int) (Math.ceil((float) strIntNumLen / 3));
		for (int i = 0; i < splitLen; i++) {
			String[] intSec = new String[splitLen];
			if ((strIntNumLen - 3 * i - 3) < 0) {
				intSec[i] = strIntNum.substring(0, strIntNumLen - 3 * i);
			} else {
				intSec[i] = strIntNum.substring(strIntNumLen - 3 * i - 3, strIntNumLen - 3 * i);
			}
			String tmpEnText = (i == 0 && Integer.parseInt(intSec[i]) > 0 && Integer.parseInt(intSec[i]) < 100
					&& Integer.parseInt(strIntNum.substring(0, strIntNumLen - 3), 10) > 0 ? " AND " : "")
					+ (Integer.parseInt(intSec[i]) == 0 && (i > 0 || Integer.parseInt(strIntNum) != 0) ? ""
							: enNumerica[Integer.parseInt(intSec[i])])
					+ (Integer.parseInt(intSec[i]) == 0 ? "" : " " + enNumericaUnit[30 + i])
					+ (i == 0 || Integer.parseInt(intSec[i]) == 0
							|| (Integer.parseInt(intSec[i]) > 0 && enText.equals("")) ? "" : " ");
			if (i != 0) {
				tmpEnText = tmpEnText.replaceAll(" AND ", " ");
			}
			enText = tmpEnText + enText;
		}
		if (!strDecNum.equals("") && Integer.parseInt(strDecNum) > 0) {
			enText += "CENTS " + enNumerica[Math.round(Float.parseFloat("." + strDecNum) * 100)];
		}
		return enText;
	}

	/** 返回报表的银行名称 */
	public static String getCnBankName() {
		return "银川市商业银行";
	}

	/** 返回报表的银行名称 */
	public static String getEnBankName() {
		return "YINCHUAN CITY COMMERCIAL BANK";
	}

	/**返回银行外管局编号*/
	public static String getBankSafeCode() {
		return "640100004501";
	}

	/**返回国际部电话*/
	public static String getBankTelephone() {
		return "0951-5058876";
	}
	
/*
	*//**获得一个月的第一天*//*
	public static java.sql.Date getMonthStartDate(String year, String month) throws EbillsException {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			return toSQLDate(df.parse(year + "-" + month + "-" + "01"));
		} catch (Exception e) {
			throw new EbillsException("获得月的第一天错误:", e);
		}
	}

	*//**获得一个月的最后一天*//*
	public static java.sql.Date getMonthEndDate(String year, String month) throws EbillsException {
		java.util.Date firstDate = getMonthStartDate(year, month);
		Calendar c = Calendar.getInstance();
		c.setTime(firstDate);
		int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.set(Calendar.DAY_OF_MONTH, lastDay);
		return toSQLDate(c.getTime());

	}

	*//**获得年的第一天*//*
	public static java.sql.Date getYearStartDate(String year) throws EbillsException {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			return toSQLDate(df.parse(year + "-01-01"));
		} catch (Exception e) {
			throw new EbillsException("获得年的第一天错误:", e);
		}
	}

	*//**获得年的第一天*//*
	public static java.sql.Date getYearStartDate(java.util.Date date) throws EbillsException {
		try {
			String year;
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			year = df.format(date);
			return toSQLDate(df.parse(year + "-01-01"));
		} catch (Exception e) {
			throw new EbillsException("获得年的第一天错误:", e);
		}
	}

	*//**获得年的最后第一天*//*
	public static java.sql.Date getYearEndDate(String year) throws EbillsException {
		java.util.Date firstDate = getYearStartDate(year);
		Calendar c = Calendar.getInstance();
		c.setTime(firstDate);
		int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		int lastMonth = c.getActualMaximum(Calendar.MONTH);
		c.set(Calendar.DAY_OF_MONTH, lastDay);
		c.set(Calendar.MONTH, lastMonth);
		return toSQLDate(c.getTime());
	}*/

	public static void main(String args[]) throws Exception {
	}

	/**报表解码用*/
	public static String decode(String key, String[] keys, String values[]) {
		return decode(key, keys, values, "");
	}

	/**报表解码用*/
	public static String decode(String key, String[] keys, String values[], String defalutValue) {
		int i = keys.length;
		String result = defalutValue;
		if (key == null)
			key = "";
		for (i = 0; i < keys.length; i++) {
			if (key.equals(keys[i])) {
				result = values[i];
				break;
			}
		}
		return result;
	}

	/**报表解码用*/
	public static String decode(Integer key, int[] keys, String values[]) {
		return decode(key, keys, values, "");
	}

	public static String decode(Integer key, int[] keys, String values[], String defualtValue) {
		int i = keys.length;
		String result = defualtValue;
		if (key == null)
			key = new Integer(0);
		for (i = 0; i < keys.length; i++) {
			if (key.intValue() == keys[i]) {
				result = values[i];
				break;
			}
		}
		return result;
	}

/*	*//***//*
	public static Date parseDate(String dateStr, String split) {
		if (dateStr == null || dateStr.trim().equals(""))
			return null;
		String items[] = dateStr.toString().split(split);
		int year = Integer.parseInt(items[0]) - 1900;
		int month = Integer.parseInt(items[1]) - 1;
		int date = Integer.parseInt(items[2]);
		return new java.sql.Date(year, month, date);
	}
*/
	public static String dateToSqlStr(java.sql.Date date) {
		String sqlStr = "";
		if (date == null)
			sqlStr = "1910-01-01";
		else {
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			sqlStr = formater.format(date);
		}
		sqlStr = "to_date('" + sqlStr + "','yyyy-mm-dd')";
		return sqlStr;
	}
	/**
	 * MD5加密
	 * @param strInput
	 * @return
	 * @throws EbillsException
	 *//*
	public static String encryptMD5(String strInput) throws EbillsException {
        String strOutput = new String("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(strInput.getBytes());
            byte b[] = md.digest();
            for (int i = 0; i < b.length; i++) {
                char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
                char[] ob = new char[2];
                ob[0] = digit[(b[i] >>> 4) & 0X0F];
                ob[1] = digit[b[i] & 0X0F];
                strOutput += new String(ob);
            }
        } catch (NoSuchAlgorithmException nsae) {
            throw new EbillsException("加密算法错误");
        }

        return strOutput;
    }
	
	*//**
	 * 判断用户密码是否过期
	 * @param user
	 * @return
	 * @throws EbillsException
	 *//*
	public static String checkPassWord(User user) throws EbillsException{
		String result = "OK";
		try{
			String pwd = user.getPassword();
			if (pwd==null||pwd.equals("")){
				result ="用户密码为空，请立即修改您的密码";
			}
			//当前日期
			Date currentDate = new Date(EbillsSystem.currentTimeMillis());
			//密码创建日期
			Date createDate = user.getCreatePW();
			if (((currentDate.getTime()-createDate.getTime())/(1000*60*60*24))>30) {
				result="您的密码已有30天未更新，请立即修改您的密码";
			}
			
		}catch (Exception e){
			throw new EbillsException("密码错误");
		}
		return result;
	}
	*/
}
