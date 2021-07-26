package com.example.readsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import org.json.JSONObject;

public class ReceiveSms extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {



        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            String msg_from ;
            if (bundle!=null){
                try {
                    Object[] pdus = (Object[]) bundle.get ("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i =0; i<msgs.length;i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msBodu = msgs[i].getMessageBody();
                        Toast.makeText(context,msg_from + " : "+msBodu,Toast.LENGTH_SHORT).show();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }
}
