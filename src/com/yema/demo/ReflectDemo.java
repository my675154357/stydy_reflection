package com.yema.demo;


import org.junit.Test;

import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReflectDemo {
    /**
     * 获取一个类的class文件对象的三种方式：
     *  1.对象名获取
     *  2.类名获取
     *  3.Class类的静态方法获取
     */
    @Test
    public void fun1() throws Exception{
        //1.对象名获取
        Person person = new Person();
        //调用Person类的父类方式getClass，返回一个当前object运行时的类
        Class<? extends Object> c1 = person.getClass();
        System.out.println(c1.toString());

        //2.类名获取，这种不会打印静态块的内容，其他两种都会
        //每个类型，包括基本和引用，都会被赋予这个类型一个静态的属性，属性名字class。任何类都可以Person.class这样，意识是说，任何类都有一个隐含的静态的名字叫class的属性。
        Class<Person> c2 = Person.class;
        System.out.println(c2.toString());
        //java中==比较的是内存中引用地址，这里比较c1==c2，比较的是class文件对像，而同一个class文件只会被引入到内存中一次，
        // 后面多少次的引用都是最初引入的那个，是同一个，相等的，所以这里是true。
        System.out.println(c1==c2);//true
        System.out.println(c1.equals(c2));//true

        //3.Class类的静态方法获取
            Class<?> c3 = Class.forName("com.yema.demo.Person");
            System.out.println(c3);

    }

    /**
     * 通过反射获取class文件中的构造方法。
     * 运行构造方法，创建对象
     */
    @Test
    public void fun2() throws Exception{
        Class<?> pc = Class.forName("com.yema.demo.Person");
        //使用class文件对象，获取类中的构造方法

        //1.getConstructors() 只能获取class文件对象中所有公共(public)的构造方法。Constructor描述构造方法对象的类
        Constructor<?>[] cons = pc.getConstructors();
        for (Constructor con: cons) {
            System.out.println(con);
        }

        //2.获取指定的构造方法，在括号中指定构造器的参数，这里不传参数，就返回空参的构造器
        Constructor<?> con = pc.getConstructor();
        //通过构造器获取实例
        Object obj = con.newInstance();
        System.out.println(obj);
        Person per = (Person)obj;
        per.eat();
    }

    /**
     * 反射获取有参public构造方法
     * @throws Exception
     */
    @Test
    public void fun3() throws Exception{
        Class<?> pc = Class.forName("com.yema.demo.Person");
        //获取带有String和int参数的构造方法
        Constructor<?> con = pc.getConstructor(String.class, int.class);
        System.out.println(con);
        //运行构造方法后传递的实际参数
        Object obj = con.newInstance("小明",18);
        System.out.println(obj);
    }

    /**
     * 反射获取构造方法并运行的快捷方式，直接一步到位，获得对像
     * 使用前提：1.被反射的类必须具有空参数的构造方法。2.构造方法的权限必须是public
     */
    @Test
    public void fun4() throws Exception{
        Class<?> pc = Class.forName("com.yema.demo.Person");
        //Class类中定义了一个方法，T newInstance()，返回的是泛型
        Object obj = pc.newInstance();
        System.out.println(obj);
    }

    /**
     * 反射获取私有的构造方法并运行
     * 不推荐：破坏了程序的封装性，安全性
     * 暴力反射
     * @throws Exception
     */
    @Test
    public void fun5() throws Exception{
        Class<?> pc = Class.forName("com.yema.demo.Person");
        //geiDeclaredConstructors()，获取所有的构造方法，包括私有的
        Constructor<?>[] cons = pc.getDeclaredConstructors();
        for (Constructor con : cons){
            System.out.println(con);
        }
        //geiDeclaredConstructor()，获取指定参数的构造方法，包括私有的。
        Constructor<?> con = pc.getDeclaredConstructor(int.class, String.class);

        //Constructor类的父类AccessiblebObject，定义方法setAccessible(boolean b)
        //值为true表示反射的对象在使用时应该取消java语言的访问检查。false表示应该实施访问检查
        con.setAccessible(true);

        Object obj = con.newInstance(20, "小花");
        //java.lang.IllegalAccessException: Class com.yema.demo.ReflectDemo can not access a member of class com.yema.demo.Person with modifiers "private"
        //直接打印会报上面那个异常，因为这是私有的构造器，外类是无权访问的。
        System.out.println(obj);
    }

    /**
     * 获取类的中成员变量，并修改变量值
     * @throws Exception
     */
    @Test
    public void fun6() throws Exception{
        Class<?> pc = Class.forName("com.yema.demo.Person");
        Object obj = pc.newInstance();
        //getFields()，获取class文件中所有的public成员变量
        Field[] fields = pc.getFields();
        for (Field field : fields){
            System.out.println(field);
        }
        //获取指定成员变量，传递一个变量名
        Field name = pc.getField("name");
        System.out.println(name);
        //修改变量值
        name.set(obj,"王根基");
        System.out.println(obj);
    }

    /**
     * 反射获取成员方法并运行
     * @throws Exception
     */
    @Test
    public void fun7() throws Exception{
        Class<?> pc = Class.forName("com.yema.demo.Person");
        Object obj = pc.newInstance();
        //getMethods()，获取class对象中所有public的成员方法，包括继承来的方法
        Method[] methods = pc.getMethods();
        for (Method method : methods){
            System.out.println(method);
        }
        //获取指定的方法，getMethod(String methodName,Class...c)传参数：方法名，方法的参数类列表
        Method eat = pc.getMethod("eat");
        System.err.println(eat);
        //运行获得的方法，invoke(Object obj, Object...obj)，对象名，和方法的参数
        eat.invoke(obj);
    }

    /**
     * 返回有参数的成员方法并执行
     * @throws Exception
     */
    @Test
    public void fun8() throws Exception{
        Class<?> pc = Class.forName("com.yema.demo.Person");
        Object obj = pc.newInstance();
        //调用Class类的方法getMethod，获取指定的方法
        Method sleep = pc.getMethod("sleep", String.class, int.class, double.class);
        sleep.invoke(obj,"休眠",100,888.99);
    }

    /**
     * 泛型擦除，因为编译后是没有泛型的。
     * 定义集合类，泛型String要求向集合中添加Integer类型
     * 利用集合中伪泛型的特点，实际的.class文件中是没有泛型的，用过反射，调用arrayList的class文件中的add()方法就可以了
     * @throws Exception
     */
    @Test
    public void fun9() throws Exception{
        List<String> array = new ArrayList<String>();
        Class<? extends List> arrayClass = array.getClass();
        //获取arrayList.class文件的add()方法
        Method add = arrayClass.getMethod("add", Object.class);
        System.out.println(add);
        add.invoke(array,15);
        add.invoke(array,150);
        add.invoke(array,1500);
        System.out.println(array);
    }

    /**
     * 调用Person,student,worker中的方法，在不修改源码的情况下实现
     * 问题：类不确定，方法名不确定
     * 通过配置文件实现，将类名和方法名写在配置文件中
     * @throws Exception
     */
    @Test
    public void fun10() throws Exception{
        //io流读取配置文件
        FileReader fr = new FileReader("config.properties");
        //创建集合对象
        Properties pro = new Properties();
        pro.load(fr);
        //此时已经将流中的内容加载到了对象中，就不需要流了，可以释放
        fr.close();
        //通过建获取值
        String CLASS_NAME = pro.getProperty("CLASS_NAME");
        String METHOD_NAME = pro.getProperty("METHOD_NAME");
        //反射获取指定类的class文件对象
        Class<?> cla = Class.forName(CLASS_NAME);
        Object obj = cla.newInstance();
        Method method = cla.getMethod(METHOD_NAME);
        method.invoke(obj);
    }
}
