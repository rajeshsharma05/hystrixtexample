package com.rajesh;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;

public class TestHystrix {

    public static void main(String[] args) throws InterruptedException {
        HystrixCommand.Setter config = HystrixCommand
                .Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupThreadPool"));

        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
        commandProperties.withExecutionTimeoutInMilliseconds(2000);
        config.andCommandPropertiesDefaults(commandProperties);
        config.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                .withMaxQueueSize(10)
                .withCoreSize(3)
                .withQueueSizeRejectionThreshold(10));

        for (int i = 0; i < 20; i++) {
            final String response = new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(4500)).execute();
            System.out.println("Service response: " + response);
        }

    }
}
