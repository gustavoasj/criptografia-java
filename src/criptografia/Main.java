package criptografia;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Thales
 */
public class Main {

    public static void printArray(Object[] vector, int length, int cl, String name) {
        System.out.println(name);
        for (int i = 0; i < length; i++) {
            if (cl == 1) {
                System.out.println(vector[i]);
            } else {
                System.out.print(vector[i] + " ");
            }

        }
        System.out.println("");
    }

    public static void printMatrix(Object[][] vector, int length, String name) {
        System.out.println(name);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                System.out.print(vector[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    public static String toCompleteBinary(String binary, int length) {
        while (binary.length() < length) {
            binary = "0" + binary;
        }
        return binary;
    }
    
    public static void encrypt(Integer[] input,Integer[] crypt){
        String[] inputx = new String[input.length];
        Double[] x = new Double[input.length];
        Double[] xl = new Double[input.length];
        String[] b = new String[input.length];
        String[] d = new String[input.length];

        printArray(input, input.length, 0, "TO ENCRYPT");

        for (int i = 0; i < input.length; i++) {
            b[i] = "00000000";
            d[i] = "00000000";
            x[i] = 0.0;
            crypt[i] = 0;
        }
        for (int i = 0; i < input.length; i++) {
            inputx[i] = toCompleteBinary(Integer.toBinaryString(input[i]), 8);
        }

        int l = inputx.length;
        double mu = 3.9;
        x[0] = 0.75;

        for (int i = 1; i < l; i++) {
            x[i] = ((Double) (mu * x[i - 1] * (1 - x[i - 1])));
        }
        //printArray(x,x.length,1,"X");

        double max = x[0];
        double min = x[0];

        for (int k = 0; k < l; k++) {
            if (x[k] > max) {
                max = x[k];
            }
            if (x[k] < min) {
                min = x[k];
            }
        }
        for (int i = 0; i < l; i++) {  //uint8
            xl[i] = ((x[i] - min) / max) * 255;
        }

        //printArray(xl,xl.length,1,"XL");
        for (int i = 0; i < input.length; i++) {
            b[i] = toCompleteBinary(Integer.toBinaryString(Math.round(xl[i].floatValue())), 8);

        }
        //printArray(b,b.length,1,"B");
        
        double[] teta = new double[8];
        Integer[][] weights = new Integer[8][8];
        for (int c = 0; c < input.length; c++) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (b[c].charAt(i) == '0' && (i == j)) {
                        weights[i][j] = 1;
                    } else if (b[c].charAt(i) == '1' && (i == j)) {
                        weights[i][j] = -1;
                    } else if ((i != j)) {
                        weights[i][j] = 0;
                    }
                }
                if (b[c].charAt(i) == '0') {
                    teta[i] = -0.5;
                } else {
                    teta[i] = 0.5;
                }
            }
            //printMatrix(weights,weights.length,"WEIGHTS");

            for (int i = 0; i < 8; i++) {
                double sum = 0;
                for (int w = 0; w < 8; w++) {
                    sum = sum + weights[i][w] * Integer.parseInt("" + inputx[c].charAt(w));
                }
                sum = sum + teta[i];
                StringBuilder dc = new StringBuilder(d[c]);
                if (sum >= 0) {
                    dc.setCharAt(i, '1');
                } else {
                    dc.setCharAt(i, '0');
                }
                d[c] = dc.toString();

            }
            //System.out.println("dc="+d[c]);
            for (int i = 0; i < 8; i++) {
                crypt[c] = crypt[c] + (Integer.parseInt(d[c].charAt(i) + "") * ((Double) (Math.pow(2, (7 - i)))).intValue());
            }
        }
        //printArray(d, d.length, 1, "D");
        printArray(crypt, crypt.length, l, "ENCRYPTED");
    }


