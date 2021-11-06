The migration will mean a complete change of tech-stack

* Hilt injection --> Koin
* Room database --> SqlDelight
* datastore-preferences --> multiplatform settings

Also, the packaging needs to package by FEATURE.  Moving to a feature based packaging would
be good for being able to ensure a piecemeal migration happens cleanly.  Tracking the updates
to tech-stack should happen first so it's clear what changed where.

The project needs tests before it can migrate to KMM.

MIGRATION #1 - move to Koin
