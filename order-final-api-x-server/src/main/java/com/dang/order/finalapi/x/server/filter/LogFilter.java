package com.dang.order.finalapi.x.server.filter;

import com.alibaba.dubbo.rpc.*;
import com.dang.order.common.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by caojian on 2016/9/5.
 */
public class LogFilter implements Filter{
    private static final Logger logger = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String service = invoker.getInterface().getName();
        service = service.substring(service.lastIndexOf('.')+1);
        String methodName = invocation.getMethodName();
        Object arguments = invocation.getArguments();
        String sequence = nextSequence();
        logger.info("seq:{} invoke {}.{}, params: {}", sequence, service, methodName, arguments);
        long startTime = System.currentTimeMillis();
        Result response = invoker.invoke(invocation);
        long elapsed = System.currentTimeMillis() - startTime;
        if(elapsed > 1000L){
            logger.warn("seq:{} long time cost: {}.{} {}ms", sequence, service, methodName, elapsed);
        }
        injectElapsed(response.getValue(), elapsed);
        logger.info("seq:{} response: {}", sequence, response);
        return response;
    }

    private String nextSequence(){
        long curmillis = System.currentTimeMillis();
        int random = ThreadLocalRandom.current().nextInt(100000);
        return String.format("%d_%d_%d", Thread.currentThread().getId(), random, curmillis%100000);
    }

    private void injectElapsed(Object responseObject, long elapsed) {
        if (responseObject != null && responseObject instanceof ApiResponse) {
            ((ApiResponse) responseObject).setElapsed(elapsed);
        }
    }
}
