/*
*   Carlos Otávio Guimarães     10277057        c.o.guimaraes@usp.br
*   Leonardo Tres Martinez      10277314        leo.tmartinez@usp.br
*/
public class Filters {
    public static ImageAccess idealLP(String command) {
        int r, i, j;
        int radius = 0;
        double f;
        ImageAccess im;

        im=new ImageAccess(256,256);
        if (command.equals("PI/16"))
                radius=16;
        if (command.equals("PI/8"))
                radius=32;
        if (command.equals("PI/4"))
                radius=64;
        for (i=0;i<256;i++) {
                for (j=0;j<256;j++) {
                        r=(int)Math.round(Math.sqrt(Math.pow((double)i-128,2)+Math.pow((double)j-128,2)));
                        if (r<=radius) {
                            im.putPixel(i,j,1);
                        } else {
                            im.putPixel(i,j,0);
                        }
                }
        }
        // im.show("Ideal Low Pass "+command);
        return im;
    }
    
    public static ImageAccess butterLP(String command){
        int r, i, j;
        int radius = 0;
        double f, h;
        ImageAccess im;
        im=new ImageAccess(256,256);
        if (command.equals("PI/16"))
                radius=16;
        if (command.equals("PI/8"))
                radius=32;
        if (command.equals("PI/4"))
                radius=64;

        for (i=0;i<256;i++) {
                for (j=0;j<256;j++) {
                    r=(int)Math.round(Math.sqrt(Math.pow((double)i-128,2)+Math.pow((double)j-128,2)));
                    h = 1/(1 + Math.pow(r/radius,2));
                    im.putPixel(i,j,h);
                }
        }
        // im.show("Butter Low Pass "+command);
        return im;
    }
   
}
