package ru.leonidm.dialogs.paths;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import ru.leonidm.dialogs.paths.entities.ConfigurablePath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticleRunnable extends BukkitRunnable {

    private static final Map<Player, List<ConfigurablePath>> paths = new HashMap<>();

    public static void addPlayer(@NotNull Player player, List<ConfigurablePath> paths) {
        ParticleRunnable.paths.put(player, paths);
    }

    public static void removePlayer(@NotNull Player player) {
        paths.remove(player);
    }

    private static ParticleRunnable instance = null;

    private ParticleRunnable() {
        runTaskTimer(DialogsPaths.getInstance(), 0, 10);
    }

    public static void start() {
        if(instance == null) instance = new ParticleRunnable();
    }

    public static void stop() {
        if(instance == null) return;

        instance.cancel();
        instance = null;
    }

    @Override
    public void run() {
        for(Map.Entry<Player, List<ConfigurablePath>> entry : paths.entrySet()) {
            entry.getValue().forEach(path -> path.spawnParticle(entry.getKey()));
        }
    }

}
