package stu.equlas;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Equals {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int a = 2;
		int b = 3;
		String c = "2";
		
		System.out.println(c.equals("2"));
		System.out.print(c.equals(b));
		
		
	}
	
	class o{
		int a;
		String b;
		ArrayList arr ;
		
		public o(){
			a=1;
			b="2";
			arr=new ArrayList();
		}
	}
}
