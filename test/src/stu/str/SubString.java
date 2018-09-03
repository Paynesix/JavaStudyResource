package stu.str;

public class SubString {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String str = "2017-08-08";
//		System.out.println(str.substring(0,4));
//		System.out.println(str.substring(5,7));
//		System.out.print(str.substring(8,10));


		String s = " === === === === ==  1,   2,3,4,,,,5 ========== === = == = = == = ==  = ";
		System.out.println(subStringSpecilChar(s, "="));
	}

	/**
	 * 去除字符串的两端特殊字符
	 * @param s
	 * @param x
	 * @return
	 */
	private static String subStringSpecilChar(String s, String x) {
		while(s.trim().indexOf(x) == 0)
			s = s.substring(1);
		s = s.trim();
		while(s.trim().lastIndexOf(x) == s.trim().length()-1)
			s = s.substring(0, s.lastIndexOf(x));
		return s.trim();	
	}

}
