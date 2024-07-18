### Changelog

All notable changes to this project will be documented in this file. Dates are displayed in UTC.

#### [2.0.3]

- Fixed the endState bug while constructing a stateMachine
- Introduced a StateMachineBuilder to construct a stateMachine from a configuration, to reduce verbosity
- Introduced a StateMachineRegistry to create multiple stateMachines atop multiple MachineBuilderConfigurations, making multiple stateMachine creations simple.
- Introduced a CHANGELOG