package stu.obj;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CreateObject implements Serializable {

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings({ "resource", "resource" })
	public static void main(String[] args) throws FileNotFoundException,
			IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Obj oNew = new Obj();
		System.out.print("New::");
		oNew.getMe();
		Obj oClass = (Obj) Class.forName("stu.obj.Obj").newInstance();
		System.out.print("Class::");
		oClass.getMe();

		Obj oClass2 = Obj.class.newInstance();
		System.out.print("Class2::");
		oClass2.getMe();

		// Class<Obj>[] constructor = Obj.class.getInterfaces();
		// Obj oConstructor = constructor.newInstance();

		// Obj oClone = <Obj>oClass.clone();

		String fileName = "D:\\YTWorkSpace\\test\\src\\stu\\obj\\Student.txt";
//		// 1.Object seri
//		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
//		Student stu = new Student("小灰灰", 22, "男");
//		oos.writeObject(stu);
//		oos.flush();
//		oos.close();
		
		//2.对象的反序列化
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
	    Student stu1 = (Student) ois.readObject();
        System.out.println(stu1);

	}

}
