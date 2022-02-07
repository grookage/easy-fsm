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
package com.grookage.fsm.services;

import com.grookage.fsm.models.entities.*;
import com.grookage.fsm.models.executors.Action;
import com.grookage.fsm.exceptions.RunningtimeException;
import com.grookage.fsm.models.executors.ErrorAction;
import com.grookage.fsm.models.executors.EventAction;
import io.grookage.fsm.models.entities.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Entity by : koushikr.
 * on 23/10/15.
 *
 */
@SuppressWarnings("ALL")
@Slf4j
public class ActionService<C extends Context> {

    private final Map<HandlerType, Action> handlers;

    public ActionService(){
        handlers = new HashMap();
    }

    public void anyTransition(EventAction<C> context) {
        handlers.put(new HandlerType(EventType.ANY_STATE_TRANSITION, null, null), context);
    }

    public void beforeTransition(State state, EventAction<C> context) {
        handlers.put(Objects.isNull(state) ?
                        new HandlerType(EventType.BEFORE_ANY_TRANSITION, null, state)
                        : new HandlerType(EventType.BEFORE_STATE_TRANSITION, null, state),
                context
        );
    }

    public void afterTransition(State state, EventAction<C> context) {
        handlers.put(Objects.isNull(state) ?
                        new HandlerType(EventType.AFTER_ANY_TRANSITION, null, state)
                        : new HandlerType(EventType.AFTER_STATE_TRANSITION, null, state),
                context
        );
    }

    public void forTransition(Event event, State state, EventAction<C> context) {
        handlers.put(new HandlerType(EventType.STATE_TRANSITION, event, state), context);
    }

    public void onFinalState(State state, EventAction<C> context) {
        handlers.put(new HandlerType(EventType.FINAL_STATE, null, state), context);
    }

    public void handleTransition(Event event, State from, State to, C context) {
        var handler = handlers.get(new HandlerType(EventType.STATE_TRANSITION, null, from));
        if(!Objects.isNull(handler)) ((EventAction<C>) handler).call(context);

        handler = handlers.get(new HandlerType(EventType.STATE_TRANSITION, event, from));
        if(!Objects.isNull(handler)) ((EventAction<C>) handler).call(context);

        handler = handlers.get(new HandlerType(EventType.ANY_STATE_TRANSITION, null, null));
        if(!Objects.isNull(handler)) ((EventAction<C>) handler).call(context);
    }

    public void handleLanding(Event event, State from, State to, C context) {
        var handler = handlers.get(new HandlerType(EventType.AFTER_STATE_TRANSITION, null, from));
        if(!Objects.isNull(handler)) ((EventAction<C>) handler).call(context);

        handler = handlers.get(new HandlerType(EventType.AFTER_ANY_TRANSITION, null, null));
        if(!Objects.isNull(handler)) ((EventAction<C>) handler).call(context);
    }

    public void handleTakeOff(Event event, State from, State to, C context, State initState) {
        var handler = handlers.get(new HandlerType(EventType.BEFORE_STATE_TRANSITION, null, to));
        if(!Objects.isNull(handler)) ((EventAction<C>) handler).call(context);

        handler = handlers.get(new HandlerType(EventType.BEFORE_ANY_TRANSITION, null, null));
        if(!Objects.isNull(handler)) ((EventAction<C>) handler).call(context);
    }

    public void handleError(RunningtimeException error) {
        var handler = handlers.get(new HandlerType(EventType.ERROR, null, null));
        if(!Objects.isNull(handler)) ((ErrorAction) handler).call(error, error.getContext());
    }

    public void setHandler(EventType eventType, State state, Event event, Action action) {
        handlers.put(new HandlerType(eventType, event, state), action);
    }
}
