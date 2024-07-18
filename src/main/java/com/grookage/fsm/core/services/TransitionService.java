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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.grookage.fsm.core.models.entities.Event;
import com.grookage.fsm.core.models.entities.State;
import com.grookage.fsm.core.models.entities.Transition;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Entity by : koushikr. on 23/10/15.
 */
public class TransitionService<E extends Event, S extends State> {

  private final Multimap<S, Transition<E, S>> transitionDetails;

  public TransitionService() {
    transitionDetails = HashMultimap.create();
  }

  public void addTransition(S state, Transition<E, S> transition) {
    transitionDetails.put(state, transition);
  }

  public Optional<Transition<E, S>> getTransition(S from, E event) {
    return transitionDetails.get(from).stream().filter(new TransitionPredicate<>(event))
        .findFirst();
  }

  public Multimap<S, Transition<E, S>> getTransitionDetails() {
    return transitionDetails;
  }

  @AllArgsConstructor
  private static final class TransitionPredicate<E extends Event, S extends State> implements
      Predicate<Transition<E, S>> {

    private E event;

    @Override
    public boolean test(Transition<E, S> transition) {
      return transition.getEvent().equals(event);
    }
  }
}
