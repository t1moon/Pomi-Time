package apps.tim.pomos.base.di.app

import android.content.Context
import apps.tim.pomos.base.app.PomoApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule(private val app: PomoApp) {

    @Provides
    @Singleton
    fun provideContext() : Context = app

}
