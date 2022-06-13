## Integrate Shared Preferences Plugin in your application


### Add Gradle Dependencies
Pluto Shared Preferences is distributed through [***mavenCentral***](https://search.maven.org/artifact/com.plutolib.plugins/preferences). To use it, you need to add the following Gradle dependency to your build.gradle file of you android app module.

> Note: add the `no-op` variant to isolate the plugin from release builds.
```groovy
dependencies {
  debugImplementation 'com.plutolib.plugins:preferences:2.0.1-beta'
  releaseImplementation 'com.plutolib.plugins:preferences-no-op:2.0.1-beta'
}
```
<br>

### Install plugin to Pluto

Now to start using the plugin, add it to Pluto
```kotlin
Pluto.Installer(this)
  .addPlugin(PlutoSharePreferencesPlugin("sharedPref"))
  .install()
```
<br>

🎉 &nbsp;You are all done!

Now re-build and run your app and open Pluto, you will see the Shared Preferences plugin installed.
