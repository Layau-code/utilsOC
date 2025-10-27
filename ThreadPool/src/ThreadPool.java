public interface ThreadPool {
    public void execute(Runnable task);
    public void shutdown();
}
