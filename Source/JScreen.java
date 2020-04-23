/**
 * JFresnel.java
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
 * Version 1.0 - Full simulation, Monochromatic+Full Spectrum+Ruler
 */

import java.lang.Math;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.Graphics;
import java.util.Scanner;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

public class JScreen extends JComponent implements MouseListener {
	
				//Window constants
	private static int HEIGHT;
	private static int WIDTH;
	
		//Constants (depending on the prism, distance,etc)
	public static double n=1.537; //biprism glass index
	public static double A=(9./60.)*(Math.PI/180.); //alpha, default angle of biprism is 9 minutes
	public static double d=1; //distance source to biprism
	public static double DD=3; //distance biprism(edge) to screen
	public static double c; //constant equal to 2d(n-1)lambda/(D+d)
	
	public static double [] [] spectrum;//double array of size 2 containing the wavelengths and gamma
	public static double step = 0.01;//mm (smaller is zoomed)
	
	private static boolean WhiteLight = true;
	private static double LaserWavelength;
	private static double LaserWavelengthVariance=20;
	
			//Buffered image
	private Graphics buffer;
	private BufferedImage Background;
	private boolean symmetric = true;
	
	public JScreen(){
		super();
		
	}
	public JScreen(int width, int height){
		this();
		addMouseListener(this);
		this.setSize(width,height);
	}
	public void setSize(int x, int y){
		super.setSize(x,y);
		WIDTH=(int)((double)getWidth()/2.);
		HEIGHT=getHeight();
		this.Background=new BufferedImage(WIDTH*2,HEIGHT,BufferedImage.TYPE_INT_RGB);
		this.buffer = Background.getGraphics();
		//runSimulation();
	}
	public void setBounds(int a, int b, int x, int y){
		super.setBounds(a,b,x,y);
		WIDTH=(int)((double)getWidth()/2.);
		HEIGHT=getHeight();
		this.Background=new BufferedImage(WIDTH*2,HEIGHT,BufferedImage.TYPE_INT_RGB);
		this.buffer = Background.getGraphics();
		//runSimulation();
	}
	public void mouseExited (MouseEvent e){
		
	}
	public void mouseEntered(MouseEvent e){
		
	}
	public void mouseReleased(MouseEvent e){
		
	}
	public void mousePressed(MouseEvent e){
		
	}
	public void mouseClicked (MouseEvent e){
		if(e.getButton()==1){
			int x=e.getX();
			if(x<=WIDTH) x=WIDTH-x;
			else x=x-WIDTH;
			double distance =x*step;
			FresnelBiprismX temp;
			int windowWidth=1024;
			if(WhiteLight) temp = new FresnelBiprismX(distance,n,d,DD,A,windowWidth);
			else temp= new FresnelBiprismX_Monochromatic(distance,LaserWavelength,n,d,DD,A,LaserWavelengthVariance,windowWidth);
			new SpectrumX(temp);
		}
	}
	public void setRefractionIndex(double index){
		n=index;
		//runSimulation();
	}
	public void setDistance1(double i){
		d=i;
		//runSimulation();
	}
	public void setDistance2(double i){
		DD=i;
		//runSimulation();
	}
	public void setMonochromaticLight(double lambda){ //TO IMPLEMENT
		WhiteLight=false;
		LaserWavelength=lambda;
		//runSimulation();
	}
	public void setMonochromaticLight(double lambda, double v){ //TO IMPLEMENT
		WhiteLight=false;
		LaserWavelength=lambda;
		LaserWavelengthVariance=v;
		//runSimulation();
	}
	public void setLaserWavelengthVariance(double v){
		LaserWavelengthVariance=v;
		//runSimulation();
	}
	public void setWhiteLight(){//TO IMPLEMENT
		WhiteLight=true;
		//runSimulation();
	}
	public void runSimulation(){
		this.buffer = Background.getGraphics();
		initializeSpectrum();
		sumOfFunctions();
		repaint();
		this.setVisible(true);
	}
	public void setParameters(double index, double d1, double d2, double lambda,double alpha, double v, boolean whiteLight){ // index=index of refraction; d1=distance 1; d2=distance 2; lambda = wavelength of laser; v=variance of laser spectrum in gaussian curve; whiteLight= yes or no
		n=index;
		d=d1;
		DD=d2;
		LaserWavelength=lambda;
		LaserWavelengthVariance=v;
		WhiteLight=whiteLight;
		A=alpha;
		runSimulation();
	}
	
	
	public static void initializeSpectrum(){ 
		spectrum= new double [4][WIDTH];//rgba table
	}
	public static void sumOfFunctions(){
		double distance = 0;
		float [] components = new float [4];//r,g,b,a
		for(int i=0;i<WIDTH;i++){
			FresnelBiprismX temp;
			if(WhiteLight) temp = new FresnelBiprismX(distance,n,d,DD,A);
			else temp= new FresnelBiprismX_Monochromatic(distance,LaserWavelength,n,d,DD,A,LaserWavelengthVariance);
			
			components=temp.resultColor.getRGBComponents(components);
			spectrum[0][i]=components[0];
			spectrum[1][i]=components[1];
			spectrum[2][i]=components[2];
			spectrum[3][i]=components[3];
			distance+=step;
		}
	}
	public void paint (Graphics g){
		setSize(getWidth(), getHeight());
		
		buffer.setColor(Color.BLACK);
		buffer.fillRect(0,0,WIDTH*2,(int)((double)HEIGHT*(0.8)));
		
		//AXIS on the bottom of the page with vertical lines every 100 & 10 nm
			//draw white square for axis
		buffer.setColor(Color.WHITE);
		buffer.fillRect(0,(int)((double)HEIGHT*(0.86)),WIDTH*2,HEIGHT);
			//draw horisontal axis in white bottom
		int axis = (int)(((double)HEIGHT*(0.86))+(0.05*(double)HEIGHT));
		buffer.setColor(Color.BLACK);
		buffer.drawLine(0,axis,WIDTH*2,axis);
		
			//draw ruler 1/10ths increment vertical lines
		int y1 = (int)((double)axis*(1-0.03));
		int y2 = (int)((double)axis*(1.03));
		for(int i=WIDTH, k=WIDTH;i>=0;i-=10, k+=10){//draws lines every 20steps : step of 0.05mm gives us gradings every 1mm
			buffer.drawLine(i,y1,i,y2);
			buffer.drawLine(k,y1,k,y2);
		}
			//draw ruler increments and write the distances below
		String writeRuler;
		int verticalPosition=(int)((double)axis*(1.05));
		int vericalNumberPosition = (int)((double)axis*(1.075));
		int textOffset = 10;
		for(int i=WIDTH, k=WIDTH;i>=0;i-=100, k+=100){//draws lines every 20steps : step of 0.05mm gives us gradings every 1mm RIGHT
			buffer.drawLine(i,0,i,verticalPosition);
			buffer.drawLine(k,0,k,verticalPosition);
			//Write distance values (very high complexity
			writeRuler = String.valueOf((WIDTH-i)*step)+" mm";
			buffer.drawString(writeRuler,i-textOffset,vericalNumberPosition);
			buffer.drawString(writeRuler,k-textOffset,vericalNumberPosition);
		}
		
		
			//displays the spectrum
		for(int i=0, k=WIDTH-1;i<WIDTH;i++,k--){
			buffer.setColor(new Color ((float)spectrum[0][WIDTH-i-1],(float)spectrum[1][WIDTH-i-1],(float)spectrum[2][WIDTH-i-1],(float)spectrum[3][WIDTH-i-1]));
			buffer.drawLine(i,0,i,(int)((double)HEIGHT*(0.86)));//first half (left)
			buffer.drawLine(WIDTH+k,0,WIDTH+k,(int)((double)HEIGHT*(0.86)));//second half (right)
		}
		
		
		g.drawImage(Background,0,0,this);
	}
	
