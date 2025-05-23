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

import com.grookage.fsm.core.exceptions.FsmException;
import com.grookage.fsm.core.exceptions.InvalidStateException;
import com.grookage.fsm.core.helpers.StateMachineHelper;
import com.grookage.fsm.core.models.executors.ErrorAction;
import com.grookage.fsm.core.stubs.TestContext;
import com.grookage.fsm.core.stubs.TestEvent;
import com.grookage.fsm.core.stubs.TestState;
import java.util.concurrent.atomic.AtomicBoolean;

import com.grookage.fsm.core.stubs.TestTransitionKey;
import org.junit.Assert;
import org.junit.Test;

/**
 * Entity by : koushikr. on 26/10/15.
 */
public class StateMachineTest {

  @Test
  public void testForValidStateMachine() throws InvalidStateException {
    final var stateMachineCore = StateMachineHelper.getValidStateMachine();
    stateMachineCore.getStateEngine().validate();
  }

  @Test(expected = InvalidStateException.class)
  public void testForInvalidStateMachine() throws InvalidStateException {
    final var stateMachineCore = StateMachineHelper.getInvalidStateMachine();
    stateMachineCore.getStateEngine().validate();
  }

  @Test
  public void testAnyEvent() {
    final var testContext = new TestContext();
    testContext.setFrom(TestState.STARTED);
    testContext.setTo(TestState.CREATED);
    testContext.setCausedEvent(TestEvent.INITIATE);
    final var stateMachineCore = StateMachineHelper.getValidStateMachine();
    stateMachineCore.getStateEngine().anyTransition(
        context -> Assert.assertSame(TestState.STARTED, context.getFrom()));
    stateMachineCore.getStateEngine().fire(TestEvent.INITIATE, testContext);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTransitionOnAnyEvent() {
    final var testContext = new TestContext();
    testContext.setFrom(TestState.CREATED);
    testContext.setTo(TestState.CREATED);
    testContext.setCausedEvent(TestEvent.INITIATE);
    final var stateMachineCore = StateMachineHelper.getValidStateMachine();
    stateMachineCore.getStateEngine().anyTransition(
        context -> Assert.assertSame(TestState.STARTED, context.getFrom()));
    stateMachineCore.fire(testContext);
  }

  @Test
  public void testInvalidTransitionOnAnyEventFireGrace() {
    final var testContext = new TestContext();
    testContext.setFrom(TestState.CREATED);
    testContext.setTo(TestState.CREATED);
    testContext.setCausedEvent(TestEvent.INITIATE);
    final var stateMachineCore = StateMachineHelper.getValidStateMachine();
    stateMachineCore.getStateEngine().anyTransition(
        context -> Assert.assertSame(TestState.STARTED, context.getFrom()));
    stateMachineCore.fireGrace(testContext);
  }

  @Test
  public void testForTransition() {
    final var testContext = new TestContext();
    testContext.setFrom(TestState.STARTED);
    testContext.setTo(TestState.CREATED);
    testContext.setCausedEvent(TestEvent.INITIATE);
    final var stateMachineCore = StateMachineHelper.getValidStateMachine();
    stateMachineCore.getStateEngine().anyTransition(
        context -> Assert.assertSame(TestState.STARTED, context.getFrom()));
    stateMachineCore.getStateEngine().fire(TestEvent.INITIATE, testContext);
  }

  @Test
  public void testStateMachine_onBeforeAnyTransition() {
    final var stateMachine = StateMachineHelper.getValidStateMachine();
    final var actionExecuted = new AtomicBoolean(false);
    stateMachine.onBeforeAnyTransition(context -> actionExecuted.set(true));
    stateMachine.start();

    final var testContext = new TestContext();
    testContext.setFrom(TestState.STARTED);
    testContext.setCausedEvent(TestEvent.INITIATE);
    testContext.setTo(TestState.IN_PROGRESS);
    stateMachine.fire(testContext);
    Assert.assertTrue("onBeforeAnyTransition action should be executed", actionExecuted.get());
  }

  @Test
  public void testStateMachine_onAfterAnyTransition() {
    final var stateMachine = StateMachineHelper.getValidStateMachine();
    final var actionExecuted = new AtomicBoolean(false);
    stateMachine.onAfterAnyTransition(context -> actionExecuted.set(true));
    stateMachine.start();

    final var testContext = new TestContext();
    testContext.setFrom(TestState.STARTED);
    testContext.setCausedEvent(TestEvent.INITIATE);
    testContext.setTo(TestState.IN_PROGRESS);
    stateMachine.fire(testContext);
    Assert.assertTrue("onAfterAnyTransition action should be executed", actionExecuted.get());
  }

  @Test
  public void testStateMachine_onBeforeStateTransition_ToState() {
    final var stateMachine = StateMachineHelper.getValidStateMachine();
    final var correctActionExecuted = new AtomicBoolean(false);
    final var wrongActionExecuted = new AtomicBoolean(false);

    stateMachine.onBeforeStateTransition(TestState.CREATED, context -> correctActionExecuted.set(true));
    stateMachine.onBeforeStateTransition(TestState.IN_PROGRESS, context -> wrongActionExecuted.set(true));
    stateMachine.start();

    final var testContext = new TestContext();
    testContext.setFrom(TestState.STARTED);
    testContext.setCausedEvent(TestEvent.INITIATE);
    testContext.setTo(TestState.CREATED);
    stateMachine.fire(testContext);

    Assert.assertTrue("onBeforeStateTransition for CREATED state should be executed", correctActionExecuted.get());
    Assert.assertFalse("onBeforeStateTransition for IN_PROGRESS state should NOT be executed", wrongActionExecuted.get());
  }

  @Test
  public void testStateMachine_onAfterStateTransition_FromState() {
    final var stateMachine = StateMachineHelper.getValidStateMachine();
    final var correctActionExecuted = new AtomicBoolean(false);
    final var wrongActionExecuted = new AtomicBoolean(false);

    stateMachine.onAfterStateTransition(TestState.STARTED, context -> correctActionExecuted.set(true));
    stateMachine.onAfterStateTransition(TestState.CREATED, context -> wrongActionExecuted.set(true));
    stateMachine.start();

    final var testContext = new TestContext();
    testContext.setFrom(TestState.STARTED);
    testContext.setCausedEvent(TestEvent.INITIATE);
    testContext.setTo(TestState.IN_PROGRESS);
    stateMachine.fire(testContext);

    Assert.assertTrue("onAfterStateTransition for STARTED state should be executed", correctActionExecuted.get());
    Assert.assertFalse("onAfterStateTransition for CREATED state should NOT be executed", wrongActionExecuted.get());
  }

  @Test
  public void testStateMachine_onStateTransition_FromState() {
    final var stateMachine = StateMachineHelper.getValidStateMachine();
    final var actionExecuted = new AtomicBoolean(false);
    stateMachine.onStateTransition(TestState.STARTED, context -> actionExecuted.set(true));
    stateMachine.start();

    final var testContext = new TestContext();
    testContext.setFrom(TestState.STARTED);
    testContext.setCausedEvent(TestEvent.INITIATE);
    testContext.setTo(TestState.IN_PROGRESS);
    stateMachine.fire(testContext);
    Assert.assertTrue("onStateTransition(fromState) action should be executed", actionExecuted.get());
  }

  @Test
  public void testStateMachine_onStateTransition_Event_FromState() {
    final var stateMachine = StateMachineHelper.getValidStateMachine();
    final var correctActionExecuted = new AtomicBoolean(false);
    final var wrongActionExecuted = new AtomicBoolean(false);

    stateMachine.onStateTransition(TestEvent.INITIATE, TestState.STARTED, context -> correctActionExecuted.set(true));
    stateMachine.onStateTransition(TestEvent.MOVE_TO_PROGRESS, TestState.STARTED, context -> wrongActionExecuted.set(true));
    stateMachine.start();

    final var testContext = new TestContext();
    testContext.setFrom(TestState.STARTED);
    testContext.setCausedEvent(TestEvent.INITIATE);
    testContext.setTo(TestState.FAILED);
    stateMachine.fire(testContext);

    Assert.assertTrue("Correct onStateTransition(event, fromState) action should be executed", correctActionExecuted.get());
    Assert.assertFalse("Wrong onStateTransition(event, fromState) action should NOT be executed", wrongActionExecuted.get());
  }

  @Test
  public void testStateMachine_onFinalStateReached() {
    final var stateMachine = StateMachineHelper.getValidStateMachine();
    final var actionExecuted = new AtomicBoolean(false);
    stateMachine.onFinalStateReached(TestState.COMPLETED, context -> actionExecuted.set(true));
    stateMachine.start();


    TestContext testContext1 = new TestContext();
    testContext1.setFrom(TestState.STARTED);
    testContext1.setCausedEvent(TestEvent.INITIATE);
    testContext1.setTo(TestState.CREATED);
    stateMachine.fire(testContext1);

    TestContext testContext2 = new TestContext();
    testContext2.setFrom(TestState.CREATED);
    testContext2.setCausedEvent(TestEvent.MOVE_TO_PROGRESS);
    testContext2.setTo(TestState.IN_PROGRESS);
    stateMachine.fire(testContext2);

    TestContext finalContext = new TestContext();
    finalContext.setFrom(TestState.IN_PROGRESS);
    finalContext.setCausedEvent(TestEvent.MOVE_TO_COMPLETED);
    finalContext.setTo(TestState.COMPLETED);
    stateMachine.fire(finalContext);

    Assert.assertTrue("onFinalStateReached action should be executed", actionExecuted.get());
  }

}
