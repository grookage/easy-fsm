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
import com.grookage.fsm.core.helpers.ResourceHelper;
import com.grookage.fsm.core.stubs.TestEvent;
import com.grookage.fsm.core.stubs.TestHub;
import com.grookage.fsm.core.stubs.TestState;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class StateMachineRegistryTest {

    @Test
    public void testHub() throws Exception {
        final var machineBuilderConfig = ResourceHelper.getResource("stateMachine.json", new TypeReference<MachineBuilderConfig<TestState, TestEvent>>() {
        });
        final var machineBuilderConfig2 = ResourceHelper.getResource("stateMachine2.json", new TypeReference<MachineBuilderConfig<TestState, TestEvent>>() {
        });
        final var machineRegistry = new StateMachineRegistry()
                .withMachineBuilderConfigs(List.of(machineBuilderConfig2, machineBuilderConfig))
                .withHub(machineBuilderConfig.getName(), TestHub.builder().build())
                .withHub(machineBuilderConfig2.getName(), TestHub.builder().build())
                .build();
        Assert.assertNotNull(machineRegistry);
        Assert.assertTrue(machineRegistry.getMachine("testMachine").isPresent());
        Assert.assertTrue(machineRegistry.getMachine("testMachine2").isPresent());
        Assert.assertTrue(machineRegistry.getMachine("testMachine3").isEmpty());
    }
}
