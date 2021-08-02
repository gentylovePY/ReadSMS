package online.iproxy.sms.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony.Sms.Intents
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import online.iproxy.sms.data.domain.repo.MessageRepository
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SmsReceiver : BroadcastReceiver() {

    private val log get() = Timber.tag("SmsReceiver")

    @Inject
    lateinit var messageRepo: MessageRepository

    override fun onReceive(context: Context, intent: Intent?) {
        when (intent?.action) {
            Intents.SMS_RECEIVED_ACTION -> {
                handleSmsIntent(intent, isData = false)
            }
            Intents.DATA_SMS_RECEIVED_ACTION -> {
                handleSmsIntent(intent, isData = true)
            }
            else -> {
                // do nothing
            }
        }
    }

    private fun handleSmsIntent(intent: Intent, isData: Boolean) {
        if (isData) {
            // todo: support data messages
            log.d("Data messages are not supported")
            return
        }

        val messages = Intents.getMessagesFromIntent(intent)
        messages.orEmpty().forEach { message ->
            val timestamp = message.timestampMillis
            val fromAddress = message.originatingAddress.orEmpty()
            val text = message.messageBody.orEmpty()
            log.d(
                "Text message received: timestamp = %d, from = %s, text = %s",
                timestamp, fromAddress, text,
            )
            // todo: get rid of blocking calls
            val result = runBlocking {
                messageRepo.saveTextMessage(
                    timestamp = timestamp,
                    from = fromAddress,
                    text = text
                )
            }
            result.onSuccess { log.d("Text message saved") }
                .onFailure { log.e(it, "Failed to save text message") }
        }
    }


}