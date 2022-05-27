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
package com.grookage.fsm.core.models.executors;

import com.grookage.fsm.core.exceptions.FsmException;
import com.grookage.fsm.core.models.entities.Context;
import com.grookage.fsm.core.models.entities.Event;
import com.grookage.fsm.core.models.entities.State;
import com.grookage.fsm.core.models.entities.TransitionKey;

/**
 * Entity by : koushikr. on 23/10/15.
 *
 * <p>
 * Denotes ErrorAction. Gets Invoked whenever there is an exception thrown
 * </p>
 */
@FunctionalInterface
public interface ErrorAction<E extends Event, S extends State, K extends TransitionKey, C extends Context<S, E, K>> extends
    Action {

  void call(FsmException error, C context);

}
