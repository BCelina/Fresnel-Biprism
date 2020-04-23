/**
 * FresnelBiprismX_Monochromatic.java
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
 * Version 1.0 No drawing capabilities, calculation only, Monochromatic~Gaussian function around a specific wavelength
 * Decided to create a different object extending on FresnelBiprismX since this requires a higher complexity for the Gaussian curve calculation of the laser input
 * WARNING: the constructor requires one more argument vs FresnelBiprismX
 */

import java.lang.Math;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.Graphics;
import java.util.Scanner;

public class FresnelBiprismX_Monochromatic extends FresnelBiprismX { //Fresnel Biprism effect at distance X using only one color
	
	private static double LaserWavelength;
	private static double Variance=20;//Gaussian Function variance in nm
	
	public FresnelBiprismX_Monochromatic(double Y){ //Default constructor with Tutorial values (7mm)
		this(7,Y); //initial value of X=7mm
	}

	public FresnelBiprismX_Monochromatic (double X, double Y){ //Only change distance of the point the screen in mm
		this(X,Y,1.537,1,3,A,Variance,0.38,0.78,5000);
		
	}
	
	public FresnelBiprismX_Monochromatic (double X,double Y,double n, double d, double DD,double alpha){ //Change only physical parameters
		this(X,Y,n,d,DD,alpha,Variance,0.38,0.78,5000);
	}
	public FresnelBiprismX_Monochromatic (double X,double Y,double n, double d, double DD,double alpha, double v){ //Change only physical parameters
		this(X,Y,n,d,DD,alpha,v,0.38,0.78,5000);
	}
	public FresnelBiprismX_Monochromatic (double X,double Y, double n, double d, double DD,double alpha,double v, int width){ //Change only physical parameters
		this(X,Y,n,d,DD,alpha,v,0.38,0.78,width);
	}
	
	public FresnelBiprismX_Monochromatic (double X,double Y, double n, double d, double DD,double alpha, double v, double LOWER_Range, double UPPER_Range, int width){ //Change all parameters, Universal Constructor
		//X=distance from center ofscreen in mm; n=index of refraction of the biprism; d = distance source to biprism; DD= distance biprism to screen; alpha = angle within the biprism in degrees;v=gaussian function variance in nm; LOWER_Range=min wavelength of spectrum; UPPER_Range=max wavelength of spectrum; width=number of discrete waves taken into account;
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
		Variance=v;//take variance in nm
		x=X;//take distance from center of screen in mm
		LaserWavelength=Y;
		//Simulation
		initializeSpectrum(); // initialize the Spectrum containing discrete wavelengths and amplitudes per each wavelength
		spectrum=function(); // calculate the actual wavelength values and amplitudes per each
		calculateAverage(); // calculate the different coefficient average in order to deduce the final color
		
	}
	
		public static double [] [] function (){ //t=f(wavelength) 
		//calculates the discrete waves per each place in the array and the amplitude or better called transmitivity of each wave
		double [][] temp = new double [2][WIDTH];
		for(int i =0;i<WIDTH;i++){ 
			temp[0][i]=wavelength(i); //temp[0] represents the discrete wavelength
			temp[1][i]=transmitivity(temp[0][i])*Math.pow(Math.E,-(Math.pow((temp[0][i]*1000-LaserWavelength),2))/2/Variance/Variance); //temp[1] represents transmitivity factor t multiplied by the initial light conditions (monochromatic light with a gaussian variance around its initial wavelength)
		}
		return temp;
    	
  	}
  	public void setWavelengthRange(double v){
		Variance = v;
	}
}

