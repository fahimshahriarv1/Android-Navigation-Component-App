package data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


data class ResponseB2b(

    var code: String?,
    var message: String?,
    var response: Response,

)

data class Response(
    var address: String?,
    var avatar: String?,
    var designation: String?,
    var dob: String?,
    var email: String?,
    var firstName: String?,
    var gender: String?,
    var lastName: String?,
    var mobileNumber: String?,
    var title: String?,
    var token: String?,
    var username: String?
)