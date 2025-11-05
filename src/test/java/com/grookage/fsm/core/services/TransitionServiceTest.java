package com.grookage.fsm.core.services;

import com.grookage.fsm.core.models.entities.Transition;
import com.grookage.fsm.core.stubs.TestEvent;
import com.grookage.fsm.core.stubs.TestState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TransitionServiceTest {

	@Test
	void testTransitionService() {
		final var transitionService = new TransitionService<TestEvent, TestState>();
		transitionService.addTransition(TestState.STARTED, new Transition<>(TestEvent.INITIATE, TestState.STARTED, TestState.CREATED));

		final var transitionDetails = transitionService.getTransitionDetails();
		assertFalse(transitionDetails.isEmpty());

		final var transition = transitionService.getTransition(TestState.STARTED, TestEvent.INITIATE).orElse(null);
		assertNotNull(transition);
	}
}
