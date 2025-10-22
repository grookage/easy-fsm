package com.grookage.fsm.core.utils;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Objects;

@UtilityClass
public class FsmUtils {

	private static final String ERROR_MESSAGE = "message";

	public static String errorString() {
		return ERROR_MESSAGE;
	}

	public static boolean isNullOrEmpty(Collection<?> clx) {
		return Objects.isNull(clx) || clx.isEmpty();
	}

}
