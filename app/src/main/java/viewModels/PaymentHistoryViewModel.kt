package viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import api.ApiClient
import data.ResponseClient
import interfaces.ApiInterface
import kotlinx.coroutines.launch
import java.lang.Exception

class PaymentHistoryViewModel:ViewModel() {

    var callError=MutableLiveData<String>()
    lateinit var paymentInfo:ResponseClient


    fun getPaymentInfo(token:String)
    {
        viewModelScope.launch {
            val req= ApiClient.callService(ApiInterface::class.java)
            try {
                paymentInfo=req.getDataGet(token)
                callError.value="Fetch Success"
            }
            catch (E:Exception)
            {
                callError.value="Fetch Failed"
            }
        }
    }
}