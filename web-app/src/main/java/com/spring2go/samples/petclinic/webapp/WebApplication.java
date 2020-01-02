package com.spring2go.samples.petclinic.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.lang.management.ManagementFactory;
import java.text.NumberFormat;

@SpringBootApplication
@EnableCircuitBreaker
public class WebApplication {

    private static Logger logger = LoggerFactory.getLogger(WebApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);


        Runtime runtime = Runtime.getRuntime();

        NumberFormat format = NumberFormat.getInstance();

        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long mb = 1024 * 1024;
        String mega = " MB";

        long physicalMemory;
        try {
            physicalMemory = ((com.sun.management.OperatingSystemMXBean) ManagementFactory
                    .getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
        } catch (Exception e) {
            physicalMemory = -1L;
        }

        int availableCores = Runtime.getRuntime().availableProcessors();

        logger.info("========================== System Info ==========================");
        logger.info("Java version: " + System.getProperty("java.vendor") + " " + System.getProperty("java.version"));
        logger.info("Operating system: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        logger.info("CPU Cores: " + availableCores);
        if (physicalMemory != -1L) {
            logger.info("Physical Memory: " + format.format(physicalMemory / mb) + mega);
        }
        logger.info("========================== JVM Memory Info ==========================");
        logger.info("Max allowed memory: " + format.format(maxMemory / mb) + mega);
        logger.info("Allocated memory:" + format.format(allocatedMemory / mb) + mega);
        logger.info("Used memory in allocated: " + format.format( (allocatedMemory - freeMemory) / mb) + mega);
        logger.info("Free memory in allocated: " + format.format(freeMemory / mb) + mega);
        logger.info("Total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / mb) + mega);
        logger.info("Heap Memory Usage: " + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage());
        logger.info("Non-Heap Memory Usage: " + ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage());
        logger.info("=================================================================\n");
    }

    @Bean
    RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }
}
