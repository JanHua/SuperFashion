package com.sf.okhttp;


import android.os.Message;
import android.os.Process;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 异步执行容器
 *
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 * @author wjh
 */
@SuppressWarnings("unchecked")
public abstract class HttpAsyncTask<Params, Progress, Result> {

    private static volatile Executor httpDefaultExecutor;
    private FutureTask<Result> futureTask;
    private WorkerRunnable<Params, Result> callable;
    private static final HttpHandler hander = new HttpHandler();

    private final int corePoolSize = 3;
    private final int maximumPoolSize = 120;
    private final int keepAliveTime = 1;
    private final int capacity = 10;

    private static final int MESSAGE_WHAT = 0x1;
    private final AtomicBoolean mTaskInvoked = new AtomicBoolean();

    private volatile Status mStatus = Status.PENDING;

    public enum Status {
        PENDING, RUNNING, FINISHED,
    }

    public HttpAsyncTask() {
        initExecutor();
        initcallable();
        initFutureTask();
    }

    /**
     * 初始化线程池
     */
    private void initExecutor() {
        if (httpDefaultExecutor == null) {
            httpDefaultExecutor = new ThreadPoolExecutor(corePoolSize,
                    maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(capacity),
                    new ThreadFactory() {

                        private final AtomicInteger atomicNumber = new AtomicInteger();

                        @Override
                        public Thread newThread(Runnable r) {
                            // TODO Auto-generated method stub
                            // 自增长模式
                            return new Thread(r, "Thread #"
                                    + atomicNumber.getAndIncrement());
                        }
                    });
        }
    }

    /**
     * 初始化异步线程
     */
    private void initcallable() {
        callable = new WorkerRunnable<Params, Result>() {
            @Override
            public Result call() throws Exception {
                // TODO Auto-generated method stub
                mTaskInvoked.set(true);
                // 线程优先级权限
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                return postResult(doInBackground(params));
            }
        };
    }

    /**
     * 异步线程回调
     */
    private void initFutureTask() {
        futureTask = new FutureTask<Result>(callable) {
            @Override
            protected void done() {
                Result result;
                try {
                    result = get();
                    postResultIfNotInvoked(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    postResultIfNotInvoked(null);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    postResultIfNotInvoked(null);
                }

            }
        };
    }

    /**
     * 加载异步线程
     *
     * @param params
     */
    @SuppressWarnings("incomplete-switch")
    protected void executeRunnable(Executor executor, Params... params) {
        if (executor == null) {
            return;
        }

        if (mStatus != Status.PENDING) {
            switch (mStatus) {
                case RUNNING:
                    throw new IllegalStateException("Cannot execute task:"
                            + " the task is already running.");
                case FINISHED:
                    throw new IllegalStateException("Cannot execute task:"
                            + " the task has already been executed "
                            + "(a task can be executed only once)");
            }
        }

        mStatus = Status.RUNNING;

        callable.params = params;
        onPreExecute();
        executor.execute(futureTask);
    }

    public void executeRunnable(Params... params) {
        executeRunnable(httpDefaultExecutor, params);
    }

    /**
     * 释放线程池
     */
    protected static void shutdown() {
        if (httpDefaultExecutor != null) {
            ((ExecutorService) httpDefaultExecutor).shutdown();
            httpDefaultExecutor = null;
        }
    }

    protected final boolean cancelled(boolean mayInterruptIfRunning) {
        return futureTask.cancel(mayInterruptIfRunning);
    }

    protected void onPreExecute() {
    }

    protected abstract Result doInBackground(Params... params);

    protected void onPostExecute(Result result) {
    }

    protected void onCancelled() {
    }

    protected final boolean isCancelled() {
        return futureTask.isCancelled();
    }

    protected final Status getStatus() {
        return mStatus;
    }

    @SuppressWarnings("hiding")
    private abstract class WorkerRunnable<Params, Result> implements
            Callable<Result> {
        Params[] params;
    }

    /**
     * 执行UI线程辅助类
     *
     * @param <Result>
     */
    @SuppressWarnings("rawtypes")
    private static class HttpResultData<Result> {
        private HttpAsyncTask httpAsyncTask;
        private Result[] data;

        public HttpResultData(HttpAsyncTask httpAsyncTask, Result... data) {
            super();
            this.httpAsyncTask = httpAsyncTask;
            this.data = data;
        }
    }

    /**
     * 线程池一个线程执行完成后进行校验
     *
     * @param result
     */
    private void postResultIfNotInvoked(Result result) {
        final boolean wasTaskInvoked = mTaskInvoked.get();
        if (!wasTaskInvoked) {
            postResult(result);
        }
    }

    /**
     * 回调UI线程方法
     *
     * @param result
     * @return
     */
    private Result postResult(Result result) {
        hander.obtainMessage(MESSAGE_WHAT,
                new HttpResultData<Result>(this, result)).sendToTarget();
        return result;
    }

    /**
     * 结束一个异步加载线程
     *
     * @param result
     */
    private void finish(Result result) {
        if (isCancelled()) {
            onCancelled();
        } else {
            onPostExecute(result);
        }

        mStatus = Status.FINISHED;
    }

    /**
     * UI线程执行器
     *
     * @author wjh
     */
    @SuppressWarnings("rawtypes")
    private static class HttpHandler extends android.os.Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HttpResultData mHttpResultData = (HttpResultData) msg.obj;
            switch (msg.what) {
                case MESSAGE_WHAT: {
                    mHttpResultData.httpAsyncTask.finish(mHttpResultData.data[0]);
                    break;
                }
            }
        }

    }
}
