package com.grookage.fsm.core.stubs;

import com.grookage.fsm.core.models.executors.EventAction;
import lombok.Getter;

import java.util.Map;

@Getter
public class TestAction implements EventAction<TestEvent, TestState, TestTransitionKey, TestContext> {

  @Override
  public void call(TestContext context) {
    context.addContext("action", Map.of("action", "testAction"));
  }
}
