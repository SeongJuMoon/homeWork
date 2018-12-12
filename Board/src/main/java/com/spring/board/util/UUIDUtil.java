package com.spring.board.util;

import java.util.UUID;

public final class UUIDUtil {
	/**
	 * UUID String�� ����ϴ�.
	 * @return uuidID
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
