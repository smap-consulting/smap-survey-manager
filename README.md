# SMAP Survey Manager

[![Build Status](https://travis-ci.org/smap-consulting/smap-survey-manager.svg)](https://travis-ci.org/smap-consulting/smap-survey-manager)

A convenience wrapper for Javarosa-lib, designed to allow survey completion via a converstion.

Uses the FormController from [ODK Collect](https://code.google.com/p/opendatakit/source/checkout?repo=collect)


### Prerequisites

Working install of JDK 1.7
Working install [gradle](http://www.gradle.org/) for builds.


## Development

#### Eclipse

- Install the [Eclipse Gradle plugin](https://github.com/spring-projects/eclipse-integration-gradle/).

```bash
git clone git@github.com:smap-consulting/smap-survey-manager.git
cd smap-survey-manager
gradle eclipse # generates eclipse project files
```

You can now import the project into eclipse as a gradle project

### IntelliJ

Recent versions of IntelliJ come with gradle integration built in

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
