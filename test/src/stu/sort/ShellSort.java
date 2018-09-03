package stu.sort;

public class ShellSort {

	public static int[] array = {12,3,5,77,5,4,23,45,66};
	/**
	 * @param args
	 * author xianyi 20170811
	 * mothed shell sort
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		shellSort(array);
		InsertSort.printSort(array);
	}
	private static void shellSort(int[] a) {
		// TODO Auto-generated method stub
		int j;
		for(int gap = a.length/2; gap>0; gap/=2){
			for(int i=gap; i<a.length; i++){
				int tmp = a[i];
				for(j=i; j>=gap&&tmp<a[j-gap]; j-=gap){
					a[j] = a[j-gap];
				}
				a[j] = tmp;
			}
		}
	}

}
