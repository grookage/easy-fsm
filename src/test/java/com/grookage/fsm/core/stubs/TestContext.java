package com.grookage.fsm.core.stubs;

import com.grookage.fsm.core.models.entities.Context;

public class TestContext extends Context<TestState, TestEvent, TestTransitionKey> {

  @Override
  public TestTransitionKey getTransitionKey() {
    return TestTransitionKey.builder()
        .tag(this.getTo().name())
        .build();
  }
}
