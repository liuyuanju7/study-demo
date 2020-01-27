package demo.concurrent_module.cyclicbarrier;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author liuyuanju1
 * @date 2019/9/12
 * @description:
 */
@Slf4j
public class CyclicBarrierDemo {
    public static void main(String[] args) {
       //testBarrier();
        testCyclicBarrier();
    }

    //测试屏障线程 即 创建CyclicBarrier时 传入 Runnable Command
    public static void testBarrier(){
        //构造器 设置屏障放开前要做的事
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            log.info("屏障放开。 屏障线程先运行 - ");
            try {
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("屏障线程的事情做完了 - ");
        });

        for(int i=0; i<3; i++){
            new Thread( () -> {
                log.info(Thread.currentThread().getName() + " 等待屏障放开");
                try {
                    // 表示 自己已经到达 栅栏
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                log.info(Thread.currentThread().getName() + " 屏障放开, 开始干活");
            }).start();
        }
    }
    // 重复使用的 栅栏
    public static void testCyclicBarrier(){
        int threadNum = 5;
        CyclicBarrier barrier = new CyclicBarrier(threadNum, () -> {
            log.info("{} 在 执行等待屏障放开时要做的任务", Thread.currentThread().getName());
        });

        for(int i=0; i<5; i++){
            new Thread( () -> {
                try {
                    Thread.sleep(1000);
                    log.info("{} 到达栅栏A", Thread.currentThread().getName());
                    barrier.await();
                    log.info("{} 冲破栅栏 A" , Thread.currentThread().getName());

                    Thread.sleep(2000);
                    log.info("{} 到达栅栏B", Thread.currentThread().getName());
                    barrier.await();
                    log.info("{} 冲破栅栏 B" , Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
