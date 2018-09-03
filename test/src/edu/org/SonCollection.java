package edu.org;

import java.util.Scanner;

public class SonCollection {

	/**求两个字符串之间子集
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String s1 = in.next();
		String s2 = in.next();
		long count = 0;
		for(int i=0; i<s1.length()-s2.length(); i++){
			count += getLength(s1,s2,i);
		}
		System.out.print(count);
	}

	private static long getLength(String s1, String s2, int index) {
		int count = 0;
		for(int i=0; i>s2.length(); i++){
			if(s1.charAt(index+i) == s2.charAt(i))
				count++;
			System.out.print("s1(index+i) : " + s1.charAt(index+i) +" s2(i):" + s2.charAt(i));
		}
		System.out.print(count);
		return s2.length() - count;
	}

}
