package com.csubigdata.futurestradingsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class SpringThreadPoolConfig {

//    @Value("${mythreadpool.maxPoolSize}")
    @Value("40")
    private Integer maxPoolSize;

//    @Value("${mythreadpool.corePoolSize}")
    @Value("20")
    private Integer corePoolSize;

//    @Value("${mythreadpool.queueCapacity}")
    @Value("500")
    private Integer queueCapacity;

//    @Value("${mythreadpool.keepAliveSeconds}")
    @Value("60")
    private Integer keepAliveSeconds;

//    @Value("${mythreadpool.threadNamePrefix}")
    @Value("springThreadPool")
    private String threadNamePrefix;

//    @Value("${mythreadpool.waitForTasksToCompleteOnShutdown}")
    @Value("true")
    private Boolean waitForTasksToCompleteOnShutdown;

//    @Bean("${mythreadpool.executorName}")
    @Bean("taskExecutor")
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数等于系统核数--8核
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(availableProcessors);
        // 设置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //配置队列大小
        executor.setQueueCapacity(queueCapacity);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程满了之后由调用者所在的线程来执行
        // 拒绝策略：CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 设置默认线程名称
        executor.setThreadNamePrefix(threadNamePrefix);
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(waitForTasksToCompleteOnShutdown);
        //执行初始化
        executor.initialize();
        return executor;
    }
}
