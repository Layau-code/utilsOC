import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LeakyBucketLimiter  {
     //用队列模拟桶（注意：实际要存的是要执行的任务，作者用Boolean代替了）
    private final ArrayBlockingQueue<Boolean> queue ;
   //定时器
    private final ScheduledExecutorService scheduler;

    //构造函数
    public LeakyBucketLimiter(int capacity, int rate) {
        //参数校验
        if(capacity <= 0 || rate <= 0){
            throw new IllegalArgumentException("参数需要大于0");
        }
        //初始化桶
        this.queue=new ArrayBlockingQueue<Boolean>(capacity);
        //计算漏水间隔，确保每个请求漏出的时间间隔相同
        long interval =1000L/rate;
        //创建一个单线程调度器
        this.scheduler = Executors.newSingleThreadScheduledExecutor(
                r -> {
                    Thread t = new Thread("LeakyBucketLimiter-Thread");
                    //线程设置为守护线程，确保JVM退出时，线程也能退出
                    t.setDaemon(true);
                    return t;
                }
        );
        scheduler.scheduleAtFixedRate(
                this::leak,    //要执行的任务
                interval,      //初始延迟
                interval,       //间隔时间
                TimeUnit.MILLISECONDS
        ) ;
    }
    //提交请求
    public boolean tryAcquire(){
        //实际要把任务加入队列，这里只是模拟
        return queue.offer(true);
    }
    private void leak(){
        //从队头移除一个元素，模拟漏水
        queue.poll();
    }

    //关闭定时器，并清空队列
    public void shutdown(){
        scheduler.shutdown();
        queue.clear();
    }
}
