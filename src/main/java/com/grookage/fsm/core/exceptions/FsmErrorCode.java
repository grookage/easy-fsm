package com.grookage.fsm.core.exceptions;

import lombok.Getter;

@Getter
public enum FsmErrorCode {

	STATE_NOT_FOUND(400),

	INVALID_MACHINE_BUILDER_CONFIG(400),

	EVENT_TRANSITION_FAILED(500),

	TRANSITION_NOT_FOUND(400),

	STATE_ENGINE_NOT_FOUND(500);

	final int status;

	FsmErrorCode(int status) {
		this.status = status;
	}
}
