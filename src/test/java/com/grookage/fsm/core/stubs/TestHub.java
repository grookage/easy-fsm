package com.grookage.fsm.core.stubs;

import com.grookage.fsm.core.hubs.TransitionProcessorHub;
import com.grookage.fsm.core.models.executors.TransitionProcessor;
import lombok.Builder;

@Builder
public class TestHub implements
    TransitionProcessorHub<TestState, TestEvent, TestTransitionKey, TestContext> {

  @Override
  public TransitionProcessor<TestState, TestEvent, TestTransitionKey, TestContext> getProcessor(TestContext testContext) {
    final var transitionKey = testContext.getTransitionKey();
    if(transitionKey.getTag().equalsIgnoreCase(TestEvent.INITIATE.name())){
      return new InitiateProcessor();
    }else if(transitionKey.getTag().equalsIgnoreCase(TestEvent.MOVE_TO_PROGRESS.name())){
      return new InProgressProcessor();
    }else if(transitionKey.getTag().equalsIgnoreCase(TestEvent.MOVE_TO_FAILED.name())){
      return new FailedProcessor();
    }else return new CompletedProcessor();
  }
}
