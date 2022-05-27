package com.grookage.fsm.core.stubs;

import com.grookage.fsm.core.models.entities.TransitionKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class TestTransitionKey implements TransitionKey {

  private String tag;
}
