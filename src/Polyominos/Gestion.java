package Polyominos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.muntjak.tinylookandfeel.Theme;
import de.muntjak.tinylookandfeel.ThemeDescription;
import de.muntjak.tinylookandfeel.TinyLookAndFeel;


/**
 * Class qui va gerer l'IHM
 * C'est ici que sont appele toutes les methodes de resolution
 * @author David Ducatel
 *
 */
@SuppressWarnings("serial")
public class Gestion extends JFrame{
		
	/**
	 * Bouton quitter de l'IHM. Permet de quitter le logiciel
	 */
	public JButton quitter;
	
	/**
	 * Bouton exectuer de l'IHM. Permet de lancer les resolutions
	 */
	public JButton executer; 
	
	/**
	 * composant qui va contenir le numero de la visualisation en cour
	 */
	public JLabel nbSolVisu;
	
	/**
	 * Popup D'attente
	 */
	public Attente att;
	
	/**
	 * Objet qui permet la diffusion du son sur l'IHM
	 */
	public static Sound s;
	
	/**
	 * boolean qui permet de savoir si on supporte les mixer
	 */
	public static boolean supportedMixer;
		
	/**
	 * Bouton qui permet de lancer la lecture de la musique
	 */
    public JButton play;
    
    /**
     * Bouton qui permet d'interromptre la lecture de la musique
     */
    public JButton stop;
    
    /**
     * Bouton qui permet d'activer la sourdine
     */
    public JButton mute;
    
    /**
     * boolean qui permet de savoir si la sourdine est activï¿½
     */
    public boolean isMute;
    
    /**
     * Slider qui va permettre de regler la balance du son
     */
    public JSlider balance;
    
    /**
     * Variable qui va contenir le numero de la visualisation courante
     */
	private int visu=0;
    
	/**
	 * Objet de l'IHM
	 */
	private static Gestion g;
	
	/**
	 * Composant permettant de choisir la methode de resolution
	 */
	private JComboBox choixMethode;
	
	/**
	 * Composant permettant de choisir la taille du probleme
	 */
	private JComboBox choixTaille;
    
	/**
	 * Composant qui va supporter la representation graphique d'une solution au probleme
	 */
	private JPanel panCentre;
	
	/**
	 * Composant qui va contenir le temps de resolution du probleme
	 */
	private JLabel temps;
	
	/**
	 * composant qui va contenir le nombre de solution trouver
	 */
	private JLabel nbSolution;
    
	/**
	 * Thread de lecture du son
	 */
	private static Thread threadPlayer;	
	
	/**
	 * Objet de la class sound, permet de jouer la musique
	 */
	private static Sound player;
	
	/**
	 * Barre d'outil pour la gestion du son
	 */
    private JToolBar toolBar;
    
    /**
     * Variable qui contient le nombre de thread maximum lancable sans concurence
     */
    private int nbThread = Runtime.getRuntime().availableProcessors();
        
