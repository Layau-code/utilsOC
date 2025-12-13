import java.sql.SQLOutput;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class test {
    public static void main(String[] args) {
        ThreadPool pool = new SimpleThreadPool(2,2);
        //ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 10, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(2));
        AtomicInteger count = new AtomicInteger();
        for (int i = 0; i < 100; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());


                    count.incrementAndGet();
                }
            });
        }
//主线程睡5秒

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pool.shutdown();
        System.out.println("任务执行完毕"+count);
    }
}
