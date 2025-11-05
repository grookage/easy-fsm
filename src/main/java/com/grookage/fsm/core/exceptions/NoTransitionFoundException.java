package com.grookage.fsm.core.exceptions;


import com.grookage.fsm.core.models.entities.Context;
import com.grookage.fsm.core.models.entities.Event;
import com.grookage.fsm.core.models.entities.State;
import lombok.Getter;

@SuppressWarnings({"rawtypes"})
@Getter
public class NoTransitionFoundException extends FsmException {

	private final State state;
	private final Event event;
	private final Context context;

	public NoTransitionFoundException(final State state,
	                                  final Event event,
	                                  final Context context,
	                                  final String errorMessage) {
		super(errorMessage);
		this.event = event;
		this.state = state;
		this.context = context;
	}
}
