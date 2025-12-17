import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TokenBucketLimiter implements RateLimiter{

    //桶的大小
    private final  int capacity;
    //每秒放入的令牌数
    private final int sRate;

    //当前令牌数量
    private  AtomicInteger curTokens;
    //上次放入令牌的时间
    private  AtomicLong lastTime;

    //构造函数
    public TokenBucketLimiter(int capacity, int sRate) {
        if(capacity <= 0 || sRate <= 0){
            throw new IllegalArgumentException("参数需要大于0");
        }
        this.capacity = capacity;
        this.sRate = sRate;
        curTokens = new AtomicInteger(0);
        lastTime = new AtomicLong(System.currentTimeMillis());
    }

    @Override
    public synchronized boolean  tryAcquire(int permits) {
        refill();
        //如果令牌数不足，则返回false
        if(curTokens.get() < permits){
            return false;
        }
        curTokens.addAndGet(-permits);
        return true;
    }
    public synchronized void  refill(){
        long now = System.currentTimeMillis();
        if(lastTime.get() > now){
           return;
        }
        //计算需要放入的令牌数
        int addTokens = (int)((now - lastTime.get()) * sRate / 1000);
        if(curTokens.get() + addTokens > capacity){
            curTokens.set(capacity);
        }else{
            curTokens.addAndGet(addTokens);
        }
        lastTime.set(now);

    }


    public synchronized int getCurTokens(){
        refill();
        return curTokens.get();
    }
}
