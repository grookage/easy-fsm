package com.grookage.fsm.core.stubs;

import com.grookage.fsm.core.StateMachine;
import com.grookage.fsm.core.hubs.TransitionProcessorHub;
import com.grookage.fsm.core.models.executors.ErrorAction;
import com.grookage.fsm.core.models.executors.EventAction;
import lombok.Builder;

public class TestMachine extends StateMachine<TestState, TestEvent, TestTransitionKey, TestContext> {

  @Builder
  public TestMachine(TestState startState,
      TransitionProcessorHub<TestState, TestEvent, TestTransitionKey, TestContext> transitionProcessorHub,
      ErrorAction<TestEvent, TestState, TestTransitionKey, TestContext> errorAction,
      EventAction<TestEvent, TestState, TestTransitionKey, TestContext> eventAction
  ) {
    super(startState, transitionProcessorHub, errorAction, eventAction);
  }
}
