package org.taktik.jrebel.gen;

import java.util.UUID;

public interface IDGenerator {
	public int incrementAndGet(String sequenceName);
	public UUID newGUID();
}