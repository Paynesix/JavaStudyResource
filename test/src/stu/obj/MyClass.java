package stu.obj;
/**
 * 
 * @author Administrator
 *   �Զ���һ������
 */
class MyClass{
    private String name="";//������ʾ,�������������ɹ�
    MyClass(String name){
        this.name=name;
        show();//��ʾ
    }
    public void show(){
        System.out.println(name);
    }
    @Override
    public String toString(){
        return "MyClass="+name;
    }
}
