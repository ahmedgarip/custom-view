package com.udacity

import DownloadItem
import DownloadProgressLiveData
import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(app : Application) : ViewModel() {
    private val _progress = MutableLiveData<Int>()
    val progress : LiveData<Int>
        get() = _progress

    var downloadId = 0L
    val download : LiveData<DownloadItem>




    val progress3 = 60
    init {
        _progress.value = 0

//        _download.value = DownloadProgressLiveData()
        download = DownloadProgressLiveData(app , downloadId)


    }



}


class MainViewModelFactory(private val app: Application): ViewModelProvider.Factory{
    override fun <T:ViewModel> create(modelClass: Class<T>):T{
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(app) as T
        }
        throw IllegalArgumentException("UNKNOWN VIEW MODEL CLASS")
    }
}