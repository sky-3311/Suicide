package me.prism3.suicide.utils;

import me.prism3.suicide.Main;
import me.prism3.suicide.commands.Suicide;
import me.prism3.suicide.events.EntityDamage;
import me.prism3.suicide.events.PlayerDeath;

import java.util.List;


public class Data {

    private static final Main main = Main.getInstance();

    public static String playedSound;
    public static String suicideReload;
    public static String suicideBypass;
    public static String suicideCommand;
    public static String suicideMessage;
    public static String noPermissionMessage;
    public static String reloadMessage;
    public static String invalidSyntaxMessage;
    public static String disabledWorldMessage;
    public static String coolDownMessage;
    public static String fireworkType;

    public static long coolDownTime;

    public static int resourceID;
    public static int soundVolume;
    public static int soundPitch;
    public static int fireworkPower;
    public static int fireworkColorRed;
    public static int fireworkColorGreen;
    public static int fireworkColorBlue;
    public static int fireworkFadeColorRed;
    public static int fireworkFadeColorGreen;
    public static int fireworkFadeColorBlue;


    public static boolean isCoolDown;
    public static boolean isBroadCast;
    public static boolean isMessage;
    public static boolean isFirework;
    public static boolean isFireworkTrail;
    public static boolean isFireworkFlicker;
    public static boolean isCoords;
    public static boolean isSound;

    public static List<String> disabledWorlds;
    public static List<String> broadCastMessages;
    public static List<String> commandAliases;

    public static void initializer() {

        initializeStrings();
        initializeLongs();
        initializeIntegers();
        initializeBooleans();
        initializeLists();
        initializePermissions();
        initializeEvents();
        initializeCommands();
    }

    private static void initializeStrings() {

        playedSound = main.getConfig().getString("Sound.SoundPlayed");
        suicideMessage = main.getConfig().getString("Messages.On-Suicide");
        noPermissionMessage = main.getConfig().getString("Messages.No-Permission");
        reloadMessage = main.getConfig().getString("Messages.Reload");
        invalidSyntaxMessage = main.getConfig().getString("Messages.Invalid-Syntax");
        disabledWorldMessage = main.getConfig().getString("Messages.Disabled");
        coolDownMessage = main.getConfig().getString("Messages.On-Cooldown");
        fireworkType = main.getConfig().getString("Firework.Type").toUpperCase();
    }

    private static void initializeLongs() {
        coolDownTime = main.getConfig().getLong("Cooldown.Timer");
    }

    private static void initializeIntegers() {

        resourceID = 93367;
        soundVolume = main.getConfig().getInt("Sound.Volume");
        soundPitch = main.getConfig().getInt("Sound.Pitch");
        fireworkColorRed = main.getConfig().getInt("Firework.Color.RED");
        fireworkColorGreen = main.getConfig().getInt("Firework.Color.GREEN");
        fireworkColorBlue = main.getConfig().getInt("Firework.Color.BLUE");
        fireworkFadeColorRed = main.getConfig().getInt("Firework.Fade.RED");
        fireworkFadeColorRed = main.getConfig().getInt("Firework.Fade.GREEN");
        fireworkFadeColorRed = main.getConfig().getInt("Firework.Fade.BLUE");
        fireworkPower = main.getConfig().getInt("Firework.Power");
    }

    private static void initializeBooleans() {

        isCoolDown = main.getConfig().getBoolean("Cooldown.Disable");
        isBroadCast = main.getConfig().getBoolean("Broadcast");
        isMessage = main.getConfig().getBoolean("Message");
        isFirework = main.getConfig().getBoolean("Firework.Disable");
        isFireworkTrail = main.getConfig().getBoolean("Firework.Trail");
        isFireworkFlicker = main.getConfig().getBoolean("Firework.Flicker");
        isCoords = main.getConfig().getBoolean("Coords");
        isSound = main.getConfig().getBoolean("Sound.Disable");
    }

    private static void initializeLists() {

        disabledWorlds = main.getConfig().getStringList("Disabled-Worlds");
        broadCastMessages = main.getConfig().getStringList("Messages.Broadcast.Messages");
        commandAliases = main.getConfig().getStringList("Aliases");
    }

    private static void initializePermissions() {

        suicideCommand = "suicide.command";
        suicideReload = "suicide.reload";
        suicideBypass = "suicide.bypass";
    }

    private static void initializeEvents() {

        main.getServer().getPluginManager().registerEvents(new PlayerDeath(), main);
        main.getServer().getPluginManager().registerEvents(new EntityDamage(), main);
    }

    private static void initializeCommands() {
        main.getCommand("suicide").setExecutor(new Suicide());
    }
}
