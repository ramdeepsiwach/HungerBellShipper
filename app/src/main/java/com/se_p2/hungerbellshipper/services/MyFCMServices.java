package com.se_p2.hungerbellshipper.services;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.se_p2.hungerbellshipper.common.Common;
import com.se_p2.hungerbellshipper.model.UpdateShippingOrderEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;

public class MyFCMServices extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Map<String,String> dataRecv=remoteMessage.getData();
        if(dataRecv!=null){
            Common.showNotification(this,new Random().nextInt(),
                    dataRecv.get(Common.NOTI_TITLE),
                    dataRecv.get(Common.NOTI_CONTENT),
                    null);

            EventBus.getDefault().postSticky(new UpdateShippingOrderEvent());
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Common.updateToken(this,s,false,true);
    }
}
