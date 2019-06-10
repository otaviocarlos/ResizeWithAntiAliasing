/*
*   Carlos Otávio Guimarães     10277057        c.o.guimaraes@usp.br
*   Leonardo Tres Martinez      10277314        leo.tmartinez@usp.br
*/
import ij.ImagePlus;
import ij.process.FloatProcessor;

public class doFFTransform {

    private ImagePlus mod;
    private ImagePlus phase;
    private ImageAccess inverse;
    
    doFFTransform(ImageAccess input){
        FFTransform(input);
    }
    
    public ImagePlus getMod(){
        return this.mod;
    }
    
    public void setMod(ImagePlus newmod){
        this.mod = newmod;
    }
    
    public ImagePlus getPhase(){
        return this.phase;
    }
    
    public ImageAccess getInverse(){
        return this.inverse;
    }
    
    public ImageAccess doInverse(){
        try{
            inverseFFTransform();
            return this.inverse;
        }
        catch(Exception e){
            throw new ArrayStoreException("Fail in inverse FFT process.");
        }
    }
    
    private void inverseFFTransform(){
        ImageAccess modAux = new ImageAccess(this.mod.getProcessor());
        ImageAccess phaseAux = new ImageAccess(this.phase.getProcessor());
        shift(modAux);
        shift(phaseAux);
        exp(modAux);
        FFT.convertPolarToCartesian(modAux, phaseAux);
        FFT.inverseFFT(modAux, phaseAux);
        this.inverse = modAux;
    }

    private void FFTransform(ImageAccess input) {

        int i = input.getWidth();
        int j = input.getHeight();
        ImageAccess real = input;
        ImageAccess imagi = new ImageAccess(i, j);
        
        FFT.doFFT(real, imagi);
        FFT.convertCartesianToPolar(real, imagi);
        
        shift(real);
        log(real);

        FloatProcessor realFP = real.createFloatProcessor();
        realFP.resetMinAndMax();
        this.mod = new ImagePlus("Module", realFP);
        
        shift(imagi);
        FloatProcessor imagiP = imagi.createFloatProcessor();
        this.phase = new ImagePlus("Phase", imagiP);
 
        
    }

public void shift(ImageAccess input)
  {
    int i = input.getWidth();
    int j = input.getHeight();
    shift(input, i / 2, j / 2);
  }
  
  public void shift(ImageAccess input, int a, int b)
  {
    int i = input.getWidth();
    int j = input.getHeight();
    if ((a < 0) || (b < 0)) {
      throw new ArrayStoreException("Unexpected center");
    }
    if ((a >= i) || (b >= j)) {
      throw new ArrayStoreException("Unexpected center");
    }
    double[] arrayOfDouble1;
    double[] arrayOfDouble2;
    int k;
    if ((a != 0) && (a != i))
    {
      arrayOfDouble1 = new double[i];
      arrayOfDouble2 = new double[i];
      for (k = 0; k < j; k++)
      {
        input.getRow(k, arrayOfDouble1);
        System.arraycopy(arrayOfDouble1, a, arrayOfDouble2, 0, i - a);
        System.arraycopy(arrayOfDouble1, 0, arrayOfDouble2, i - a, a);
        input.putRow(k, arrayOfDouble2);
      }
    }
    if ((b != 0) && (b != j))
    {
      arrayOfDouble1 = new double[j];
      arrayOfDouble2 = new double[j];
      for (k = 0; k < i; k++)
      {
        input.getColumn(k, arrayOfDouble1);
        System.arraycopy(arrayOfDouble1, b, arrayOfDouble2, 0, j - b);
        System.arraycopy(arrayOfDouble1, 0, arrayOfDouble2, j - b, b);
        input.putColumn(k, arrayOfDouble2);
      }
    }
  }
  
  public void log(ImageAccess input)
  {
    int i = input.getWidth();
    int j = input.getHeight();
    int k = i * j;
    double[][] arrayOfDouble = input.getArrayPixels();
    for (int m = 0; m < i; m++) {
      for (int n = 0; n < j; n++) {
        arrayOfDouble[m][n] = Math.log(arrayOfDouble[m][n]);
      }
    }
    input.putArrayPixels(arrayOfDouble);
  }
  
  public void exp(ImageAccess input)
  {
    int i = input.getWidth();
    int j = input.getHeight();
    int k = i * j;
    double[][] arrayOfDouble = input.getArrayPixels();
    for (int m = 0; m < i; m++) {
      for (int n = 0; n < j; n++) {
        arrayOfDouble[m][n] = Math.exp(arrayOfDouble[m][n]);
      }
    }
    input.putArrayPixels(arrayOfDouble);
  }
    
}
