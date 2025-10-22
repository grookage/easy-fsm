package com.grookage.fsm.core.stubs;

import com.grookage.fsm.core.models.executors.TransitionProcessor;
import java.util.Map;
import java.util.Set;

public class CompletedProcessor implements
    TransitionProcessor<TestState, TestEvent, TestTransitionKey, TestContext> {

  @Override
  public Set<TestTransitionKey> keys() {
    return Set.of();
  }

  @Override
  public void process(TestContext context) {
    context.addContext(TestState.COMPLETED.name(), Map.of("state", TestState.COMPLETED.name()));
  }
}