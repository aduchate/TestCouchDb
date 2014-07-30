package org.taktik.jrebel.test.applications.app;

public interface App extends AutoCloseable {
	public void start() throws Exception;

	public void stop() throws Exception;
}