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
package com.grookage.fsm;

import com.grookage.fsm.models.entities.Context;
import com.grookage.fsm.models.entities.Event;
import com.grookage.fsm.models.entities.State;
import com.grookage.fsm.models.entities.Transition;
import com.grookage.fsm.models.executors.ErrorAction;
import com.grookage.fsm.services.ActionService;
import com.grookage.fsm.services.StateManagementService;
import com.grookage.fsm.services.TransitionService;

import java.util.Collection;

/**
 * Entity by : koushikr.
 * on 23/10/15.
 */
@SuppressWarnings({"unused"})
public class MachineBuilder<C extends Context> {

    private final StateMachine<C> stateMachine;

    private void addTransition(final Event event, final State from, final State to){
        stateMachine.addTransition(from, new Transition(event, from, to));
    }

    protected MachineBuilder(final State startState,
                             final TransitionService transitionService,
                             final StateManagementService stateManagementService,
                             final ActionService<C> actionService
    ){
        stateMachine = new StateMachine<>(startState, transitionService, stateManagementService, actionService);
    }

    public static <C extends Context> MachineBuilder<C> start(final State startState){
        return new MachineBuilder<>(startState, new TransitionService(), new StateManagementService(), new ActionService<>());
    }

    public MachineBuilder<C> onTransition(final Event event, final Collection<State> fromStates, final State to){
        fromStates.forEach(state -> addTransition(event, state, to));
        return this;
    }

    public MachineBuilder<C> onError(final ErrorAction<C> eventAction) {
        stateMachine.addError(eventAction);
        return this;
    }

    public MachineBuilder<C> onTransition(final Event event, final State from, final State to){
        addTransition(event, from, to);
        return this;
    }

    public StateMachine<C> end(final Collection<State> endStates){
        stateMachine.addEndStates(endStates);
        return stateMachine;
    }
}
