package com.grookage.fsm.core.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.grookage.fsm.core.stubs.TestState;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StateManagementServiceTest {

  @Test
  void testStateManagementService(){
    final var stateManagementService = new StateManagementService<TestState>();
    stateManagementService.setFrom(TestState.STARTED);
    stateManagementService.addEndStates(Set.of(TestState.COMPLETED, TestState.FAILED));

    assertFalse(stateManagementService.getEndStates().isEmpty());
    final var allStates = stateManagementService.allStates();
    assertTrue(allStates.contains(TestState.STARTED) && allStates.contains(TestState.COMPLETED) & allStates.contains(TestState.FAILED));
    assertFalse(allStates.contains(TestState.CREATED));
  }
}
