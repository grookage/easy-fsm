package com.grookage.fsm.core.services;

import com.grookage.fsm.core.stubs.TestState;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class StateManagementServiceTest {

  @Test
  public void testStateManagementService(){
    final var stateManagementService = new StateManagementService<TestState>();
    stateManagementService.setFrom(TestState.STARTED);
    stateManagementService.addEndStates(Set.of(TestState.COMPLETED, TestState.FAILED));

    Assert.assertFalse(stateManagementService.getEndStates().isEmpty());
    final var allStates = stateManagementService.allStates();
    Assert.assertTrue(allStates.contains(TestState.STARTED) && allStates.contains(TestState.COMPLETED) & allStates.contains(TestState.FAILED));
    Assert.assertFalse(allStates.contains(TestState.CREATED));
  }
}
