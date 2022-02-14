# Pluto
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.plutolib/pluto/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.plutolib/pluto)
[![CLA assistant](https://cla-assistant.io/readme/badge/plutolib/pluto)](https://cla-assistant.io/plutolib/pluto)
[![Daily Builds](https://github.com/mocklets/pluto/actions/workflows/daily_builds.yml/badge.svg)](https://github.com/mocklets/pluto/actions/workflows/daily_builds.yml)

Pluto is a on-device debugging framework for Android applications, which helps in inspection of HTTP requests/responses, capture Crashes and ANRs and manipulating application data on-the-go.

It comes with a UI to monitor and share the information, as well as APIs to access and use that information in your application.

-----

## Integrate Pluto in your application



### Add Gradle Dependencies

Pluto is distributed through [***mavenCentral***](https://search.maven.org/artifact/com.plutolib/pluto). To use it, you need to add the following Gradle dependency to your build.gradle file of you android app module.


```groovy
dependencies {
  debugImplementation 'com.plutolib:pluto:2.0.0'
}
```


### Intialize Pluto

Now to start using Pluto, intialize Pluto SDK from you application class by passing context to it.
```kotlin
Pluto.Installer(this)
  .addPlugin(DemoPlugin("demo_id"))
  .install()
```


### Install plugins

Unlike [version 1.x.x](https://github.com/plutolib/pluto/wiki/Integrating-Pluto-1.x.x), Pluto now allows developers to add debuggers as plugin based on their need.

Here is the list of some plugin provided by us

- **[Network Plugin](https://github.com/plutolib/plugin-network)**
- **[Exceptions & Crashes Plugin](https://github.com/plutolib/plugin-exceptions)**
- **[Logger Plugin](https://github.com/plutolib/plugin-logger)**
- **[Shared Preferences Plugin](https://github.com/plutolib/plugin-shared-preferences)**

We will be adding more to the [list](https://search.maven.org/search?q=com.plutolib.plugins). So please stay tuned.

Please refer to their respective README for integration steps.



🎉 You are all set!

Now re-build and run your app, you will receive a notification from Pluto, use it to access Pluto UI.

***


## Contribution

**We're looking for contributors, help us improve Pluto.** 😁 

Please refer to your [`Contribution guidelines`](/CONTRIBUTING.md) to get started.

***


## License

```
Copyright 2021 Plutolib.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
