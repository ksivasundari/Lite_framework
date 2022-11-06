package org.nfcu.sre.clf.core;

public interface ILogEvent{

	public String asString();
	default boolean isConsistentContext() { return false; }
	default String getAlertIndicatorKey() { return ""; }
	default boolean appendToExisting() { return false; }
}
