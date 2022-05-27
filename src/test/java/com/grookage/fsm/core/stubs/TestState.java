package com.grookage.fsm.core.stubs;

import com.grookage.fsm.core.models.entities.State;

public enum TestState implements State {

  STARTED,

  CREATED,

  IN_PROGRESS,

  COMPLETED,

  FAILED;
}
