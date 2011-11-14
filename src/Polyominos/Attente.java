package Polyominos;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JWindow;

/**
 * Class qui va gere la popup d'attente
 * @author David Ducatel
 *
 */
@SuppressWarnings("serial")
public class Attente extends JWindow{
	
	/**
	 * Constructeur de la popup d'attente
	 * @param g Objet de L'IHM sur lequel la popup doit etre placer
	 */
	public Attente(Gestion g){
		super(g);
		setLayout(new BorderLayout());
		
		JLabel lab=new JLabel("Traitement en cours");
		lab.setHorizontalAlignment(JLabel.CENTER);
		add(lab,BorderLayout.NORTH);
		
		//ajout du JPanel animer
		add(new Gif());
		
		setSize(220,35);
		setVisible(true);
		this.setLocationRelativeTo(null);
		
	}

}

