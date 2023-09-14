## Integrate Layout Inspector Plugin in your application


### Add Gradle Dependencies
Pluto Layout Inspector is distributed through [***mavenCentral***](https://central.sonatype.com/artifact/com.plutolib.plugins/layout-inspector). To use it, you need to add the following Gradle dependency to your build.gradle file of you android app module.

> Note: add the `no-op` variant to isolate the plugin from release builds.
```groovy
dependencies {
  debugImplementation "com.plutolib.plugins:layout-inspector:$plutoVersion"
  releaseImplementation "com.plutolib.plugins:layout-inspector-no-op:$plutoVersion"
}
```
<br>

### Install plugin to Pluto

Now to start using the plugin, add it to Pluto
```kotlin
Pluto.Installer(this)
  .addPlugin(PlutoLayoutInspectorPlugin())
  .install()
```
<br>

🎉 &nbsp;You are all done!

Now re-build and run your app and open Pluto, you will see the Layout Inspector plugin installed.

<br>


### Open Plugin view programmatically
To open Layout Inspector plugin screen via code, use this
```kotlin
Pluto.open(PlutoLayoutInspectorPlugin.ID)
```
