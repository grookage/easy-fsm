package com.grookage.fsm.core.stubs;

import com.grookage.fsm.core.models.executors.EventAction;
import java.util.Map;
import lombok.Getter;

@Getter
public class TestAction implements EventAction<TestEvent, TestState, TestTransitionKey, TestContext> {

  @Override
  public void call(TestContext context) {
    context.addContext("action", Map.of("action", "testAction"));
  }
}
