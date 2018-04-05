package demo.innerclass;

/**
 * @author liuyuanju1
 * @date 2018/4/5
 * @description: 内部类
 */
public class InnerDemo {
    private String name;
    private int age;

    public InnerDemo(String name,int age){
        this.name = name;
        this.age = age;
    }
    //普通内部类
    public class Student{
        private String stuId;
        public Student(String stuId){
            this.stuId = stuId;
        }
        public void stuInfo(){
            System.out.println("name :" + name + " age : " + age + " stuId :" + stuId);
        }
    }
    public Student getInnerClass(String stuId){
        return new Student(stuId);
    }

    //静态内部类 实现 同时计算一个数组的最大最小值，并同时返回
    public void talk(final String otherName){
        //局部内部类
        class Teacher{
            public void talkToStu(){
                System.out.println("hello : " + otherName);
            }
        }
        Teacher teacher = new Teacher();
        teacher.talkToStu();
    }

    public Result getMaxMin(int[] arr){
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for(int n : arr){
            if(n < min){
                min = n;
            }
            if(n > max){
                max = n;
            }
        }
        return new Result(min,max);
    }

    public static class Result{
        private int min;
        private int max;
        public Result(int min,int max){
            this.min = min;
            this.max = max;
        }
        public void showVal(){
            System.out.println("max : "+ max + " min : " + min);
        }
    }


    public static void main(String[] args) {
        InnerDemo innerDemo = new InnerDemo("liuYj",22);
        innerDemo.getInnerClass("144520").stuInfo();

        innerDemo.talk("Mr.li");

        //匿名内部类
        Person person  = new Person(){
            public void myself() {
                System.out.println("this is in innerclass");
            }
        };
        person.myself();

        InnerDemo.Result result= innerDemo.getMaxMin(new int[]{2,4,5,1,6});
        result.showVal();

    }

}
