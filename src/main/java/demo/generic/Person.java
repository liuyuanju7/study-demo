package demo.generic;

/**
 * @author liuyuanju1
 * @date 2018/4/5
 * @description: 普通 泛型类示例 : 不同 数据类型的人
 */
public class Person<T> {
    private T data;

    public Person(T data){
        this.data = data;
    }
    public void info(){
        System.out.println("my type is " + data.getClass());
    }

    //泛型方法

    public static <T> T getVal(T arg){
        return arg;
    }

    public static void main(String[] args) {
        Person<String> person =  new Person<String>("liu");
        person.info();
        Person<Integer> person1 = new Person<Integer>(1);
        person1.info();
    }
}