	/**
	 * Constructeur de L'IHM
	 * @param sound true si la JVM peut utilisé un mixer
	 */
	public Gestion(boolean supportedMixer){
		
		// creation et initialization de la fenetre
		super("Polyominos");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(650, 650);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		Gestion.supportedMixer=supportedMixer;
		
		// changement du lookAndFeel
		try {
			ThemeDescription[] theme = Theme.getAvailableThemes();
			Theme.loadTheme(theme[3]);
			UIManager.setLookAndFeel(new TinyLookAndFeel());
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		//JPanel qui va contenir la bar d'outil et les boutons de l'ihm
		JPanel supPanH=new JPanel(new GridLayout(2, 1));
		
		/********* Barre d'outils ************/
		
		//creation de la barre d'outil
		toolBar = new JToolBar();
		
		//on empeche le deplacement de la bar d'outil
		toolBar.setFloatable(false);
				
		toolBar.add(new JLabel("Gestion de la musique"));
		
		//creation du bouton play
		play = new JButton(new ImageIcon(getClass().getResource("/resources/play.png")));
		play.addActionListener(new EcouteurSon());
		
		//vue que la musique est lance avec l'ihm, il faut bloque le bouton play 
		play.setEnabled(false);
		toolBar.add(play);
		
		//creation du bouton stop
		stop = new JButton(new ImageIcon(getClass().getResource("/resources/stop.png")));
		stop.addActionListener(new EcouteurSon());
		toolBar.add(stop);
		
		//creation du bouton mute
		mute = new JButton(new ImageIcon(getClass().getResource("/resources/son.png")));
		mute.addActionListener(new EcouteurSon());
		toolBar.add(mute);
		
		//si on ne supporte pas le mixer, on bloque l'utilisation de la sourdine
		if(!supportedMixer)
			mute.setEnabled(false);
		
		
		
		// slider de balance
		
		//creation du slider et des bornes
		if(supportedMixer){
			balance = new JSlider(
	                (int) player.getBalanceControl().getMinimum() * 100,
	                (int) player.getBalanceControl().getMaximum() * 100);
			
			//on place la valeur pardefaut
	        balance.setValue((int) player.getBalanceControl().getValue());
	        
	        //on dit que l'ont veux desiner les trait de separation
	        balance.setPaintTicks(true);
	                
	        //on place les petite trait de separation tout les 10 pas
	        balance.setMinorTickSpacing(10);
	        
	        //on place les grand trait de separation a chaque bout
	        balance.setMajorTickSpacing((int) player.getBalanceControl()
	                .getMaximum()
	                * 100 - (int) player.getBalanceControl().getMinimum() * 100);
	        
	       
	        //on force le deplacementpas a pas
	        balance.setSnapToTicks(true);
	        
	        //On place une bulle d'aide
	        balance.setToolTipText("Reglage de la balance");
	
	        balance.addChangeListener(new EcouteurBalance());
	        
	        toolBar.add(balance);
		}
		
		supPanH.add(toolBar);
		

		/************ PARTIE HAUTE ************/

		JPanel panH = new JPanel(new FlowLayout());

		executer = new JButton("Executer");
		executer.addActionListener(new EcouteurBouton());
		panH.add(executer);

		quitter = new JButton("Quitter");
		quitter.addActionListener(new EcouteurBouton());
		panH.add(quitter);
		
		choixMethode = new JComboBox();
		choixMethode.addItem("Resolution par Aleatoire pure");
		choixMethode.addItem("Resolution par Semi-aleatoire");
		choixMethode.addItem("Resolution par Backtracking 1 solution");
		choixMethode.addItem("Resolution par Backtracking");
		choixMethode.addItem("Resolution par Recuit simule");
		choixMethode.addItem("Resolution par Aleatoire pure MultiThread");
		choixMethode.addItem("Resolution par Semi-Aleatoire MultiThread");
		choixMethode.addItem("Resolution par Recuit simule MultiThread");

		panH.add(choixMethode);
		
		panH.add(new JLabel("Taille :"));
		choixTaille=new JComboBox();
		choixTaille.addItem("3*3");
		choixTaille.addItem("4*4");
		choixTaille.addItem("5*5");
		choixTaille.addItem("9*9");
		panH.add(choixTaille);
				
		supPanH.add(panH);
		
		add(supPanH, BorderLayout.NORTH);

		/************ AFFICHAGE 2D **************/
		
		// rempli par la methode createVisu
		panCentre=new JPanel();
		add(panCentre,BorderLayout.CENTER);
		
		/************* BAS ********************/
		JPanel panB=new JPanel();
		
		JSeparator sep=new JSeparator(SwingConstants.VERTICAL);
		sep.setPreferredSize(new Dimension(1, 10));
		temps=new JLabel("Temps d'execution: 0h00min00s00ms");
		panB.add(temps);
		panB.add(sep);
	

		JSeparator sep2=new JSeparator(SwingConstants.VERTICAL);
		sep2.setPreferredSize(new Dimension(1, 10));
		nbSolution=new JLabel("Nombre de solution trouve: 0");
		panB.add(nbSolution);
		panB.add(sep2);
		
		nbSolVisu=new JLabel("Visualisation 0");
		panB.add(nbSolVisu);
		
		add(panB,BorderLayout.SOUTH);
			
		/****************************************/

		setVisible(true);
		setResizable(false);
		
		ImageIcon icone = new ImageIcon(getClass().getResource(
		"/resources/icone.png"));
		this.setIconImage(icone.getImage());
		isMute=false;
	}
	
	/**
	 * Fonction qui affiche graphiquement une solution
	 * @param tabCoul tableau contenant les couleurs des polyominos solution ainsi que leur noms et leurs nombres de bloc
	 */
	public void createVisu(String[][] tabCoul){
			remove(panCentre);
		
		panCentre=new JPanel(new GridLayout(getTaille(), getTaille(),5,5));
		for(String[] c : tabCoul){
			for(String s : c){
				String tmp[]=s.split("_");
				JLabel lab = new JLabel(tmp[1]);
				lab.setBackground(Color.decode(tmp[0]));
				lab.setHorizontalAlignment(JLabel.CENTER);
				lab.setOpaque(true);
				panCentre.add(lab);
				
			}
		}
		add(panCentre,BorderLayout.CENTER);
		validate();
	}
	
	/**
	 * Fonction qui affiche graphiquement un ensemble de solution
	 * (une solution par seconde)
	 * @param tabPlat arrayList contenant tous les plateaux solution
	 */
	public void createVisu(HashSet<Plateau> tabPlat){
		visu=0;
		for(Plateau p :tabPlat){
			visu++;
			nbSolVisu.setText("Visualisation "+visu);
			createVisu(p.tabCoul);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		visu=0;
	}
	
	/**
	 * Fonction qui permet la modification de l'affichage du nombre de solution trouve
	 * @param i nombre de solution
	 */
	public void setNbSol(int i){
		nbSolution.setText("Nombre de solution trouve: "+i);
	}

	/**
	 * Fonction qui convertis des millisecondes en heure minute seconde et millisecondes
	 * @param ms millisecondes a convertir
	 */
	public void convertTime(long ms){
		long millisecondes=ms%1000; 
		ms=ms/1000; 
		long secondes=ms%60; 
		ms=ms/60; 
		long minutes=ms%60; 
		ms=ms/60; 
		long heures=ms;
		temps.setText("Temps d'execution: "+heures+"h"+minutes+"min"+secondes+"s"+millisecondes+"ms");
	}
	
	/**
	 * Fonction qui retourne la taille du plateau choisi par l'utilisateur
	 * @return retourne la taille du plateau a utiliser
	 */
	public int getTaille(){
		String s=(String) choixTaille.getSelectedItem();
		s=s.substring(0, 1);
		return Integer.parseInt(s);
	}
	
	/**
	 * Fonction qui initialise la musique
	 */
	public static void InitMusique(boolean supportedMixer){
		player=new Sound(supportedMixer); 
		
	}
	
	/**
	 * Fonction qui lance la musique
	 */
	public static void lanceMusique(){
		player.setGestion(g);
		threadPlayer = new Thread(player);
		threadPlayer.start();
	}
	
	/**
	 * Methode main du programme
	 * @param args inutilise
	 */
	public static void main(String[] args) {

		//boolean permettant de savoir si la JVM supporte le Mixer
		supportedMixer=Sound.supportedMixer();

		//Initialise la lecture de la musique
		InitMusique(supportedMixer);
		
		//lance l'IHM
		g=new Gestion(supportedMixer);
		
		//si la JVM ne supporte pas de mixer, on ouvre une popup pour prevenir les utilisateurs
		if(!supportedMixer)
			unsupportedMixer();
		
		//lance la lecture de la musique
		lanceMusique();
				
	}
	
	/**
	 * Fonction qui affiche une popup pour prevenir l'utilisateur
	 * que sa JVM ne gere pas les mixer et donc les controles du son
	 */
	private static void unsupportedMixer(){
		String s="<html><center>Votre JVM ne supporte pas certaine parti de l'API Java Sound.<br/> De ce fait, vous ne pourrez " +
				"pas utiliser la sourdine et le <br/>reglage de la balance</center></html>";
		JOptionPane.showMessageDialog(null, s , "JVM Mixer Warning", JOptionPane.INFORMATION_MESSAGE);

	}
	
	/**
	 * Class de gestion des actions sur les boutons de controle de la musique
	 */
	public class EcouteurSon implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource()==play){
				//ici le bouton play du lecteur

				InitMusique(supportedMixer);
				lanceMusique();
				play.setEnabled(false);
				stop.setEnabled(true);	
				if(supportedMixer){
					balance.setEnabled(true);
					mute.setEnabled(true);
				}
			}
			else if(e.getSource() == stop){
				//ici le bouton stop du lecteur
				
				player.stop();
				
				//replacement dans les etat initiaux
				play.setEnabled(true);
				stop.setEnabled(false);
				
				if(supportedMixer){
					balance.setValue(0);
					balance.setEnabled(false);
					mute.setIcon(new ImageIcon(getClass().getResource("/resources/son.png")));
					mute.setEnabled(false);
					isMute=false;
				}
			}
			else{
				//ici l'activation /desactivation de la sourdine
				if(!isMute){
					mute.setIcon(new ImageIcon(getClass().getResource("/resources/mute.png")));
					 player.getMute().setValue(!player.getMute().getValue());
				}
				else{
					mute.setIcon(new ImageIcon(getClass().getResource("/resources/son.png")));
					 player.getMute().setValue(!player.getMute().getValue());
				}
				
				//inversion de l'etat de la sourdine
				isMute=!isMute;
			}
		}
		
	}
	
	/**
	 * Class de gestion du slider de controle de la balance
	 */
	public class EcouteurBalance implements ChangeListener{

		public void stateChanged(ChangeEvent e) {
			float bal = balance.getValue();
            player.getBalanceControl().setValue(bal);
		}
		
	}
	
	/**
	 * Classe de gestion des actions sur les boutons
	 * (Declenche les traitements)
	 */
	public class EcouteurBouton implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == executer) {
				//on lance la popup d'attente
				att=new Attente(g);
				
				//on ne permet pas de recliquer dans que le traitement n'est pas termine
				executer.setEnabled(false);
				
				// lancement de la resolution suivant la methode choisie
				switch(choixMethode.getSelectedIndex()){
				case 0:
					//methode aleatoire pure
					setNbSol(0);
					Aleatoire al=new Aleatoire(getTaille(),g,false);
					al.start();
					break;
					
				case 1:
					//methode semi aleatoire
					setNbSol(0);
					Aleatoire al1=new Aleatoire(getTaille(),g,true);
					al1.start();
					break;
				
				case 2:
					//methode backtracking 1 solution
					setNbSol(0);
					Backtracking bt= new Backtracking(getTaille(),false,g);
					bt.start();
					break;
					
				case 3:
					//methode backtracking
					setNbSol(0);
					Backtracking bt1= new Backtracking(getTaille(),true,g);
					bt1.start();
					break;
					
				case 4:
					//methode par recuit
					setNbSol(0);
					Recuit rt= new Recuit(getTaille(),g);
					rt.start();
					break;
					
				case 5:
					//methode full -aleatoire multiThreader
					setNbSol(0);
					Thread[] tabThread=new Thread[nbThread];
					for(int i=0;i<nbThread;i++){
						Thread t=new Thread(new AleatoireMultiThread(getTaille(),g,false));
						tabThread[i]=t;
					}
					
					for(Thread t: tabThread)
						t.start(); 
					break;
					
				case 6:
					//methode semi-aleatoire multiThreader
					setNbSol(0);
					Thread[] tabThread1=new Thread[nbThread];
					for(int i=0;i<nbThread;i++){
						Thread t=new Thread(new AleatoireMultiThread(getTaille(),g,true));
						tabThread1[i]=t;
					}
					
					for(Thread t: tabThread1)
						t.start(); 
					break;
					
				case 7:
					//methode de resolution par recuit simulÃ© multiThreader
					setNbSol(0);
					Thread[] tabThread2=new Thread[nbThread];
					for(int i=0;i<nbThread;i++){
						Thread t=new Thread(new RecuitMultiThread(getTaille(),g));
						tabThread2[i]=t;
					}
					
					for(Thread t: tabThread2)
						t.start(); 
					break;
				
				default:
					System.out.println("Methode de resolution inconnu");
					System.exit(-1);
				}
			} else {
				// ici bouton quitter
				System.exit(0);
			}
		}

	}
}
