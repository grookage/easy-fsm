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
package com.grookage.fsm.core.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import com.grookage.fsm.core.FsmSerDe;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity by : koushikr. on 23/10/15.
 *
 * <p>
 * Denotes the Context in which the StateEngine will function Keeps metadeta about the from and
 * to states along with the causedEvent
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor
@Data
public abstract class Context<S extends State, E extends Event, K extends TransitionKey> implements
    Serializable {

  private static final long serialVersionUID = 42L;
  private S from;
  private S to;
  private E causedEvent;
  private Map<String, Object> data = new HashMap<>();

  protected Context(S from, S to, E event) {
    this.from = from;
    this.to = to;
    this.causedEvent = event;
  }

  public abstract K getTransitionKey();

  @JsonIgnore
  public <V> void addContext(Class<V> klass, V value) {
    addContext(klass.getSimpleName().toUpperCase(), value);
  }

  @JsonIgnore
  public <V> Optional<V> getContext(Class<V> tClass) {
    return getContext(tClass.getSimpleName().toUpperCase(), tClass);
  }

  @JsonIgnore
  public <V> void addContext(String key, V value) {
    if (Strings.isNullOrEmpty(key.toUpperCase())) {
      throw new IllegalArgumentException("Invalid key for context data. Key cannot be null/empty");
    }
    if (this.data == null) {
      this.data = new HashMap<>();
    }
    this.data.put(key.toUpperCase(), value);
  }

  @JsonIgnore
  public <V> Optional<V> getContext(String key, Class<V> tClass) {
    var value = this.data.get(key.toUpperCase());
    return Optional.ofNullable(value).map(o -> FsmSerDe.convertValue(o, tClass));
  }
}
