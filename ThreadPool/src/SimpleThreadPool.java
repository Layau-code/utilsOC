import java.util.concurrent.*;

public class SimpleThreadPool implements ThreadPool{
   private  final int threadPoolSize;
   public final BlockingQueue<Runnable> taskQueue;
   public final Thread[] workers;
   public volatile boolean isShutdown = false;

    public SimpleThreadPool(int threadPoolSize,int taskQueueSize) {
        this.threadPoolSize = threadPoolSize;
        taskQueue = new LinkedBlockingDeque<>(taskQueueSize);
        workers = new Thread[threadPoolSize];
        // 启动工作线程
        for (int i = 0; i < threadPoolSize; i++) {
            workers[i] = new Thread(new Worker(), "pool-thread-" + i);
            workers[i].start();
        }
    }

    @Override
    public void execute(Runnable task) {
if(isShutdown){
    throw new IllegalStateException("ThreadPool is shutdown");
}
taskQueue.offer(task);
    }

    @Override
    public void shutdown() {
        isShutdown = true;
        for (Thread worker : workers) {
            worker.interrupt();
        }
    }
    // 工作线程逻辑（关键！）
    private class Worker implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Runnable task = null; // 阻塞获取
                try {
                    task = taskQueue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                task.run();
            }
        }
    }
}
