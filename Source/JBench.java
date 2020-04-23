import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;

public class JBench extends JPanel{
	public int dT; //total distance between laser and screen
	public int D;// distance lens-screen
	public int d; //distance laser-lens
	public int width; //width of the panel
	public int height; //height of the panel
	JLabel Screen=new JLabel();
	JLabel Lens = new JLabel();
	JLabel Laser = new JLabel();
	JLabel Bench = new JLabel();
	JLabel Title = new JLabel();
	
	public JBench(int realD,int reald, int aWidth, int aHeight){
		
		//first we set the size of the JBench component
		width=aWidth;
		height=aHeight;
		setSize(width,height);
		setLayout(null);
		
		//then we add all the JComponents with the right sizes
		setParameters(realD,reald);
		
		//finally we display everything
		setVisible(true);
		
	}
	
	//this method enables us to adapt the size of an image to the size of the label where it is displayed
	public void displayImage (JLabel aLabel, ImageIcon icon) {
		Image im = icon.getImage();
		int	hauteur	= aLabel.getHeight();
		int	largeur	= aLabel.getWidth();
		im	= im.getScaledInstance(largeur,hauteur,Image.SCALE_DEFAULT);
		aLabel.setIcon(new ImageIcon(im));
		add(aLabel);
	}
	
	//in this method, we compute the parameters of our JComponents and add them to the JBench
	public void setParameters(int realD,int reald){
		
		dT=(int)(2.0/3.0*width);  //we decided that the length of our bench should be 2 thirds of the total length of the JBench
		D=realD*dT/(realD+reald);//then we make a crossproduct to find the appropriate distances on our screen
		d=dT-D;
		
		
		//Screen, 
		//we first compute the parameters depending on the size of the JBench
		int wScreen=(int)width/30;
		int hScreen=(int) height/3;
		//the values for the position are computed for the upper left corner
		int xScreen=(int)11*width/12-wScreen; 
		int yScreen=(int)(height-hScreen)*3/4;		
		Screen.setBounds(xScreen,yScreen,wScreen,hScreen);
		
		//then we add the corresponding image
		ImageIcon iconScreen = new ImageIcon("./ecran.jpg");
		displayImage(Screen, iconScreen);
		
		//Laser
		//we first compute the parameters depending on the size of the JBench
		int wLaser = width/6;
		int hLaser = height/4;
		//the values for the position are computed for the upper left corner
		int xLaser = xScreen-dT-wLaser;
		int yLaser = (height-hLaser)*3/4;
		Laser.setBounds(xLaser,yLaser,wLaser,hLaser); 
		
		//then we add the corresponding image
		ImageIcon iconLaser = new ImageIcon("./laser.jpg");
		displayImage(Laser, iconLaser);
		
		//Lens
		//we first compute the parameters depending on the size of the JBench
		int wLens = width/20;
		int hLens = height/4;
		//the values for the position are computed for the upper left corner
		int xLens = xLaser+wLaser+d; 
		int yLens = (height-hLens)*3/4;
		Lens.setBounds(xLens,yLens,wLens,hLens); 
		
		//then we add the corresponding image
		ImageIcon iconLens = new ImageIcon("./lens.jpg");
		displayImage(Lens, iconLens);
		
		
		//Title
		//we first compute the parameters depending on the size of the JBench
		int wTitle = dT/2;
		int hTitle = height/4;
		//the values for the position are computed for the upper left corner
		int xTitle = (width-wTitle)/2; 
		int yTitle = height/20;
		Title.setBounds(xTitle,yTitle,wTitle,hTitle); 
		
		//then we add the corresponding image
		
		ImageIcon iconTitle = new ImageIcon("./title.jpg");
		displayImage(Title, iconTitle);
		
		
		repaint();
	}
	     
}
