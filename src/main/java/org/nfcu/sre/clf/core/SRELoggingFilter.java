package org.nfcu.sre.clf.core;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;

import org.nfcu.sre.clf.constants.LoggingConstants;
import org.nfcu.sre.clf.exception.HttpReturnResponse;
import org.nfcu.sre.clf.slf4j.SRELogger;
import org.nfcu.sre.clf.slf4j.SRELoggerFactory;

import org.slf4j.MDC;

import brave.propagation.TraceContext;

import javax.servlet.Filter;
//import io.opentelemetry.api.trace.Span;

public class SRELoggingFilter implements Filter {

	private static final SRELogger LOG = SRELoggerFactory.getLogger(SRELoggingFilter.class);

	private SRELoggingConfig sreLoggingConfig = null;
	
	protected Properties loggingLiteProps;
	
	private static final String[] IP_HEADER_CANDIDATES = { 
			"X-Forwarded-For", 
			"Proxy-Client-IP", 
			"WL-Proxy-Client-IP", 
			"HTTP_X_FORWARDED_FOR",
			"HTTP_X_FORWARDED", 
			"HTTP_X_CLUSTER_CLIENT_IP",
			"HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR",
			"HTTP_FORWARDED",
			"HTTP_VIA",
			"REMOTE_ADDR" };
	

	public String getClientIpAddress (HttpServletRequest request) {
		for (String header : IP_HEADER_CANDIDATES) {
			String ip = request.getHeader(header); 
			if (ip != null && ip.length()!= 0 && ! "unknown".equalsIgnoreCase(ip)) { 
				return ip;
			}
		}
		return request.getRemoteAddr();
	}
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		sreLoggingConfig = SRELoggingConfig.Singleton();
		loggingLiteProps = sreLoggingConfig.loadJVMProps();
	}
	
	private void createSRELoggerContext(HttpServletRequest request, String serviceName) {

		try {
			SRELoggingUtility.updatePropagationFields("applicationAddress", request.getServerName());
			SRELoggingUtility.updatePropagationFields("serviceNameProtocol", request.getScheme());
			SRELoggingUtility.updatePropagationFields("SpringAppName", request.getRequestURI());
			SRELoggingUtility.updatePropagationFields("sourceAddress", getClientIpAddress(request));
			SRELoggingUtility.updatePropagationFields(LoggingConstants.USER_AGENT_ATTRIBUTES, request.getHeader("User-Agent"));
			SRELoggingUtility.updatePropagationFields("httpRequestMethod", request.getMethod());
			SRELoggingUtility.updatePropagationFields("httpEndpoint", request.getScheme()+"://" + request.getServerName()+ ":" +request.getServerPort() + request.getRequestURI());
			SRELoggingUtility.updatePropagationFields("applicationAddress", request.getServerName());
			SRELoggingUtility.updatePropagationFields("applicationAddress", request.getServerName());

			
		}catch(Exception e) {
			LOG.error("SRE CLF : Error in SRELoggingFilter createSRELoggerContext", e);
		}
	}
	
	private void setCorrelationId(HttpServletRequest request)
	{
		try {
			String correlationHeader = loggingLiteProps.getProperty("sre.logger.additionalproperties.correlation"); 
			if (StringUtils.isNotEmpty(correlationHeader)) {
					String correlationValue = request.getHeader(correlationHeader); 
					if (StringUtils.isNotEmpty(correlationValue)) { 
						SRELoggingUtility.updatePropagationFields(correlationHeader, correlationValue);
						} else {
							LOG.warn("Not able to update propagation field. No value present for header key: "+ correlationHeader); 
							} 
					}
		}catch (Exception e) { 
			LOG.error("SRE: Error in RequestContextFilter getCorrelationid", e);
		}
	}
	
	public void createRequestSummaryLog(String status, final long durationElapsed) {
		SRELoggingUtility.updatePropagationFields("httpDurations", String.valueOf(durationElapsed));
		SRELoggingUtility.updatePropagationFields("responseStatus", status);
		LOG.info("RequestContextFilter: Request Completed");
	}

	
	@Override 
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
	{
		final long durationStart = System.currentTimeMillis();
		HttpServletRequest request = (HttpServletRequest) servletRequest; 
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		TraceContext traceCurrentContext = SRELoggingUtility.getTraceCurrentContext(request.getServletPath());
		try { 
			setCorrelationId(request); 
			String traceId = traceCurrentContext.traceIdString() + ""+ RandomStringUtils.random(5, true, false);
			String spanId = traceCurrentContext.spanIdString() + ""+ RandomStringUtils.random(5, true, false);
			
			SRELoggingUtility.updatePropagationFields("traceId", traceId);
			SRELoggingUtility.updatePropagationFields("spanId", spanId);
			
			String serviceName = request.getRequestURI().substring(request.getContextPath().length());
			
			if(loggingLiteProps != null) {
				SRELoggingUtility.updatePropagationFields("applicationName", loggingLiteProps.getProperty("sre.logging.applicationName"));
				SRELoggingUtility.updatePropagationFields("applicationName", loggingLiteProps.getProperty("sre.logging.applicationName"));
			}

			createSRELoggerContext(request, serviceName); 
			filterChain.doFilter(request, response); 
		}
		finally {
			createRequestSummaryLog(String.valueOf(response.getStatus()), System.currentTimeMillis() - durationStart);
	
			if (request.getHeader("SKIP_MOC_CLEAR") == null) {
				MDC.clear();
			}
		}
	}


	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
