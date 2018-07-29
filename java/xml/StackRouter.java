/*
 * Created on 2008-10-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package stu.xml;

import java.util.ArrayList;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StackRouter {
	ArrayList rules = new ArrayList();
	
	/**
	 * 文件写入list
	 * @param file
	 * @throws Exception
	 */
	public void loadTable(String file) throws Exception {
		String s = Tools.file2String(file);
		String lines[] = s.split("\\n");
		
		for (int i=0; i<lines.length; i++) {
			s = lines[i].trim();
			if (0 == s.length()) continue;
			String[] items = s.split(" +");
			rules.add(items);
		}
	}
	
	/**
	 * 根据异常路径产生日志名称
	 * @param stacks
	 * @return
	 */
	public String route(StackTraceElement[] stacks) {
		for (int i=0; i<rules.size(); i++) {
			String[] items = (String[]) rules.get(i);
			
			boolean matched = true;
			boolean starmatch = false;
			int stackpos = 1;
			for (int k=1; k<items.length; k++) {
				String item = items[k];
				String tomatch = stacks[stackpos].getClassName();
				
				if (starmatch) {
					boolean allused = false;
					for (int x=stackpos; x<stacks.length; x++) {
						tomatch = stacks[stackpos].getClassName();
						stackpos = x + 1;
						if (tomatch.equals(item))
							break;
						if (stacks.length == x+1)
							allused = true;
					}
					
					//退出本条规则
					if (allused) {
						matched = false;
						break;
					}
					
					starmatch = false;
					continue;
				}
				
				if ("*".equals(item) || tomatch.equals(item)) {
					stackpos ++;
					continue;
				}
					

				//*** 匹配多个项目
				if ("***".equals(item)) {
					starmatch = true;
					continue;
				}
					
				matched = false;
				break;
			}
			
			if (matched)
				return items[0];
			
		}
		
		return null;
	}
}
