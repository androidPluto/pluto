<!-- Contributing
============

If you would like to contribute code to Pluto you can do so through GitHub by forking the repository and sending a pull request.

When submitting code, please make every effort to follow existing conventions and style in order to keep the code as readable as possible. Please also make sure your code compiles by running `./gradlew build`. For any formatting errors, run `./gradlew prCheck` to fix them.

Before your code can be accepted into the project you must also sign the [Individual Contributor License Agreement (CLA)][1].

[1]: https://cla-assistant.io/androidPluto/pluto -->



**We're looking for contributors, help us improve Pluto.** 😁 

<!-- If you would like to contribute code to Pluto you can do so through GitHub by forking the repository and sending a pull request. -->

Hers's how you can help
  - Look for issues marked as [`help wanted`](https://github.com/androidPluto/pluto/labels/help%20wanted).
  - Submit a new Plugin, see the [guidelines](/SUBMIT_GUIDELINES.md).
  - While submitting a new PR, make sure tests are all successful. If you think we need any new test, feel free to add new tests.

### Prerequisite

In order to start contributing to Pluto, you need to fork the project and open it in Android Studio/IntelliJ IDEA.

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

>
> Before your code can be accepted into the project you must also sign the [Individual Contributor License Agreement (CLA)][1]. You will be prompted to sign the agreement once you raise the pull request.
> 

[1]: https://cla-assistant.io/androidPluto/pluto

