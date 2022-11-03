# Pluto
<a href="https://www.producthunt.com/posts/pluto-0265aff1-a022-48a7-bea2-85d33cf1f15a?utm_source=badge-featured&utm_medium=badge&utm_souce=badge-pluto&#0045;0265aff1&#0045;a022&#0045;48a7&#0045;bea2&#0045;85d33cf1f15a" target="_blank"><img src="https://api.producthunt.com/widgets/embed-image/v1/featured.svg?post_id=349545&theme=light" alt="Pluto - Open&#0045;sourced&#0032;On&#0045;device&#0032;debug&#0032;framework&#0032;for&#0032;Android&#0032;apps | Product Hunt" style="width: 250px; height: 54px;" width="250" height="54" /></a>

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.plutolib/pluto/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.plutolib/pluto)
[![CLA assistant](https://cla-assistant.io/readme/badge/plutolib/pluto)](https://cla-assistant.io/plutolib/pluto)
[![Daily Builds](https://github.com/plutolib/pluto/actions/workflows/daily_builds.yml/badge.svg)](https://github.com/plutolib/pluto/actions/workflows/daily_builds.yml)

Pluto is an on-device debugging framework for Android applications, which helps in the inspection of HTTP requests/responses, captures Crashes, and ANRs, and manipulates application data on the go.

It comes with a UI to monitor and share the information, as well as APIs to access and use that information in your application.

<br>

## 🖇 &nbsp;Integrate Pluto in your application



### Add Gradle Dependencies

Pluto is distributed through [***mavenCentral***](https://search.maven.org/artifact/com.plutolib/pluto). To use it, you need to add the following Gradle dependency to your build.gradle file of your app module.

> Note: add both the `pluto` and the `pluto-no-op` variant to isolate Pluto from release builds.
```groovy
dependencies {
  ....
  debugImplementation 'com.plutolib:pluto:2.0.6'
  releaseImplementation 'com.plutolib:pluto-no-op:2.0.6'
  ....
}
```


### Initialize Pluto

Now to start using Pluto, initialize Pluto SDK from you application class by passing context to it.
```kotlin
Pluto.Installer(this)
  .addPlugin(....)
  .install()
```


### Install plugins

Unlike [version 1.x.x](https://github.com/plutolib/pluto/wiki/Integrating-Pluto-1.x.x), Pluto now allows developers to add debuggers as plugin bundle or individual plugins based on their need.

Plugin bundle comes with all the basic plugins bundled together as single dependency.
```groovy
dependencies {
  ....
  debugImplementation 'com.plutolib.plugins:bundle-core:2.0.6'
  releaseImplementation 'com.plutolib.plugins:bundle-core-no-op:2.0.6'
  ....
}
```

But, if you want to use individual plugins, here is the list of some plugins provided by us

- **[Network Plugin](pluto-plugins/plugins/network)**
- **[Exceptions & Crashes Plugin](pluto-plugins/plugins/exceptions)**
- **[Logger Plugin](pluto-plugins/plugins/logger)**
- **[Shared Preferences Plugin](pluto-plugins/plugins/shared-preferences)**
- **[Rooms Database Plugin](pluto-plugins/plugins/rooms-database)**
- **[Datastore Preferences Plugin](pluto-plugins/plugins/datastore)**

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

Have an idea to improve Pluto? Let's connect on 
- [Twitter](https://twitter.com/srtv_prateek)
- [Github](https://github.com/srtvprateek)

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
