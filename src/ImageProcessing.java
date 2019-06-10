/*
*   Carlos Otávio Guimarães     10277057        c.o.guimaraes@usp.br
*   Leonardo Tres Martinez      10277314        leo.tmartinez@usp.br
*/
import ij.*;
import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageProcessor;
import java.awt.*;
import java.awt.event.*;
import ij.process.*;

public class ImageProcessing {
    
    public ImageProcessing(){
    }

    static void noProcessing(String path, int w, int h, String ft, String fs) {
        // Processamento sem Anti Aliasing
        
        ImagePlus oImg;
        ImageAccess input;
        ImageAccess output;
        ImageAccess res; 
        
        System.out.println("Parameters: \n    Path:"+path+"\n    New Width:"+w+"\n    New Heigth:"+h+"\n    Filter type:"+ft+"\n    Filter size:"+fs);
        
        
        oImg = new Opener().openImage(path);
        oImg.show("");
        input = new ImageAccess(oImg.getProcessor());
        
        /*
        Forçando o aliasing:
        A imagem passa por um redimensionamento para 100x100px usando a 
        função resize(), resultando em uma imagem com aliasing */
        output = resize(input, 200, 200);
        
        /*A imagem então passa por outro redimensionamento para o tamanho 
        escolhido por paremetro*/
        input = resize(output, w, h);
        
        // A função rescale é usada para processar a imagem para 8 bits
        res = rescale(input);
        
        res.show("Result of " + oImg.getTitle() + " resize" ); //resultado aparece aqui

        
    }
    
    public static void antialias(String path, int w, int h, String ft,String fs){
       /*   Rescale com Anti-aliasing 
        *   Explicação do algoritmo:
        *       Passo 1:
        *               O programa lê a imagem passada
        *       Passo 2:
        *               È feito um redimensionamento usando a função resize(),
        *               gerando uma imagem com aliasing nítido (para fins de comparações).
        *       Passo 3:
        *               È aplicado a transformada de Fourier.
        *       Passo 3:
        *               È aplicado o filtro de buterworth no modulo de
        *               Fourier.
        *       Passo 3:
        *               È feita a transformada inversa de Fourier
        *       Passo 3:
        *               O programa entao mostra a imagem resutante junto da imagem original.
        */

        doFFTransform fft;
        ImageAccess pbFilter;
        ImagePlus filter;
        ImagePlus mod;
        ImagePlus phase;
        ImageAccess inverse; 
        ImageAccess res; 
        ImagePlus oImg;
        ImageAccess input;
        ImageAccess output;
        
        System.out.println("Parameters: \n    Path:"+path+"\n    New Width:"+w+"\n    New Heigth:"+h+"\n    Filter type:"+ft+"\n    Filter size:"+fs);
        
        // loading image
        oImg = new Opener().openImage(path);
        oImg.show("");
        input = new ImageAccess(oImg.getProcessor());
        
        
        // processing
        
        int nx = input.getWidth();
        int ny = input.getHeight();
        
        // filtro
        switch (ft) {
            case "Butterworth Low Pass (Default)":
                pbFilter = Filters.butterLP(fs);
                break;
            case "Ideal Low Pass":
                pbFilter = Filters.idealLP(fs);
                break;
            default:
                throw new ArrayStoreException("Error: No input entered");
        }
        
        // Forçando o aliasing:
        output = resize(input, 200, 200);
        
        // resize da imagem e do filtro
        input = resize(output,w,h);
        pbFilter = resize(pbFilter,w,h);
        
        filter = new ImagePlus("filtro", pbFilter.createByteProcessor());
        
        
        fft = new doFFTransform(input);
        
        mod = fft.getMod();
        phase = fft.getPhase();
        mod = ImageCalculator.mult(mod,filter);
        
        fft.setMod(mod);
        inverse = fft.doInverse();
        
        res = rescale(inverse);
        res.show("Result of " + oImg.getTitle() + " processing" ); //resultado aparece aqui
    }
    
    private static double getInterpolatedPixelNearestNeighbor(ImageAccess image, double x, double y) {
	double arr[][] = new double[2][2];
	int i = (int)Math.round(x);
	int j = (int)Math.round(y);
        double v = image.getPixel(i,j);

	return v;
    }
    
    static public ImageAccess resize(ImageAccess input, int mx, int my){		
	int nx = input.getWidth();
	int ny = input.getHeight();
		
	double x0, x1, xa, ya, v=0;
        double cx = nx/2;
	double cy = ny/2;
	double dx, dy;
	int i, j;
	double scalex = (double)nx/(double)mx;
	double scaley = (double)ny/(double)my;
	ImageAccess output = new ImageAccess(mx, my);
	double[][] array = input.getArrayPixels();

	for (int xo=0; xo<mx; xo++)
	for (int yo=0; yo<my; yo++) {
		dx = xo-mx/2;
		dy = yo-my/2;
		xa = cx + dx*scalex;
		ya = cy + dy*scaley;
			v = getInterpolatedPixelNearestNeighbor(input, xa, ya);
                        
			output.putPixel(xo, yo, v);
		}
		
	return output;

    }
    
    static public ImageAccess rescale(ImageAccess input) {
        int nx = input.getWidth();
        int ny = input.getHeight();
        double max = input.getMaximum();
        double min = input.getMinimum();
        ImageAccess output = new ImageAccess(nx, ny);
        double value = 0.0;
        double a = 255 / (max-min);
        double b = min;
        for (int x=0; x<nx; x++)
        for (int y=0; y<ny; y++) {
                value = input.getPixel(x, y);
                value = a*(value - b);
                output.putPixel(x, y, value);
            }
    return output;	
    }    
}
