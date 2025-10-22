/*
 * Copyright 2015 Koushik R <rkoushik.14@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.grookage.fsm.core;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.grookage.fsm.core.exceptions.InvalidStateException;
import com.grookage.fsm.core.helpers.StateMachineHelper;
import com.grookage.fsm.core.stubs.TestContext;
import com.grookage.fsm.core.stubs.TestEvent;
import com.grookage.fsm.core.stubs.TestState;
import org.junit.jupiter.api.Test;

/**
 * Entity by : koushikr. on 26/10/15.
 */
class StateMachineTest {

  @Test
  void testForValidStateMachine() throws InvalidStateException {
    final var stateMachineCore = StateMachineHelper.getValidStateMachine();
    stateMachineCore.getStateEngine().validate();
  }

  @Test
  void testForInvalidStateMachine(){
    final var stateMachineCore = StateMachineHelper.getInvalidStateMachine();
    assertThrows(InvalidStateException.class, () -> stateMachineCore.getStateEngine().validate());
  }

  @Test
  void testAnyEvent() {
    final var testContext = new TestContext();
    testContext.setFrom(TestState.STARTED);
    testContext.setTo(TestState.CREATED);
    testContext.setCausedEvent(TestEvent.INITIATE);
    final var stateMachineCore = StateMachineHelper.getValidStateMachine();
    stateMachineCore.getStateEngine().anyTransition(
        context -> assertSame(TestState.STARTED, context.getFrom()));
    stateMachineCore.getStateEngine().fire(TestEvent.INITIATE, testContext);
  }

  @Test
  void testInvalidTransitionOnAnyEvent() {
    assertThrows(IllegalArgumentException.class, () -> {
      final var testContext = new TestContext();
      testContext.setFrom(TestState.CREATED);
      testContext.setTo(TestState.CREATED);
      testContext.setCausedEvent(TestEvent.INITIATE);
      final var stateMachineCore = StateMachineHelper.getValidStateMachine();
      stateMachineCore.getStateEngine().anyTransition(
          context -> assertSame(TestState.STARTED, context.getFrom()));
      stateMachineCore.fire(testContext);
    });
  }

  @Test
  void testInvalidTransitionOnAnyEventFireGrace() {
    final var testContext = new TestContext();
    testContext.setFrom(TestState.CREATED);
    testContext.setTo(TestState.CREATED);
    testContext.setCausedEvent(TestEvent.INITIATE);
    final var stateMachineCore = StateMachineHelper.getValidStateMachine();
    stateMachineCore.getStateEngine().anyTransition(
        context -> assertSame(TestState.STARTED, context.getFrom()));
    stateMachineCore.fireGrace(testContext);
  }

  @Test
  void testForTransition() {
    final var testContext = new TestContext();
    testContext.setFrom(TestState.STARTED);
    testContext.setTo(TestState.CREATED);
    testContext.setCausedEvent(TestEvent.INITIATE);
    final var stateMachineCore = StateMachineHelper.getValidStateMachine();
    stateMachineCore.getStateEngine().anyTransition(
        context -> assertSame(TestState.STARTED, context.getFrom()));
    stateMachineCore.getStateEngine().fire(TestEvent.INITIATE, testContext);
  }
}
