## Integrate Exceptions Plugin in your application


### Add Gradle Dependencies
Pluto Exceptions is distributed through [***mavenCentral***](https://search.maven.org/artifact/com.plutolib.plugins/exceptions). To use it, you need to add the following Gradle dependency to your build.gradle file of you android app module.

> Note: add the `no-op` variant to isolate the plugin from release builds.
```groovy
dependencies {
  debugImplementation 'com.plutolib.plugins:exceptions:2.0.1-beta'
  releaseImplementation 'com.plutolib.plugins:exceptions-no-op:2.0.1-beta'
}
```
<br>

### Install plugin to Pluto

Now to start using the plugin, add it to Pluto
```kotlin
Pluto.Installer(this)
  .addPlugin(PlutoExceptionsPlugin("exceptions"))
  .install()
```
<br>

###  Set Global Exception Handler

To intercept uncaught exceptions in your app, attach `UncaughtExceptionHandler` to Pluto
```kotlin
PlutoExceptions.setExceptionHandler { thread, throwable ->
    Log.d("Exception", "uncaught exception handled on thread: " + thread.name, throwable)
}
```
<br>

🎉 &nbsp;You are all done!

Now re-build and run your app and open Pluto, you will see the Exceptions plugin installed.
