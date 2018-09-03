package stu.file;

/**
 * find next permutation
 * 
 * @author pactera
 * 
 */
public class Permutation {

	public static void nextPermutation(int[] num) {
		if (null == num || num.length == 0) {
			return;
		}
		int i = num.length-2;
		while (i >= 0 && num[i] > num[i + 1]) {
			i--;
		}
		if(i>=0){
			int j = num.length-1;
			while(j>=0 && num[j] < num[i]){
				j--;
			}
			swap(num, i, j);
		}
		reverse(num, i+1);
	}

	private static void reverse(int[] num, int start) {
		int i = start;
		int j = num.length -1;
		while(i<j){
			swap(num, i, j);
			i++;
			j--;
		}
	}

	private static void swap(int[] num, int i, int j) {
		int tmp = num[i];
		num[i] = num[j];
		num[j] = tmp;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] a = {1, 2, 3, 4};
		for(int i : a)
			System.out.print(i + " ");
		nextPermutation(a);
		System.out.println();
		for(int i:a){
			System.out.print(i + " ");
		}
	}

}
