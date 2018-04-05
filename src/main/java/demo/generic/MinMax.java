package demo.generic;


/**
 * @author liuyuanju1
 * @date 2018/4/5
 * @description: 带有限定的 泛型 <T extends  Class & Interface>
 */
public class MinMax<T extends Comparable> {

    public Result<T> getMinMax(T[] a){
        if(a == null || a.length <= 0){
            return null;
        }
        T min = a[0];
        T max = a[0];
        for(T val : a){
            if(val.compareTo(min) < 0){
                min = val;
            }
            if(val.compareTo(max) > 0){
                max = val;
            }
        }
        return new Result<T>(min,max);
    }

    public class Result<T>{
        private T min;
        private T max;

        public Result(T min,T max){
            this.max = max;
            this.min = min;
        }
        public void showVal(){
            System.out.println("min : " + min + " max : " + max);
        }
    }

    public static void main(String[] args) {
        MinMax<Integer> minMax = new MinMax<Integer>();
        minMax.getMinMax(new Integer[]{2,4,6,3}).showVal();
    }
}
