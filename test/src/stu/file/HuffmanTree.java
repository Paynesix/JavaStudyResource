package stu.file;

import java.util.Arrays;
import java.util.PriorityQueue;

public class HuffmanTree {

	private static class Node{
		int val;
		Node left;
		Node right;
		Node father;
		public Node(int val){
			this.val = val;
			left = null;
			right = null;
			father = null;
		}
	}
	
	private static class Heap{
		static int[] arr;
		static int size;
		
		public Heap(int[] array) throws Exception{
			if(null == array){
				throw new Exception("arr is empty");
			}
			arr = array;
			for(int i=1; i<arr.length; i++){
				up(arr, i);
				size++;
			}
		}
		
		public static void up(int[] array, int i) {
			int j = i;
			while(j>0){
				int parent = j>>1;
				if(array[parent] > array[j]){
					swap(array, parent, j);
					j = parent;
				}else{
					break;
				}
			}
		}

		public static void swap(int[] a, int i, int j) {
			int tmp = a[i];
			a[i] = a[j];
			a[j] = tmp;
		}

		public static int peek(){
			if(size < 0){
				return Integer.MIN_VALUE;
			}
			return arr[0];
		}
		
		public static int get(){
			if(size < 0){
				return Integer.MIN_VALUE;
			}
			int res = arr[0];
			swap(arr,0,size--);
			down(arr, 0);
			return res;
		}
		
		private static void down(int[] array, int i) {
			int j = i;
			int left = (j<<1) + 1;
			int right = left+1;
			while(right < size){
				int largest = array[left]<array[right] ? left : right; 
				if(array[j] > array[largest]){
					swap(array, j, largest);
					j = largest;
					left = j<<1 + 1;
					right = left + 1;
				}else{
					break;
				}
			}
		}

		public static boolean add(int num) throws Exception{
			if(size == arr.length){
				throw new Exception("arr is full!");
			}
			arr[++size] = num;
			up(arr, size);
			return true;
		}
	}
	

	public static Node huffman(int[] arr) {
		if(null == arr){
			return null;
		}
		Arrays.sort(arr);
		Node root = null;
		for(int i=0; i<arr.length; i++){
			
		}
		return root;
	}
	/**
	 * main
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		int[] arr = new int[]{11,1, 3, 4, 8, 5, 9};
//		huffman(arr);
		Heap h = new Heap(arr);
		System.out.println(h.peek());
		System.out.println(h.get());
		System.out.println(h.get());
		System.out.println(h.get());
		System.out.println(h.get());
		System.out.println(h.get());
		for(int i : arr)
			System.out.print(i + " ");
	}
}
