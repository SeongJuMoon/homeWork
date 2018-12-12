package com.spring.board.util;

import java.util.UUID;

public final class UUIDUtil {
	/**
	 * @return uuidID
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
