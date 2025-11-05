package com.grookage.fsm.core.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grookage.fsm.core.stubs.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ActionServiceTest {

	private static final ObjectMapper mapper = new ObjectMapper();

	@Test
	void testActionServiceEmptyTransitions() {
		final var actionService = new ActionService<TestEvent, TestState, TestTransitionKey, TestContext>();

		final var testContext = new TestContext();
		actionService.handleTransition(TestEvent.INITIATE, TestState.STARTED, testContext);

		final var actionMap = testContext.getContext("action",
				o -> Optional.ofNullable(o).map(obj -> mapper.convertValue(obj, Map.class))).orElse(null);
		assertNull(actionMap);
	}

	@Test
	void testActionService() {
		final var actionService = new ActionService<TestEvent, TestState, TestTransitionKey, TestContext>();
		actionService.anyTransition(new TestAction());

		final var testContext = new TestContext();
		actionService.handleTransition(TestEvent.INITIATE, TestState.STARTED, testContext);

		final var actionMap = testContext.getContext("action",
				o -> Optional.ofNullable(o).map(obj -> mapper.convertValue(obj, Map.class))).orElse(null);
		assertNotNull(actionMap);
		assertEquals("testAction", actionMap.get("action"));
	}
}
