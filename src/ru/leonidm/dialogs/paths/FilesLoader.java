package ru.leonidm.dialogs.paths;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.leonidm.dialogs.paths.entities.Condition;
import ru.leonidm.dialogs.paths.entities.ConfigurablePath;
import ru.leonidm.dialogs.paths.entities.Point;

import java.io.File;
import java.util.*;

public class FilesLoader {

    static {
        reload();
    }

    private static Map<String, File> getAllFilesWithRecursion(String path) {
        if(path.endsWith("/")) path = path.substring(0, path.length() - 1);
        Map<String, File> output = new HashMap<>();
        for(File f : new File(path).listFiles()) {
            if(f.isDirectory()) output.putAll(getAllFilesWithRecursion(path + "/" + f.getName()));
            else if(f.getName().endsWith(".yml") || f.getName().endsWith(".yaml")) output.put(path + "/" + f.getName(), f);
        }
        return output;
    }

    public static void reload() {
        ConfigurablePath.clear();
        File pathsDirectory = new File("plugins/DialogsM/paths/");
        if(pathsDirectory.mkdirs()) return;

        Map<String, File> files = getAllFilesWithRecursion("plugins/DialogsM/paths/");

        for(Map.Entry<String, File> fileEntry : files.entrySet()) {
            FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(fileEntry.getValue());

            ConfigurationSection conditions = fileConfiguration.getConfigurationSection("conditions");
            if(conditions == null) {
                DialogsPaths.warn("[" + fileEntry.getKey() + "] Parameter 'conditions' is null!");
                continue;
            }

            EnumMap<Condition, List<String>> pushConditions = new EnumMap<>(Condition.class);

            for(Condition conditionEnum : Condition.values()) {
                List<String> condition = conditions.getStringList(conditionEnum.toString());
                if(condition.isEmpty()) {
                    String singleCondition = conditions.getString(conditionEnum.toString());
                    if(singleCondition != null) condition = Collections.singletonList(singleCondition);
                }
                pushConditions.put(conditionEnum, condition);
            }

            String worldName = fileConfiguration.getString("world");
            if(worldName == null) worldName = "world";

            World world = Bukkit.getWorld(worldName);
            if(world == null) {
                DialogsPaths.warn("[" + fileEntry.getKey() + "] Parameter 'world' doesn't represent existing world!");
                continue;
            }

            List<?> points = fileConfiguration.getList("points");
            if(points == null) {
                DialogsPaths.warn("[" + fileEntry.getKey() + "] Parameter 'points' is null!");
                continue;
            }

            List<Point> outPoints = new ArrayList<>();
            for(Object point : points) {
                if(!(point instanceof List<?> list)) {
                    DialogsPaths.warn("[" + fileEntry.getKey() + "] Point '" + point + "'isn't represented by the List!");
                    continue;
                }

                if(list.size() != 3) {
                    DialogsPaths.warn("[" + fileEntry.getKey() + "] Point '" + list + "' have less or more than 3 values!");
                    continue;
                }

                Object x = list.get(0), y = list.get(1), z = list.get(2);
                if(x instanceof Integer && y instanceof Integer && z instanceof Integer) {
                    outPoints.add(new Point((int) x + 0.5, (int) y, (int) z + 0.5));
                }
                else {
                    DialogsPaths.warn("[" + fileEntry.getKey() + "] At least one of the value of the point '" + list + "' isn't represented by the Integer!");
                }
            }
            if(outPoints.size() < 2) {
                DialogsPaths.warn("[" + fileEntry.getKey() + "] There must be 2 or more points!");
                continue;
            }

            List<Integer> particleColor = fileConfiguration.getIntegerList("rgb");
            if(particleColor.size() != 3) {
                DialogsPaths.warn("[" + fileEntry.getKey() + "] List 'rgb' have less or more than 3 values!");
                continue;
            }

            double size = fileConfiguration.getDouble("size");
            if(size == 0) size = 1;

            Color color = Color.fromRGB(particleColor.get(0), particleColor.get(1), particleColor.get(2));
            Particle.DustTransition dustTransition = new Particle.DustTransition(color, color, (float) size);

            ConfigurablePath.add(fileEntry.getKey(), world, outPoints, dustTransition, pushConditions);
        }
    }
}