	public static void main (String args[]) {
		/*
		 * This main is demo in order to understand how to use the JScreen object 
		 * JScreen suffers from bugs when combined with JPanel in certain scenarios,
		 * thus it is prefered to use JScreen directly on the JFrame without a intermediary JPanel 
		 */
		JFrame myFrame = new JFrame("Test");
		myFrame.setSize(1920,640);
		
		JPanel myPanel = new JPanel();
		myPanel.setBounds(0,0,1920,600);
		
		JScreen fres = new JScreen();
		fres.setBounds(0,0,1920,600);
		
		double indexOfRefraction = 1.537;
		double distanceFromLightSource = 20;//20cm
		double distanceFromScreen = 70; //70 cm
		double lightSourceWavelength = 532;//green laser
		double lightSourceVariance = 20;
		double biprismAngle = 0.537;
		boolean isWhiteLight = true;
		
		//Simulate with the parameters as defined above
		fres.setParameters(indexOfRefraction,distanceFromLightSource,distanceFromScreen,lightSourceWavelength,biprismAngle,lightSourceVariance,isWhiteLight);
		

		myFrame.setLayout(null);
		myPanel.setLayout(null);
		fres.addMouseListener(fres);
		myPanel.add(fres);
		
		myFrame.add(myPanel);
		//myFrame.setContentPane(myPanel);
		
		myFrame.setVisible(true);

	}
}
