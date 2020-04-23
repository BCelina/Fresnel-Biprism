/**
 * FresnelBiprism.java
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
 * 
 */
/**
 * Current Version 1.0
 *
 * Version 0.0 The original Spectrum Code
 * Version 0.1 Addition of wavelength bars and small optimisations
 * Version 0.2 Proper Final average color algorithm
 * Version 0.3 Optimizations + Comments + Universal Constructor
 * Version 0.4 Buffer Added + fixed possible unpredictable color axis background 
 * 				+ fixed each 10nm increments + added nm at the bottom of axis 
 * 				+ Changed main for simple running
 * Version 1.0 No drawing capabilities, calculation only, Full Spectrum
 */
 
import java.lang.Math;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.Graphics;
import java.util.Scanner;

public class FresnelBiprismX{ //Fresnel Biprism effect at distance X from the screen
	
	//Variable Declaration
	
		//Constants (depending on the prism, distance,etc)
	public static double n=1.537; //biprism glass index
	public static double A=(9./60.)*(Math.PI/180.); //alpha, default angle of biprism is 9 minutes
	public static double d=1; //distance source to biprism
	public static double DD=3; //distance biprism(edge) to screen
	public static double c; //constant equal to 2d(n-1)lambda/(D+d)
	public static double x; //distance of the point of the screen in mm
	protected static double [] [] spectrum;//double array of size 2 containing the wavelengths and gamma
		//Window constants
	protected static int WIDTH =5000; //The number of discrete wavelength taken into account for calculations. The bigger WIDTH is, the more accurate the calculations but the more time it takes.
		//Spectrum range constants
	protected static double LOWER_RANGE = 0.38; //lower range lambda (violet) - i.e. the min wavelength of the light spectrum taken into consideration
	protected static double UPPER_RANGE = 0.78; //upper range lambda  (red) - i.e. the max wavelength of the light spectrum taken into consideration
	protected static double RANGE; //range of spectrum, calculated and used by the program
	protected static double resolution; //resolution of spectrum, calculated and used by the program
	public static Color resultColor; //Final average color found from finding the coefficient depended RBG average of all the discrete wavelengths.


	public FresnelBiprismX(){ //Default constructor with Tutorial values (7mm)
		this(7); //initial value of X=7mm
	}

	public FresnelBiprismX (double X){ //Only change distance of the point the screen in mm
		this(X,1.537,1,3,A,0.38,0.78,5000);
		
	}
	
	public FresnelBiprismX (double X,double n, double d, double DD){ //Change only physical parameters
		this(X,n,d,DD,A,0.38,0.78,5000);
	}
	public FresnelBiprismX (double X,double n, double d, double DD, double alpha){ //Change only physical parameters
		this(X,n,d,DD,alpha,0.38,0.78,5000);
	}
	public FresnelBiprismX (double X,double n, double d, double DD, double alpha, int width){ //Change only physical parameters
		this(X,n,d,DD,alpha,0.38,0.78,width);
	}
	
	public FresnelBiprismX (double X, double n, double d, double DD,double alpha, double LOWER_Range, double UPPER_Range, int width){ //Change all parameters, Universal Constructor
		//X=distance from center ofscreen in mm; n=index of refraction of the biprism; d = distance source to biprism; DD= distance biprism to screen; alpha = angle within the biprism in degrees; LOWER_Range=min wavelength of spectrum; UPPER_Range=max wavelength of spectrum; width=number of discrete waves taken into account;
		//Numerical Values and Constants Calculations
		this.n = n;
		this.d = d;
		this.DD = DD;
		this.UPPER_RANGE=UPPER_Range;
		this.LOWER_RANGE=LOWER_Range;
		this.WIDTH=width;
		A=alpha*(Math.PI/180.);//Convert angle from degrees into radians
		c=(2*d*(n-1)*A)/(DD+d);//Calculate a constant which depends on the physical parameters of the situation (n,d,DD)
		RANGE=UPPER_RANGE-LOWER_RANGE; //calculated total range
		resolution=RANGE/WIDTH; //calculate resolution of color spectrum
		x=X;//take distance from center of screen in mm
		
		//Simulation
		initializeSpectrum(); // initialize the Spectrum containing discrete wavelengths and amplitudes per each wavelength
		spectrum=function(); // calculate the actual wavelength values and amplitudes per each
		calculateAverage(); // calculate the different coefficient average in order to deduce the final color
	}
	
	//initializes Spectrum array
	public static void initializeSpectrum(){ 
		spectrum= new double [2][WIDTH];//[0]=wavelengths, [1]=amplitudes
	}

	//function of lambda, stores values in spectrum array with all the numerical values
	public static double [] [] function (){ //t=f(wavelength) 
		//calculates the discrete waves per each place in the array and the amplitude or better called transmitivity of each wave
		double [][] temp = new double [2][WIDTH];
		for(int i =0;i<WIDTH;i++){ 
			temp[0][i]=wavelength(i); //temp[0] represents the discrete wavelength
			temp[1][i]=transmitivity(temp[0][i]); //temp[1] represents transmitivity factor t
		}
		return temp;
    	
  	}
	//calculate wavelength corresponding to place in array
	public static double wavelength(int p){ 
		  return(LOWER_RANGE+(resolution*p));
	}
	//calculate gamma (transmitivity) according to the following formula
	public static double transmitivity(double l){ //or gamma
		return(0.1e1 / 0.2e1 + Math.cos(0.2000e4 * Math.PI * x * c / l) / 0.2e1);
	}
	//Calculates average final color, by adding all rgb and gamma components seperately, and creating the final color in the end.
	public static void calculateAverage(){
		float [] sumO = new float [4]; //Array containing the sum of all RGBA components of the waves in the spectrum
		float [] components = new float [4];//Components of each discrete wave in RGBA
		float [] finComponents = new float [4];//final values in RGBA
		for(int i=0;i<WIDTH;i++){
			Color temp = Wavelength.wvColor((float)spectrum[0][i]*1000,(float)spectrum[1][i]);//get Color of the wavelength at a pont i in the color spectrum
			for(int k =0;k<4;k++){
				components=temp.getRGBComponents(components); //Get RGBA components of the color
				sumO[k]+=components[k]; // Add the respective components to the sum of all components (used for average)
			}
		}
		for(int k =0;k<4;k++){
			finComponents[k]=sumO[k]/WIDTH; //find average
		}
		//Offset fixed to white color (Due to having more reds than violets in the spectrum as a function of wavelength)
		float rFix = 1.7708f*0.9995296214f; // Red color correction - there are more reds than other colors in the visible light spectrum
		float gFix = 2.7128f*0.9996533202f*0.9999999f;//Green color correction
		float bFix = 3.75f*1.0079f*0.9989601823f;//Blue color correction
		float aFix = 1f; // No Alpha correction requeired.
		
		//return final color with corrected RGB values
		resultColor = new Color (finComponents[0]*rFix,finComponents[1]*gFix,finComponents[2]*bFix,finComponents[3]*aFix);
	}
	
	public double [][] getSpectrum(){
		return spectrum;//returns the complete spectrum with discrete wavelengths and amplitudes (if needed to paint it)
	}
	public double getScreenDistance(){
		return x;//returns the distance on the screen from the center
	}
	public int getWidth(){
		return WIDTH;//returns the total number of discrete wavelengths which is also the width of the screen if we draw it - each wavelength one pixel width
	}
	public double getLowerRange(){
		return LOWER_RANGE;//get minimum wavelength
	}
	public double getUpperRange(){
		return UPPER_RANGE;//get maxmimum wavelength
	}



}


