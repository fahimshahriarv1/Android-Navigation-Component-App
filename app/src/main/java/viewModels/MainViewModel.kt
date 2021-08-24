package viewModels
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel:ViewModel(){
    var num=ObservableField<String>()
    var numMinus= MutableLiveData("0")
    var numPlus= MutableLiveData("0")
    var mAction = MutableLiveData<Boolean>()

    fun changeData()
    {
        numPlus.value= num.get()?.toInt()?.plus(100).toString()
        numMinus.value= (100 - num.get()?.toInt()!!).toString()
    }

    fun getAction(){
        mAction.value=true
    }
}