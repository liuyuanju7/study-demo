### CyclicBarrier

#### 1、什么是CyclicBarrier ？
中文意思`循环栅栏` , 即可以循环利用的屏障，作用就是 会让所有线程都等待完成后，再继续下一步操作，比如几个朋友在餐厅聚餐，
只有所有的人都 到达 餐厅之后 我们才会开始吃饭， 此时 各个朋友就是各个线程，餐厅就是 CyclicBarrier

#### 2、@JavaDoc
CyclicBarrier是一个同步辅助类，它允许一组线程相互等待直到所有线程都到达一个公共的屏障点。
CyclicBarrier在涉及一定大小的线程的程序而这些线程有时必须彼此等待的情况下很有用。这个屏障之所以用循环修饰，
是因为在所有的线程释放之后，这个屏障是可以重新使用的。

CyclicBarrier支持可选择的Runnable命令(实例)，在团队中的最后一个线程到达之后，但是在释放任何线程之前
，每个屏障点运行一次(执行Runnable命令的run方法)。在内部任何一个线程继续之前，这种屏障操作对于更新共享状态是有用的。

#### 3、特性
- CyclicBarrier可以使一定数量的线程在栅栏位置处汇集。当线程到达栅栏位置时将调用await方法，
这个方法将阻塞直到所有线程都到达栅栏位置。如果所有线程都到达栅栏位置，那么栅栏将打开，此时所有的线程都将被释放，
而栅栏将被重置以便下次使用。
  
- CyclicBarrier支持在线程执行前首先执行一个Runnable Command。
  
- 如果某个线程出了问题(如InterruptedException)，那么将会影响其他线程。
  
- CyclicBarrier使用ReentranLock进行加锁，使用Condition的await、signal及signalAll方法进行线程间通信。


#### 4、个人使用总结
1. 使用上来看，属于多个线程彼此等待完成，到达某一步之后，再进行下一步操作， 可以用在这种场景：多个线程 分别计算一批数据，
等所有线程的数据全部计算完成后，再做合并操作 （这里有一个地方就是 每个线程都不能出错 ，如果某个线程出错，会影响整体的下一步操作
在允许某个线程异常拿不到数据的场景下，CyclicBarrier 就不太适合了 可以 考虑 CountDownLatch

##### 构造方法
```
public CyclicBarrier(int parties)
public CyclicBarrier(int parties, Runnable barrierAction)
```
Note: parties 是参与线程的个数
      第二个构造方法有一个 Runnable 参数，这个参数的意思是最后一个到达线程要做的任务
      
##### 重要方法
```
public int await() throws InterruptedException, BrokenBarrierException
public int await(long timeout, TimeUnit unit) throws InterruptedException, BrokenBarrierException, TimeoutException
```
Note: 线程调用 await() 表示自己已经到达栅栏
      BrokenBarrierException 表示栅栏已经被破坏，破坏的原因可能是其中一个线程 await() 时被中断或者超时

##### 需求与场景
- 一个线程组的线程需要等待所有线程完成任务后再继续执行下一次任务
- 可以用于多线程计算数据，最后合并计算结果的场景。

#### 两者区别
CountDownLatch 是一次性的，CyclicBarrier 是可循环利用的
CountDownLatch CountDownLatch主要是实现了1个或N个线程需要等待其他线程完成某项操作之后才能继续往下执行操作，
描述的是1个线程或N个线程等待其他线程的关系。CyclicBarrier主要是实现了多个线程之间相互等待
，直到所有的线程都满足了条件之后各自才能继续执行后续的操作，描述的多个线程内部相互等待的关系
