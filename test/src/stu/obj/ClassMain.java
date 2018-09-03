/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stu.obj;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 * ������ʾͨ���������������󣬴������Ĺ��췽��
 */
public class ClassMain {
    
    public ClassMain(){}
    public static void main(String args[]){
//        //�������������Ķ���
//        ReflectClass rc1=(ReflectClass) ClassMain.getInstance("stu.obj.ReflectClass");
//        System.out.println("ReflectClass111="+rc1);
        //�����������Ķ���
        System.out.println("******************************");
        ReflectClass rc2=(ReflectClass) ClassMain.getInstance("stu.obj.ReflectClass",
                                                              new Class[]{Integer.TYPE,String.class,MyClass.class},
                                                              new Object[]{20,"����ReflectClass",new MyClass("����MyClass")});
        System.out.println("ReflectClass222="+rc2);
    }
    
    /**
     * @param className ��·��������
     * @return ���ظ���classNameָ��������Ϣ
     */
    @SuppressWarnings("rawtypes")
	public static Class getclass(String className){
        Class c=null;
        try {
            c=Class.forName(className);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClassMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }
    
    /**
     * @param name ��·��
     * @param classParas Class����Ϣ�����б�
     *  ����ǻ������������ǿ���ʹ����Tpye���ͣ������class�ֶ�����Ч��
     *  ����Ƿ��������Ϳ���ʹ�õ�class�ֶ���������Class����Ϣ������Щ��Ҫ���ء�
     * @param paras      ʵ�ʲ����б�����
     * @return           ����Object���õĶ���ʵ��ʵ�ʴ��������Ķ������Ҫʹ�ÿ���ǿ��ת��Ϊ�Լ���Ҫ�Ķ���
     * 
     * �������ķ��䴴������
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object getInstance(String name,Class classParas[],Object paras[]){
        Object o=null;
        try {
            Class c=getclass(name);
            Constructor con=c.getConstructor(classParas);//��ȡʹ�õ�ǰ���췽�������������Constructor������������ȡ���캯����һЩ //��Ϣ
            try {
                o=con.newInstance(paras);//���뵱ǰ���캯��Ҫ�Ĳ����б�
            } catch (InstantiationException ex) {
                Logger.getLogger(ClassMain.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ClassMain.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ClassMain.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(ClassMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(ClassMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ClassMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return o;//���������Object���õĶ���
    }
    
    /**
     * @param name ��·��
     * @return ���������ķ��䴴������
     */
    @SuppressWarnings("rawtypes")
	public static Object getInstance(String name){
        Class c=getclass(name);
        Object o=null;
        try {
             o=c.newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(ClassMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ClassMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return o;
    }
}