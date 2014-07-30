package org.taktik.jrebel.gen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class UUIDGenerator implements IDGenerator {
	protected static final Logger log = LoggerFactory.getLogger(UUIDGenerator.class);

	@Override
	public synchronized int incrementAndGet(String sequenceName) {
		throw new IllegalStateException("Not supported");
	}

	@Override
	public UUID newGUID() {
		return UUID.randomUUID();
    }
}