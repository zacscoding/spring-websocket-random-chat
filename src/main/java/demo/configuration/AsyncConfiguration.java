package demo.configuration;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author zacconding
 * @Date 2018-08-20
 * @GitHub : https://github.com/zacscoding
 */
@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean
    public Executor asyncThreadPool() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        taskExecutor.setCorePoolSize(3);
        taskExecutor.setMaxPoolSize(30);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.setThreadNamePrefix("Async-Executor-");
        taskExecutor.setDaemon(true);
        taskExecutor.initialize();

        return taskExecutor;
    }
}
