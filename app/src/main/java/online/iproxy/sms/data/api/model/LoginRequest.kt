package online.iproxy.sms.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class LoginRequest {

    @Serializable
    class Password(
        @SerialName("email")
        val email: String,
        @SerialName("password")
        val password: String,
        @SerialName("installationId")
        val installationId: String
    ) : LoginRequest()

    @Serializable
    class Pin(
        @SerialName("pin")
        val pin: String,
        @SerialName("installationId")
        val installationId: String
    ) : LoginRequest()
}
