package stu.file;

/**
 * loop print arr all element
 * @author pactera
 *
 */
public class LoopPrintArr {

	private static int count = 0;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] arr = new int[]{1, 2, 3, 4, 5};
		printAllArrSubstring(arr, 0, arr.length-1);
		System.out.println(count);
	}

	private static void printAllArrSubstring(int[] arr, int first, int length) {
		if(first==length){
			for(int i : arr)
				System.out.print(i + " ");
			System.out.println();
			count++;
		}else{
			for(int i=first; i<=length; i++){
				swap(arr,first, i);
				printAllArrSubstring(arr, first+1, length);
				swap(arr,first, i);
			}
		}
	}

	private static void swap(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i]	= arr[j];
		arr[j] = tmp;
	}

}
