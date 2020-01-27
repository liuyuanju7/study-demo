package demo.concurrent_module.forkjoinpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author liuyuanju1
 * @date 2019/9/11
 * @description: 有返回结果的 多线程并行执行任务
 */
public class ForkJoinPoolTaskDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 大任务
        SumTask sumTask = new SumTask(0, 100);
        ForkJoinPool pool = ForkJoinPool.commonPool();
        // 提交递归分治 执行
        ForkJoinTask<Integer> submit = pool.submit(sumTask);
        // 获取结果
        int result = submit.get();
        System.out.println(result);

        int s = 0;
        for (int i=0; i<100; i++) {
            s +=i;
        }
        System.out.println(s);
    }

    static class SumTask extends RecursiveTask<Integer>{
        private final static int THRESHOLD = 10;

        private int start;

        private int end;

        SumTask(int start, int end){
            super();
            this.start = start;
            this.end = end;
        }

        // 定义子任务拆分规则和任务算法
        @Override
        protected Integer compute() {
            int sum = 0;
            if( end - start < THRESHOLD){
                for(int i= start; i<end; i++){
                    sum += i;
                }
            }else{
                int middle = (start + end) / 2;
                SumTask left = new SumTask(start, middle);
                SumTask right = new SumTask(middle, end);
                // 主动执行其它的ForkJoinTask，并等待Task完成。（是同步的）
                SumTask.invokeAll(left, right);
                // join  获取数据
                sum = left.join() + right.join();

            }
            return sum;
        }
    }
}
