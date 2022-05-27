package com.grookage.fsm.core.stubs;

import com.grookage.fsm.core.models.executors.TransitionProcessor;
import java.util.Map;

public class InProgressProcessor implements
    TransitionProcessor<TestState, TestEvent, TestTransitionKey, TestContext> {

  @Override
  public void process(TestContext context) {
    context.addContext(TestState.IN_PROGRESS.name(), Map.of("state", TestState.IN_PROGRESS.name()));
  }
}
