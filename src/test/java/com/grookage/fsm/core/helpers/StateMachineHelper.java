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
package com.grookage.fsm.core.helpers;

import com.google.common.collect.Sets;
import com.grookage.fsm.core.StateMachine;
import com.grookage.fsm.core.engine.StateEngine;
import com.grookage.fsm.core.stubs.TestContext;
import com.grookage.fsm.core.stubs.TestEvent;
import com.grookage.fsm.core.stubs.TestHub;
import com.grookage.fsm.core.stubs.TestMachine;
import com.grookage.fsm.core.stubs.TestState;
import com.grookage.fsm.core.stubs.TestTransitionKey;
import lombok.experimental.UtilityClass;

/**
 * Entity by : koushikr. on 26/10/15.
 */
@UtilityClass
public class StateMachineHelper {

  public static StateMachine<TestState, TestEvent, TestTransitionKey, TestContext> getInvalidStateMachine() {
    final var stateMachine = TestMachine.builder().
        startState(TestState.STARTED)
        .transitionProcessorHub(TestHub.builder().build())
        .build();
    stateMachine
        .onTransition(TestEvent.INITIATE, TestState.STARTED, TestState.CREATED)
        .onTransition(TestEvent.MOVE_TO_COMPLETED, TestState.IN_PROGRESS, TestState.COMPLETED)
        .onTransition(TestEvent.MOVE_TO_FAILED,
            Sets.newHashSet(TestState.STARTED, TestState.IN_PROGRESS),
            TestState.FAILED)
        .end(Sets.newHashSet(TestState.COMPLETED, TestState.FAILED));
    return stateMachine;
  }

  public static StateMachine<TestState, TestEvent, TestTransitionKey, TestContext> getValidStateMachine() {
    final var stateMachine = TestMachine.builder()
        .startState(TestState.STARTED)
        .transitionProcessorHub(TestHub.builder().build())
        .build();
    stateMachine
        .onTransition(TestEvent.INITIATE, TestState.STARTED, TestState.CREATED)
        .onTransition(TestEvent.MOVE_TO_PROGRESS, TestState.CREATED, TestState.IN_PROGRESS)
        .onTransition(TestEvent.MOVE_TO_COMPLETED, TestState.IN_PROGRESS, TestState.COMPLETED)
        .onTransition(TestEvent.MOVE_TO_FAILED,
            Sets.newHashSet(TestState.STARTED, TestState.CREATED, TestState.IN_PROGRESS),
            TestState.FAILED)
        .end(Sets.newHashSet(TestState.COMPLETED, TestState.FAILED));
    return stateMachine;
  }
}
