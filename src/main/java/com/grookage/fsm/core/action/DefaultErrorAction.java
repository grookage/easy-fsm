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
package com.grookage.fsm.core.action;

import com.grookage.fsm.core.exceptions.FsmException;
import com.grookage.fsm.core.models.entities.Context;
import com.grookage.fsm.core.models.entities.Event;
import com.grookage.fsm.core.models.entities.State;
import com.grookage.fsm.core.models.entities.TransitionKey;
import com.grookage.fsm.core.models.executors.ErrorAction;
import java.util.Objects;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class DefaultErrorAction<S extends State, E extends Event, K extends TransitionKey, C extends Context<S, E, K>> implements
    ErrorAction<E, S, K, C> {

  @Override
  @SneakyThrows
  public void call(FsmException error, C context) {
    var errorMessage = "Runtime Error in state [" + error.getState() + "]";
    if (!Objects.isNull(error.getEvent())) {
      errorMessage = errorMessage + "on Event [" + error.getEvent() + "]";
    }
    log.error(
        "Error performing a transition with from state and in current state {} with the event {} and message {}",
        context.getFrom(),
        context.getTo(),
        errorMessage
    );
    log.debug(
        "Error performing a transition with from state and in current state {} with the event {} and message {} with trace {}",
        context.getFrom(),
        context.getTo(),
        errorMessage,
        error
    );
    throw error;
  }
}