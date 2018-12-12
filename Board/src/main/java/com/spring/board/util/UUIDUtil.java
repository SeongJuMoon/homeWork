package com.spring.board.util;

import java.util.UUID;

public final class UUIDUtil {
	/**
	 * UUID String¿ª ∏∏µÏ¥œ¥Ÿ.
	 * @return uuidID
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
