/**
 * SpectrumX.java
 * 
 * Copyright 2018 Bedredin CELINA 
 * 
 * INSA-Lyon
 * 
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY
 * 
 * 
 * Version 1.0 - Draws FresnelBiprismX objects
 */
/**
 *This simulation can help you visualise (with wavelength values and colors) 
 *of the spectrum of any point of the screen between 0 and 20 mm.
 * 
 * It helps visualise the Tutorial on Fresnel Biprism
 * 
 * 
 */
 
import java.lang.Math;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.Graphics;
import java.util.Scanner;

public class SpectrumX extends JFrame{ //Draws a FresnelBiprismX and its components
	
	//Variable Declaration
	
	private static double [] [] spectrum;//double array of size 2 containing the wavelengths and gamma
		//Window constants
	private static int HEIGHT = 300;
	private static int WIDTH = 1024;
		//Spectrum range constants
	private static double LOWER_RANGE; //lower range lambda (violet)
	private static double UPPER_RANGE; //upper range lambda  (red)
	private static double RANGE; //range of spectrum
	private static double resolution; //resolution of spectrum
	public static Color resultColor; //Final average color

		//Buffered image
	private Graphics buffer;
	private BufferedImage Background;
	
	public SpectrumX(FresnelBiprismX fbx){
		spectrum=fbx.getSpectrum();//gets spectrum that need to be painted
		resultColor=fbx.resultColor;//gets the final color which will be drawn on top
		LOWER_RANGE=fbx.getLowerRange();//gets min wavelength
		UPPER_RANGE=fbx.getUpperRange();//gets max wavelength
		RANGE=UPPER_RANGE-LOWER_RANGE;//gets total range
		WIDTH=fbx.getWidth();//gets width of JFrame that is propotional to the number of discrete wavelengths - one pixel - one wavelength
		resolution=RANGE/WIDTH;
		this.setTitle("Spectrum of light at screenpoint distance = "+fbx.getScreenDistance()+" mm");
		this.setLayout(null);
		this.setSize(WIDTH,HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.Background=new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		this.buffer = Background.getGraphics();
		this.setVisible(true);
	}

	public void paint(Graphics g){ 
		//Initial Black background
			//draw black background
			/*
		buffer.setColor(Color.BLACK);
		buffer.fillRect(0,0,WIDTH,(int)((double)HEIGHT*(1-0.2)));
		*/
		//AXIS on the bottom of the page with vertical lines every 100 & 10 nm
			//draw white square for axis
		buffer.setColor(Color.WHITE);
		buffer.fillRect(0,(int)((double)HEIGHT*(1-0.2)),WIDTH,HEIGHT);
			//draw axis with vertical lines
		int axis = (int)(((double)HEIGHT*(0.84))+(0.05*(double)HEIGHT));
		buffer.setColor(Color.BLACK);
		buffer.drawLine(0,axis,WIDTH,axis);
		
		//Big increments ruler
		int verticalPosition=(int)((double)axis*(1.03));
		int vericalNumberPosition = (int)((double)axis*(1.08));
		int textOffset = 10;
		String writeRuler;

		for(int i=400;i<=(int)(UPPER_RANGE*1000);i+=100){//draws lines every 20steps : step of 0.05mm gives us gradings every 1mm RIGHT
			buffer.drawLine((int)((i-(LOWER_RANGE*1000))/resolution/1000),0,(int)((i-(LOWER_RANGE*1000))/resolution/1000),verticalPosition);
			//Write distance values (very high complexity
			writeRuler = String.valueOf(i)+" nm";
			buffer.drawString(writeRuler,(int)((i-(LOWER_RANGE*1000))/resolution/1000)-textOffset,vericalNumberPosition);
		}
		//Small increments ruler
		int y1 = (int)((double)axis*(1-0.03));
		int y2 = (int)((double)axis*(1.03));
		for(int i=(int)(LOWER_RANGE*1000);i<=(int)(UPPER_RANGE*1000);i+=10){ //draws lines every 20steps : step of 0.05mm gives us gradings every 1mm RIGHT
			buffer.drawLine((int)((i-(LOWER_RANGE*1000))/resolution/1000),y1,(int)((i-(LOWER_RANGE*1000))/resolution/1000),y2);
		}
	
		//Wavelengths (Displays a pretty Spectrum). Vertical lines with different gamma values (darkness) of the color of the wavelength.
		for(int i=0;i<WIDTH;i++){
			buffer.setColor(Wavelength.wvColor((float)(spectrum[0][i]*1000),((float)spectrum[1][i])));
			//System.out.println((float)(spectrum[0][i]*1000) +" | "+spectrum[1][i]+"  |  "+Wavelength.wvColor((float)spectrum[0][i]*1000,(float)spectrum[1][i]));//displays numerical values per each line drawn
			buffer.drawLine(i,0,i,(int)((double)HEIGHT*(1-0.2)));
		}
		
		//function (displays a wave with coresponding colors and luminosities)- plots the fuction of the amplitude on top of the color spectrum
		buffer.setColor(Color.WHITE);
		for(int i=0;i<WIDTH-1;i++){
			int t1 = (int)((1-spectrum[1][i])*HEIGHT*0.6);
			int t2 = (int)((1-spectrum[1][i+1])*HEIGHT*0.6);
			buffer.drawLine(i,t1+(int)(0.2*HEIGHT),i+1,t2+(int)(0.2*HEIGHT));
		}
		

		//Average V0.2 Displays a bar on top of the simulation with the corresponding final color and luminosity

		//Draws a rectangle of 20% of the screen (upper part) with the corresponding average color percieved by our eyes.
		buffer.setColor(resultColor);
		buffer.fillRect(0,0,WIDTH,(int)((double)HEIGHT*(1-0.8)));
		//System.out.println("Resulting Color: "+resultColor); //diplays composition of final color
		

		//draw buffered image
		g.drawImage(Background,0,0,this);
	}



}

