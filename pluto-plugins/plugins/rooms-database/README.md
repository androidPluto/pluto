## Integrate Rooms Database Plugin in your application


### Add Gradle Dependencies
Pluto Rooms Database is distributed through [***mavenCentral***](https://central.sonatype.com/artifact/com.plutolib.plugins/rooms-db). To use it, you need to add the following Gradle dependency to your build.gradle file of you android app module.

> Note: add the `no-op` variant to isolate the plugin from release builds.
```groovy
dependencies {
  debugImplementation 'com.plutolib.plugins:rooms-db:2.1.6'
  releaseImplementation 'com.plutolib.plugins:rooms-db-no-op:2.1.6'
}
```
<br>

### Install plugin to Pluto

Now to start using the plugin, add it to Pluto
```kotlin
Pluto.Installer(this)
  .addPlugin(PlutoRoomsDatabasePlugin())
  .install()
```
<br>

###  Start watching Rooms Database

Create intance of DataStore Preferences and start watching in Pluto.
```kotlin
// DB_NAME should be same as database name assigned while creating the database.
PlutoRoomsDBWatcher.watch(DB_NAME, SampleDatabase::class.java)
```
<br>

🎉 &nbsp;You are all done!

Now re-build and run your app and open Pluto, you will see the Rooms Database plugin installed.

<br>

### Open Plugin view programmatically
To open Rooms Database plugin screen via code, use this
```kotlin
Pluto.open(PlutoRoomsDatabasePlugin.ID)
```
