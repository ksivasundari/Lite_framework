package org.nfcu.sre.clf.slf4j;

import ch.qos.logback.classic.Logger;
import org.nfcu.sre.clf.constants.LoggingConstants;
import org.nfcu.sre.clf.core.SRELoggingUtility;
import org.nfcu.sre.clf.core.ILogEvent;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.MDC;
import org.slf4j.Marker;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class SRELogger {

	private final Logger slf4Logger;

	private String errorAlert;
	private String warnAlert;
	private String businessAlert;

	public SRELogger(final Logger logger) {
		this.slf4Logger = logger;
	}

	public void error(final String msg, final ILogEvent... event) {
		if (slf4Logger.isErrorEnabled()) {

			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				checkForAlerts();
				slf4Logger.error(msg);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	public void error(final String msg, final Throwable t, final ILogEvent... event) {
		if (slf4Logger.isErrorEnabled()) {

			String originalLog = null;
			try {
				originalLog = this.putMdcContext(event);
				checkForAlerts(t);
				slf4Logger.error(msg, t);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	public void error(final Marker marker, final String msg, final ILogEvent... event) {
		if (slf4Logger.isErrorEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				checkForAlerts();
				slf4Logger.error(marker, msg);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	public void error(final Marker marker, final String format, final Object arg, final ILogEvent... event) {
		if (slf4Logger.isErrorEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				checkForAlerts(arg);
				slf4Logger.error(marker, format, arg);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	public void error(final Marker marker, final String format, final Object arg1, final Object arg2,
			final ILogEvent... event) {

		if (this.slf4Logger.isErrorEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				checkForAlerts(arg1, arg2);
				slf4Logger.error(marker, format, arg1, arg2);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	public void error(final Marker marker, final String msg, final Throwable t, final ILogEvent... event) {

		if (this.slf4Logger.isErrorEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				checkForAlerts(t);
				slf4Logger.error(marker, msg, t);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	public void error(final String format, final Object... argArray) {
		if (slf4Logger.isErrorEnabled()) {
			String originalLog = null;
			ILogEvent[] event = getLogEvent(argArray);
			Object[] args = getLogValues(argArray);

			try {
				checkForAlerts(argArray);
				originalLog = putMdcContext(event);
				slf4Logger.error(format, args);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	// Debug
	public void debug(final String msg, final ILogEvent... event) {
		if (slf4Logger.isDebugEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				slf4Logger.debug(msg);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	public void debug(final String format, final Object arg, final ILogEvent... event) {
		if (slf4Logger.isDebugEnabled()) {
			String originalLog = null;
			try {
				checkForAlerts(arg);
				originalLog = putMdcContext(event);
				slf4Logger.debug(format, arg);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	public void debug(final String format, final Object arg1, final Object arg2, final ILogEvent... event) {
		if (slf4Logger.isDebugEnabled()) {
			String originalLog = null;
			try {
				originalLog = this.putMdcContext(event);
				checkForAlerts(arg1, arg2);
				slf4Logger.debug(format, arg1, arg2);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	public void debug(final String msg, final Throwable t, final ILogEvent... event) {
		if (this.slf4Logger.isDebugEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				checkForAlerts(t);
				slf4Logger.debug(msg, t);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	public void debug(final Marker marker, final String msg, final ILogEvent... event) {
		if (slf4Logger.isDebugEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				slf4Logger.debug(marker, msg);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	public void debug(final Marker marker, final String format, final Object arg, final ILogEvent... event) {
		if (slf4Logger.isDebugEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				checkForAlerts(arg);
				slf4Logger.debug(marker, format, arg);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	public void debug(final Marker marker, final String format, final Object arg1, final Object arg2,
			final ILogEvent... event) {
		if (slf4Logger.isDebugEnabled()) {
			String originalLog = null;
			try {
				originalLog = this.putMdcContext(event);
				checkForAlerts(arg1, arg2);
				slf4Logger.debug(marker, format, arg1, arg2);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	public void debug(final Marker marker, final String msg, final Throwable t, final ILogEvent... event) {
		if (slf4Logger.isDebugEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				slf4Logger.debug(marker, msg, t);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	public void debug(final String format, final Object... argArray) {
		if (slf4Logger.isDebugEnabled()) {
			String originalLog = null;
			ILogEvent[] event = getLogEvent(argArray);
			Object[] args = getLogValues(argArray);
			try {
				originalLog = putMdcContext(event);
				checkForAlerts(argArray);
				slf4Logger.debug(format, args);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

// Trace
	/**
	 * 
	 * @param msg
	 * @param event
	 */
	public void trace(final String msg, final ILogEvent... event) {
		if (slf4Logger.isTraceEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				slf4Logger.trace(msg);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	/**
	 * 
	 * @param format
	 * @param arg
	 * @param event
	 */
	public void trace(final String format, final Object arg, final ILogEvent... event) {
		if (slf4Logger.isTraceEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				checkForAlerts(arg);
				slf4Logger.trace(format, arg);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	/**
	 * 
	 * @param format
	 * @param arg1
	 * @param arg2
	 * @param event
	 */
	public void trace(final String format, final Object arg1, final Object arg2, final ILogEvent... event) {
		if (slf4Logger.isTraceEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				checkForAlerts(arg1, arg2);
				slf4Logger.trace(format, arg1, arg2);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	/**
	 * 
	 * @param msg
	 * @param t
	 * @param event
	 */
	public void trace(final String msg, final Throwable t, final ILogEvent... event) {
		if (slf4Logger.isTraceEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				slf4Logger.trace(msg, t);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	/**
	 * 
	 * @param marker
	 * @param msg
	 * @param event
	 */
	public void trace(final Marker marker, final String msg, final ILogEvent... event) {
		if (slf4Logger.isTraceEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				slf4Logger.trace(marker, msg);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}
	/**
	 * 
	 * @param marker
	 * @param format
	 * @param arg
	 * @param event
	 */
	public void trace(final Marker marker, final String format, final Object arg, final ILogEvent... event) {
		if (slf4Logger.isTraceEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				checkForAlerts(arg);
				slf4Logger.trace(marker, format, arg);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}
	
	/**
	 * 
	 * @param marker
	 * @param format
	 * @param arg1
	 * @param arg2
	 * @param event
	 */
	public void trace(final Marker marker, final String format, final Object arg1, final Object arg2,
			final ILogEvent... event) {
		if (slf4Logger.isTraceEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				checkForAlerts(arg1, arg2);
				slf4Logger.trace(marker, format, arg1, arg2);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}
	
	/**
	 * 
	 * @param marker
	 * @param msg
	 * @param t
	 * @param event
	 */
	public void trace(final Marker marker, final String msg, final Throwable t, final ILogEvent... event) {
		if (slf4Logger.isTraceEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				checkForAlerts(t);
				slf4Logger.trace(marker, msg, t);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	/**
	 * 
	 * @param format
	 * @param argArray
	 */
	public void trace(final String format, final Object... argArray) {
		if (slf4Logger.isTraceEnabled()) {
			String originalLog = null;
			ILogEvent[] event = getLogEvent(argArray);
			Object[] args = getLogValues(argArray);
			try {
				originalLog = putMdcContext(event);
				checkForAlerts(argArray);
				slf4Logger.trace(format, args);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	// Warn
	/**
	 * 
	 * @param msg
	 * @param event
	 */
	public void warn(final String msg, final ILogEvent... event) {
		if (slf4Logger.isWarnEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				addWarnAlertKey();
				slf4Logger.warn(msg);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}
	
	/**
	 * 
	 * @param format
	 * @param arg
	 * @param event
	 */
	public void warn(final String format, final Object arg, final ILogEvent... event) {
		if (slf4Logger.isWarnEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				addWarnAlertKey();
				slf4Logger.warn(format, arg);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	/**
	 * 
	 * @param msg
	 * @param t
	 * @param event
	 */
	public void warn(final String msg, final Throwable t, final ILogEvent... event) {
		if (slf4Logger.isWarnEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				addWarnAlertKey();
				slf4Logger.warn(msg, t);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}
	
	/**
	 * 	
	 * @param format
	 * @param arg1
	 * @param arg2
	 * @param event
	 */
	public void warn(final String format, final Object arg1, final Object arg2, final ILogEvent... event) {
		if (slf4Logger.isWarnEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				addWarnAlertKey();
				slf4Logger.warn(format, arg1, arg2);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	/**
	 * 
	 * @param marker
	 * @param msg
	 * @param event
	 */
	public void warn(final Marker marker, final String msg, final ILogEvent... event) {
		if (slf4Logger.isWarnEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				addWarnAlertKey();
				slf4Logger.warn(marker, msg);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	/**
	 * 
	 * @param marker
	 * @param format
	 * @param arg
	 * @param event
	 */
	public void warn(final Marker marker, final String format, final Object arg, final ILogEvent... event) {
		if (slf4Logger.isWarnEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				addWarnAlertKey();
				slf4Logger.warn(marker, format, arg);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	/**
	 * 
	 * @param marker
	 * @param format
	 * @param arg1
	 * @param arg2
	 * @param event
	 */
	public void warn(final Marker marker, final String format, final Object arg1, final Object arg2,
			final ILogEvent... event) {
		if (slf4Logger.isWarnEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				addWarnAlertKey();
				slf4Logger.warn(marker, format, arg1, arg2);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	/**
	 * 
	 * @param marker
	 * @param msg
	 * @param t
	 * @param event
	 */
	public void warn(final Marker marker, final String msg, final Throwable t, final ILogEvent... event) {
		if (slf4Logger.isWarnEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				addWarnAlertKey();
				slf4Logger.warn(marker, msg, t);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	/**
	 * 
	 * @param format
	 * @param argArray
	 */
	public void warn(final String format, final Object... argArray) {
		if (slf4Logger.isWarnEnabled()) {
			String originalLog = null;
			ILogEvent[] event = getLogEvent(argArray);
			Object[] args = getLogValues(argArray);
			addWarnAlertKey();
			try {
				originalLog = putMdcContext(event);
				slf4Logger.warn(format, args);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	// Info
	/**
	 * 
	 * @param msg
	 * @param event
	 */
	public void info(final String msg, final ILogEvent... event) {
		if (slf4Logger.isInfoEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				slf4Logger.info(msg);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	/**
	 * 
	 * @param format
	 * @param arg1
	 * @param arg2
	 * @param event
	 */
	public void info(final String format, final Object arg1, final Object arg2, final ILogEvent... event) {
		if (slf4Logger.isInfoEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				checkForAlerts(arg1, arg2);
				slf4Logger.info(format, arg1, arg2);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	/**
	 * 
	 * @param format
	 * @param argArray
	 */
	public void info(final String format, final Object... argArray) {
		if (slf4Logger.isInfoEnabled()) {
			String originalLog = null;
			ILogEvent[] event = getLogEvent(argArray);
			Object[] args = getLogValues(argArray);
			try {
				originalLog = putMdcContext(event);
				checkForAlerts(argArray);
				slf4Logger.info(format, args);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	/**
	 * 
	 * @param msg
	 * @param t
	 * @param event
	 */
	public void info(final String msg, final Throwable t, final ILogEvent... event) {
		if (slf4Logger.isInfoEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				checkForAlerts(t);
				slf4Logger.info(msg, t);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	/**
	 * 
	 * @param marker
	 * @param msg
	 * @param event
	 */
	public void info(final Marker marker, final String msg, final ILogEvent... event) {
		if (slf4Logger.isInfoEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				slf4Logger.info(marker, msg);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	/**
	 * 
	 * @param marker
	 * @param format
	 * @param arg
	 * @param event
	 */
	public void info(final Marker marker, final String format, final Object arg, final ILogEvent... event) {
		if (slf4Logger.isInfoEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				checkForAlerts(arg);
				slf4Logger.info(marker, format, arg);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	/**
	 * 
	 * @param marker
	 * @param format
	 * @param arg1
	 * @param arg2
	 * @param event
	 */
	public void info(final Marker marker, final String format, final Object arg1, final Object arg2,
			final ILogEvent... event) {
		if (slf4Logger.isInfoEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				checkForAlerts(arg1, arg2);
				slf4Logger.info(marker, format, arg1, arg2);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}
	
	/**
	 * 
	 * @param marker
	 * @param msg
	 * @param t
	 * @param event
	 */
	public void info(final Marker marker, final String msg, final Throwable t, final ILogEvent... event) {
		if (slf4Logger.isInfoEnabled()) {
			String originalLog = null;
			try {
				originalLog = putMdcContext(event);
				checkForAlerts(t);
				slf4Logger.info(marker, msg, t);
			} finally {
				clearMdcContext(originalLog, event);
			}
		}
	}

	/**
	 * 
	 * @param events
	 * @return
	 */
	private String putMdcContext(final ILogEvent... events) {
		String originallog = null;
		if (ArrayUtils.isNotEmpty(events)) {
			StringBuilder b = new StringBuilder();
			Arrays.stream(events).forEach(i -> b.append(i.asString()));
			originallog = MDC.get(LoggingConstants.SRE_ATTRIBUTES);
			if (!StringUtils.isEmpty(b.toString())) {
				// MDC.put("serviceParam", createLog(b, events));
				addFieldsToLogContext(LoggingConstants.SRE_ATTRIBUTES, createLog(b, events));
			}
		}
		return originallog;
	}

	/**
	 * 
	 * @param logObjs
	 */
	protected void checkForAlerts(final Object... logObjs) {
		if (Objects.nonNull(logObjs)) {
			Arrays.stream(logObjs).forEach(this::addErrorAlertKey);
		}
	}

	/**
	 * 
	 * @param exception
	 */
	protected void addErrorAlertKey(Object exception) {
		if (exception instanceof RuntimeException) {
			addFieldsToLogContext(LoggingConstants.ALERT_KEY, this.errorAlert);
		} else if (exception instanceof Exception) {
			addFieldsToLogContext(LoggingConstants.ALERT_KEY, this.businessAlert);
		}
	}

	protected void addWarnAlertKey() {
		addFieldsToLogContext(LoggingConstants.ALERT_KEY, this.warnAlert);
	}

	/**
	 * 
	 * @param newLog
	 * @param events
	 * @return
	 */
	protected String createLog(final StringBuilder newLog, final ILogEvent... events) {
		if ( !CollectionUtils.isEmpty(Arrays.stream(events).filter(i -> i.appendToExisting())
					.collect(Collectors.toList())) && !StringUtils.isEmpty(MDC.get(LoggingConstants.SRE_ATTRIBUTES))) {
				return MDC.get(LoggingConstants.SRE_ATTRIBUTES) + LoggingConstants.LOG_DELIMITTER + newLog.toString();
		}
		return newLog.toString();
	}

	private void clearMdcContext(final String originalLog, final ILogEvent... events) {
		MDC.remove(LoggingConstants.ALERT_KEY);
		if (ArrayUtils.isNotEmpty(events) && hasConsistentContext(events)) {
			MDC.remove(LoggingConstants.SRE_ATTRIBUTES);
			addFieldsToLogContext(LoggingConstants.SRE_ATTRIBUTES, originalLog);
		}
	}

	private boolean hasConsistentContext(ILogEvent[] events) {
		return !Arrays.stream(events).map(e -> e.isConsistentContext()).collect(Collectors.toList()).contains(true);
	}

	public void clearLoggingContext() {
		MDC.remove(LoggingConstants.SRE_ATTRIBUTES);
	}

	protected Object[] getLogValues(final Object... args) {
		if (ArrayUtils.isNotEmpty(args)) {
			return Arrays.stream(args).filter(i -> i instanceof ILogEvent == false).toArray();
		}
		return new Object[0];
	}

	protected ILogEvent[] getLogEvent(final Object... args) {
		if (ArrayUtils.isNotEmpty(args)) {
			Optional<Object> eventOptional = Arrays.stream(args).filter(i -> i instanceof ILogEvent).findFirst();
			if (eventOptional.isPresent()) {
				return new ILogEvent[] { (ILogEvent) eventOptional.get() };
			}
		}
		return new ILogEvent[0];
	}

	public String getThisLoggerName() {
		return this.slf4Logger.getName();
	}

	public void addFieldsToLogContext(final String name, final String value) {
		SRELoggingUtility.addToLogContext(name, value);
	}

	public void removeFieldsToLogContext(final String name) {
		SRELoggingUtility.removeFromLogContext(name);
	}

}
