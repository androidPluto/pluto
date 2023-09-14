# Android Pluto

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.plutolib/pluto/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.plutolib/pluto)
[![CLA assistant](https://cla-assistant.io/readme/badge/androidPluto/pluto)](https://cla-assistant.io/androidPluto/pluto)
[![Daily Builds](https://github.com/androidPluto/pluto/actions/workflows/daily_builds.yml/badge.svg)](https://github.com/androidPluto/pluto/actions/workflows/daily_builds.yml)

Pluto is an on-device debugging framework for Android applications, which helps in the inspection of HTTP requests/responses, captures Crashes, and ANRs, and manipulates application data on the go.

It comes with a UI to monitor and share the information, as well as APIs to access and use that information in your application.

<br>

## 🖇 &nbsp;Integrate Pluto in your application



### Add Gradle Dependencies

Pluto is distributed through [***mavenCentral***](https://central.sonatype.com/artifact/com.plutolib/pluto). To use it, you need to add the following Gradle dependency to your build.gradle file of your app module.

> Note: add both the `pluto` and the `pluto-no-op` variant to isolate Pluto from release builds.
```groovy
def plutoVersion = "2.1.7"

dependencies {
  ....
  debugImplementation "com.plutolib:pluto:$plutoVersion"
  releaseImplementation "com.plutolib:pluto-no-op:$plutoVersion"
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

Unlike [version 1.x.x](https://github.com/androidPluto/pluto/wiki/Integrating-Pluto-1.x.x), Pluto now allows developers to add debuggers as plugin bundle or individual plugins based on their need.

Plugin bundle comes with all the basic plugins bundled together as single dependency.
```groovy
dependencies {
  ....
  debugImplementation "com.plutolib.plugins:bundle-core:$plutoVersion"
  releaseImplementation "com.plutolib.plugins:bundle-core-no-op:$plutoVersion"
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
- **[Layout Inspector Plugin](pluto-plugins/plugins/layout-inspector)**

We will be adding more to the [list](https://central.sonatype.com/search?q=com.plutolib.plugins). So please stay tuned.<br>
Please refer to their respective README for integration steps.
<br><br>
> You can also help us expand the Pluto ecosystem now. <br>Pluto now allows to develop custom debuggers as plugin. Read [Develop Custom Plugins](https://github.com/androidPluto/pluto/wiki/Develop-Custom-Pluto-Plugins-(Beta)).

<br>

🎉 &nbsp;You are all set!

Now re-build and run your app, you will receive a notification from Pluto, use it to access Pluto UI.

<br>

## Grouping Plugins *(Optional)*
Pluto now allows to group similar plugins together to have better readability & categorization.
<br>
To create a group, we need to override PluginGroup & attach Plugins to it. *(We have taken the example of grouping datasource plugins together)*

```kotlin
class DataSourcePluginGroup : PluginGroup("datasource-group") {

    override fun getConfig() = PluginGroupConfiguration(
        name = "DataSource Group"
    )

    override fun getPlugins() = listOf(
        PlutoSharePreferencesPlugin(),
        PlutoDatastorePreferencesPlugin(),
        PlutoRoomsDatabasePlugin()
    )
}
```

Then add the group to Plugin installer.
```kotlin
Pluto.Installer(this)
  .addPluginGroup(DataSourcePluginGroup())
  .install()
```

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
