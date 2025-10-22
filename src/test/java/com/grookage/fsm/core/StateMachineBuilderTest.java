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
import com.grookage.fsm.core.exceptions.FsmException;
import com.grookage.fsm.core.helpers.ResourceHelper;
import com.grookage.fsm.core.stubs.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StateMachineBuilderTest {

	@Test
	@SneakyThrows
	void testValidStateMachineBuilder() {
		final var machineBuilderConfig = ResourceHelper.getResource("stateMachine.json", new TypeReference<MachineBuilderConfig<TestState, TestEvent>>() {
		});
		assertNotNull(machineBuilderConfig);
		final var stateMachine = new StateMachineBuilder<TestState, TestEvent, TestTransitionKey, TestContext>()
				.withMachineBuilderConfig(machineBuilderConfig)
				.withTransitionProcessorHub(TestHub.builder().build())
				.build();
		assertNotNull(stateMachine);
	}

	@Test
	@SneakyThrows
	void testForInvalidStateMachine() {
		final var machineBuilderConfig = ResourceHelper.getResource("invalidMachine.json", new TypeReference<MachineBuilderConfig<TestState, TestEvent>>() {
		});
		assertThrows(FsmException.class, () -> new StateMachineBuilder<TestState, TestEvent, TestTransitionKey, TestContext>()
				.withMachineBuilderConfig(machineBuilderConfig)
				.withTransitionProcessorHub(TestHub.builder().build())
				.build());
	}

	@Test
	@SneakyThrows
	void testForInvalidMachineBuilderConfig() {
		final var machineBuilderConfig = new MachineBuilderConfig<TestState, TestEvent>();
		assertThrows(FsmException.class, () -> constructStateMachine(machineBuilderConfig));

		machineBuilderConfig.setName("name");
		assertThrows(FsmException.class, () -> constructStateMachine(machineBuilderConfig));

		machineBuilderConfig.setStartState(TestState.CREATED);
		assertThrows(FsmException.class, () -> constructStateMachine(machineBuilderConfig));

		machineBuilderConfig.setEndStates(Set.of(TestState.COMPLETED));
		assertThrows(FsmException.class, () -> constructStateMachine(machineBuilderConfig));
	}

	private StateMachine<TestState, TestEvent, TestTransitionKey, TestContext> constructStateMachine(
			final MachineBuilderConfig<TestState, TestEvent> machineBuilderConfig
	) {
		return new StateMachineBuilder<TestState, TestEvent, TestTransitionKey, TestContext>()
				.withMachineBuilderConfig(machineBuilderConfig)
				.withTransitionProcessorHub(TestHub.builder().build())
				.build();
	}
}
