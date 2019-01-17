package com.rajesh;

import com.netflix.hystrix.HystrixCommand;

class RemoteServiceTestCommand extends HystrixCommand<String> {

    private RemoteServiceTestSimulator remoteService;

    RemoteServiceTestCommand(Setter config, RemoteServiceTestSimulator remoteService) {
        super(config);
        this.remoteService = remoteService;
    }

    @Override
    protected String run() throws Exception {
        return remoteService.execute();
    }

    @Override
    protected String getFallback() {
        System.out.println("------Inside fallback exception=" + getExecutionException());

        System.out.println("_metric=Error count : " + this.getMetrics().getHealthCounts().getErrorCount());
        System.out.println("_metric=Error % : " + this.getMetrics().getHealthCounts().getErrorPercentage());
        System.out.println("_metric=Total request : " + this.getMetrics().getHealthCounts().getTotalRequests());
        System.out.println("_metric=Concurrent execution count: " + this.getMetrics().getCurrentConcurrentExecutionCount());
        System.out.println("_metric=Execution time mean : " + this.getMetrics().getExecutionTimeMean());

        if (isCircuitBreakerOpen()) {
            System.out.println("_circuit_open");
        }
        return null;
    }
}
