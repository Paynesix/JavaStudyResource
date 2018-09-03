package stu.obj;
/**
 * 
 * @author Administrator
 *   自定义一个类型
 */
class MyClass{
    private String name="";//名字显示,用来表明创建成功
    MyClass(String name){
        this.name=name;
        show();//显示
    }
    public void show(){
        System.out.println(name);
    }
    @Override
    public String toString(){
        return "MyClass="+name;
    }
}
