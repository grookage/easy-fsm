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
package com.grookage.fsm.core.config;

import com.grookage.fsm.core.models.entities.Event;
import com.grookage.fsm.core.models.entities.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransitionConfig<S extends State, E extends Event> {

    private E causedEvent;
    private Set<S> from;
    private S to;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final var that = (TransitionConfig) o;
        if (!Objects.equals(causedEvent, that.causedEvent)) {
            return false;
        }
        return Objects.equals(causedEvent.name().toLowerCase(Locale.ROOT),
                that.causedEvent.name().toUpperCase(Locale.ROOT));
    }

    @Override
    public int hashCode() {
        return 31 * causedEvent.hashCode();
    }
}
