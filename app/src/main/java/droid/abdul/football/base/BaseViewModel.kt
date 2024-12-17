package droid.abdul.football.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel: ViewModel() {

    private val disposable = CompositeDisposable()

    fun fetch(task: () -> Disposable) {
        disposable.add(task())
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}