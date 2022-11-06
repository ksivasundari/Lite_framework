package org.nfcu.sre.clf.slf4j;

import ch.qos.logback.classic.Logger;

public abstract class SRELoggerFactory {

	private SRELoggerFactory() {
		
	}
	
	public static SRELogger getLogger(
			final Class<?> clazz) {
		return new SRELogger(
				(Logger) org.slf4j.LoggerFactory.getLogger(clazz));
	}
	
	
	public static SRELogger getLogger(
			final String clazz) {
		return new SRELogger(
				(Logger) org.slf4j.LoggerFactory.getLogger(clazz));
	}
	
}
