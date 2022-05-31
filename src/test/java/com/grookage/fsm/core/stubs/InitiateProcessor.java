package com.grookage.fsm.core.stubs;

import com.grookage.fsm.core.models.executors.TransitionProcessor;
import java.util.Map;
import java.util.Set;

public class InitiateProcessor implements
    TransitionProcessor<TestState, TestEvent, TestTransitionKey, TestContext> {

  @Override
  public Set<TestTransitionKey> keys() {
    return Set.of();
  }

  @Override
  public void process(TestContext context) {
    context.addContext(TestState.CREATED.name(), Map.of("state", TestState.CREATED.name()));
  }
}
