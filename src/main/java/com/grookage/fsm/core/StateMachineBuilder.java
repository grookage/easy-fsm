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

import com.grookage.fsm.core.config.MachineBuilderConfig;
import com.grookage.fsm.core.exceptions.FsmErrorCode;
import com.grookage.fsm.core.exceptions.FsmException;
import com.grookage.fsm.core.hubs.TransitionProcessorHub;
import com.grookage.fsm.core.models.entities.Context;
import com.grookage.fsm.core.models.entities.Event;
import com.grookage.fsm.core.models.entities.State;
import com.grookage.fsm.core.models.entities.TransitionKey;
import com.grookage.fsm.core.models.executors.ErrorAction;
import com.grookage.fsm.core.models.executors.EventAction;
import com.grookage.fsm.core.utils.FsmUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class StateMachineBuilder<S extends State, E extends Event, K extends TransitionKey, C extends Context<S, E, K>> {

	private MachineBuilderConfig<S, E> machineBuilderConfig;
	private TransitionProcessorHub<S, E, K, C> transitionProcessorHub;
	private ErrorAction errorAction;
	private EventAction<E, S, K, C> eventAction;
	@Getter
	private StateMachine<S, E, K, C> stateMachine;

	public StateMachineBuilder<S, E, K, C> withMachineBuilderConfig(MachineBuilderConfig<S, E> machineBuilderConfig) {
		this.machineBuilderConfig = machineBuilderConfig;
		return this;
	}

	public StateMachineBuilder<S, E, K, C> withTransitionProcessorHub(TransitionProcessorHub<S, E, K, C> transitionProcessorHub) {
		this.transitionProcessorHub = transitionProcessorHub;
		return this;
	}

	public StateMachineBuilder<S, E, K, C> withEventAction(EventAction<E, S, K, C> eventAction) {
		this.eventAction = eventAction;
		return this;
	}

	public StateMachineBuilder<S, E, K, C> withErrorAction(ErrorAction errorAction) {
		this.errorAction = errorAction;
		return this;
	}

	public StateMachine<S, E, K, C> build() {
		validateMachineBuilderConfig();
		this.stateMachine = new StateMachine<>(machineBuilderConfig.getName(),
				machineBuilderConfig.getStartState(), transitionProcessorHub, errorAction, eventAction);
		machineBuilderConfig.getTransitionConfigs().forEach(transitionConfig ->
				stateMachine.onTransition(transitionConfig.getCausedEvent(), transitionConfig.getFrom(), transitionConfig.getTo()));
		this.stateMachine.end(machineBuilderConfig.getEndStates());
		this.stateMachine.start();
		return stateMachine;
	}

	private void validateMachineBuilderConfig() {
		if (null == machineBuilderConfig) {
			throw FsmException.error(FsmErrorCode.INVALID_MACHINE_BUILDER_CONFIG,
					Map.of(FsmUtils.errorString(), "Machine Builder Config can't be null"));
		}

		final var startState = machineBuilderConfig.getStartState();
		if (null == startState) {
			throw FsmException.error(FsmErrorCode.INVALID_MACHINE_BUILDER_CONFIG,
					Map.of(FsmUtils.errorString(), "Start State can't be null"));
		}

		final var endStates = machineBuilderConfig.getEndStates();
		if (FsmUtils.isNullOrEmpty(endStates)) {
			throw FsmException.error(FsmErrorCode.INVALID_MACHINE_BUILDER_CONFIG,
					Map.of(FsmUtils.errorString(), "End States can't be null or empty"));
		}

		final var transitionConfigs = machineBuilderConfig.getTransitionConfigs();
		if (FsmUtils.isNullOrEmpty(transitionConfigs)) {
			throw FsmException.error(FsmErrorCode.INVALID_MACHINE_BUILDER_CONFIG,
					Map.of(FsmUtils.errorString(), "Transition Configs can't be null or empty"));
		}
	}
}
