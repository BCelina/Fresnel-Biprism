import javax.swing.*;
import java.awt.*;
import java.awt.GraphicsDevice;
import java.awt.event.ActionListener; 
import java.awt.event.KeyListener; 
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FresnelBiprismSimulator extends JFrame implements ActionListener, KeyListener  {
	int w;
	int h;
	JTextField n=new JTextField ("1.537"); 
	JTextField d= new JTextField ("1"); 
	JTextField D= new JTextField ("3"); 
	JTextField Var= new JTextField ("20");
	JTextField Wavelength= new JTextField ("532");
	JTextField angle= new JTextField ("0.537"); 
	JCheckBox  WhiteLight= new JCheckBox ("Whitelight");
	JButton simulate = new JButton ("Simulate");
	JButton credentials = new JButton ("?");
	static int SCREEN_WIDTH;
	static int SCREEN_HEIGHT;
	double indexOfRefraction=1.537;
	double distanceFromLightSource=1.;
	double distanceFromScreen=3.;
	double lightSourceWavelength=20.;
	double lightSourceVariance=532.;
	double biprismAngle=0.537;
	boolean isWhiteLight=false;
	JScreen fres = new JScreen();
	JBench banc;
	
	// method to obtain the size of a screen
	private static void getScreenSpecs(){ //gets all screen specs
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();//get graphics environment to analyse
		GraphicsDevice[] devices = env.getScreenDevices();
		
		for (GraphicsDevice device : devices) {
            
            SCREEN_WIDTH = device.getDisplayMode().getWidth();
			SCREEN_HEIGHT = device.getDisplayMode().getHeight();
        }
	}
	
	public FresnelBiprismSimulator(){
			super("Fresnel Biprism Simulator");
			
			getScreenSpecs();
			
			setLocation(0,0);         
			setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
			
			
			//Descriptions of boxes
			JLabel n1 = new JLabel();
			n1.setText("Index of refraction");
			
			JLabel d1 = new JLabel();
			d1.setText("Distance from lightSource in cm");
		
			JLabel D1 = new JLabel();
			D1.setText("Distance from the screen in cm");
			
			JLabel Var1 = new JLabel();
			Var1.setText("Light source variance in nm");
			
			JLabel Wavelength1 = new JLabel();
			Wavelength1.setText("Wavelength in nm");
			
			JLabel angle1 = new JLabel();
			angle1.setText("Alpha in degrees");
			

			

			//adding the different buttons to the container and setting bounds
			JPanel container1 = new JPanel();
			container1.setLayout(null);
			
			credentials.setBounds(SCREEN_WIDTH*7/24,SCREEN_HEIGHT*11/27,SCREEN_WIDTH/32,SCREEN_HEIGHT/27);
			
			simulate.setBounds(SCREEN_HEIGHT/27,SCREEN_HEIGHT*11/27,SCREEN_WIDTH*7/48,SCREEN_HEIGHT/27);
			
			int spacingW=SCREEN_WIDTH*17/96;
			int spacingH=SCREEN_HEIGHT/54;
			int spacingHstep=spacingH*3;
			int widthBox=SCREEN_WIDTH*7/48;
			int widthText=SCREEN_WIDTH*29/192;
			
			n1.setBounds(spacingW,spacingH,widthText,SCREEN_HEIGHT/27);
			n.setBounds(SCREEN_HEIGHT/27,spacingH,widthBox,SCREEN_HEIGHT/27);
			spacingH+=spacingHstep;
			
			d1.setBounds(spacingW,spacingH,widthText,SCREEN_HEIGHT/27);
			d.setBounds(SCREEN_HEIGHT/27,spacingH,widthBox,SCREEN_HEIGHT/27); 
			spacingH+=spacingHstep;
			
			D1.setBounds(spacingW,spacingH,widthText,SCREEN_HEIGHT/27);
			D.setBounds(SCREEN_HEIGHT/27,spacingH,widthBox,SCREEN_HEIGHT/27);
			spacingH+=spacingHstep;
			
			Wavelength1.setBounds(spacingW,spacingH,widthText,SCREEN_HEIGHT/27);
			Wavelength.setBounds(SCREEN_HEIGHT/27,spacingH,widthBox,SCREEN_HEIGHT/27);
			spacingH+=spacingHstep;
			
			Var1.setBounds(spacingW,spacingH,widthText,SCREEN_HEIGHT/27);
			Var.setBounds(SCREEN_HEIGHT/27,spacingH,widthBox,SCREEN_HEIGHT/27);
			spacingH+=spacingHstep;
			
			angle1.setBounds (spacingW,spacingH,widthText,SCREEN_HEIGHT/27);
			angle.setBounds(SCREEN_HEIGHT/27,spacingH,widthBox,SCREEN_HEIGHT/27);
			spacingH+=spacingHstep;
			
			WhiteLight.setBounds(SCREEN_HEIGHT/27,spacingH,widthBox,SCREEN_HEIGHT/27);
			
			container1.add(credentials);
			container1.add(simulate);
			container1.add(n1);
			container1.add(n);
			n.addKeyListener(this);
			container1.add(d1);
			container1.add(d);
			d.addKeyListener(this);
			container1.add(D1);
			container1.add(D);
			D.addKeyListener(this);
			container1.add(Wavelength1);
			container1.add(Wavelength);
			Wavelength.addKeyListener(this);
			container1.add(Var1);
			container1.add(Var);
			Var.addKeyListener(this);
			container1.add(WhiteLight);
			container1.add(angle1);
			container1.add(angle);
			angle.addKeyListener(this);
			simulate.addActionListener(this);
			
			credentials.addActionListener(this);
			
			container1.setBounds (0,0, SCREEN_WIDTH/3, SCREEN_HEIGHT*25/54 );
			
			
			
			//JFrame JBench
			banc = new JBench((int)distanceFromScreen, (int)distanceFromLightSource, SCREEN_WIDTH*45/64,SCREEN_HEIGHT*25/54);
			banc.setLocation(SCREEN_WIDTH/3,0);
			banc.setBackground(Color.WHITE);
			this.add(banc);
			
			//JFrame FresnelBisprism
			fres.setLayout(null);
			fres.setBounds(0,SCREEN_HEIGHT*25/54,SCREEN_WIDTH,SCREEN_HEIGHT*25/54);
			fres.addMouseListener(fres);
			this.add(fres);
			actionPerformed(new ActionEvent(simulate,ActionEvent.ACTION_PERFORMED,null));
			
			
			this.add(container1);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			this.setVisible(true);
			this.setLayout(null);
			
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	} 
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == simulate){
			//Values update
			String index1 = n.getText();
			String distancelight1 = d.getText();
			String distancescreen1 = D.getText();
			String wavelength1 = Wavelength.getText();
			String lightsource1 = Var.getText();
			String beta1 = angle.getText();
			indexOfRefraction = Double.parseDouble(index1);
			distanceFromLightSource = Double.parseDouble(distancelight1);
			distanceFromScreen = Double.parseDouble(distancescreen1);
			lightSourceWavelength = Double.parseDouble(wavelength1);
			lightSourceVariance = Double.parseDouble(lightsource1);
			biprismAngle = Double.parseDouble(beta1);
			isWhiteLight = WhiteLight.isSelected();
			//JScreen update
			fres.setParameters(indexOfRefraction,distanceFromLightSource,distanceFromScreen,lightSourceWavelength,biprismAngle,lightSourceVariance,isWhiteLight);
			//JBench update
			banc.setParameters((int)distanceFromScreen, (int)distanceFromLightSource);
			
		}
		if (e.getSource() == credentials) {
			new JCredentials();
		}
			
			
	}
	public void keyReleased(KeyEvent e){

	}
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_ENTER){
			actionPerformed(new ActionEvent(simulate,ActionEvent.ACTION_PERFORMED,null));
		}
	}
	public void keyTyped(KeyEvent e){

	}
	public static void main (String [] args) {
		new FresnelBiprismSimulator();
	}
	
	
}
