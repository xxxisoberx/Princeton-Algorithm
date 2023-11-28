import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private int picWidth;
    private int picHeight;
    private double[][] picEnergy;
    private PixelsGraph pixelsGraph;
    
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        
        init(picture);
    }

    // Self-added
    private void init(Picture pic) {
        this.picture = new Picture(pic);
        this.picWidth = picture.width();
        this.picHeight = picture.height();
        picEnergy();
        pixelsGraph = new PixelsGraph(picture, picEnergy);
        pixelsGraph.downRefresh();
        pixelsGraph.rightRefresh();
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picWidth;
    }

    // height of current picture
    public int height() {
        return picHeight;
    }

    // Self-added
    private boolean ifColOut(int x) {
        return (x < 0 || x > picWidth - 1);
    }

    private boolean ifRowOut(int y) {
        return (y < 0 || y > picHeight - 1);
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (ifColOut(x) || ifRowOut(y)) throw new IllegalArgumentException();

        if (x == 0 || x == picWidth - 1 || y == 0 || y == picHeight - 1) return 1000.0;
        double diffRx = picture.get(x - 1, y).getRed() - picture.get(x + 1, y).getRed();
        double diffGx = picture.get(x - 1, y).getGreen() - picture.get(x + 1, y).getGreen();
        double diffBx = picture.get(x - 1, y).getBlue() - picture.get(x + 1, y).getBlue();
        double diffRy = picture.get(x, y - 1).getRed() - picture.get(x, y + 1).getRed();
        double diffGy = picture.get(x, y - 1).getGreen() - picture.get(x, y + 1).getGreen();
        double diffBy = picture.get(x, y - 1).getBlue() - picture.get(x, y + 1).getBlue();
        double xgraSquare = Math.pow(diffRx, 2) + Math.pow(diffGx, 2) + Math.pow(diffBx, 2);
        double ygraSquare = Math.pow(diffRy, 2) + Math.pow(diffGy, 2) + Math.pow(diffBy, 2);
        return Math.sqrt(xgraSquare + ygraSquare);
    }

    // Self-added
    private void picEnergy() {
        picEnergy = new double[picHeight][picWidth];
        for (int col = 0; col < picWidth; col++) {
            for (int row = 0; row < picHeight; row++) {
                picEnergy[row][col] = energy(col, row);
            }
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] rowIndices = new int[picWidth];
        Pixel p = pixelsGraph.findRightMinPixel();
        for (int col = 0; col < picWidth; col++) {
            rowIndices[picWidth - 1 - col] = p.row();
            p = p.minLefterPre();
        }
        return rowIndices;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] colIndices = new int[picHeight];
        Pixel p = pixelsGraph.findBottomMinPixel();
        for (int row = 0; row < picHeight; row++) {
            colIndices[picHeight - 1 - row] = p.col();
            p = p.minUpperPre();
        }
        return colIndices;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || picHeight <= 1 || seam.length != picWidth) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length - 1; i++) {
            if (ifRowOut(seam[i]) || Math.abs(seam[i] - seam[i + 1]) > 1) throw new IllegalArgumentException();
        }
        if (ifRowOut(seam[seam.length - 1])) throw new IllegalArgumentException();
        
        Picture newPicture = new Picture(picWidth, picHeight - 1);
        for (int col = 0; col < picWidth; col++) {
            for (int row = 0; row < picHeight - 1; row++) {
                if (row < seam[col]) newPicture.set(col, row, picture.get(col, row));
                else newPicture.set(col, row, picture.get(col, row + 1));
            }
        }
        init(newPicture);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || picWidth <= 1 || seam.length != picHeight) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length - 1; i++) {
            if (ifColOut(seam[i]) || Math.abs(seam[i] - seam[i + 1]) > 1) throw new IllegalArgumentException();
        }
        if (ifColOut(seam[seam.length - 1])) throw new IllegalArgumentException();
        
        Picture newPicture = new Picture(picWidth - 1, picHeight);
        for (int row = 0; row < picHeight; row++) {
            for (int col = 0; col < picWidth - 1; col++) {
                if (col < seam[row]) newPicture.set(col, row, picture.get(col, row));
                else newPicture.set(col, row, picture.get(col + 1, row));
            }
        }
        init(newPicture);
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        int[] a = new int[2];
        for (int i : a) System.out.println(i);
    }

}