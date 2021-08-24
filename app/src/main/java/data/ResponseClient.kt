package data

data class ResponseClient(
    val code: String,
    val message: String,
    val response: Responses
)

data class Responses(
    val count: Int?,
    val `data`: List<Data>?,
    val limit: Int?,
    val offset: Int?
)

data class Data(
    val amount: Int?,
    val bank: Any?,
    val branch: Any?,
    val depositDate: String?,
    val depositedTo: Any?,
    val gateway: String?,
    val givenAmount: Int?,
    val note: Any?,
    val paymentMode: String?,
    val paymentStatus: String?,
    val reference: String?,
    val serviceCharge: Float?,
    val servicePercent: Float?
)
