package br.com.fullcycle.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class OutboxConfig {
    
    @Bean
    TaskExecutor queueExecutor() {
        var executor = new ThreadPoolTaskExecutor();
        executor.setQueueCapacity(200);
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 4);
        executor.setCorePoolSize(2);

        return executor;
    }
}
