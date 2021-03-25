package fr.maximouz.thepit.Tag;

import java.lang.reflect.Constructor;
import java.util.*;

import fr.maximouz.thepit.utils.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Tag {

    private final Player player;
    private String prefix;
    private final String priority;
    private String suffix;

    public Tag(Player player, String prefix, String priority, String suffix) {
        this.player = player;
        this.prefix = prefix;
        this.priority = priority;
        this.suffix = suffix;
        settingTab();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getPriority() {
        return priority;
    }

    public String getPrefix() {
        return prefix;
    }

    private void settingTab() {

        clearTab();

        String team_name = priority + player.getName();
        if (team_name.length() > 16) {
            team_name = team_name.substring(0, 16);
        }

        if (suffix.length() > 16) {
            setSuffix(suffix.substring(0, 16));
        }

        if (prefix.length() > 16) {
            setPrefix(prefix.substring(0, 16));
        }

        try {

            Constructor<?> constructor = Objects.requireNonNull(Reflection.getNMSClass("PacketPlayOutScoreboardTeam")).getConstructor();
            Object packet = constructor.newInstance();
            List<String> contents = new ArrayList<>();
            contents.add(player.getName());

            try {

                Reflection.setField(packet, "a", team_name);
                Reflection.setField(packet, "b", team_name);
                Reflection.setField(packet, "c", prefix);
                Reflection.setField(packet, "d", suffix);
                Reflection.setField(packet, "e", "ALWAYS");
                Reflection.setField(packet, "h", 0);
                Reflection.setField(packet, "g", contents);

            } catch (Exception ex) {

                Reflection.setField(packet, "a", team_name);
                Reflection.setField(packet, "b", team_name);
                Reflection.setField(packet, "c", prefix);
                Reflection.setField(packet, "d", suffix);
                Reflection.setField(packet, "e", "ALWAYS");
                Reflection.setField(packet, "i", 0);
                Reflection.setField(packet, "h", contents);

            }

            Bukkit.getOnlinePlayers().forEach(target -> Reflection.sendPacket(target, packet));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void clearTab() {

        try {

            String team_name = priority + player.getName();

            if (team_name.length() > 16) {
                team_name = team_name.substring(0, 16);
            }

            Constructor<?> constructor = Objects.requireNonNull(Reflection.getNMSClass("PacketPlayOutScoreboardTeam")).getConstructor();
            Object packet = constructor.newInstance();
            List<String> contents = new ArrayList<>();
            contents.add(priority + player.getName());

            try {

                Reflection.setField(packet, "a", team_name);
                Reflection.setField(packet, "b", team_name);
                Reflection.setField(packet, "e", "ALWAYS");
                Reflection.setField(packet, "h", 1);
                Reflection.setField(packet, "g", contents);

            } catch (Exception ex) {

                Reflection.setField(packet, "a", team_name);
                Reflection.setField(packet, "b", team_name);
                Reflection.setField(packet, "e", "ALWAYS");
                Reflection.setField(packet, "i", 1);
                Reflection.setField(packet, "h", contents);

            }

            Bukkit.getOnlinePlayers().forEach(target -> Reflection.sendPacket(target, packet));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
