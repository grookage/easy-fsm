package com.grookage.fsm.core.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grookage.fsm.core.stubs.TestAction;
import com.grookage.fsm.core.stubs.TestContext;
import com.grookage.fsm.core.stubs.TestEvent;
import com.grookage.fsm.core.stubs.TestState;
import com.grookage.fsm.core.stubs.TestTransitionKey;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ActionServiceTest {

  private static final ObjectMapper mapper = new ObjectMapper();

  @Test
  void testActionServiceEmptyTransitions(){
    final var actionService = new ActionService<TestEvent, TestState, TestTransitionKey, TestContext>();

    final var testContext = new TestContext();
    actionService.handleTransition(TestEvent.INITIATE, TestState.STARTED, testContext);

    final var actionMap = testContext.getContext("action",
        o -> Optional.ofNullable(o).map(obj -> mapper.convertValue(obj, Map.class))).orElse(null);
    assertNull(actionMap);
  }

  @Test
  void testActionService(){
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
