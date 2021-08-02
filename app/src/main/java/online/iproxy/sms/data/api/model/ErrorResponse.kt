package online.iproxy.sms.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ErrorResponse(
    @SerialName("code")
    val code: String = "",
    @SerialName("message")
    val message: String? = null
)
