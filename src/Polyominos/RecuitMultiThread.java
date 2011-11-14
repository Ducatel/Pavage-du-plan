package Polyominos;

/**
 * Class qui permet de resoudre le probleme via le recuit simule
 * mais en utilisant tous les threads d'execution sans concurrence disponible 
 * sur le processeur
 * @author David Ducatel
 *
 */
public class RecuitMultiThread extends Recuit {

	/**
	 * Boolean permettant de savoir si un thread a trouver une solution
	 */
	public static boolean resolu;
	
	public RecuitMultiThread(int taille, Gestion g) {
		super(taille, g);
		resolu=false;
		
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
	 * Methode d'action des threads
	 * Lance la resolution du probleme et le premier thread qui obtient une solution 
	 * effectue des operations pour l'affichage
	 */
	public void run(){
		//lancement de la resolution du probleme
		resolution();
		
		//lance les traitements finaux	
		finalise();
	}
	
	private synchronized void finalise(){
		
		//seul le premier thread va pouvoir entree ici
		if(!setResolu()){
			
			//Affichage du temps de traitement
			g.convertTime(System.currentTimeMillis() - start); 
			
			//fermeture de la popup d'attente
			g.att.dispose(); 
			
			//on genere l'affichage pour une solution
			g.createVisu(plat.tabCoul); 
			
			//on affiche que l'ont a trouver une solution
			g.setNbSol(1);
			
			//on affiche que nous visualison la premiere solution
			g.nbSolVisu.setText("Visualisation 1");
			
			//Deblocage du bouton executer 
			g.executer.setEnabled(true);
			
			//arrete les autres thread
			setResolu();
		}
	}
	
	/**
	 * Fonction qui resoud le probleme en utilisant le recuit simule
	 */
	public void resolution() {
		
			//temperature
			double T = plat.getScore2();

			//iterations a chaque pas de temperature
			int Nt = 10;
			
			//coef pour le recalcul de la temperature
			double coef=0.6;
			
			//generation d'un second plateau (pour comparaison)
			Plateau plateau2;
			
			while(plat.getScore2()!=0 && !resolu) {
				for( int m=0; m < Nt; m++ ) {
					plateau2 = voisin();
					int delta = plateau2.getScore2() - plat.getScore2();
					if(accepte( delta, T )){
						plat = plateau2;
						listePolyo=listeTmp;
					}
				}
				
				//recalcul de la temperature
				T = decroissance(T, coef);
				
			}
		
	}

}
