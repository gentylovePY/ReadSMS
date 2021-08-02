package online.iproxy.sms.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LoginResponse(
    @SerialName("error")
    val error: ErrorResponse? = null,
    @SerialName("phones")
    val phones: List<PhoneResponse>? = null,
    @SerialName("user")
    val user: UserResponse? = null,
    @SerialName("token")
    val token: String? = null,
) {
    @Serializable
    class PhoneResponse(
        @SerialName("id")
        val id: String,
        @SerialName("name")
        val name: String? = null,
        @SerialName("description")
        val description: String? = null,
    ) {

        @Serializable
        class PaymentPlanResponse(
            @SerialName("name")
            val name: String? = null,
        )
    }
}
