package me.prism3.suicide.utils;

import me.prism3.suicide.Main;

import java.util.List;

public class Data {

    private final Main main = Main.getInstance();

    public static String playedSound;

    public static String suicideReload;
    public static String suicideBypass;

    public static long coolDownTime;

    public static int resourceID;
    public static int soundVolume;
    public static int soundPitch;

    public static boolean isCoolDown;
    public static boolean isBroadCast;
    public static boolean isBroadCastRandom;
    public static boolean isMessage;
    public static boolean isFirework;
    public static boolean isCoords;
    public static boolean isSound;

    public static List<String> disabledWorlds;
    public static List<String> broadCast;
    public static List<String> commandAliases;

    public void initializeStrings(){

        playedSound = this.main.getConfig().getString("Sound.SoundPlayed");

    }

    public void initializeStringPermissions(){

        suicideReload = "suicide.reload";
        suicideBypass = "suicide.bypass";

    }

    public void initializeLongs(){

        coolDownTime = this.main.getConfig().getLong("Cooldown.Timer");

    }

    public void initializeIntegers(){

        resourceID = 93367;
        soundVolume = this.main.getConfig().getInt("Sound.Volume");
        soundPitch = this.main.getConfig().getInt("Sound.Pitch");

    }

    public void initializeBooleans(){

        isCoolDown = this.main.getConfig().getBoolean("Cooldown.Disable");
        isBroadCast = this.main.getConfig().getBoolean("Broadcast");
        isBroadCastRandom = this.main.getConfig().getBoolean("Messages.Broadcast.Random");
        isMessage = this.main.getConfig().getBoolean("Message");
        isFirework = this.main.getConfig().getBoolean("Firework");
        isCoords = this.main.getConfig().getBoolean("Coords");
        isSound = this.main.getConfig().getBoolean("Sound.Disable");

    }

    public void initializeLists(){

        disabledWorlds = this.main.getConfig().getStringList("Disabled-Worlds");
        broadCast = this.main.getConfig().getStringList("Messages.Broadcast.Messages");
        commandAliases = this.main.getConfig().getStringList("Aliases");

    }

}
