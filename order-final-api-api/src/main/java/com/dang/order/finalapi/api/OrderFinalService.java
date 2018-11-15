package com.dang.order.finalapi.api;

import com.dang.order.common.api.ApiRequest;
import com.dang.order.common.api.ApiResponse;
import com.dang.order.finalapi.api.param.OrderParam;

public interface OrderFinalService {
	public ApiResponse processFinalOrder(ApiRequest<OrderParam> request);
}
