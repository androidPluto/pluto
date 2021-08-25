# Pluto
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.mocklets/pluto/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.mocklets/pluto)
[![CLA assistant](https://cla-assistant.io/readme/badge/mocklets/pluto)](https://cla-assistant.io/mocklets/pluto)
[![Daily Builds](https://github.com/mocklets/pluto/actions/workflows/daily_builds.yml/badge.svg)](https://github.com/mocklets/pluto/actions/workflows/daily_builds.yml)

Pluto is a on-device debugger for Android applications, which helps in inspection of HTTP requests/responses, capture Crashes and ANRs and manipulating application data on-the-go.

It comes with a UI to monitor and share the information, as well as APIs to access and use that information in your application.


<p align="center">
  <img src="https://firebasestorage.googleapis.com/v0/b/pluto-web/o/pluto_summary_gif.gif?alt=media" alt="pluto demo" width="35%"/>
</p>

***


## Integrate Pluto in your application



### Add Gradle Dependencies

Pluto is distributed through [***mavenCentral***](https://search.maven.org/artifact/com.mocklets/pluto). To use it, you need to add the following Gradle dependency to your build.gradle file of you android app module.



> Note: add both the pluto and the pluto-no-op variant to isolate Pluto from release builds.
```groovy
dependencies {
  debugImplementation 'com.mocklets:pluto:LATEST_VERSION'
  releaseImplementation'com.mocklets:pluto-no-op:LATEST_VERSION'
}
```


### Intialize Pluto

Now to start using Pluto, intialize Pluto SDK from you application class by passing context to it.
```kotlin
Pluto.initialize(context)
```



###  Add Pluto interceptor

To debug HTTP requests/responses, plug the PlutoInterceptor in your OkHttp Client Builder
```kotlin
val client = OkHttpClient.Builder()
    .addInterceptor(PlutoInterceptor())
    .build()
```



### Listen to ANRs

Pluto can capture and store potential ANRs occurring in the app. You can also listen to these ANRs and report these to any Crash reporting tools like Firebase Crashlytics, Bugsnag, etc.
```kotlin
Pluto.setANRListener(object: ANRListener {
    override fun onAppNotResponding(exception: ANRException) {
        exception.printStackTrace()
        PlutoLog.e("ANR", exception.threadStateMap)
    }
})
```



### Add Pluto Logs

Pluto allows you to log and persist the user journey through the app, and help debug them without any need to connect to Logcat.
```kotlin
PlutoLog.event("analytics", eventName, HashMap(attributes))
PlutoLog.d("debug_log", "button clicked")
PlutoLog.e("error_log", "api call falied with http_status 400")
PlutoLog.w("warning_log", "warning log")
PlutoLog.i("info_log", "api call completed")
```

But if you are connected to Logcat, PlutoLogs behave similar to Log class, with an improvement to tag the method and file name also. In Logcat, PlutoLogs will look like the following.
```
D/onClick(MainActivity.kt:40) | debug_log: button clicked
E/onFailure(NetworkManager.kt:17) | error_log: api call falied with http_status 400
```


### Set App Properties

Pluto allows storing information like App status(like app configurations), User properties(like email, profile) and Device fingerprint(like IMEI).

This data can later be accessed via Pluto debug UI. This method can be called multiple times and it will keep on appending the data.
```kotlin
Pluto.setAppProperties(hashMapOf(
    "User id" to "2whdue-dn4f-3hr-dfhrhs",
    "User email" to "john.smith@gmail.com"
))
```

🎉 You are all done!

Now re-build and run your app, you will receive a notification from Pluto, use it to access Pluto UI.

***


## Contribution

**We're looking for contributors, help us improve Pluto.** 😁 

Hers's how you can help
- Look for issues marked as [`help wanted`](https://github.com/mocklets/pluto/labels/help%20wanted)
- While submitting a new PR, make sure tests are all successful. If you think we need any new test, feel free to add new tests.

### Prerequisite

In order to start working on Pluto, you need to fork the project and open it in Android Studio/IntelliJ IDEA.

Before committing we suggest you install the pre-commit hooks with the following command:
```
./gradlew installGitHook
```

This will make sure your code is validated against `ktLint` and `detekt` before every commit.
The command will run automatically before the `clean` task, so you should have the pre-commit hook installed by then.

Before submitting a PR please run:
```
./gradlew build
```
This will build the library and make sure your CI checks will pass.

***


## Acknowledgments

Big thanks 🙏 to [ChuckerTeam/chucker](https://github.com/ChuckerTeam/chucker) for inspiration behind network interceptor code.


***


## License

```
Copyright 2021 Graylattice Communications Private Limited.

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
