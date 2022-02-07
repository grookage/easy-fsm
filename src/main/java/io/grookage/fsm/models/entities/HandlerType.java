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
package io.grookage.fsm.models.entities;

import io.grookage.fsm.models.executors.EventAction;

import java.util.Objects;

/**
 * Entity by : koushikr.
 * on 23/10/15.
 *
 * <p>
 *     HandlerType is the type of {EventType, Event, State} tuple against which transition transitions
 *     will be saved. The same request mapping is later used to fetch the appropriate handler
 *     {@link EventAction} for execution
 * </p>
 */
public class HandlerType {

    EventType eventType;
    Event event;
    State state;

    public HandlerType(EventType eventType, Event event, State state) {
        this.eventType = eventType;
        this.event = event;
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (HandlerType) o;
        if (!Objects.equals(event, that.event)) return false;
        if (that.eventType != eventType) return false;
        return Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        int result = eventType.hashCode();
        result = 31 * result + (event != null ? event.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }

}
