package ru.leonidm.dialogs.paths.entities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import ru.leonidm.dialogs.api.DialogsAPI;
import ru.leonidm.dialogs.paths.ParticleRunnable;

import java.util.*;

public class ConfigurablePath {

    private static final Map<String, ConfigurablePath> paths = new HashMap<>();

    public static void clear() {
        paths.clear();
    }

    public static void add(String name, World world, List<Point> points, Particle.DustTransition dustOptions, EnumMap<Condition, List<String>> conditions) {
        paths.put(name, new ConfigurablePath(world, points, dustOptions, conditions));
    }

    public static ConfigurablePath get(String name) {
        return paths.get(name);
    }

    public static void checkPlayer(Player player) {
        List<ConfigurablePath> out = new ArrayList<>();
        for(ConfigurablePath path : paths.values()) {
            if(path.checkConditions(player)) out.add(path);
        }

        if(out.isEmpty()) ParticleRunnable.removePlayer(player);
        else ParticleRunnable.addPlayer(player, out);
    }

    private final List<Location> locations = new ArrayList<>();
    private final Particle.DustTransition dustOptions;
    private final EnumMap<Condition, List<String>> conditions;

    private ConfigurablePath(World world, List<Point> points, Particle.DustTransition dustOptions, EnumMap<Condition, List<String>> conditions) {
        this.dustOptions = dustOptions;
        this.conditions = conditions;

        Location pointLocation = new Location(world, 0, 0, 0);
        for(int pointIterator = 0; pointIterator < points.size() - 1; pointIterator++) {
            Point from = points.get(pointIterator);
            Point to = points.get(pointIterator + 1);

            double distance = (int) Math.round(from.distance(to));

            pointLocation.setX(from.x());
            pointLocation.setY(from.y());
            pointLocation.setZ(from.z());

            double deltaX = to.deltaX(from) / distance;
            double deltaY = to.deltaY(from) / distance;
            double deltaZ = to.deltaZ(from) / distance;

            for(int i = 0; i < distance; i++) {
                locations.add(pointLocation.clone());
                pointLocation.add(deltaX, deltaY, deltaZ);
            }
        }
    }

    public boolean checkConditions(Player player) {
        List<String> emptyList = new ArrayList<>();
        List<String> playerHas = DialogsAPI.getReadDialogsNames(player, emptyList);

        for(String toCheck : conditions.get(Condition.BEFORE_DIALOGS))
            if(playerHas.contains(toCheck)) return false;

        for(String toCheck : conditions.get(Condition.AFTER_DIALOGS))
            if(!playerHas.contains(toCheck)) return false;

        playerHas = DialogsAPI.getCompletedQuestsNames(player, emptyList);
        for(String toCheck : conditions.get(Condition.BEFORE_QUESTS))
            if(playerHas.contains(toCheck)) return false;

        for(String toCheck : conditions.get(Condition.AFTER_QUESTS))
            if(!playerHas.contains(toCheck)) return false;

        playerHas = DialogsAPI.getQuestsInProgressNames(player, emptyList);
        for(String toCheck : conditions.get(Condition.WHEN_QUESTS))
            if(!playerHas.contains(toCheck)) return false;

        return true;
    }

    public void spawnParticle(Player player) {
        for(Location location : locations) {
            player.spawnParticle(Particle.DUST_COLOR_TRANSITION, location, 1, dustOptions);
        }
    }
}
