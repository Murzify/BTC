package ru.mmurzin.networking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mmurzin.networking.api.blockchair.responce.Repo

class MyViewModel: ViewModel() {
    val repo = MutableLiveData<Repo>()

    fun updateData(data: Repo){
        repo.value = data
    }
}