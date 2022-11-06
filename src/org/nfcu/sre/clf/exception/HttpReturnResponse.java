package org.nfcu.sre.clf.exception;

import java.util.HashMap;
import java.util.Map;

public class HttpReturnResponse {

	  private String errCode;
	  private String errMsg;

	  public HttpReturnResponse() {}

	  public HttpReturnResponse(String errCode, String errMsg) {
	    this.errCode = errCode;
	    this.errMsg = errMsg;
	  }

	  public String getErrCode() {
	    return this.errCode;
	  }

	  public void setErrCode(String errCode) {
	    this.errCode = errCode;
	  }

		public String getMsg(String statusCode) {
			String resultMsg = "OK";
			int stCode = Integer.valueOf(statusCode);
			httpStatusCode = printStatusCode();
			if (httpStatusCode.containsKey(statusCode)) {
				resultMsg = httpStatusCode.get(statusCode);
			} else {
				if (stCode >= 100 && stCode <= 199)
					resultMsg = "Informal";
				else if (stCode >= 200 && stCode <= 299)
					resultMsg = "Success";
				else if (stCode >= 300 && stCode <= 399)
					resultMsg = "Redirection";
				else if (stCode >= 400 && stCode <= 499)
					resultMsg = "Client error";
				else if (stCode >= 500 && stCode <= 599)
					resultMsg = "Server error";
				else
					resultMsg = "HTTP value not-assigned";
			}
			return resultMsg;
		}

	  public void setErrMsg(String errMsg) {
	    this.errMsg = errMsg;
	  }
	  
	  Map<String, String> httpStatusCode = new HashMap<String, String>();

	  public Map<String, String> printStatusCode() {
		httpStatusCode.put("200","OK");
		httpStatusCode.put("201","Created");
		httpStatusCode.put("202","Accepted");
		httpStatusCode.put("203","Non-Authoritative");
		httpStatusCode.put("204","No Content");
		httpStatusCode.put("300","Multiple Choices");
		httpStatusCode.put("304","Not Modified");
		httpStatusCode.put("400","Bad Request");
		httpStatusCode.put("401","Unauthorized");
		httpStatusCode.put("403","Forbidden");
		httpStatusCode.put("404","Not Found");
		httpStatusCode.put("409","Conflict");
		httpStatusCode.put("500","Internal Server Error");
		return httpStatusCode;
	  }
	}