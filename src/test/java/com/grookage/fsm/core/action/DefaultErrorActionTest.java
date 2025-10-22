package com.grookage.fsm.core.action;

import static org.junit.jupiter.api.Assertions.assertThrows;
import com.grookage.fsm.core.exceptions.FsmException;
import com.grookage.fsm.core.stubs.TestContext;
import com.grookage.fsm.core.stubs.TestEvent;
import com.grookage.fsm.core.stubs.TestState;
import com.grookage.fsm.core.stubs.TestTransitionKey;
import org.junit.jupiter.api.Test;


class DefaultErrorActionTest {

  @Test
  void testDefaultErrorAction() {
    final var defaultErrorAction = new DefaultErrorAction<TestState, TestEvent, TestTransitionKey, TestContext>();
    assertThrows(FsmException.class, () -> defaultErrorAction.call(new FsmException(
        TestState.STARTED,
        TestEvent.INITIATE,
        null,
        null,
        null
    ), new TestContext()));
  }
}
