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

## Coding pattern
[MVVM][2] design pattern was used in the implementation of this project with [Koin][3] for Dependency injection
and [AndroidX][4] artifacts were used for this project. The project employs a fragmented approach.

##Libraries used
1. [Glide][5] image loading library combined with
2. [AndroidSVG][6] for loading images from the API
3. [Koin][3] for Dependency Injection
4. [Retrofit][7] and [RxJava2][8] for Network calls
5. [OkHttp3][9] for Network Logging interceptor
6. [JUnit][10] for Unit Tests
7. [Espresso][11] for UI and Automated tests




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