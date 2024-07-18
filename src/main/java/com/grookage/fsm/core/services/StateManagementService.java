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

import com.google.common.collect.Sets;
import com.grookage.fsm.core.models.entities.State;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity by : koushikr. on 23/10/15.
 */
@Getter
@Setter
@NoArgsConstructor
public class StateManagementService<S extends State> {

  private final Set<S> endStates = Sets.newHashSet();
  private S from;

  public void addEndStates(Collection<S> endStates) {
    this.endStates.addAll(endStates);
  }

  public Set<S> allStates() {
    var states = new HashSet<S>();
    states.add(this.from);
    states.addAll(this.endStates);
    return states;
  }
}
