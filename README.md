# Pluto
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.plutolib/pluto/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.plutolib/pluto)
[![CLA assistant](https://cla-assistant.io/readme/badge/plutolib/pluto)](https://cla-assistant.io/plutolib/pluto)
[![Daily Builds](https://github.com/plutolib/pluto/actions/workflows/daily_builds.yml/badge.svg)](https://github.com/plutolib/pluto/actions/workflows/daily_builds.yml)

Pluto is an on-device debugging framework for Android applications, which helps in the inspection of HTTP requests/responses, captures Crashes, and ANRs, and manipulates application data on the go.

It comes with a UI to monitor and share the information, as well as APIs to access and use that information in your application.

<br><br>

## 🖇 &nbsp;Integrate Pluto in your application



### Add Gradle Dependencies

Pluto is distributed through [***mavenCentral***](https://search.maven.org/artifact/com.plutolib/pluto). To use it, you need to add the following Gradle dependency to your build.gradle file of your android app module.

> Note: add both the `pluto` and the `pluto-no-op` variant to isolate Pluto from release builds.
```groovy
dependencies {
  debugImplementation 'com.plutolib:pluto:2.0.1-beta'
  releaseImplementation 'com.plutolib:pluto-no-op:2.0.1-beta'
}
```


### Initialize Pluto

Now to start using Pluto, initialize Pluto SDK from you application class by passing context to it.
```kotlin
Pluto.Installer(this)
  .addPlugin(DemoPlugin("demo_id"))
  .install()
```


### Install plugins

Unlike [version 1.x.x](https://github.com/plutolib/pluto/wiki/Integrating-Pluto-1.x.x), Pluto now allows developers to add debuggers as plugins based on their need.

Here is the list of some plugins provided by us

- **[Network Plugin](https://github.com/plutolib/pluto/blob/plugin_documentation/plugins/network/INTEGRATION.md)**
- **[Exceptions & Crashes Plugin](https://github.com/plutolib/pluto/blob/plugin_documentation/plugins/exceptions/INTEGRATION.md)**
- **[Logger Plugin](https://github.com/plutolib/pluto/blob/plugin_documentation/plugins/logger/INTEGRATION.md)**
- **[Shared Preferences Plugin](https://github.com/plutolib/pluto/blob/plugin_documentation/plugins/shared-preferences/INTEGRATION.md)**

We will be adding more to the [list](https://search.maven.org/search?q=com.plutolib.plugins). So please stay tuned.<br>
Please refer to their respective README for integration steps.
<br><br>
> You can also help us expand the Pluto ecosystem now. <br>Pluto now allows to develop custom debuggers as plugin. Read [Develop Custom Plugins](https://github.com/plutolib/pluto/wiki/Develop-Custom-Pluto-Plugins-(Beta)).

<br>

🎉 &nbsp;You are all set!

Now re-build and run your app, you will receive a notification from Pluto, use it to access Pluto UI.

<br>


## 📝 &nbsp;Contribution

**We're looking for contributors, help us improve Pluto.** 😁 

Please refer to your [`Contribution guidelines`](/CONTRIBUTING.md) to get started.

<br>


## 📃 &nbsp;License

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
