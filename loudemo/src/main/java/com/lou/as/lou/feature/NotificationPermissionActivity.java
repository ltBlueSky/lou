package com.lou.as.lou.feature;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.lyloou.lou.activity.LouActivity;
import com.lyloou.lou.util.Uapp;
import com.lyloou.lou.util.Usp;

public class NotificationPermissionActivity extends LouActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        String text;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            text = "系统版本太低，无法检测";
        } else if (Uapp.isNotificationEnabled(this)) {
            text = "已开启通知权限";
        } else {
            text = "通知权限尚未开启, 点我去开启";
        }
        textView.setText(text);
        textView.setOnClickListener(v -> enableNotificationPermission(this));
        setContentView(textView);
    }

    private void enableNotificationPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Uapp.isNotificationEnabled(context)) {
            return;
        }

        String KEY_SP_CLOSE_NOTIFICATION = "KEY_SP_CLOSE_NOTIFICATION";
        if (Usp.init(context).getBoolean(KEY_SP_CLOSE_NOTIFICATION, false)) {
            new AlertDialog.Builder(context,
                    android.R.style.Theme_DeviceDefault_Light_Dialog)
                    .setTitle("您已取消，是否仍然开启？")
                    .setPositiveButton("继续", (dialog, which) -> {
                        Usp.init(context).remove(KEY_SP_CLOSE_NOTIFICATION).commit();
                        enableNotificationPermission(context);
                    })
                    .setNegativeButton("否", ((dialog, which) -> {
                    }))
                    .create()
                    .show();
            return;
        }

        AlertDialog notificationDialog = new AlertDialog.Builder(context,
                android.R.style.Theme_DeviceDefault_Light_Dialog)
                .setTitle("友情提示").setMessage("为了提供更优质的服务，请您开启通知权限")
                .setNegativeButton("不用了", (dialog, which) -> Usp.init(context)
                        .putBoolean(KEY_SP_CLOSE_NOTIFICATION, true)
                        .commit())
                .setNeutralButton("下次再说", (dialog, which) -> {
                    // do nothing
                })
                .setPositiveButton("去开启", (dialog, which) -> {
                    Uapp.openAppDetailSettings(context);
                })
                .setCancelable(false)
                .create();
        notificationDialog.setCanceledOnTouchOutside(false);
        notificationDialog.show();
    }
}
