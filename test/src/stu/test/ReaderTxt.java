package stu.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReaderTxt {

	/**
	 * 读取txt文本，以制表符分割的数据.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String targetFilePath = "D:\\gjyw\\uploads\\swift\\test.txt";
//		readTxtFile(targetFilePath);
//		praseTxt(targetFilePath);
//		String s = StringStartTrim(",,,a,bc,,de,fg,", ",");
//		String t = StringEndTrim(",,,a,bc,,de,fg,", ",");
//		String v = StringStartAndEndTrim(",	,1232,,", ",");
//		System.out.print ( v);
		
	}

	/**
	 * 解析文本字段值，取出想要的字段.
	 * 
	 * @param targetFilePath
	 * @throws IOException
	 */
	private static void praseTxt(String targetFilePath) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(targetFilePath));
		String s;
		String targe = "\\t";
		s = in.readLine();
		while ((s = in.readLine()) != null) {
			String country = find(s, 45, 46, targe).replaceAll("\\t", "");
			String swiftCode = find(s, 7, 9, targe).replaceAll("\\t", "");
			String enName = find(s, 16, 18, targe).replaceAll("\\t", "") + find(s, 44, 45, targe)	;		//国家名称
			String countryName = find(s, 46, 47, targe).replaceAll("\\t", "");
			//英文名称
			String bankName = find(s, 16, 18, targe).replaceAll("\\t", "") + find(s, 44, 45, targe).replaceAll("\\t", "")	;
			
//			String enAddr = find(s, 20, 21, targe) + find(s, 40, 41, targe);
			String enAddr = find(s, 20, 28, targe).replaceAll("\\t", ",");
			enAddr = StringStartTrim(enAddr, ",");
//			System.out.println("conutry:" + country + " || swiftcode:" + swiftCode + " || enName:" + enName +" || enAddr:" + enAddr);
			System.out.println(country + " || " + swiftCode + " || " + enName +" || " + enAddr + " || " + countryName + " || " + bankName);
		}
		in.close();
	}

	/** 
     * 去掉指定字符串的开头的指定字符 
     * @param stream 原始字符串 
     * @param trim 要删除的字符串 
     * @return 
     */  
    public static String StringStartTrim(String stream, String trim) {  
        // null或者空字符串的时候不处理  
        if (stream == null || stream.length() == 0 || trim == null || trim.length() == 0) {  
            return stream;  
        }  
        // 要删除的字符串结束位置  
        int end;  
        // 正规表达式  
        String regPattern = "[" + trim + "]*+";  
        Pattern pattern = Pattern.compile(regPattern, Pattern.CASE_INSENSITIVE);  
        // 去掉原始字符串开头位置的指定字符  
        Matcher matcher = pattern.matcher(stream);  
        if (matcher.lookingAt()) {  
            end = matcher.end();  
            stream = stream.substring(end);  
        }  
        // 返回处理后的字符串  
        return stream;  
    }  
    /** 
     * 去掉指定字符串的开头和结尾的指定字符 
     * @param stream 原始字符串 
     * @param trim 要删除的字符串 
     * @return 
     */  
    public static String StringStartAndEndTrim(String stream, String trim) {  
    	// null或者空字符串的时候不处理  
    	if (stream == null || stream.length() == 0 || trim == null || trim.length() == 0) {  
    		return stream;  
    	}  
    	// 要删除的字符串结束位置  
    	int end;  
    	// 正规表达式  
    	String regPattern = "[" + trim + "]*+";  
    	Pattern pattern = Pattern.compile(regPattern, Pattern.CASE_INSENSITIVE);  
    	// 去掉原始字符串开头位置的指定字符  
    	Matcher matcher = pattern.matcher(stream);  
    	if (matcher.lookingAt()) {  
    		end = matcher.end();  
    		stream = stream.substring(end);  
    	}  
    	
    	String temp = reverse(stream);
    	matcher = pattern.matcher(temp);  
    	if (matcher.lookingAt()) {  
    		end = matcher.end();  
    		temp = temp.substring(end);  
    	}  
    	stream =  reverse(temp);
    	// 返回处理后的字符串  
    	return stream;  
    }  
    
    /**
     * 反转字符串
     * @param str
     * @return
     */
    public static String reverse(String str){  
        return new StringBuilder(str).reverse().toString();  
    }  
    /** 
     * 去掉指定字符串的结尾的指定字符 
     * @param stream 原始字符串 
     * @param trim 要删除的字符串 
     * @return 
     */  
    public static String StringEndTrim(String stream, String trim) {  
    	// null或者空字符串的时候不处理  
    	if (stream == null || stream.length() == 0 || trim == null || trim.length() == 0) {  
    		return stream;  
    	}  
    	// 要删除的字符串结束位置  
    	int end;  
    	// 正规表达式  
    	String regPattern = "[" + trim + "]*+";  
    	Pattern pattern = Pattern.compile(regPattern, Pattern.CASE_INSENSITIVE);  
    	// 去掉原始字符串开头位置的指定字符  
    	stream = reverse(stream);
    	Matcher matcher = pattern.matcher(stream);  
    	if (matcher.lookingAt()) {  
    		end = matcher.end();  
    		stream = stream.substring(end);  
    	}  
    	stream = reverse(stream);
    	// 返回处理后的字符串  
    	return stream;  
    }  
	
	/**
	 * 获取指定范围的值.
	 * 
	 * @param str
	 *            字符串
	 * @param start
	 *            开始点
	 * @param end
	 *            结束点
	 * @param targe
	 *            分隔符
	 * @return
	 */
	private static String find(String str, int start, int end, String targe) {
		if (null == str || "" == str) {
			return "";
		}
		int startIndex = getCharacterPosition(str, targe, start);
		int endIndex = getCharacterPosition(str, targe, end);
		String targeStr = str.substring(startIndex, endIndex);
		return targeStr;
	}

	/**
	 * 获取字符在字符串中出现的第n次位置
	 * @param str 字符串
	 * @param tag 获取的符号
	 * @param num 第几次出现
	 * @return
	 */
	public static int getCharacterPosition(String str, String tag, int num) {
		// 这里是获取"tag"符号的位置
		Matcher slashMatcher = Pattern.compile(tag).matcher(str);
		int mIdx = 0;
		while (slashMatcher.find()) {
			mIdx++;
			// 当"/"符号第n次出现的位置
			if (mIdx == num) {
				break;
			}
		}
		return slashMatcher.start();
	}

	/**
	 * @author wyd 2012-12-17 一个字符串在另一个字符串中 第N次出现的位置
	 *         (如果是倒序则将indexOf改成lastIndexOf) 使用 : int num =
	 *         getStringNum("aaasdfzsssasef", "s", 10); System.out.println(num);
	 * @param sA
	 *            是第一个字符(大的字符串)
	 * @param sB
	 *            被包含的字符串
	 * @param num
	 *            sB在sA 中第几次出现 num 必须大于1
	 * @return sB 在sA 中第num次出现的位置
	 * */
	public int getStringNum(String sA, String sB, int num) {
		int weizhi = 0;
		for (int i = 1; i < num; i++) {
			if (weizhi == 0) {
				weizhi = sA.toString().indexOf(sB);
			}
			weizhi = sA.indexOf(sB, weizhi+1);
		}
		return weizhi + 1;
	}

	/**
	 * 字符替换.
	 * 
	 * @param targetFilePath
	 * @throws IOException
	 */
	private static void readTxtFile(String targetFilePath) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(targetFilePath));
		String s;
		int n = 0, m = 20;
		while ((s = in.readLine()) != null) {
			String[] lineArr = s.split("\\t");
			String newline = "";
			for (int j = 0; j < lineArr.length; j++) {
				if (j < lineArr.length) {
					newline = newline + lineArr[j] + " ———— ";
				} else
					newline = newline + lineArr[j];
			}
			System.out.print(newline);
			System.out.println();
			if (n++ > m)
				return;
		}

	}

}
