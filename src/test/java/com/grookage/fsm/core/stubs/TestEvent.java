package com.grookage.fsm.core.stubs;

import com.grookage.fsm.core.models.entities.Event;

public enum TestEvent implements Event {

  INITIATE,

  MOVE_TO_PROGRESS,

  MOVE_TO_COMPLETED,

  MOVE_TO_FAILED;

}
