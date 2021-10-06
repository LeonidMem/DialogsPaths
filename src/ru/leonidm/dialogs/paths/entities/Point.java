package ru.leonidm.dialogs.paths.entities;

import org.jetbrains.annotations.NotNull;

public record Point(double x, double y, double z) {

    public double distance(@NotNull Point p) {
        return Math.sqrt((x - p.x)*(x - p.x) + (y - p.y)*(y - p.y) + (z - p.z)*(z - p.z));
    }

    public double deltaX(@NotNull Point p) {
        return x - p.x;
    }

    public double deltaY(@NotNull Point p) {
        return y - p.y;
    }

    public double deltaZ(@NotNull Point p) {
        return z - p.z;
    }
}
