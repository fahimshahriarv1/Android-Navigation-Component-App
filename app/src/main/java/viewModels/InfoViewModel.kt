package viewModels

import android.graphics.Bitmap
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import api.ApiClient
import data.ResponseClient
import database.UserProfileDao
import event.Event
import interfaces.ApiInterface
import kotlinx.coroutines.launch


class InfoViewModel:ViewModel() {
    var firstName= ObservableField<String>()
    var lastName= ObservableField<String>()
    var email= ObservableField<String>()
    var mobile= ObservableField<String>()
    var imageSrc=MutableLiveData<Bitmap>()
    var callError=MutableLiveData<Event<String>>()
    lateinit var paymentInfo: ResponseClient
    var allinfo= MutableLiveData<Event<String>>()



    fun getPaymentInfo(token:String)
    {
        viewModelScope.launch {
            val req= ApiClient.callService(ApiInterface::class.java)
            try {
                paymentInfo=req.getDataGet(token)
                callError.value=Event("Fetch Success")
            }
            catch (E: Exception)
            {
                callError.value=Event("Fetch Failed")
            }
        }
    }
    fun getAllInfoFromDB(uname: String,dao: UserProfileDao){
        viewModelScope.launch {
            allinfo.value= Event(dao.findByUname(uname))
        }
    }

}