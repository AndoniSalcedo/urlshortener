package com.unizar.urlshorter.scheduling

import java.util.concurrent.Executor

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.scheduling.concurrent.*
import org.springframework.scheduling.annotation.*
import org.springframework.context.annotation.*
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor
import java.util.concurrent.*


@Configuration
@EnableAsync
public class ThreadConfig {
    @Bean("taskExecutor")
    fun taskExecutor(): TaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 2
        executor.maxPoolSize = 2
        executor.setQueueCapacity(500)
        executor.setThreadNamePrefix("ThreadPool-")
        executor.initialize()
        return DelegatingSecurityContextAsyncTaskExecutor(executor);
    }

    @Bean("requestExecutor")
    fun requestExecutor(): TaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 2
        executor.maxPoolSize = 2
        executor.setQueueCapacity(500)
        executor.setThreadNamePrefix("ThreadPool-")
        executor.initialize()
        return DelegatingSecurityContextAsyncTaskExecutor(executor);
    }
}