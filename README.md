# SMAP Survey Manager

[![Build Status](https://travis-ci.org/smap-consulting/smap-survey-manager.svg)](https://travis-ci.org/smap-consulting/smap-survey-manager)

A high level wrapper for javarosa-core, designed to allow survey completion via a textual conversation.

### Prerequisites

* Working install of JDK 1.7
* Working install [gradle](http://www.gradle.org/) for builds.

## Dependencies

Uses a vendored jar @mitchellsundt's fork of [JavaRosa](https://bitbucket.org/m.sundt/javarosa) which remove's the libraries J2ME dependencies, and contains some other useful tweaks.

Uses the FormController class from [ODK Collect](https://code.google.com/p/opendatakit/source/checkout?repo=collect)

## Development

#### Eclipse

Install the [Eclipse Gradle plugin](https://github.com/spring-projects/eclipse-integration-gradle/#installing-gradle-tooling-from-update-site).

######WINDOWS
```
Install in eclipse using:
Help -> Install New Software
Location: http://dist.springsource.com/milestone/TOOLS/gradle
Install the 'Extensions / Gradle Integration' package
```
######MAC:
```bash
git clone git@github.com:smap-consulting/smap-survey-manager.git
cd smap-survey-manager
gradle eclipse # generates eclipse project files
```

You can now import the project into eclipse as a gradle project

### IntelliJ

Recent versions of IntelliJ come with gradle integration built in.

```bash
git clone git@github.com:smap-consulting/smap-survey-manager.git
cd smap-survey-manager
gradle idea # generates intellij project files
```

You can now import the project into eclipse as a gradle project

#### Running the tests

```bash
gradle check
```

#### Building the jar

```bash
gradle jar
```
