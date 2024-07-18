/*
 * Copyright 2022 Koushik R <rkoushik.14@gmail.com>.
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.grookage.fsm.core.config.MachineBuilderConfig;
import com.grookage.fsm.core.exceptions.InvalidStateException;
import com.grookage.fsm.core.helpers.ResourceHelper;
import com.grookage.fsm.core.helpers.StateMachineHelper;
import com.grookage.fsm.core.stubs.*;
import org.junit.Assert;
import org.junit.Test;

public class StateMachineBuilderTest {

    @Test
    public void testValidStateMachineBuilder() throws Exception {
        final var machineBuilderConfig = ResourceHelper.getResource("stateMachine.json", new TypeReference<MachineBuilderConfig<TestState, TestEvent>>() {
        });
        Assert.assertNotNull(machineBuilderConfig);
        final var stateMachine = new StateMachineBuilder<TestState, TestEvent, TestTransitionKey, TestContext>()
                .withMachineBuilderConfig(machineBuilderConfig)
                .withTransitionProcessorHub(TestHub.builder().build())
                .build();
        Assert.assertNotNull(stateMachine);
    }

    @Test(expected = InvalidStateException.class)
    public void testForInvalidStateMachine() throws Exception {
        final var machineBuilderConfig = ResourceHelper.getResource("invalidMachine.json", new TypeReference<MachineBuilderConfig<TestState, TestEvent>>() {
        });
        new StateMachineBuilder<TestState, TestEvent, TestTransitionKey, TestContext>()
                .withMachineBuilderConfig(machineBuilderConfig)
                .withTransitionProcessorHub(TestHub.builder().build())
                .build();
    }
}
