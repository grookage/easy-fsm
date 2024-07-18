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

import com.google.common.base.Preconditions;
import com.grookage.fsm.core.config.MachineBuilderConfig;
import com.grookage.fsm.core.hubs.TransitionProcessorHub;
import com.grookage.fsm.core.models.entities.Context;
import com.grookage.fsm.core.models.entities.Event;
import com.grookage.fsm.core.models.entities.State;
import com.grookage.fsm.core.models.entities.TransitionKey;
import com.grookage.fsm.core.models.executors.ErrorAction;
import com.grookage.fsm.core.models.executors.EventAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StateMachineBuilder<S extends State, E extends Event, K extends TransitionKey, C extends Context<S, E, K>> {

    private MachineBuilderConfig<S, E> machineBuilderConfig;
    private TransitionProcessorHub<S, E, K, C> transitionProcessorHub;
    private ErrorAction<E, S, K, C> errorAction;
    private EventAction<E, S, K, C> eventAction;
    @Getter
    private StateMachine<S,E,K,C> stateMachine;

    public StateMachineBuilder<S, E, K, C> withMachineBuilderConfig(MachineBuilderConfig<S, E> machineBuilderConfig){
        this.machineBuilderConfig = machineBuilderConfig;
        return this;
    }

    public StateMachineBuilder<S, E, K, C> withTransitionProcessorHub(TransitionProcessorHub<S, E, K, C> transitionProcessorHub){
        this.transitionProcessorHub = transitionProcessorHub;
        return this;
    }

    public StateMachineBuilder<S, E, K, C> withEventAction(EventAction<E, S, K, C> eventAction){
        this.eventAction = eventAction;
        return this;
    }

    public StateMachineBuilder<S, E, K, C> withErrorAction(ErrorAction<E, S, K, C> errorAction) {
        this.errorAction = errorAction;
        return this;
    }

    public StateMachine<S,E,K,C> build(){
        Preconditions.checkNotNull(machineBuilderConfig, "Machine Builder Config can't be null");
        final var startState = machineBuilderConfig.getStartState();
        final var endStates = machineBuilderConfig.getEndStates();
        this.stateMachine = new StateMachine<>(machineBuilderConfig.getName(),
                startState, transitionProcessorHub, errorAction, eventAction);
        final var transitionConfigs = machineBuilderConfig.getTransitionConfigs();
        transitionConfigs.forEach(transitionConfig ->
                stateMachine.onTransition(transitionConfig.getCausedEvent(), transitionConfig.getFrom(), transitionConfig.getTo()));
        this.stateMachine.end(endStates);
        this.stateMachine.start();
        return stateMachine;
    }
}
