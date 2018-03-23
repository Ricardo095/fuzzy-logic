package logica_difusa;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class panelImagen extends JPanel
{
	private static final long serialVersionUID = 8696697065305494950L;
	public int x,altura,y=0;
	ImageIcon nave = new ImageIcon(panelImagen.class.getResource("cohete.png"));
	
	public panelImagen() {
		setBackground(Color.white);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		altura=this.getHeight();

		x=(this.getWidth()/2)-(nave.getIconWidth()/2);
		
		g.drawImage(nave.getImage(), x, y, null);
	}
}
