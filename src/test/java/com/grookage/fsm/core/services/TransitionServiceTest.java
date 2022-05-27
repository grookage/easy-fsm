package com.grookage.fsm.core.services;

import com.grookage.fsm.core.models.entities.Transition;
import com.grookage.fsm.core.stubs.TestEvent;
import com.grookage.fsm.core.stubs.TestState;
import org.junit.Assert;
import org.junit.Test;

public class TransitionServiceTest {

  @Test
  public void testTransitionService(){
    final var transitionService = new TransitionService<TestEvent, TestState>();
    transitionService.addTransition(TestState.STARTED, new Transition<>(TestEvent.INITIATE, TestState.STARTED, TestState.CREATED));

    final var transitionDetails = transitionService.getTransitionDetails();
    Assert.assertFalse(transitionDetails.isEmpty());

    final var transition = transitionService.getTransition(TestState.STARTED, TestEvent.INITIATE).orElse(null);
    Assert.assertNotNull(transition);
  }
}
