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
import com.grookage.fsm.core.models.executors.ErrorAction;
import com.grookage.fsm.core.models.executors.EventAction;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"rawtypes", "unchecked"})
@NoArgsConstructor
public class StateMachineRegistry {

    private Map<String, StateMachine> machineRegistry = new ConcurrentHashMap<>();
    private List<MachineBuilderConfig> machineBuilderConfigs = new ArrayList<>();
    private Map<String, ErrorAction> errorRegistry = new ConcurrentHashMap<>();
    private Map<String, EventAction> eventRegistry = new ConcurrentHashMap<>();

    private Map<String, TransitionProcessorHub> hubs = new ConcurrentHashMap<>();

    public Optional<StateMachine> getMachine(final String name) {
        return Optional.ofNullable(machineRegistry.get(name.toUpperCase(Locale.ROOT)));
    }

    public StateMachineRegistry withMachineBuilderConfigs(List<MachineBuilderConfig> machineBuilderConfigs) {
        this.machineBuilderConfigs = machineBuilderConfigs;
        return this;
    }

    public StateMachineRegistry withHub(final String machineName, TransitionProcessorHub transitionProcessorHub){
        hubs.putIfAbsent(machineName.toUpperCase(Locale.ROOT), transitionProcessorHub);
        return this;
    }

    public StateMachineRegistry withEventAction(final String machineName, EventAction eventAction){
        eventRegistry.putIfAbsent(machineName.toUpperCase(Locale.ROOT), eventAction);
        return this;
    }

    public StateMachineRegistry withErrorAction(final String machineName, ErrorAction errorAction) {
        errorRegistry.putIfAbsent(machineName.toUpperCase(Locale.ROOT), errorAction);
        return this;
    }

    public StateMachineRegistry build() {
        Preconditions.checkArgument(null != machineBuilderConfigs && !machineBuilderConfigs.isEmpty(),
                "Machine Builder Configs can't be null or empty");
        machineBuilderConfigs.forEach(machineBuilderConfig -> {
            final var stateMachine = new StateMachineBuilder<>()
                    .withMachineBuilderConfig(machineBuilderConfig)
                    .withTransitionProcessorHub(hubs.get(machineBuilderConfig.getName()))
                    .withErrorAction(errorRegistry.get(machineBuilderConfig.getName()))
                    .withEventAction(eventRegistry.get(machineBuilderConfig.getName()))
                    .build();
            machineRegistry.putIfAbsent(machineBuilderConfig.getName().toUpperCase(Locale.ROOT), stateMachine);
        });
        return this;
    }
}
