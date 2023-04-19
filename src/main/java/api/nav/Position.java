package api.nav;

import java.text.DecimalFormat;

public class Position {

    private double x, y, z, a = 180.0, b = 0.0, c = 180.0;

    public Position(double x, double y, double z, double a, double b, double c) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return New Position with the same parameters
     */
    public Position copy() {
        return new Position(x, y, z, a, b, c);
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

    /**
     * Paramers are split by ',' and commas within a position (as strings) are displayed by a '.'
     */
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

    /**
     * Alters the z-index by a value RELATIVE TO THE VACUUM PUMP (TCP) of the robotarm
     * IMPORTANT: Z index works exaclty like in a MELFA BASIC 4 Program. So PSAFE, -50 in MELFA BASIC 4 will be pSafe.alterZ(-50). NOT 50
     *
     * @param value Value by which the z index is to be altered (neg value: up; pos value: down)
     * @return COPY of position with altered z-index
     */
    public Position alterZ(double value) {
        Position tmp = this.copy();
        tmp.setZ(tmp.getZ() - value); // x--y = x+y
        return tmp;
    }

    /**
     * Returns a COPY of the current position with an absolut z-index
     *
     * @param value Absolute Z-index of position
     * @return COPY of position with absolute z-index
     */
    public Position alterAbsoluteZ(double value) {
        Position tmp = this.copy();
        tmp.setZ(value);
        return tmp;
    }

    /**
     * Calculates the relative position to the current position which you need to move the robot to a certain position
     *
     * @param sourcePosition Position the robot currently is at
     * @param targetPosition Position you want to go to
     * @return Relative position to the current position
     */
    public static Position getDifferenceToPosition(Position sourcePosition, Position targetPosition) {
        return new Position(
                targetPosition.getX() - sourcePosition.getX(),
                targetPosition.getY() - sourcePosition.getY(),
                targetPosition.getZ() - sourcePosition.getZ(),
                targetPosition.getA() - sourcePosition.getA(),
                targetPosition.getB() - sourcePosition.getB(),
                targetPosition.getC() - sourcePosition.getC()
        );
    }
}
