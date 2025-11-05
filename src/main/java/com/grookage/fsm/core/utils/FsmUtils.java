package com.grookage.fsm.core.utils;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Objects;

@UtilityClass
public class FsmUtils {

	public static boolean isNullOrEmpty(Collection<?> clx) {
		return Objects.isNull(clx) || clx.isEmpty();
	}

}
