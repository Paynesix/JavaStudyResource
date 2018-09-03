package stu.socket;

import java.io.File;
import java.io.FilenameFilter;

public class DirFilter implements FilenameFilter {
	String ext;//存放查询条件

	DirFilter(String afn) {
		this.ext = "." + afn;
	}

	//满足查询条件，返回true
	public boolean accept(File dir, String name) {
		return name.endsWith(ext);
	}

}
