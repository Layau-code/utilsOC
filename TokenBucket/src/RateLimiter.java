public interface RateLimiter {
    boolean tryAcquire(int permits);
    boolean tryAcquire();
}
