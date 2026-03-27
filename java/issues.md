# Issues

* Mehr Szenarien als Tests.

  * The main difference is that many logging and reporting
    scenarios are not tested, particularly around the COUNT,
    DUMP, and End of Day Report features.

* Context sind globale Daten mit allen Maps.

* JaCOCO fehlt jetzt, kein Line Coverage Report.
* `System.out` in Tests
* Kein Focus auf Error Cases in Tests.

## Nice

* Tests besser fokussiert
* nur 1 Assertion
* Test Namen besser
* Report Generator schön
* Expiry Checker schön
* Coverage ist 90%, "ned schlecht"

## Was könnten wir an Qualität verbessern?

* Checkstyle in Maven für Formatting, die 100 LoC pro Klasse usw.
* PMD in Maven
* ArchUnit Test
  * keine Cycles zwischen Klassen
  * keine Cycles zwischen Packages

### Prompts

* "use packages"
* decompose into objects:
  * follow DDD approach: Use Entities, Value Objects, Services, Repositories
  * group fields into entities by shared prefix in names
