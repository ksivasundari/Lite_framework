package org.nfcu.sre.clf.core;

import brave.Span;
import brave.Tracing;
import brave.baggage.BaggageField;
import brave.propagation.TraceContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.UUID;
//31:10
import org.slf4j.MDC;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

public class SRELoggingUtility{

	private SRELoggingUtility() {
	}
	
	public static boolean checkIfTracingContextExists() {
		return (Objects.nonNull(Tracing.current())
				&& Objects.nonNull(Tracing.current().currentTraceContext())
				&& Objects.nonNull(
						Tracing.current().currentTraceContext().get()));
	}

	public static TraceContext getTraceCurrentContext(String serviceName) {
		String uuid = UUID.randomUUID().toString();
		if(!checkIfTracingContextExists()) { 
			Tracing.newBuilder().localServiceName(serviceName+"Trace"+uuid).build();
			
		final Span span = Tracing.currentTracer().nextSpan()
				.name(serviceName+"Span"+uuid).start();
		Tracing.currentTracer().withSpanInScope(span); 
		}
		return Tracing.current().currentTraceContext().get();
	}
	
	public static boolean updatePropagationFields(final String name, final String value) {
		Assert.isTrue(StringUtils.isNotBlank(name),
				"Name for field propagation cannot be empty"); 
		BaggageField b = BaggageField.getByName(name);
		if (Objects.isNull(b)) {
			b = BaggageField.create(name);
		}
		addToLogContext(name, value);
		return b.updateValue(value);
	}

	public static void addToLogContext(final String name, final String value) {
		MDC.put(name, value);
	}
	
	public static void removeFromLogContext(final String name) {
		MDC.remove(name);
	}
}
