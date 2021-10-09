package ru.leonidm.dialogs.paths.entities;

public record Point(double x, double y, double z) {

    public double distance(Point p) {
        return Math.sqrt((x - p.x)*(x - p.x) + (y - p.y)*(y - p.y) + (z - p.z)*(z - p.z));
    }

    public double deltaX(Point p) {
        return x - p.x;
    }

    public double deltaY(Point p) {
        return y - p.y;
    }

    public double deltaZ(Point p) {
        return z - p.z;
    }
}
