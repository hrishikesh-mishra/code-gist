package com.hrishikeshmishra.intellij.plugin.code.service;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

import java.security.acl.Group;

public class Notify {

    private static final String NOTIFICATION_GROUP = "CODE GIST";

    public static void error(String title, String content) {
        Notifications.Bus.notify(new Notification(NOTIFICATION_GROUP, title, content, NotificationType.ERROR));
    }

    public static void warn(String title, String content) {
        Notifications.Bus.notify(new Notification(NOTIFICATION_GROUP, title, content, NotificationType.WARNING));
    }

    public static void info(String title, String content) {
        Notifications.Bus.notify(new Notification(NOTIFICATION_GROUP, title, content, NotificationType.INFORMATION));
    }

}
