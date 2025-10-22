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
package com.grookage.fsm.core.services;

import com.grookage.fsm.core.exceptions.FsmException;
import com.grookage.fsm.core.models.entities.Context;
import com.grookage.fsm.core.models.entities.Event;
import com.grookage.fsm.core.models.entities.EventType;
import com.grookage.fsm.core.models.entities.HandlerType;
import com.grookage.fsm.core.models.entities.State;
import com.grookage.fsm.core.models.entities.TransitionKey;
import com.grookage.fsm.core.models.executors.Action;
import com.grookage.fsm.core.models.executors.ErrorAction;
import com.grookage.fsm.core.models.executors.EventAction;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 * Entity by : koushikr. on 23/10/15.
 */
@Slf4j
@SuppressWarnings({"unchecked", "rawtypes"})
public class ActionService<E extends Event, S extends State, K extends TransitionKey, C extends Context<S, E, K>> {

  private final Map<HandlerType<E, S>, Action> handlers;

  public ActionService() {
    handlers = new HashMap<>();
  }

  public void anyTransition(EventAction<E, S, K, C> context) {
    handlers.put(new HandlerType<>(EventType.ANY_STATE_TRANSITION, null, null), context);
  }

  public void beforeTransition(S state, EventAction<E, S, K, C> context) {
    handlers.put(Objects.isNull(state) ?
            new HandlerType<>(EventType.BEFORE_ANY_TRANSITION, null, null)
            : new HandlerType<>(EventType.BEFORE_STATE_TRANSITION, null, state),
        context
    );
  }

  public void afterTransition(S state, EventAction<E, S, K, C> context) {
    handlers.put(Objects.isNull(state) ?
            new HandlerType<>(EventType.AFTER_ANY_TRANSITION, null, null)
            : new HandlerType<>(EventType.AFTER_STATE_TRANSITION, null, state),
        context
    );
  }

  public void forTransition(E event, S state, EventAction<E, S, K, C> context) {
    handlers.put(new HandlerType<>(EventType.STATE_TRANSITION, event, state), context);
  }

  public void onFinalState(S state, EventAction<E, S, K, C> context) {
    handlers.put(new HandlerType<>(EventType.FINAL_STATE, null, state), context);
  }

  public void handleTransition(E event, S from, C context) {
    var handler = handlers.get(new HandlerType(EventType.STATE_TRANSITION, null, from));
    if (!Objects.isNull(handler)) {
      ((EventAction<E, S, K, C>) handler).call(context);
    }

    handler = handlers.get(new HandlerType(EventType.STATE_TRANSITION, event, from));
    if (!Objects.isNull(handler)) {
      ((EventAction<E, S, K, C>) handler).call(context);
    }

    handler = handlers.get(new HandlerType(EventType.ANY_STATE_TRANSITION, null, null));
    if (!Objects.isNull(handler)) {
      ((EventAction<E, S, K, C>) handler).call(context);
    }
  }

  public void handleLanding(S from, C context) {
    var handler = handlers.get(new HandlerType(EventType.AFTER_STATE_TRANSITION, null, from));
    if (!Objects.isNull(handler)) {
      ((EventAction<E, S, K, C>) handler).call(context);
    }

    handler = handlers.get(new HandlerType(EventType.AFTER_ANY_TRANSITION, null, null));
    if (!Objects.isNull(handler)) {
      ((EventAction<E, S, K, C>) handler).call(context);
    }
  }

  public void handleTakeOff(S to, C context) {
    var handler = handlers.get(new HandlerType(EventType.BEFORE_STATE_TRANSITION, null, to));
    if (!Objects.isNull(handler)) {
      ((EventAction<E, S, K, C>) handler).call(context);
    }

    handler = handlers.get(new HandlerType(EventType.BEFORE_ANY_TRANSITION, null, null));
    if (!Objects.isNull(handler)) {
      ((EventAction<E, S, K, C>) handler).call(context);
    }
  }

  public void handleError(FsmException error) {
    var handler = handlers.get(new HandlerType(EventType.ERROR, null, null));
    if (!Objects.isNull(handler)) {
      ((ErrorAction) handler).call(error, error.getContext());
    }
  }

  public void setHandler(EventType eventType, S state, E event, Action action) {
    handlers.put(new HandlerType(eventType, event, state), action);
  }
}
