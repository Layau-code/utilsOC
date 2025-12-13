public class TokenBucketLimiter implements RateLimiter{

    //桶的大小
    private int capacity;
    //每秒放入的令牌数
    private int sRate;

    //当前令牌数量
    private int curTokens;
    //上次放入令牌的时间
    private long lastTime;

    //构造函数
    public TokenBucketLimiter(int capacity, int sRate) {
        this.capacity = capacity;
        this.sRate = sRate;
        this.curTokens = capacity;
        this.lastTime = System.currentTimeMillis();
    }

    @Override
    public boolean tryAcquire(int permits) {
        refill();
        //如果令牌数不足，则返回false
        if (curTokens < permits) {
            return false;
        }
        curTokens -= permits;
        return true;
    }

    @Override
    public boolean tryAcquire() {
        return tryAcquire(1);
    }
    public void refill(){
        long now = System.currentTimeMillis();
        //计算需要放入的令牌数
        int needTokens = (int) ((now - lastTime)* sRate / 1000 );
        curTokens = Math.min(curTokens + needTokens, capacity);
        lastTime = now;
    }
}
