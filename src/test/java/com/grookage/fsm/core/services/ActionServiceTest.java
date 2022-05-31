package com.grookage.fsm.core.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grookage.fsm.core.stubs.TestAction;
import com.grookage.fsm.core.stubs.TestContext;
import com.grookage.fsm.core.stubs.TestEvent;
import com.grookage.fsm.core.stubs.TestState;
import com.grookage.fsm.core.stubs.TestTransitionKey;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.junit.Assert;
import org.junit.Test;

public class ActionServiceTest {

  private static final ObjectMapper mapper = new ObjectMapper();

  @Test
  public void testActionServiceEmptyTransitions(){
    final var actionService = new ActionService<TestEvent, TestState, TestTransitionKey, TestContext>();

    final var testContext = new TestContext();
    actionService.handleTransition(TestEvent.INITIATE, TestState.STARTED, testContext);

    final var actionMap = testContext.getContext("action",
        o -> Optional.ofNullable(o).map(obj -> mapper.convertValue(obj, Map.class))).orElse(null);
    Assert.assertNull(actionMap);
  }

  @Test
  public void testActionService(){
    final var actionService = new ActionService<TestEvent, TestState, TestTransitionKey, TestContext>();
    actionService.anyTransition(new TestAction());

    final var testContext = new TestContext();
    actionService.handleTransition(TestEvent.INITIATE, TestState.STARTED, testContext);

    final var actionMap = testContext.getContext("action",
        o -> Optional.ofNullable(o).map(obj -> mapper.convertValue(obj, Map.class))).orElse(null);
    Assert.assertNotNull(actionMap);
    Assert.assertEquals("testAction", actionMap.get("action"));
  }
}
