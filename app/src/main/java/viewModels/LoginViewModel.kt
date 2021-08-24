package viewModels

import database.AppDatabase
import database.User
import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import api.ApiClient
import com.google.gson.Gson
import data.Response
import data.ResponseB2b
import database.UserProfileDao
import event.Event
import interfaces.ApiInterface
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var firstName = MutableLiveData<String>()
    var lastName = MutableLiveData<String>()
    var allData = MutableLiveData<ResponseB2b>()
    var uname = ObservableField<String>("mobileTeamAgent")
    var pass = ObservableField<String>("Mobile@123")
    lateinit var call: ResponseB2b
    var dbError = MutableLiveData<Event<String>>()

    var isLoading = MutableLiveData<Boolean>(false)

    val error = MutableLiveData<Event<String>>()

    fun getDataAndSet() {
        viewModelScope.launch {
            val req = ApiClient.callService(ApiInterface::class.java)
            isLoading.value = true
            try {

                call = req.getDataPost(pass.get().toString()!!, uname.get().toString()!!)
                error.value = Event("Success")
                isLoading.value = false

            } catch (E: Exception) {
                Log.i("loginViewModel", "Catching now")
                error.value = Event("Something went wrong")
            }


        }
    }

    fun insertToDB(userDao:UserProfileDao, x: Response) {
        viewModelScope.launch {
            try {
                val g = Gson()
                val user = User(uname.get()!!, pass.get(), g.toJson(x))
                userDao.insert(user)
                dbError.value = Event("Successful DB operation")
            } catch (e: Exception) {
                dbError.value = Event(e.toString())
            }
            isLoading.value = false

        }

    }

}