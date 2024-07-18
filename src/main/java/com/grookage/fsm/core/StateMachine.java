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
import com.grookage.fsm.core.action.DefaultErrorAction;
import com.grookage.fsm.core.engine.StateEngine;
import com.grookage.fsm.core.hubs.TransitionProcessorHub;
import com.grookage.fsm.core.models.entities.Context;
import com.grookage.fsm.core.models.entities.Event;
import com.grookage.fsm.core.models.entities.State;
import com.grookage.fsm.core.models.entities.Transition;
import com.grookage.fsm.core.models.entities.TransitionKey;
import com.grookage.fsm.core.models.executors.ErrorAction;
import com.grookage.fsm.core.models.executors.EventAction;
import com.grookage.fsm.core.services.ActionService;
import com.grookage.fsm.core.services.StateManagementService;
import com.grookage.fsm.core.services.TransitionService;
import java.util.Collection;
import java.util.Locale;

import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Getter
public class StateMachine<S extends State, E extends Event, K extends TransitionKey, C extends Context<S, E, K>> {

  private final String name;
  private final StateEngine<E, S, K, C> stateEngine;
  private final TransitionProcessorHub<S, E, K, C> transitionProcessorHub;
  private final ErrorAction<E, S, K, C> errorAction;
  private final EventAction<E, S, K, C> eventAction;

  public StateMachine(
      final String name,
      final S startState,
      final TransitionProcessorHub<S, E, K, C> transitionProcessorHub,
      final ErrorAction<E, S, K, C> errorAction,
      final EventAction<E, S, K, C> eventAction
  ) {
    this.name = name.toUpperCase(Locale.ROOT);
    this.stateEngine = new StateEngine<>(
        startState,
        new TransitionService<>(),
        new StateManagementService<>(),
        new ActionService<>()
    );
    this.transitionProcessorHub = transitionProcessorHub;
    this.errorAction = null != errorAction ? errorAction : new DefaultErrorAction<>();
    this.eventAction = null != eventAction ? eventAction : context -> {
      log.info("Processing fsm transition for event {} moving from {} to {}",
          context.getCausedEvent(), context.getFrom(), context.getTo());
      final var processor = transitionProcessorHub.getProcessor(context);
      if (null == processor) {
        log.info(
            "No fsm transition for event {} moving from {} to {} found. Gracefully ignoring",
            context.getCausedEvent(), context.getFrom(), context.getTo());
        return;
      }
      processor.process(context);
      log.info("Processed fsm transition for event {} moving from {} to {}",
          context.getCausedEvent(), context.getFrom(), context.getTo());
    };
  }

  private void addTransition(final E event, final S from, final S to) {
    stateEngine.addTransition(from, new Transition<>(event, from, to));
  }

  public StateMachine<S, E, K, C> onTransition(final E event, final Collection<S> fromStates,
      final S to) {
    fromStates.forEach(state -> addTransition(event, state, to));
    return this;
  }

  public StateMachine<S, E, K, C> onError(final ErrorAction<E, S, K, C> eventAction) {
    stateEngine.addError(eventAction);
    return this;
  }

  public StateMachine<S, E, K, C> onTransition(final E event, final S from, final S to) {
    addTransition(event, from, to);
    return this;
  }

  public StateMachine<S, E, K, C> end(final Collection<S> endStates) {
    stateEngine.addEndStates(endStates);
    return this;
  }

  @SneakyThrows
  public void start() {
    Preconditions.checkNotNull(stateEngine, "State machine can't be null");
    this.stateEngine.validate();
    this.stateEngine.anyTransition(this.eventAction);
    this.stateEngine.addError(this.errorAction);
  }

  @SneakyThrows
  public void fireGrace(C context) {
    Preconditions.checkNotNull(stateEngine, "StateMachine core can't be null. It seems to have not been initiated or started");
    stateEngine
        .getTransition(context.getFrom(), context.getCausedEvent())
        .ifPresent(transition -> stateEngine.fire(context.getCausedEvent(), context));
  }

  @SneakyThrows
  public void fire(C context) {
    Preconditions.checkNotNull(stateEngine, "StateMachine core can't be null. It seems to have not been initiated or started");
    stateEngine
        .getTransition(context.getFrom(), context.getCausedEvent())
        .ifPresentOrElse(transition -> stateEngine.fire(context.getCausedEvent(), context),
            () -> {
              throw new IllegalArgumentException(
                  "Can't find a transition from " + context.getFrom() + " with event "
                      + context.getTo());
            });
  }

}
