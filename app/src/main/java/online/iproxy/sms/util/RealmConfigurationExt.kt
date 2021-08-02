package online.iproxy.sms.util

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import java.util.concurrent.atomic.AtomicReference

inline fun <R> RealmConfiguration.doWithRealm(action: (Realm) -> R): R {
    return Realm.getInstance(this).use(action)
}

suspend inline fun <R> RealmConfiguration.doWithRealmTransaction(
    crossinline action: (Realm) -> R
): R {
    return doWithRealm {
        val result = AtomicReference<R>()
        it.executeTransactionAwait { realm ->
            result.compareAndSet(null, action(realm))
        }
        result.get()
    }
}