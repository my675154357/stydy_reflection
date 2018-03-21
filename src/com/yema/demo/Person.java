package com.yema.demo;

/**
 * 关于权限：
 * 默认：本包可用
 * pvivate：本类私用
 * public：外包可用
 * protected：子包专用
 */
public class Person {

    public String name;
    private int age;

    static{
        System.out.println("静态代码块");
    }

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private Person(int age, String name) {
        this.name = name;
        this.age = age;
    }

    public void eat(){
        System.out.println("人吃饭");
    }

    public void sleep(String str, int n, double dou){
        System.out.println("人在睡觉"+str+"..."+n+"..."+dou);
    }

    private void playGame(){
        System.out.println("人在打游戏");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
