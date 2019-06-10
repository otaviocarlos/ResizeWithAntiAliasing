
import ij.ImagePlus;

/**
 *
 * @author carlos
 */
public class ImageCalculator {
    public static ImagePlus mult(ImagePlus a, ImagePlus b){
        ImageAccess im1 = new ImageAccess(a.getProcessor());
        ImageAccess im2 = new ImageAccess(b.getProcessor());
        
        ImageAccess result = new ImageAccess(im1.getHeight(),im2.getWidth());
        result.multiply(im1, im2);
        
        ImagePlus finalRes = new ImagePlus("Multiplication", result.createByteProcessor());
        return finalRes;
    }
}
