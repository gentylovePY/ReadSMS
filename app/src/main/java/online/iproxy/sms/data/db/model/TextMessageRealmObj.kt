package online.iproxy.sms.data.db.model

import io.realm.RealmModel
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import org.bson.types.ObjectId

@RealmClass
open class TextMessageRealmObj(
    @PrimaryKey
    var id: ObjectId = ObjectId(),
    @Index
    var timestamp: Long = 0,
    var from: String = "",
    var body: String = "",
) : RealmModel