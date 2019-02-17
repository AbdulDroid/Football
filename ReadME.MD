# Football Data App
An App that fetches data from [Football Data API][1] on free plane so the data users are able to access on the app is limited to the free tier

## Usage guide
Add your API Key to the `build.gradle` file in the `app` module as so
```groovy
android {
    ...
    defaultConfig {
        ...
        buildConfigField("String", "X_AUTH_TOKEN", "\"API_KEY\"")
        ...
    
}
```
and build the project on a mobile design or an emulator of choice.

## Coding pattern
[MVVM][2] design pattern was used in the implementation of this project with [Koin][3] for Dependency injection
and [AndroidX][4] artifacts were used for this project. The project employs a fragmented approach and repository 
pattern to abstract data from the views.

## Libraries used

1. [Android Support Libraries][4]
2. [Constraint Layout][12] for laying out views and view groups, the project uses the version `2.0.0-alpha3` as to be able to use the `MotionLayout` option
3. [Glide][5] image loading library combined with
4. [AndroidSVG][6] for loading images from the API
5. [Koin][3] for Dependency Injection
6. [Retrofit][7] and [RxJava2][8] for Network calls
7. [OkHttp3][9] for Network Logging interceptor
8. [JUnit][10] for Unit Tests
9. [Espresso][11] for UI and Automated tests




[1]: https://www.football-data.org/documentation/quickstart
[2]: https://www.journaldev.com/20292/android-mvvm-design-pattern
[3]: https://insert-koin.io/
[4]: https://developer.android.com/jetpack/androidx/migrate
[5]: https://github.com/bumptech/glide
[6]: https://github.com/BigBadaboom/androidsvg
[7]: https://square.github.io/retrofit/
[8]: https://github.com/ReactiveX/RxJava
[9]: https://square.github.io/okhttp/
[10]: https://junit.org/junit4/
[11]: https://developer.android.com/training/testing/espresso/
[12]: https://developer.android.com/training/constraint-layout/