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

import com.grookage.fsm.exceptions.InvalidStateException;
import com.grookage.fsm.exceptions.RunningtimeException;
import com.grookage.fsm.exceptions.StateNotFoundException;
import com.grookage.fsm.models.entities.*;
import com.grookage.fsm.models.executors.ErrorAction;
import com.grookage.fsm.models.executors.EventAction;
import com.grookage.fsm.services.ActionService;
import com.grookage.fsm.services.StateManagementService;
import com.grookage.fsm.services.TransitionService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Entity by : koushikr.
 * on 23/10/15.
 */
@SuppressWarnings("ALL")
@Slf4j
public class StateMachine<C extends Context> {

    public static class DefaultErrorAction implements ErrorAction<Context> {
        @Override
        public void call(RunningtimeException error, Context context) {
            String errorMessage = "Runtime Error in state [" + error.getState() + "]";
            if(!Objects.isNull(error.getEvent())) errorMessage += "on Event [" + error.getEvent() + "]";
            errorMessage += "with context [" + error.getContext() + "]";
            log.error("ERROR", new Exception(errorMessage, error));
        }
    }

    private final TransitionService transitionService;
    private final StateManagementService stateManagementService;
    private final ActionService<C> actionService;

    public StateMachine(final State startState,
                        final TransitionService transitionService,
                        final StateManagementService stateManagementService,
                        final ActionService<C> actionService
    ){
        this.transitionService = transitionService;
        this.stateManagementService = stateManagementService;
        this.actionService = actionService;
        this.stateManagementService.setFrom(startState);
        this.actionService.setHandler(EventType.ERROR, null, null, new DefaultErrorAction());
    }

    public void addTransition(final State key, final Transition transition){
        transitionService.addTransition(key, transition);
    }

    public void addEndStates(final Collection<State> endStates){
        stateManagementService.addEndStates(endStates);
    }

    public StateMachine<C> addError(final ErrorAction<C> errorHandler) {
        this.actionService.setHandler(EventType.ERROR, null, null, errorHandler);
        return this;
    }

    public StateMachine<C> beforeTransition(final EventAction<C> before) {
        actionService.beforeTransition(null, before);
        return this;
    }

    public StateMachine<C> afterTransition(final EventAction<C> after) {
        actionService.afterTransition(null, after);
        return this;
    }

    public StateMachine<C> beforeTransitionTo(final State state,final EventAction<C> before) {
        actionService.beforeTransition(state, before);
        return this;
    }

    public StateMachine<C> afterTransitionFrom(final State state, final EventAction<C> after) {
        actionService.afterTransition(state, after);
        return this;
    }

    public StateMachine<C> anyTransition(final EventAction<C> transition) {
        actionService.anyTransition(transition);
        return this;
    }

    public StateMachine<C> forTransition(final State state, final EventAction<C> context) {
        actionService.forTransition(null, state, context);
        return this;
    }

    public StateMachine<C> forTransition(final Event event, final State state, final EventAction<C> context) {
        actionService.forTransition(event, state, context);
        return this;
    }

    public StateMachine<C> onFinalState(final State state, final EventAction<C> context) {
        actionService.onFinalState(state, context);
        return this;
    }

    private void handleStateTransition(final Event event, final State from, final C context) {
        actionService.handleTransition(event, from, context);
    }

    private void handleLanding(final State from, final C context) {
        actionService.handleLanding(from, context);
    }

    private void handleTakeOff(final State to, final C context) {
        actionService.handleTakeOff(to, context);
    }

    public Optional<Transition> getTransition(final State from, final Event event) {
        return transitionService.getTransition(from, event);
    }

    public void handleError(RunningtimeException error){
        actionService.handleError(error);
    }

    @SneakyThrows
    public void fire(final Event event, final C context) {
        final var from = context.getFrom();
        final var transition = getTransition(from, event);
        if(transition.isEmpty()) throw new StateNotFoundException("Invalid Event: " + event + " triggered while in State: " + context.getFrom() + " for " + context);
        try{
            var to = transition.get().getTo();
            handleTakeOff(to, context);
            handleStateTransition(event, from, context);
            handleLanding(from, context);
        }catch (Exception e){
            handleError(new RunningtimeException(from, event, e, e.getMessage(), context));
        }
    }

    /**
     * A stateMachine is said to be valid iff it meets the following conditions
     * <ul>
     *     <li>It should have a valid start state and a nonempty set of end states. It has to be halting</li>
     *     <li>For all the states defined, make sure there are transitions from each one of 'em except for the end state</li>
     *     <li>Make sure there are no transitions defined from the endstate</li>
     * </ul>
     */
    public void validate() throws InvalidStateException {
        if(Objects.isNull(stateManagementService.getFrom())) throw new InvalidStateException("No start state found");
        if(stateManagementService.getEndStates().isEmpty()) throw new InvalidStateException("No end states found");

        var allStates = stateManagementService.getReferenceStates();
        var map = transitionService.getTransitionDetails();
        map.keySet().forEach(state -> {
            allStates.add(state);
            map.get(state).forEach(transition -> {
                allStates.add(transition.getFrom());
                allStates.add(transition.getTo());
            });
        });

        for(State state: allStates){
            Set<Transition> transitions = (Set<Transition>) map.get(state);
            if(isNullOrEmpty(transitions)){
                if(!stateManagementService.getEndStates().contains(state)){
                    throw new InvalidStateException("state :"+ state +" is not an end state but"
                            + " has no outgoing transitions");
                } else if(stateManagementService.getEndStates().contains(state) && null != transitions && !transitions.isEmpty()){
                        throw new InvalidStateException("state :"+ state +" is an end state"
                                + " and cannot have any out going transition");
                    }
                }
        }
    }

    private boolean isNullOrEmpty(Collection<?> clx){
        return Objects.isNull(clx) || clx.isEmpty();
    }
}