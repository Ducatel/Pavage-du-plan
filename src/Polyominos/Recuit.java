package Polyominos;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class qui gere la resolution par recuit
 * @author David Ducatel
 *
 */
public class Recuit extends Thread{
	
	/**
	 * Plateau ou vont etre poser les polyominos au cour de la resolution
	 */
	public Plateau plat;
	
	/**
	 * ArrayList des polyominos a placer pour obtenir une solution 
	 */
	public ArrayList<Polyominos> listePolyo, listeTmp;
	
	/**
	 * Variable qui va contenir la taille du plateau du probleme
	 */
	public int taille;
	
	/**
	 * Objet de l'IHM, va me permettre de mettre a jour celui-ci
	 */
	protected Gestion g;
	
	/**
	 * Variable qui va permettre de calculer le temps de resolution
	 */
	protected long start ;
		
	/**
	 * Constructeur qui va mettre en place les variables utile a la resolution du probleme
	 * @param taille taille du probleme
	 * @param g Objet de l'IHM
	 */
	public Recuit(int taille,Gestion g){
		
		//lancement du compteur
		start=System.currentTimeMillis();
		
		plat=new Plateau(taille);
		this.taille=taille;
		this.g=g;
		
		listePolyo=new ArrayList<Polyominos>();
		
		//ajout des polyo suivant la taille du plateau passé en parametre
		switch(taille){
		 case 3:
			 listePolyo.add(new Polyominos(5, 'J'));
			 listePolyo.add(new Polyominos(4, 'D'));
			 break;
			
		 case 4:
			listePolyo.add(new Polyominos(5, 'J'));
			listePolyo.add(new Polyominos(4, 'D'));
			listePolyo.add(new Polyominos(4, 'A'));
			listePolyo.add(new Polyominos(3, 'A'));
			 break;
			 
		 case 5:
			listePolyo.add(new Polyominos(5, 'A'));
			listePolyo.add(new Polyominos(5, 'B'));
			listePolyo.add(new Polyominos(5, 'C'));
			listePolyo.add(new Polyominos(5, 'H'));
			listePolyo.add(new Polyominos(1, 'A'));
			listePolyo.add(new Polyominos(4, 'E'));
			 break;
			 
		 case 9:
			//ajout des 3 dominos
			listePolyo.add(new Polyominos(2, 'A')); 
			listePolyo.add(new Polyominos(2, 'A')); 
			listePolyo.add(new Polyominos(2, 'A')); 
			
			//ajout des pentaminos
			listePolyo.add(new Polyominos(5, 'A')); 
			listePolyo.add(new Polyominos(5, 'B')); 
			listePolyo.add(new Polyominos(5, 'C'));
			listePolyo.add(new Polyominos(5, 'D')); 
			listePolyo.add(new Polyominos(5, 'E')); 
			listePolyo.add(new Polyominos(5, 'F')); 
			listePolyo.add(new Polyominos(5, 'G')); 
			listePolyo.add(new Polyominos(5, 'H')); 
			listePolyo.add(new Polyominos(5, 'I'));
			listePolyo.add(new Polyominos(5, 'J')); 
			listePolyo.add(new Polyominos(5, 'K')); 
			listePolyo.add(new Polyominos(5, 'L')); 
			
			//ajout des doublons
			listePolyo.add(new Polyominos(5, 'G')); 
			listePolyo.add(new Polyominos(5, 'J')); 
			listePolyo.add(new Polyominos(5, 'K')); 
			break;
			 
		 default:
			 System.out.println("Liste de polyominos non gere");
			 System.exit(-1);
		}

		//preremplissage du plateau
		remplissage();
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
			
			while(plat.getScore2()!=0) {
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
	
	/**
	 * Fonction qui calcul la decroissance 	de la temperature
	 * @param T Temperature actuel
	 * @param coef coeficient de modification de temperature
	 * @return nouvelle temperature
	 */
	public double decroissance(double T, double coef){
		double r=coef*T;
		return r;
	}
	
	/**
	 * Fonction qui calcul sur un plateau est accepter ou non
	 * @param delta ecart de score entre les deux plateau
	 * @param T temperature
	 * @return retourne true si le plateau est accepter
	 */
	public boolean accepte( int delta, double T ) {
		if( delta >= 0 ) {
			double A = Math.exp( -(double)(delta)/(double)(T) );
			if( Math.random() >= A ) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Fonction qui compte le nombre de piece poser sur le plateau
	 * @return nbPiece poser
	 */
	public int nbPiecePose(){
		int nbPiece=0;
		
		for(Polyominos p: listePolyo){
			if(p.pose){
				System.out.println("+-+");
				nbPiece++;
			}
		}
		
		return nbPiece;
	}
	
	/**
	 * Methode d'action du thread
	 * Lance la resolution du probleme et effectue des operations pour l'affichage
	 */
	public void run(){
		//lancement de la resolution du probleme
		resolution();
		
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
	}
	
	/**
	 * Fonction qui calcul un plateau voisin
	 * @return retourne un plateau voisin
	 */
	protected Plateau voisin(){
		Plateau tmp=new Plateau(plat);
		listeTmp=new ArrayList<Polyominos>();
		for(Polyominos p : listePolyo){
			listeTmp.add(new Polyominos(p));
		}
		/** on enleve un certain pourcentage de piece qui sont deja placer sur le plateau **/

		//melange de la liste des polyominos
		Collections.shuffle(listeTmp);
		
		//index de parcour
		int k=0;
		int l=0;
		
		//polyominos qui va etre selection et peut etre supprimé
		Polyominos pol;
		
		//Nombre de polyo a remove du plateau
		int nbSupp=(int)(Math.ceil(listeTmp.size()*0.3));
		
		//on enleve les pieces
		while(k<nbSupp && l<listeTmp.size()){
			
			//recuperation du polyominos a supprimmé
			pol=listeTmp.get(l);
			
			//on regarde si le polyominos est pose 
			if(pol.pose){
				
				//suppression du polyominos
				tmp.removeBlock(pol);
				
				//on replace sont nombre de variante a sa valeur initial
				pol.resetNbVarianteRestante();
				k++;
			}
			l++;
		}

		/** On transforme un certain pourcentage de piece  **/
		
		//nombre de pieces a transformer
		int nbPolyoTransf=(int)Math.ceil(listeTmp.size()*0.2);
		
		//on mute le nombre de piece voulu
		for(int i=0;i<nbPolyoTransf;i++){
			
			
			// index de la pieces a transformer
			int indexTransf;
			
			// polyominos qui va etre transformer
			Polyominos p;
			
			//tant que nous n'avons pas trouver de piece non poser
			do{
				indexTransf=(int)(Math.random()*listeTmp.size());
				p=listeTmp.get(indexTransf);
			}while(p.pose);
			
			//on effectue une transformation aleatoire sur le polyominos
			p.getVarianteAleat();
			
			
		}
		
		/** On repose des polyominos **/
		
		for(Polyominos p1 : listeTmp){
			//on essaie de poser le polyominos si celui-ci n'est pas deja poser
			if(!p1.pose){
				tmp.simpleAddBlock(p1);
			}
		}
		
		return tmp;
	}
	
	/**
	 * Fonction qui pre-remplie le tableau pour le recuit
	 */
	private void remplissage(){
		Collections.shuffle(listePolyo);
		for(Polyominos p : listePolyo){
			plat.addBlock(p);
		}
	}

}
