import javax.swing.*;
import java.awt.*;
import java.awt.GraphicsDevice;
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent;

public class JCredentials extends JFrame {
	
	public JCredentials (){
		super("Credits");
		setLocation(680,500);
		setSize(598,100);
		JLabel a = new JLabel();
		a.setText("   This software was made by Aniis Koodoruth, Bedredin Celina, Laura Schraen and Mei-Lin Grouzelle");
		a.setLocation(0,0);
		this.add(a);
		setVisible(true);
	}
	public static void main (String [] args) {
		new JCredentials();
	}
}
