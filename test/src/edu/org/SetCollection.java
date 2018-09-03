package edu.org;

import java.util.ArrayList;
import java.util.List;

public class SetCollection {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public List<String> getSubSet(String largeItemSet) {
		List<String> list = new ArrayList<String>();
		char[] lisChar = largeItemSet.toCharArray();
		for (int i = 0; i < lisChar.length; i++) {
			list.add(new String(lisChar[i] + ""));
		}
		for (int j = 0; j < list.size(); j++) {
			for (int k = 0; k < lisChar.length; k++) {
				if (list.get(j).indexOf(new String(lisChar[k] + "")) == -1) {
					String tempStr = list.get(j) + new String(lisChar[k] + "");
					list.add(tempStr);
				}
			}
		}
		return list;
	}

}
