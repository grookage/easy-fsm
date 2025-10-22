### Changelog

All notable changes to this project will be documented in this file. Dates are displayed in UTC.

#### [2.0.4]

- Removed the javax validation, removed guava. Using native java checks.
- Upgraded junit to 5.x
- Upgraded dated libs
- Moved the build to maven central from s01.oss.sonatype.org
- Started handling as a qualified exception instead of runtime exceptions.
- Added tests for the changes and fixed the earlier tests.

#### [2.0.3]

- Fixed the endState bug while constructing a stateMachine
- Introduced a StateMachineBuilder to construct a stateMachine from a configuration, to reduce verbosity
- Introduced a StateMachineRegistry to create multiple stateMachines atop multiple MachineBuilderConfigurations, making
  multiple stateMachine creations simple.
- Introduced a CHANGELOG