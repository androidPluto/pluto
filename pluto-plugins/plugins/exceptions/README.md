## Integrate Exceptions Plugin in your application

[Crashes and ANRs Demo.webm](https://github.com/user-attachments/assets/ed690b3b-5d7e-415e-9373-6f6d183a4def)

### Add Gradle Dependencies
Pluto Exceptions is distributed through [***mavenCentral***](https://central.sonatype.com/artifact/com.androidpluto.plugins/exceptions). To use it, you need to add the following Gradle dependency to your build.gradle file of you android app module.

> Note: add the `no-op` variant to isolate the plugin from release builds.
```groovy
dependencies {
  debugImplementation "com.androidpluto.plugins:exceptions:$plutoVersion"
  releaseImplementation "com.androidpluto.plugins:exceptions-no-op:$plutoVersion"
}
```
<br>

### Install plugin to Pluto

Now to start using the plugin, add it to Pluto
```kotlin
Pluto.Installer(this)
  .addPlugin(PlutoExceptionsPlugin())
  .install()
```
<br>

###  Set Global Exception Handler

To intercept uncaught exceptions in your app, attach `UncaughtExceptionHandler` to PlutoExceptions
```kotlin
PlutoExceptions.setExceptionHandler { thread, throwable ->
    Log.d("exception_demo", "uncaught exception handled on thread: " + thread.name, throwable)
}
```

To intercept & report potential ANRs in your app, attach `UncaughtANRHandler` to PlutoExceptions
```kotlin
PlutoExceptions.setANRHandler { thread, exception ->
    Log.d("anr_demo", "potential ANR detected on thread: " + thread.name, exception)
}
```

You can also modify the Main thread response time, after which the above callback will be triggered.
```kotlin
PlutoExceptions.mainThreadResponseThreshold = 10_000
```
<br>

🎉 &nbsp;You are all done!

Now re-build and run your app and open Pluto, you will see the Exceptions plugin installed.

<br>

### Open Plugin view programmatically
To open Exceptions plugin screen via code, use this
```kotlin
Pluto.open(PlutoExceptionsPlugin.ID)
```
