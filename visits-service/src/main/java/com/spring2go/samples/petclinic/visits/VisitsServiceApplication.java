package com.spring2go.samples.petclinic.visits;

/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.management.ManagementFactory;
import java.text.NumberFormat;

/**
 * @author Maciej Szarlinski
 */
@SpringBootApplication
public class VisitsServiceApplication {
    private static Logger logger = LoggerFactory.getLogger(VisitsServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(VisitsServiceApplication.class, args);


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
}
