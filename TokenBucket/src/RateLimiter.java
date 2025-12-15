public interface RateLimiter {
    boolean tryAcquire(int permits);
    default boolean tryAcquire(){
        return tryAcquire(1);
    }

}
