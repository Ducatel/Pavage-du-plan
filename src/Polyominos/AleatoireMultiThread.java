package Polyominos;

/**
 * Class qui permet de resoudre le probleme de maniere aleatoire
 * mais en utilisant tous les threads d'execution sans concurrence disponible 
 * sur le processeur
 * @author David Ducatel
 *
 */
public class AleatoireMultiThread extends Aleatoire implements Runnable{

	/**
	 * Boolean permettant de savoir si un thread a trouver une solution
	 */
	public static boolean resolu;
	
	/**
	 * Construteur qui va generer les donnees utile a la resolution du probleme
	 * @param taille taille du plateau du probleme
	 * @param g Objet de l'IHM
	 * @param methode boolean qui permet de choisir quel methode de resolution va etre utilise
	 */
	public AleatoireMultiThread(int taille, Gestion g, boolean methode) {
		super(taille, g, methode);
		resolu=false;
	}
	
	/**
	 * Methode d'action des threads
	 * Lance la resolution du probleme et le premier thread qui obtient une solution 
	 * effectue des operations pour l'affichage
	 */
	public void run(){

		//on lance la resolution
		if(this.methode)
			resolutionSemiAleat(); 
		else
			resolutionFullAleat();
		
		//lance les traitements finaux	
		finalise();
		
	}
	
	/**
	 * Fonction qui permet de faire arrete le travail des Thread
	 */
	public synchronized static boolean setResolu(){
		boolean b=resolu;
		resolu=true;
		return b;
	}
	
	/**
	 * Fonction permet d'afficher le resultat du thread qui fini en premier sa resolution
	 */
	private synchronized void finalise(){		
		
		//seul le premier thread va pouvoir entree ici
		if(!setResolu()){
			//on genere l'affichage
			g.createVisu(plat.tabCoul); 
			
			//on affiche que l'ont a trouver une solution
			g.setNbSol(1);
			
			//on affiche que nous visualison la premiere solution
			g.nbSolVisu.setText("Visualisation N 1");
			
			//on affiche le temps de traitement
			g.convertTime(System.currentTimeMillis() - start); 
			
			//on ferme la popup d'attente
			g.att.dispose(); 
			
			//on re permet de cliquer sur le bouton executer
			g.executer.setEnabled(true);
			
			//arrete les autres thread
			setResolu();
		}
	}

	/**
	 * Fonction qui effectue la resolution de maniere semi-aleatoire
	 * c'est a dire que la pose du polyominos test toutes les variantes possible
	 * afin d'augmente les chances de pouvoir pose le polyominos
	 */
	private void resolutionSemiAleat(){

		Plateau tmp=new Plateau(plat);

		//tant que le score du plateau n'est pas egal au score maximal
		while(tmp.getScore()!=tmp.scoreOptimal && !resolu ){
			
			//on melange la liste de polyominos
			shuffle();
			//on creer un plateau temporaire vide
			tmp=new Plateau(plat);
			
			//on essaie de pose tous les polyominos de la liste
			for(int i=0;i<listePolyo.size();i++){
				Polyominos p=listePolyo.get(i);
				tmp.addBlock(p);
			}
		}
		
		//on a trouve une solution
		plat=new Plateau(tmp);
	}
	
	/**
	 * Fonction qui effectue la resolution de maniere aleatoire
	 */
	private void resolutionFullAleat(){
		Plateau tmp=new Plateau(plat);

		//tant que le score du plateau n'est pas egal au score maximal
		while(tmp.getScore()!=tmp.scoreOptimal  && !resolu){
			
			//on melange la liste de polyominos
			shuffle();
			//on creer un plateau temporaire vide
			tmp=new Plateau(plat);
			
			//on essaie de pose tous les polyominos de la liste
			for(int i=0;i<listePolyo.size();i++){
				Polyominos p=listePolyo.get(i);
				p.getVarianteAleat();
				tmp.simpleAddBlock(p);
			}
		}
		
		//on a trouve une solution
		plat=new Plateau(tmp);
	}
}
