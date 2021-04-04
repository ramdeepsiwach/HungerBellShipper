package com.se_p2.hungerbellshipper.common;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.FirebaseDatabase;
import com.se_p2.hungerbellshipper.model.ShipperUserModel;
import com.se_p2.hungerbellshipper.model.TokenModel;
import com.se_p2.hungerbellshipper.R;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.NotificationCompat;

public class Common {
    public static final String SHIPPER_REF = "Shipper";
    public static final String ORDER_REF = "Order";
    public static final String NOTI_TITLE = "title";
    public static final String NOTI_CONTENT = "content";
    public static final String TOKEN_REF = "Tokens";
    public static final String SHIPPING_ORDER_REF = "ShippingOrder";
    public static final String SHIPPING_ORDER_DATA = "ShippingData";
    public static final String TRIP_START = "Trip";

    public static ShipperUserModel currentShipperUser;

    public static void setSpanStringColor(String s, String format, TextView txt_time, int Color) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(s);
        SpannableString spannableString = new SpannableString(format);
        spannableString.setSpan(new ForegroundColorSpan(Color), 0, format.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableString);
        txt_time.setText(builder, TextView.BufferType.SPANNABLE);
    }

    /*public static String convertStatusToString(int orderStatus) {
        switch (orderStatus) {
            case 0:
                return "Placed";
            case 1:
                return "Shipping";
            case 2:
                return "Shipped";
            case -1:
                return "Cancelled";
            default:
                return "Error";
        }
    }

    public static String createTopicOrder() {
        return "/topics/new_order";
    }*/

    public static void updateToken(Context context, String newToken, boolean isServer, boolean isShipper) {
        if (Common.currentShipperUser != null) {
            FirebaseDatabase.getInstance()
                    .getReference(Common.TOKEN_REF)
                    .child(Common.currentShipperUser.getUid())
                    .setValue(new TokenModel(Common.currentShipperUser.getPhone(), newToken, isServer, isShipper))
                    .addOnFailureListener(e -> Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    public static void showNotification(Context context, int id, String title, String content, Intent intent) {
        PendingIntent pendingIntent = null;
        if (intent != null) {
            pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        String NOTIFICATION_CHANNEL_ID = "hunger_bells";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Hunger Bell", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Hunger Bells");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_restaurant_menu_24));
        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
        }
        Notification notification = builder.build();
        notificationManager.notify(id, notification);

    }

    public static float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

    public static List<LatLng> decodePoly(String encoded) {
        ArrayList poly = new ArrayList();
        int index = 0, len = encoded.length();

        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dLat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dLat;

            shift = 0;
            result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dLng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dLng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    public static String buildLocationString(Location location) {
        return location.getLatitude() + "," +
                location.getLongitude();
    }
}
