package com.sky;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TitleManager {
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);

            Class<?> enumTitleAction = Class.forName("net.minecraft.server." + getServerVersion() + ".PacketPlayOutTitle$EnumTitleAction");
            Class<?> packetPlayOutTitleClass = Class.forName("net.minecraft.server." + getServerVersion() + ".PacketPlayOutTitle");
            Class<?> iChatBaseComponentClass = Class.forName("net.minecraft.server." + getServerVersion() + ".IChatBaseComponent");
            Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + getServerVersion() + ".IChatBaseComponent$ChatSerializer");

            Object titleComponent = chatSerializerClass.getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
            Object subtitleComponent = chatSerializerClass.getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");

            Constructor<?> packetConstructor = packetPlayOutTitleClass.getConstructor(enumTitleAction, iChatBaseComponentClass, int.class, int.class, int.class);

            Object packetTitle = packetConstructor.newInstance(enumTitleAction.getEnumConstants()[0], titleComponent, fadeIn, stay, fadeOut);
            Object packetSubtitle = packetConstructor.newInstance(enumTitleAction.getEnumConstants()[1], subtitleComponent, fadeIn, stay, fadeOut);

            Method sendPacket = playerConnection.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server." + getServerVersion() + ".Packet"));
            sendPacket.invoke(playerConnection, packetTitle);
            sendPacket.invoke(playerConnection, packetSubtitle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getServerVersion() {
        String name = org.bukkit.Bukkit.getServer().getClass().getPackage().getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }
}