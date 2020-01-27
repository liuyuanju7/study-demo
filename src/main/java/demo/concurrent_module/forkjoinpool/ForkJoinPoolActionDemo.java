package demo.concurrent_module.forkjoinpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * @author liuyuanju1
 * @date 2019/9/11
 * @description: 无返回结果的 多线程并行执行任务
 * @since JDK 1.7
 */
@Slf4j
public class ForkJoinPoolActionDemo {
    public static void main(String[] args) throws InterruptedException {
        // 初始化一个大任务
        ActionTask parentTask = new ActionTask(0, 100);
        // 创建线程池实例
        //@since JDK 1.8
        ForkJoinPool pool = ForkJoinPool.commonPool();
        //ForkJoinPool pool = new ForkJoinPool();
        pool.submit(parentTask);
        //pool.awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * RecursiveAction 无返回结果的任务
     * @since JDK 1.7
     */
    public static class ActionTask extends RecursiveAction{
        private static final int THRESHOLD = 10; // 最多处理的阀值
        private int start;
        private int end;

        ActionTask(int start, int end){
            super();
            this.start = start;
            this.end = end;
        }

        //定义子任务拆分规则和任务算法
        protected void compute() {

            // 任务分割 粒度 分治思想
            if(end - start < THRESHOLD){
               for(int i=start; i<end; i++){
                   System.out.println(Thread.currentThread().getName() + " : i = " + i);
               }
            }else {
                int middle = (start + end) / 2;
                ActionTask left = new ActionTask(start, middle);
                ActionTask right = new ActionTask(middle, end);
                // 并行 执行两个 小任务
                left.fork();
                right.fork();
            }

        }
    }
}
