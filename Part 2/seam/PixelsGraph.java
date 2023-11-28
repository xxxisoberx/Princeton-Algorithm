import edu.princeton.cs.algs4.Picture;

public class PixelsGraph {
    private final int picWidth;
    private final int picHeight;
    private final Pixel[][] pixels;

    public PixelsGraph(Picture picture, double[][] picEnergy) {
        this.picWidth = picture.width();
        this.picHeight = picture.height();
        pixels = new Pixel[picHeight][picWidth];
        for (int row = 0; row < picHeight; row++) {
            for (int col = 0; col < picWidth; col++) {
                pixels[row][col] = new Pixel(col, row, picEnergy[row][col]);
            }
        }
    }

    /* border checker --- start */
    private boolean ifTopBorder(Pixel p) {
        return p.row() == 0;
    }

    private boolean ifBottomBorder(Pixel p) {
        return p.row() == picHeight - 1;
    }

    private boolean ifLeftBorder(Pixel p) {
        return p.col() == 0;
    }

    private boolean ifRightBorder(Pixel p) {
        return p.col() == picWidth - 1;
    }
    /* border checker --- end */

    private void downPixelRefresh(Pixel p) {
        // p in top border
        if (ifTopBorder(p)) {
            p.setDownTotalEnergy(p.energy());
            return;
        }
        
        // p not in top border
        Pixel minPre = pixels[p.row() - 1][p.col()]; // set minPre to midPre
        double minEne = minPre.downTotalEnergy() + p.energy(); // set minEne to minPreEne
        if (!ifLeftBorder(p)) {
            Pixel leftPre = pixels[p.row() - 1][p.col() - 1];
            double leftEne = leftPre.downTotalEnergy() + p.energy();
            if (leftEne < minEne) {
                minEne = leftEne;
                minPre = leftPre;
            }
        }
        if (!ifRightBorder(p)) {
            Pixel rightPre = pixels[p.row() - 1][p.col() + 1];
            double rightEne = rightPre.downTotalEnergy() + p.energy();
            if (rightEne < minEne) {
                minEne = rightEne;
                minPre = rightPre;
            }
        }
        p.setMinUpperPre(minPre);
        p.setDownTotalEnergy(minEne);
    }

    public void downRefresh() {
        for (int i = 0; i < picHeight; i++) {
            for (int j = 0; j < picWidth; j++) {
                downPixelRefresh(pixels[i][j]);
            }
        }
    }

    public Pixel findBottomMinPixel() {
        double minEne = Double.POSITIVE_INFINITY;
        Pixel minPix = null;
        for (int col = 0; col < picWidth; col++) {
            Pixel curPix = pixels[picHeight - 1][col];
            double curEne = curPix.downTotalEnergy();
            if (curEne < minEne) {
                minEne = curEne;
                minPix = curPix;
            }
        }
        return minPix;
    }

    private void rightPixelRefresh(Pixel p) {
        // p in left border
        if (ifLeftBorder(p)) {
            p.setRightTotalEnergy(p.energy());
            return;
        }
        
        // p not in left border
        Pixel minPre = pixels[p.row()][p.col() - 1]; // set minPre to midPre
        double minEne = minPre.rightTotalEnergy() + p.energy(); // set minEne to minPreEne
        if (!ifTopBorder(p)) {
            Pixel upPre = pixels[p.row() - 1][p.col() - 1];
            double upEne = upPre.rightTotalEnergy() + p.energy();
            if (upEne < minEne) {
                minEne = upEne;
                minPre = upPre;
            }
        }
        if (!ifBottomBorder(p)) {
            Pixel downPre = pixels[p.row() + 1][p.col() - 1];
            double downEne = downPre.rightTotalEnergy() + p.energy();
            if (downEne < minEne) {
                minEne = downEne;
                minPre = downPre;
            }
        }
        p.setMinLefterPre(minPre);
        p.setRightTotalEnergy(minEne);
    }

    public void rightRefresh() {
        for (int j = 0; j < picWidth; j++) {
            for (int i = 0; i < picHeight; i++) {
                rightPixelRefresh(pixels[i][j]);
            }
        }
    }

    public Pixel findRightMinPixel() {
        double minEne = Double.POSITIVE_INFINITY;
        Pixel minPix = null;
        for (int row = 0; row < picHeight; row++) {
            Pixel curPix = pixels[row][picWidth - 1];
            double curEne = curPix.rightTotalEnergy();
            if (curEne < minEne) {
                minEne = curEne;
                minPix = curPix;
            }
        }
        return minPix;
    }
}