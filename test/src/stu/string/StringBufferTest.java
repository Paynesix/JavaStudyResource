package stu.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import stu.string.Person;

public class StringBufferTest {

	private static final int Integer = 0;

	public static void main(String args[]) {
//		StringBuffer sBuffer = new StringBuffer("菜鸟教程官网：");
//		sBuffer.append("www");
//		sBuffer.append(".runoob");
//		sBuffer.append(".com");
//		System.out.println(sBuffer.toString());
//
//		sBuffer.reverse();
//		System.out.println(sBuffer.toString());
//		
//		arrayFill();
		
		sortObjectByAge();
	}
	/**
	 * sort array by myComparator
	 * @param arr
	 * @param Comparator
	 */
	public void sortArray(Integer[] arr){
		Arrays.sort(arr,  new MyComparator());
		print(arr);
	}
	/**
	 * Collections sort list
	 */
	public static void sortObjectByAge(){
		ArrayList<Person> alist = new ArrayList<Person>();
		alist.add(new Person(18, "xxx"));
		alist.add(new Person(28, "mls"));
		alist.add(new Person(42, "lhj"));
		alist.add(new Person(36, "cgg"));
		alist.add(new Person(24, "xy"));
		alist.add(new Person(-1, ""));
		Collections.sort(alist, new Comparator<Person>(){
			@Override
			public int compare(Person p1, Person p2){
				if(p1.age > p2.age){
					return 1;
				}else if(p1.age < p2.age){
					return -1;
				}else{
					return 0;
				}
			}
		});
		for(int i=0; i<alist.size(); i++){
			System.out.println(alist.get(i).age +" = " +  alist.get(i).name);
		}
	}
	
	/**
	 * sort array by myComparator
	 * @param arr
	 * @param Comparator
	 */
	@SuppressWarnings("unchecked")
	public void sortArrayMyComparator(Integer[] arr ){
		String[] s = {"a", "b", "c"};
		Arrays.sort(s,  new Comparator<String>(){
			@Override
			public int compare(String o1, String o2) {
				return 0;
			}
		});
		print(arr);
	}
	
	/**
	 * Comparator sort 
	 * @author xianyi
	 *
	 */
	class MyComparator implements Comparator<Integer>{
		@Override
		public int compare(Integer o1, Integer o2) {
			if(o1 > o2){
				return 1;
			}else if(o1 < o2){
				return -1;
			}else{
				return 0;
			}
		}
		
	}
	
	/**
	 * array fill
	 */
	public static void arrayFill(){
		int[] arr =new int[12];
		print(arr);
		Arrays.fill(arr, (int)(Math.random()*100));
		print(arr);
		
	}
	
	/**
	 * print array
	 * @param arr
	 */
	public static void print(int[] arr){
		for(int i=0; i<arr.length-1; i++){
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}
	/**
	 * print array
	 * @param arr
	 */
	public static void print(Integer[] arr){
		for(int i=0; i<arr.length-1; i++){
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}
}
