## Integrate Datastore Preferences Plugin in your application


### Add Gradle Dependencies
Pluto Datastore Preferences is distributed through [***mavenCentral***](https://central.sonatype.com/artifact/com.plutolib.plugins/datastore-pref). To use it, you need to add the following Gradle dependency to your build.gradle file of you android app module.

> Note: add the `no-op` variant to isolate the plugin from release builds.
```groovy
dependencies {
  debugImplementation 'com.plutolib.plugins:datastore-pref:2.1.6'
  releaseImplementation 'com.plutolib.plugins:datastore-pref-no-op:2.1.6'
}
```
<br>

### Install plugin to Pluto

Now to start using the plugin, add it to Pluto
```kotlin
Pluto.Installer(this)
  .addPlugin(PlutoDatastorePreferencesPlugin())
  .install()
```
<br>

###  Start watching Datastore Preference

Create intance of DataStore Preferences and start watching in Pluto.
```kotlin
val Context.appPreferences by preferencesDataStore(
    name = PREF_NAME
)

PlutoDatastoreWatcher.watch(PREF_NAME, appPreferences)
```
<br>

🎉 &nbsp;You are all done!

Now re-build and run your app and open Pluto, you will see the Datastore Preferences plugin installed.

<br>

### Open Plugin view programmatically
To open Datastore plugin screen via code, use this
```kotlin
Pluto.open(PlutoDatastorePreferencesPlugin.ID)
```

