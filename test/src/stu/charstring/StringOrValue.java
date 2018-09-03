package stu.charstring;

import java.util.HashMap;
import java.util.Map;

public class StringOrValue {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int num = 5; 
		String str = String.valueOf(num);
		
		Integer num2 = 6; 
		String str2 = num2.toString();
		
		
		String s1 = "666";
		int i = Integer.parseInt(s1);
		
		
		String s2 = "3.14";
		float f1 = Float.parseFloat(s2);
//		System.out.println(f1);
		
		float f2 = 3.14f;
		String s3 = String.valueOf(f2);
//		System.out.println(s3);
		
		char c = '中';
		char c1 = '1';
		char c2 = 'b';
		
//		charTest();
		stringEquals();
	}
	
	public static void charTest(){
		String str = "Hello World   ";
		char c = str.charAt(0);
		
		char[] cs = str.toCharArray();
		
		String ss1 = str.substring(0);
		String ss2 = str.substring(3, 5);
		
		String[] str1 = str.split(" ");
		
		String str2 = str.trim();
		
		str.toLowerCase();
		str.toUpperCase();
		
//		str.toLowerCase(new Locale(str2));
		
		str.replaceFirst(" " , str);
		
		str.indexOf("ell");
		
		str.indexOf("or", 4);
		
//		System.out.println(str.indexOf("or", 4));
		
		
	}
	
	public static void stringEquals(){
		String str = "Hello girl";

		String str3 = "Hello girl";
		String str2 = new String(str);
		
		System.out.println(str == str2	);
		System.out.println(str == str3	);
		
		System.out.println(str.startsWith("H")	);
		System.out.println(str.endsWith("l")	);
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("on", 1);
		
				
	}

}