    public static void main(String[] args) {
        /*Integer[] input = new Integer[]{11, 23, 37, 45, 68, 25, 236, 58, 59, 90};
        Integer[] crypt = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        //Encrypt
        encrypt(input, crypt);
        System.out.println("");
        //Decrypt
        encrypt(crypt, input);
        */
        
        //--------------------Criptografia de imagem----------------------------------------//
        
       
        try{
        	//Carrega a imagem
        	BufferedImage originalImage = 
                                      ImageIO.read(new File("/Users/gustavoasj/Documents/workspace/criptografia/src/criptografia/bird_small.png"));
         
        	
        	// Usando um label para mostrar a imagem
            JFrame frame = new JFrame();
            JLabel label = new JLabel(new ImageIcon(originalImage));
            frame.getContentPane().add(label, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        	
        	
        	// convert BufferedImage to byte array
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        	ImageIO.write( originalImage, "png", baos );
        	baos.flush();
        	byte[] imageInByte = baos.toByteArray();
        	baos.close();
        	
        	
        	
        	byte[] encripted = new byte[imageInByte.length];
        	byte[] saida = new byte[imageInByte.length];
        	int inpt = 0;
        	Integer[] inputt = new Integer[imageInByte.length];
        	Integer[] cryptt = new Integer[imageInByte.length];
        	
        	for (int l=0; l<inputt.length;l++){
        		inpt = (int) imageInByte[l] & 0xFF ;
        		inputt[l] = new Integer(inpt);
        	}
        	
        	//Encrypt
        	encrypt(inputt, cryptt);
        	
        	for (int l=0; l<cryptt.length;l++){
            	inpt = cryptt[l];
                byte b = (byte) inpt;
                encripted[l] = b;
        	}
        	
        	
        	System.out.println("");
        	
            //Decrypt
            encrypt(cryptt, inputt);
            
            for (int l=0; l<inputt.length;l++){
            	inpt = inputt[l];
                byte b = (byte) inpt;
                saida[l] = b;
        	}
            
            /****--------------- Imagem encriptada ---------------****/
            //Cria a imagem encriptada
        	int width = 120;
        	int height = 120;

        	DataBuffer buffer = new DataBufferByte(encripted, encripted.length);
        	//3 bytes per pixel: red, green, blue
        	WritableRaster raster = Raster.createInterleavedRaster(buffer, width, height, 3 * width, 3, new int[] {0, 1, 2}, (Point)null);
        	ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE); 
        	BufferedImage im = new BufferedImage(cm, raster, true, null);

        	ImageIO.write(im, "png", new File("/Users/gustavoasj/Documents/workspace/criptografia/src/criptografia/encriptado.png"));
        	
        	// Usando um label para mostrar a imagem
        	BufferedImage img = ImageIO.read(new File("/Users/gustavoasj/Documents/workspace/criptografia/src/criptografia/encriptado.png"));
            JFrame frame1 = new JFrame();
            JLabel label1 = new JLabel(new ImageIcon(img));
            frame1.getContentPane().add(label1, BorderLayout.CENTER);
            frame1.pack();
            frame1.setVisible(true);
            /*--------------------------------------------*/
            
            
            /****--------------- Imagem de saida ---------------****/
            // convert byte array back to BufferedImage
        	InputStream in2 = new ByteArrayInputStream(saida);
        	BufferedImage bImageFromConvert2 = ImageIO.read(in2);
        	ImageIO.write(bImageFromConvert2, "png", new File(
        						"/Users/gustavoasj/Documents/workspace/criptografia/src/criptografia/saida.png"));
            
            
        	// Usando um label para mostrar a imagem
            BufferedImage img2 = ImageIO.read(new ByteArrayInputStream(saida)); 
            JFrame frame2 = new JFrame();
            JLabel label2 = new JLabel(new ImageIcon(img2));
            frame2.getContentPane().add(label2, BorderLayout.CENTER);
            frame2.pack();
            frame2.setVisible(true);
            /*--------------------------------------------*/
            
        	}catch(IOException e){
        		System.out.println(e.getMessage());
        	}
        
        
    }
}

