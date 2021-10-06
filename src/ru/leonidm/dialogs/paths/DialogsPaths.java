package ru.leonidm.dialogs.paths;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class DialogsPaths extends JavaPlugin {

    private static DialogsPaths instance;

    @Override
    public void onEnable() {
        instance = this;

        File exampleFile = new File("/plugins/DialogsM/paths/example.yml");
        if(exampleFile.mkdirs()) {
            try {
                exampleFile.createNewFile();
                InputStream is = getClass().getResourceAsStream("example.yml");

                OutputStream os = new FileOutputStream("/plugins/DialogsM/paths/example.yml");

                byte[] buffer = new byte[4096];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }

                os.close();
                is.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        Bukkit.getPluginManager().registerEvents(new EventsHandler(), this);
        ParticleRunnable.start();

        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled!");
    }

    public static DialogsPaths getInstance() {
        return instance;
    }

    public static void warn(String message) {
        instance.getLogger().warning(message);
    }
}

