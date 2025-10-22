package com.grookage.fsm.core.action;

import com.grookage.fsm.core.exceptions.FsmErrorCode;
import com.grookage.fsm.core.exceptions.FsmException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultErrorActionTest {

	@Test
	void testDefaultErrorAction() {
		final var defaultErrorAction = new DefaultErrorAction();
		final var fsmException = FsmException.error(
				FsmErrorCode.INVALID_MACHINE_BUILDER_CONFIG,
				Map.of()
		);
		assertThrows(FsmException.class, () -> defaultErrorAction.call(fsmException));
	}
}
