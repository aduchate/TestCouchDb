package org.taktik.jrebel.test.applications.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public abstract class BaseApp implements App {
	protected Logger log = LoggerFactory.getLogger(getClass());

	protected File homeDir;

	public BaseApp(File homeDir) {
		this.homeDir = homeDir;
	}

	@Override
	public final void close() throws Exception {
		stop();
	}
}