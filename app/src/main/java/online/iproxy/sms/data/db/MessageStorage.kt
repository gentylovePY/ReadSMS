package online.iproxy.sms.data.db

import io.realm.RealmConfiguration
import io.realm.Sort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import online.iproxy.sms.data.db.model.TextMessageRealmObj
import online.iproxy.sms.data.domain.model.TextMessage
import online.iproxy.sms.data.domain.model.toRealmObj
import online.iproxy.sms.data.domain.model.toTextMessage
import online.iproxy.sms.util.doWithRealm
import online.iproxy.sms.util.doWithRealmTransaction
import org.bson.types.ObjectId
import javax.inject.Inject

class MessageStorage @Inject constructor(
    private val realmConfig: RealmConfiguration
) {

    suspend fun getMessages(before: Long, limit: Long): List<TextMessage> {
        return withContext(Dispatchers.IO) {
            realmConfig.doWithRealm { realm ->
                realm.where(TextMessageRealmObj::class.java)
                    .lessThanOrEqualTo("timestamp", before)
                    .sort("timestamp", Sort.DESCENDING, "id", Sort.DESCENDING)
                    .limit(limit)
                    .findAll()
                    .map { it.toTextMessage() }
            }
        }
    }

    suspend fun saveMessage(message: TextMessage) {
        withContext(Dispatchers.IO) {
            realmConfig.doWithRealmTransaction { realm ->
                realm.insertOrUpdate(message.toRealmObj())
            }
        }
    }

    suspend fun deleteMessage(id: String) {
        withContext(Dispatchers.IO) {
            realmConfig.doWithRealmTransaction { realm ->
                realm.where(TextMessageRealmObj::class.java)
                    .equalTo("id", ObjectId(id))
                    .findAll()
                    .deleteAllFromRealm()
            }
        }
    }

}