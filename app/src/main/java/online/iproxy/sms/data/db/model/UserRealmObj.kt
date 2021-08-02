package online.iproxy.sms.data.db.model

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class UserRealmObj(
    @PrimaryKey
    var id: String = "",
    var email: String = "",
    var firstName: String = "",
    var lastName: String = "",
) : RealmModel