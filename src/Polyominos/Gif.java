package Polyominos;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Class qui va gere le JPanel animer pour la popup d'attente
 * @author David Ducatel
 *
 */
@SuppressWarnings("serial")
public class Gif extends JPanel  {
	 
	   protected void paintComponent(Graphics g) {
	      // dessine le gif :
	      ImageIcon icone = new ImageIcon(getClass().getResource("/resources/wait.gif"));
	      g.drawImage(icone.getImage(), 1, 1, 220, 19, this);
	   }
	 
}
