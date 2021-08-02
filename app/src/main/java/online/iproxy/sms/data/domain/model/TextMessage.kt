package online.iproxy.sms.data.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import online.iproxy.sms.data.db.model.TextMessageRealmObj
import org.bson.types.ObjectId

@Parcelize
data class TextMessage(
    val id: String = ObjectId.get().toHexString(),
    val timestamp: Long = System.currentTimeMillis(),
    val from: String = "",
    val body: String = "",
) : Parcelable

fun TextMessage.toRealmObj() = TextMessageRealmObj(
    id = ObjectId(id),
    timestamp = timestamp,
    from = from,
    body = body
)

fun TextMessageRealmObj.toTextMessage() = TextMessage(
    id = id.toHexString(),
    timestamp = timestamp,
    from = from,
    body = body
)