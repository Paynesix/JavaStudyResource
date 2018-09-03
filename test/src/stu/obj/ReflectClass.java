package stu.obj;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Administrator
 *  ͨ�����䴴������
 */
class ReflectClass{
    private String name="ReflectClass";
    public ReflectClass(int age,String name,MyClass my){
         this.name=name;
         show(age,name,my);
    }
//    ReflectClass(){//���캯�����أ�ʹ�ò�ͬ�Ĳ����б�������
//        //û�д������Ĺ��췽��
//    }
    /**
     * 
     * @param age
     * @param name
     * @param my
     */
    public final void show(int age,String name,MyClass my){
        System.out.println("age="+age+" name="+name+" my="+my);
    }
    @Override
    public String toString(){
        return "ReflectClass="+name;
    }

/**
     * 
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
     * 
     * @param name ��·��
     * @param classParas Class����Ϣ�����б�
     *  ����ǻ������������ǿ���ʹ����Tpye���ͣ������class�ֶ�����Ч��
     *  ����Ƿ��������Ϳ���ʹ�õ�class�ֶ���������Class����Ϣ������Щ��Ҫ���ء�
     * @param paras      ʵ�ʲ����б�����
     * @return           ����Object���õĶ���ʵ��ʵ�ʴ��������Ķ������Ҫʹ�ÿ���ǿ��ת��Ϊ�Լ���Ҫ�Ķ���
     * 
     * �������ķ��䴴������
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
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
     * 
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

/**
     * 
     * @param name ��·��
     * @param paras    ʵ�ʲ����б�����
     * @return         ����Object���õĶ���ʵ��ʵ�ʴ��������Ķ������Ҫʹ�ÿ���ǿ��ת��Ϊ�Լ���Ҫ�Ķ���
     * 
     * �������ķ��䴴������
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object getInstance(String name,Object paras[]){
        Object o=null;
        try {
            Class c=getclass(name);
            int len=paras.length;
            Class classParas[]=new Class[len];
            for(int i=0;i<len;++i){
                classParas[i]=paras[i].getClass();//��������Ϣ
                System.out.println("classParas[i]="+classParas[i]);
            }
            Constructor con=c.getConstructor(classParas);//��ȡʹ�õ�ǰ���췽�������������Constructor������������ȡ���캯����һЩ
            try {
                //��Ϣ
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
}