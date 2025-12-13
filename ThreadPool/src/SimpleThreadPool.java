import java.util.concurrent.*;

public class SimpleThreadPool implements ThreadPool{
   private  final int threadPoolSize;
   public final BlockingQueue<Runnable> taskQueue;
   public final Thread[] workers;
   public volatile boolean isShutdown = false;

    public SimpleThreadPool(int threadPoolSize,int taskQueueSize) {
        this.threadPoolSize = threadPoolSize;
        taskQueue = new ArrayBlockingQueue<>(taskQueueSize);
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
        try {
            taskQueue.put(task); // 阻塞直到入队成功
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Task submission interrupted", e);
        }
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
                try
                {
                    Runnable task = taskQueue.take(); // 阻塞获取任务
                    task.run(); // 安全：take() 成功返回，task 不为 null
                } catch
                (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 恢复中断状态（良好实践）
                    break;
                }
            }
        }
    }
}
