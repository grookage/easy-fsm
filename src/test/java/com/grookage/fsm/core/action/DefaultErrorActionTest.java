package com.grookage.fsm.core.action;

import com.grookage.fsm.core.exceptions.FsmException;
import com.grookage.fsm.core.exceptions.InvalidStateMachineException;
import org.junit.jupiter.api.Test;

import static com.grookage.fsm.core.exceptions.InvalidStateMachineException.FSMErrorCode.NO_END_STATE;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultErrorActionTest {

	@Test
	void testDefaultErrorAction() {
		final var defaultErrorAction = new DefaultErrorAction();
		final var fsmException = new InvalidStateMachineException(NO_END_STATE, "Invalid Fsm Exception");
		assertThrows(FsmException.class, () -> defaultErrorAction.call(fsmException));
	}
}
