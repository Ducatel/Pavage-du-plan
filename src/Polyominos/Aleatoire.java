package Polyominos;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class qui va gere la resolution par methode aleatoire
 * @author David Ducatel
 *
 */
public class Aleatoire extends Thread {

	/**
	 * ArrayList des polyominos a placer pour obtenir une solution
	 */
	public ArrayList<Polyominos> listePolyo ;
	
	/**
	 * Plateau ou vont etre poser les polyominos au cour de la resolution
	 */
	public Plateau plat;
	
	/**
	 * Objet de l'IHM, va me permettre de mettre a jour celui-ci
	 */
	protected Gestion g;
	
	/**
	 * boolean qui va permettre de choisir que methode de resolution aleatoire doit etre choisie
	 */
	protected boolean methode;
	
	/**
	 * Variable qui va permettre de calculer le temps de resolution
	 */
	protected long start ;
	
	/**
	 * Construteur qui va generer les donnees utile a la resolution du probleme
	 * @param taille taille du plateau du probleme
	 * @param g Objet de l'IHM
	 * @param methode boolean qui permet de choisir quel methode de resolution va etre utilise
	 */
	public Aleatoire(int taille,Gestion g,boolean methode){
		//lancement du compteur
		start=System.currentTimeMillis();
		
		plat=new Plateau(taille);
		listePolyo=new ArrayList<Polyominos>();
		this.g=g;
		this.methode=methode;
		
		
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
		

	}
	
	/**
	 * Fonction qui replace aux valeurs par defauts les 
	 *	variation restante de chaque polyominos
	 */
	public void resetNbVarianteRestanteAll(){
		for(int i=0;i<listePolyo.size();i++)
			listePolyo.get(i).resetNbVarianteRestante();
	}
	
	/**
	 * Fonction qui melange la liste des polyominos
	 */
	public void shuffle(){
		resetNbVarianteRestanteAll();
		Collections.shuffle(listePolyo);
	}

	/**
	 * Methode d'action du thread
	 * Lance la resolution du probleme et effectue des operations pour l'affichage
	 */
	public void run() {
		//on lance la resolution
		if(methode)
			resolutionSemiAleat(); 
		else
			resolutionFullAleat();
		
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
	}
	
	/**
	 * Fonction qui effectue la resolution de maniere semi-aleatoire
	 * c'est a dire que la pose du polyominos test toutes les variantes possible
	 * afin d'augmente les chances de pouvoir pose le polyominos
	 */
	private void resolutionSemiAleat(){
		Plateau tmp=new Plateau(plat);

		//tant que le score du plateau n'est pas egal au score maximal
		while(tmp.getScore()!=tmp.scoreOptimal  ){
			
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
		while(tmp.getScore()!=tmp.scoreOptimal  ){
			
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
