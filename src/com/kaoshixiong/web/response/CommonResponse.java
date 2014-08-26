package com.kaoshixiong.web.response;

public class CommonResponse {
	
	public String getErrno() {
		return errno;
	}

	public void setErrno(String errno) {
		this.errno = errno;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	private String errno;
	private String errmsg;
	
	public CommonResponse(String errno,String errmsg){
		this.errmsg = errmsg;
		this.errno = errno;
	}
}
