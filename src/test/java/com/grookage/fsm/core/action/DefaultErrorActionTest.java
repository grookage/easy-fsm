package com.grookage.fsm.core.action;

import com.grookage.fsm.core.exceptions.FsmException;
import com.grookage.fsm.core.stubs.TestContext;
import com.grookage.fsm.core.stubs.TestEvent;
import com.grookage.fsm.core.stubs.TestState;
import com.grookage.fsm.core.stubs.TestTransitionKey;
import org.junit.Test;

public class DefaultErrorActionTest {

  @Test(expected = FsmException.class)
  public void testDefaultErrorAction(){
    final var defaultErrorAction = new DefaultErrorAction<TestState, TestEvent, TestTransitionKey, TestContext>();
    defaultErrorAction.call(new FsmException(
        TestState.STARTED,
        TestEvent.INITIATE,
        null,
        null,
        null
    ), new TestContext());
  }
}
