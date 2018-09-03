package stu.sort;

public class InsertSort {
	public static int[] array = {13,3,54,67,8,32,31,99};
	/**
	 * @param args
	 * auther xianyi 20170811
	 * mothed use insert sort
	 */
	public static void main(String[] args) {
		insertSort(array);
		printSort(array);
	}

	static void printSort(int[] sort) {
		// TODO Auto-generated method stub
		for(int i:sort){
			System.out.print(i + ", ");
		}
	}

	private static void insertSort(int[] sort) {
		// TODO Auto-generated method stub
		int j;
		for(int i=1; i<sort.length-1; i++){
			int temp = sort[i];
			for(j=i; j>0&&temp<sort[j-1]; j--){
				sort[j] = sort[j-1];
			}
			sort[j] = temp;
		}
	}

	public static boolean compareTo(int a, int b){
		return a-b>0;
	}
}
