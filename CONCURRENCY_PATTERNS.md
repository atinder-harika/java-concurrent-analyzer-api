# Concurrency Patterns in Java: Learning Guide

Master Java concurrency from fundamentals to advanced patterns for high-performance applications.

---

## Table of Contents

1. [Concurrency Fundamentals](#concurrency-fundamentals)
2. [Thread Management](#thread-management)
3. [ExecutorService & Thread Pools](#executorservice--thread-pools)
4. [CompletableFuture Patterns](#completablefuture-patterns)
5. [Thread Safety & Synchronization](#thread-safety--synchronization)
6. [Performance Optimization](#performance-optimization)
7. [Future Enhancements](#future-enhancements)

---

## Concurrency Fundamentals

### Why Concurrency?

**Benefits:**
- Utilize multi-core processors effectively
- Improve application throughput
- Reduce latency for I/O-bound operations
- Better resource utilization

**When to Use:**
- Processing large datasets
- Handling multiple simultaneous requests
- Background task execution
- Real-time data processing

### Threads vs Processes

**Thread:** Lightweight execution unit within a process
```java
Thread thread = new Thread(() -> {
    System.out.println("Running in thread: " + Thread.currentThread().getName());
});
thread.start();
```

**Creating Threads (3 Ways):**
```java
// 1. Extending Thread class
class MyThread extends Thread {
    public void run() {
        System.out.println("Thread running");
    }
}

// 2. Implementing Runnable
Runnable task = () -> System.out.println("Runnable task");
new Thread(task).start();

// 3. Using ExecutorService (recommended)
ExecutorService executor = Executors.newFixedThreadPool(4);
executor.submit(() -> System.out.println("Executor task"));
```

---

## Thread Management

### Thread Lifecycle

```
NEW → RUNNABLE → RUNNING → BLOCKED/WAITING → TERMINATED
```

**Thread States:**
```java
Thread thread = new Thread(() -> {
    try {
        Thread.sleep(1000);  // TIMED_WAITING
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
});

System.out.println(thread.getState());  // NEW
thread.start();
System.out.println(thread.getState());  // RUNNABLE
thread.join();  // Wait for completion
System.out.println(thread.getState());  // TERMINATED
```

### Thread Interruption

```java
Thread thread = new Thread(() -> {
    while (!Thread.currentThread().isInterrupted()) {
        try {
            Thread.sleep(100);
            System.out.println("Processing...");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted");
            break;
        }
    }
});

thread.start();
Thread.sleep(500);
thread.interrupt();  // Signal to stop
```

**Learning Exercise:**
- Create threads using all three methods
- Monitor thread states during execution
- Implement proper thread interruption handling

---

## ExecutorService & Thread Pools

### Types of Thread Pools

**1. Fixed Thread Pool:**
```java
ExecutorService executor = Executors.newFixedThreadPool(4);

for (int i = 0; i < 10; i++) {
    final int taskId = i;
    executor.submit(() -> {
        System.out.println("Task " + taskId + " on " + Thread.currentThread().getName());
        Thread.sleep(1000);
        return taskId * 2;
    });
}

executor.shutdown();
executor.awaitTermination(1, TimeUnit.MINUTES);
```

**2. Cached Thread Pool:**
```java
// Creates threads as needed, reuses idle threads
ExecutorService executor = Executors.newCachedThreadPool();
```

**3. Single Thread Executor:**
```java
// Executes tasks sequentially in a single thread
ExecutorService executor = Executors.newSingleThreadExecutor();
```

**4. Scheduled Thread Pool:**
```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

// Run after delay
scheduler.schedule(() -> System.out.println("Delayed task"), 5, TimeUnit.SECONDS);

// Run periodically
scheduler.scheduleAtFixedRate(
    () -> System.out.println("Periodic task"), 
    0,  // initial delay
    10, // period
    TimeUnit.SECONDS
);
```

### Custom Thread Pool Configuration

```java
int corePoolSize = 4;
int maxPoolSize = 8;
long keepAliveTime = 60L;
BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(100);

ThreadPoolExecutor executor = new ThreadPoolExecutor(
    corePoolSize,
    maxPoolSize,
    keepAliveTime,
    TimeUnit.SECONDS,
    workQueue,
    new ThreadPoolExecutor.CallerRunsPolicy()  // Rejection policy
);
```

### Future Interface

```java
ExecutorService executor = Executors.newFixedThreadPool(2);

Future<Integer> future = executor.submit(() -> {
    Thread.sleep(2000);
    return 42;
});

System.out.println("Doing other work...");

try {
    Integer result = future.get(3, TimeUnit.SECONDS);  // Blocking call
    System.out.println("Result: " + result);
} catch (TimeoutException e) {
    future.cancel(true);
    System.out.println("Task cancelled due to timeout");
}
```

**Learning Exercise:**
- Create different types of thread pools
- Measure performance differences
- Configure custom thread pool sizes
- Handle Future cancellation

---

## CompletableFuture Patterns

### Basic CompletableFuture

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    return "Result from async computation";
});

future.thenAccept(result -> System.out.println(result));
```

### Chaining Operations

```java
CompletableFuture.supplyAsync(() -> {
    return fetchUserData();  // Step 1
})
.thenApply(userData -> {
    return enrichUserData(userData);  // Step 2
})
.thenApply(enrichedData -> {
    return formatResponse(enrichedData);  // Step 3
})
.thenAccept(response -> {
    sendResponse(response);  // Step 4
})
.exceptionally(ex -> {
    handleError(ex);
    return null;
});
```

### Combining Multiple Futures

**thenCombine (both complete successfully):**
```java
CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> 10);
CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> 20);

CompletableFuture<Integer> combined = future1.thenCombine(future2, (a, b) -> a + b);
System.out.println(combined.get());  // 30
```

**allOf (wait for all):**
```java
List<CompletableFuture<String>> futures = List.of(
    CompletableFuture.supplyAsync(() -> "Result 1"),
    CompletableFuture.supplyAsync(() -> "Result 2"),
    CompletableFuture.supplyAsync(() -> "Result 3")
);

CompletableFuture<Void> allComplete = CompletableFuture.allOf(
    futures.toArray(new CompletableFuture[0])
);

allComplete.thenRun(() -> {
    List<String> results = futures.stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
    System.out.println(results);
});
```

**anyOf (first to complete):**
```java
CompletableFuture<Object> firstComplete = CompletableFuture.anyOf(
    CompletableFuture.supplyAsync(() -> slowOperation1()),
    CompletableFuture.supplyAsync(() -> slowOperation2()),
    CompletableFuture.supplyAsync(() -> slowOperation3())
);

Object result = firstComplete.get();  // Returns first completed result
```

### Error Handling

```java
CompletableFuture.supplyAsync(() -> {
    if (new Random().nextBoolean()) {
        throw new RuntimeException("Random failure");
    }
    return "Success";
})
.exceptionally(ex -> {
    logger.error("Error occurred", ex);
    return "Default value";
})
.thenAccept(result -> System.out.println(result));
```

**Learning Exercise:**
- Build a multi-step async pipeline
- Combine results from parallel operations
- Implement proper error handling
- Measure performance vs sequential code

---

## Thread Safety & Synchronization

### Synchronized Keyword

```java
public class Counter {
    private int count = 0;
    
    public synchronized void increment() {
        count++;
    }
    
    public synchronized int getCount() {
        return count;
    }
}
```

### Locks (More Flexible)

```java
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private double balance = 0;
    private final ReentrantLock lock = new ReentrantLock();
    
    public void deposit(double amount) {
        lock.lock();
        try {
            balance += amount;
        } finally {
            lock.unlock();  // Always unlock in finally
        }
    }
    
    public boolean tryDeposit(double amount, long timeout) throws InterruptedException {
        if (lock.tryLock(timeout, TimeUnit.MILLISECONDS)) {
            try {
                balance += amount;
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false;
    }
}
```

### Concurrent Collections

```java
// Thread-safe collections
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
BlockingQueue<Task> queue = new LinkedBlockingQueue<>();

// Atomic operations
map.putIfAbsent("key", 1);
map.computeIfAbsent("key", k -> expensiveComputation());
```

### Atomic Variables

```java
import java.util.concurrent.atomic.*;

AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet();  // Thread-safe increment
counter.compareAndSet(5, 10);  // Compare-and-swap

AtomicReference<User> currentUser = new AtomicReference<>();
currentUser.set(new User("John"));
```

**Learning Exercise:**
- Create race conditions intentionally
- Fix them with synchronization
- Compare synchronized vs Lock performance
- Use concurrent collections

---

## Performance Optimization

### Choosing the Right Pool Size

**CPU-Bound Tasks:**
```java
int cores = Runtime.getRuntime().availableProcessors();
int optimalThreads = cores + 1;  // One extra for I/O contingency
```

**I/O-Bound Tasks:**
```java
int optimalThreads = cores * (1 + waitTime / computeTime);
// Example: 8 cores, 90% waiting, 10% computing
// optimalThreads = 8 * (1 + 90/10) = 80 threads
```

### Measuring Performance

```java
long startTime = System.nanoTime();
// Execute concurrent tasks
long endTime = System.nanoTime();
long duration = (endTime - startTime) / 1_000_000;  // milliseconds

System.out.println("Execution time: " + duration + "ms");
```

### Avoiding Common Pitfalls

**1. Deadlock Prevention:**
```java
// Always acquire locks in the same order
lock1.lock();
try {
    lock2.lock();
    try {
        // Critical section
    } finally {
        lock2.unlock();
    }
} finally {
    lock1.unlock();
}
```

**2. Resource Cleanup:**
```java
ExecutorService executor = Executors.newFixedThreadPool(4);
try {
    // Submit tasks
} finally {
    executor.shutdown();
    executor.awaitTermination(1, TimeUnit.MINUTES);
}
```

**Learning Exercise:**
- Benchmark sequential vs concurrent processing
- Find optimal thread pool sizes
- Create and resolve a deadlock
- Profile thread CPU usage

---

## Future Enhancements

### 1. Virtual Threads (Java 21+)
```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    IntStream.range(0, 10_000).forEach(i -> {
        executor.submit(() -> {
            Thread.sleep(Duration.ofSeconds(1));
            return i;
        });
    });
}
```

### 2. Structured Concurrency
```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Future<String> user = scope.fork(() -> fetchUser());
    Future<Integer> order = scope.fork(() -> fetchOrder());
    
    scope.join();
    scope.throwIfFailed();
    
    return new Response(user.resultNow(), order.resultNow());
}
```

### 3. Kafka Integration
- Process messages concurrently
- Implement backpressure
- Handle failures gracefully

### 4. Distributed Tracing
- Track concurrent operations across services
- Measure end-to-end latency
- Identify bottlenecks

### 5. Advanced Patterns
- Work-stealing algorithms
- Fork/Join framework
- Reactive streams with Project Reactor

---

## Resources

**Books:**
- "Java Concurrency in Practice" by Brian Goetz
- "Concurrent Programming in Java" by Doug Lea

**Official Docs:**
- [Java Concurrency Utilities](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/package-summary.html)
- [CompletableFuture API](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/CompletableFuture.html)

**Tools:**
- JProfiler for thread profiling
- VisualVM for monitoring
- JMH for microbenchmarking

---

## Progress Checklist

- [ ] Understand thread lifecycle
- [ ] Create and manage thread pools
- [ ] Master CompletableFuture chains
- [ ] Handle concurrent collections
- [ ] Implement proper synchronization
- [ ] Measure and optimize performance
- [ ] Test concurrent code properly
- [ ] Explore Virtual Threads (Java 21+)
