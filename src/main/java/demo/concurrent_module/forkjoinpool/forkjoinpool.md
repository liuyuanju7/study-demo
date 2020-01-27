## ForkJoinPool

ForkJoinPool采用工作窃取算法,将一个大任务根据阈值分割成很多个子任务,最后根据场景是否要合并子任务运算结果;

适用于 大任务是很多个相同的小任务组成的，然后再采用分治的思想，拆分成多个子任务 并行执行(比如给 1000个人发短信)

根据是否需要合并子任务运算结果,任务需要继承抽象类**RecursiveAction,RecursiveTask<V>,**后者为需要合并子任务结果,泛型为结果类型; 

我们只需要实现抽象方法 protected Void compute(),定义子任务拆分规则和任务算法就可以了;

有2种使用方案:

- fork : 递归划分子任务,无需合并子任务结果;
- fork & join : 递归划分子任务,最后合并子任务计算结果;


#### 关于ForkJoinTask

1. 可以使用invokeAll(task)方法，主动执行其它的ForkJoinTask，并等待Task完成。（是同步的）

2. 还可以使用fork方法，让一个task执行（这个方法是异步的）

3. 还可以使用join方法，让一个task执行（这个方法是同步的，它和fork不同点是同步或者异步的区别

4. ForkJoinTask有两个子类，RecursiveAction和RecursiveTask。他们之间的区别是，RecursiveAction没有返回值，RecursiveTask有返回值。

#### 关于ForkJoinPool：

1. 可以使用ForkJoinPool.execute(异步，不返回结果)/invoke(同步，返回结果)/submit(异步，返回结果)方法，来执行ForkJoinTask。

2. ForkJoinPool有一个方法commonPool()，这个方法返回一个ForkJoinPool内部声明的静态ForkJoinPool实例。 
文档上说，这个方法适用于大多数的应用。这个静态实例的初始线程数，为“CPU核数-1 ”（Runtime.getRuntime().availableProcessors() - 1）。 
ForkJoinTask自己启动时，使用的就是这个静态实例
