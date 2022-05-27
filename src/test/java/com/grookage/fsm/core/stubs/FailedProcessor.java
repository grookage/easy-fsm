package com.grookage.fsm.core.stubs;

import com.grookage.fsm.core.models.executors.TransitionProcessor;
import java.util.Map;
import java.util.Set;

public class FailedProcessor implements
    TransitionProcessor<TestState, TestEvent, TestTransitionKey, TestContext> {

  @Override
  public Set<TestTransitionKey> keys() {
    return Set.of(TestTransitionKey.builder()
        .tag(TestState.FAILED.name())
        .build()
    );
  }

  @Override
  public void process(TestContext context) {
    context.addContext(TestState.FAILED.name(), Map.of("state", TestState.FAILED.name()));
  }
}
