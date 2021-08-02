package online.iproxy.sms.data.domain.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import online.iproxy.sms.data.db.MessageStorage
import online.iproxy.sms.data.domain.model.TextMessage
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    private val messageStorage: MessageStorage
) {

    private val log get() = Timber.tag("MessageRepository")
    private val latestMessage = MutableStateFlow<TextMessage?>(null)

    suspend fun saveTextMessage(timestamp: Long, from: String, text: String): Result<TextMessage> {
        return runCatching {
            val message = TextMessage(
                timestamp = timestamp,
                from = from,
                body = text
            )
            messageStorage.saveMessage(message)
            latestMessage.value = message
            message
        }
    }

    suspend fun getMessagesBefore(
        before: Long = Long.MAX_VALUE - 1,
        limit: Int = 50
    ): Result<List<TextMessage>> {
        require(limit >= 0) {
            "Limit must not be negative"
        }
        return runCatching {
            messageStorage.getMessages(before = before, limit = limit.toLong())
        }
    }

    suspend fun observeMessagesBefore(
        before: Long = Long.MAX_VALUE - 1,
        limit: Int = 50
    ): Flow<List<TextMessage>> {
        require(limit >= 0) {
            "Limit must not be negative"
        }
        return latestMessage.asStateFlow()
            .filterNotNull()
            .transformLatest {
                if (it.timestamp <= before) {
                    emit(messageStorage.getMessages(before = before, limit = limit.toLong()))
                }
            }
            .onStart {
                emit(messageStorage.getMessages(before = before, limit = limit.toLong()))
            }
            .flowOn(Dispatchers.Default)
    }

}