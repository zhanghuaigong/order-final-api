package com.dang.order.finalapi.dto.enums;

public enum RespStatusCode {
	SUCCESS(0,"Success",0,"Success%s"),

	REQUEST_PARAM_INVALID(1, "System Exception",1994, "request parameter invalidate,msg=%s"),

	UPDATE_DATA_FAIL(1,"System Exception",1995,"更新数据失败"),

	RECORD_NOT_EXIST(1, "System Exception",1996, "data not exists,identity is %s "),

	SEND_EVENT_QUEUE_MSG_FAIL(1, "System Exception",1997, "发order-event消息失败"),

	INSERT_ORDER_EVENT_QUEUE_FAIL(1, "System Exception",1998, "写ordevt.order_event_queue失败"),

	UN_KNOWN_EXCEPTION(1,"System Exception",1999,"系统异常%s"),

	REVERSE_API_ERROR(1,"System Exception",1880,"调用退换货api异常"),

	RETURNNUM_ERROR(2,"Business Exception",2003,"销退数量异常,%s");

	/**
	 * 状态码
	 */
	private Integer code;

	/**
	 * 描述
	 */
	private String msg;

	/**
	 * 子码
	 */
	private Integer subCode;

	/**
	 * 消息模板
	 */
	private String templete;

	private RespStatusCode(Integer code, String msg, Integer subCode, String templete) {
		this.code = code;
		this.msg = msg;
		this.subCode = subCode;
		this.templete = templete;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTemplete() {
		return templete;
	}

	public void setTemplete(String templete) {
		this.templete = templete;
	}

	public Integer getSubCode() {
		return subCode;
	}

	public void setSubCode(Integer subCode) {
		this.subCode = subCode;
	}

	public String formatSubMsg(Object... params) {
		return String.format(this.templete, params);
	}

}
