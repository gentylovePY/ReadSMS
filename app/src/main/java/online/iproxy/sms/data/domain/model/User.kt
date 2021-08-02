package online.iproxy.sms.data.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import online.iproxy.sms.data.api.model.UserResponse
import online.iproxy.sms.data.db.model.UserRealmObj

@Parcelize
data class User(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
) : Parcelable

fun User.toRealmObj() = UserRealmObj(
    id = id,
    email = email,
    firstName = firstName,
    lastName = lastName,
)

fun UserRealmObj.toUser() = User(
    id = id,
    email = email,
    firstName = firstName,
    lastName = lastName,
)

fun UserResponse.toUser() = User(
    id = id,
    email = email,
    firstName = firstName ?: "",
    lastName = lastName ?: "",
)