public class Pixel {
    private final int col;
    private final int row;
    private final double energy;
    private double downTotalEnergy;
    private Pixel minUpperPre;
    private double rightTotalEnergy;
    private Pixel minLefterPre;

    public Pixel(int col, int row, double energy) {
        this.col = col;
        this.row = row;
        this.energy = energy;
        this.downTotalEnergy = Double.POSITIVE_INFINITY;
        this.minUpperPre = null;
        this.rightTotalEnergy = Double.POSITIVE_INFINITY;
        this.minLefterPre = null;
    }

    public int col() {
        return col;
    }

    public int row() {
        return row;
    }

    public double energy() {
        return energy;
    }

    public double downTotalEnergy() {
        return downTotalEnergy;
    }

    public void setDownTotalEnergy(double downTotalEnergy) {
        this.downTotalEnergy = downTotalEnergy;
    }

    public double rightTotalEnergy() {
        return rightTotalEnergy;
    }

    public void setRightTotalEnergy(double rightTotalEnergy) {
        this.rightTotalEnergy = rightTotalEnergy;
    }

    public Pixel minUpperPre() {
        return minUpperPre;
    }

    public void setMinUpperPre(Pixel minUpperPre) {
        this.minUpperPre = minUpperPre;
    }

    public Pixel minLefterPre() {
        return minLefterPre;
    }

    public void setMinLefterPre(Pixel minLefterPre) {
        this.minLefterPre = minLefterPre;
    }

}