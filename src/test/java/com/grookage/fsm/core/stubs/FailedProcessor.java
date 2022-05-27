package com.grookage.fsm.core.stubs;

import com.grookage.fsm.core.models.executors.TransitionProcessor;
import java.util.Map;

public class FailedProcessor implements
    TransitionProcessor<TestState, TestEvent, TestTransitionKey, TestContext> {

  @Override
  public void process(TestContext context) {
    context.addContext(TestState.FAILED.name(), Map.of("state", TestState.FAILED.name()));
  }
}
