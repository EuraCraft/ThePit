package fr.maximouz.thepit.utils;

import java.io.*;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.google.common.io.ByteStreams;

public class Configuration {

    private final Plugin plugin;
    private final File file;
    private FileConfiguration config;

    public Configuration(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "/"+fileName+".yml");
    }

    public void saveDefaultConfig() {
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
        try {
            if (!file.exists()) {
                file.createNewFile();
                InputStream inputStream = plugin.getResource(file.getName());
                OutputStream outputStream = new FileOutputStream(file);
                ByteStreams.copy(inputStream, outputStream);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        if(config == null) this.config = YamlConfiguration.loadConfiguration(file);
        return config;
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

}
