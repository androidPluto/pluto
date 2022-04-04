## Submit a plugin to Pluto

We are looking for contributors to add new & exciting Plugins for the Android developers out there. But at the same time we need to made the uniformity and standard through out the plugins.

To start with a new plugins, start a discussion for [Plugin Suggestions](https://github.com/plutolib/pluto/discussions/categories/plugin-suggestions). This will help you gather valuable feedback and insights even before putting any dev effort.

Once you have enough information, lets jump into development.

<br>

### 🛠 &nbsp;Develop

- We have a uniform package name for all our plugins `com.pluto.plugins.<PLUGIN>`. Please make sure you follow this.
- All the plugins need to accept `identifier` in `Plugin.kt` constructor. This helps developers to open the Plugin directly using the `identifier`.
- In your plugin's *build.gradle*, mention `resourcePrefix 'pluto_PLUGIN-NAME___'`. This will make sure your resources are not being overridden by the parent apps.
- Make sure you add both `lib` & `lib-no-op` variants.
<br>

### 🚀 &nbsp;Publish

- Make sure you use the same version logic as the whole project.
- Setup your Maven configuration, like given below
``` groovy
ext {
    PUBLISH_GROUP_ID = "com.plutolib.plugins" // do not change this
    PUBLISH_VERSION = verPublish              // do not change this
    PUBLISH_ARTIFACT_ID = 'PLUGIN_NAME'
}
```
<br>

### 🎖 &nbsp;Get your plugin merged

Once the development is done, its time to get your code merged. Open a Pull Request with label `submit plugin`.

We will review it & if everything is good, it ll be merged to **develop** 🎊.
