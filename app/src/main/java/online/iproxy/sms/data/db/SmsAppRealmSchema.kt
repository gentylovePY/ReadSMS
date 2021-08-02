package online.iproxy.sms.data.db

import io.realm.annotations.RealmModule
import online.iproxy.sms.data.db.model.TextMessageRealmObj
import online.iproxy.sms.data.db.model.UserRealmObj

@RealmModule(
    library = false,
    classes = [
        UserRealmObj::class,
        TextMessageRealmObj::class,
    ]
)
class SmsAppRealmSchema