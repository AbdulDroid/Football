package droid.abdul.football

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import droid.abdul.football.di.module.appModule
import droid.abdul.football.di.module.networkModule
import droid.abdul.football.di.module.providerModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Football: Application(), SingletonImageLoader.Factory {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Football)
            modules(
                listOf(
                    appModule,
                    networkModule,
                    providerModule
                )
            )
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .crossfade(true)
            .build()
    }
}