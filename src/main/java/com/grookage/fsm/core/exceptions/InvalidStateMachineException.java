package com.grookage.fsm.core.exceptions;

import lombok.Getter;

@Getter
public class InvalidStateMachineException extends FsmException {

	private final FSMErrorCode errorCode;

	public InvalidStateMachineException(final FSMErrorCode errorCode, final String errorMessage) {
		super(errorMessage);
		this.errorCode = errorCode;
	}

	public enum FSMErrorCode {

		NO_START_STATE,

		NO_END_STATE,

		INVALID_MACHINE_BUILDER_CONFIG,

		MISSING_TRANSITIONS_FOR_NON_START_STATE,

		OUTGOING_TRANSITIONS_FROM_END_STATE
	}
}
