package stu.file;

import java.util.HashMap;
import java.util.List;

/**
 * 并查集
 * @author pactera
 *
 */
public class UnionFindSet {
	//数据基础类型随意
	public static class Data{
		String val;
	}

	private static HashMap<Data, Data> fatherMap;
	private static HashMap<Data, Integer> sizeMap;
	
	//初始化时，给每个元素父节点赋值为自己
	public UnionFindSet(List<Data> nodes){
		fatherMap = new HashMap<Data, Data>();
		sizeMap = new HashMap<Data, Integer>();
		
		for(Data node : nodes)
		{
			fatherMap.put(node, node);
			sizeMap.put(node, 1);
		}
	}
	
	//判断是否属于同一集合
	public static boolean isSameElement(Data a, Data b){
		return findHead(a) == findHead(b);
	}
	
	//合并两个集合
	public static Data unionSet(Data a, Data b){
		if(null == a && null == b){
			return null;
		}
		Data aHead = fatherMap.get(a);
		Data bHead = fatherMap.get(b);
		Data res = null ;
		if(aHead != bHead){
			int aSize = sizeMap.get(aHead);
			int bSize = sizeMap.get(bHead);
			if(aSize > bSize){
				fatherMap.put(bHead, aHead);
				sizeMap.put(aHead, aSize + bSize);
				res = aHead;
			}else{
				fatherMap.put(aHead, bHead);
				sizeMap.put(bHead, aSize + bSize);
				res = bHead;
			}
		}
		return res;
	}
	
	// 找到这个集合的代表节点
	private static Data findHead(Data node) {
		Data father = fatherMap.get(node);
		if(father != node){
			findHead(father);
		}
		fatherMap.put(node, father);
		return father;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

}
