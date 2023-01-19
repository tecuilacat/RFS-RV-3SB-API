package nav;

import java.text.DecimalFormat;

public class Position {

    private double x, y, z, a, b, c;

    public Position(double x, double y, double z, double a, double b, double c) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Position copy() {
        return new Position(x,y,z,a,b,c);
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("##0.00");
        return "(" +
                convertValue(x, df) +
                "," +
                convertValue(y, df) +
                "," +
                convertValue(z, df) +
                "," +
                convertValue(a, df) +
                "," +
                convertValue(b, df) +
                "," +
                convertValue(c, df) +
                ")";
    }

    private String convertValue(double val, DecimalFormat df) {
        return df.format(val).replaceAll(",", ".");
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Double.compare(position.x, x) == 0
                && Double.compare(position.y, y) == 0
                && Double.compare(position.z, z) == 0
                && Double.compare(position.a, a) == 0
                && Double.compare(position.b, b) == 0
                && Double.compare(position.c, c) == 0;
    }

    public double getDirectDistanceToPosition(Position position) {
        double distance;
        double differenceX = position.getX() - x;
        double differenceY = position.getY() - y;
        double differenceZ = position.getZ() - z;
        distance = Math.sqrt(Math.pow(differenceX, 2) + Math.pow(differenceY, 2));
        distance = Math.sqrt(Math.pow(distance, 2) + Math.pow(differenceZ, 2));
        return distance;
    }
}
