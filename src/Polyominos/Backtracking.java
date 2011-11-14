package Polyominos;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class qui va gere la resolution du probleme par backtracking
 * @author David Ducatel
 *
 */
public class Backtracking extends Thread{

	/**
	 * Plateau ou vont etre poser les polyominos au cour de la resolution
	 */
	public Plateau plat;
	
	/**
	 * ArrayList des polyominos a placer pour obtenir une solution 
	 */
	public ArrayList<Polyominos> listePolyo;
	
	/**
	 * HashSet qui va contenir toutes les solutions du probleme
	 */
	public HashSet<Plateau> listeSolution;
	
	/**
	 * Variable qui permet de savoir si on veux une ou toutes les solutions du probleme
	 * true == toutes les solutions , false == une solution
	 */
	public boolean allSolution; 
	
	/**
	 * Variable qui va contenir la taille du plateau du probleme
	 */
	public int taille;
	
	/**
	 * Objet de l'IHM, va me permettre de mettre a jour celui-ci
	 */
	private Gestion g;
	
	/**
	 * Variable qui va permettre de calculer le temps de resolution
	 */
	private long start ;
	
	
	/**
	 * Constructeur qui va mettre en place les variables utile a la resolution du probleme
	 * @param taille taille du probleme
	 * @param allSolution Si l'on veut une ou toutes les solution
	 * @param g Objet de l'IHM
	 */
	public Backtracking(int taille, boolean allSolution,Gestion g){
		//lancement du compteur
		start=System.currentTimeMillis();
		
		plat=new Plateau(taille);
		this.taille=taille;
		this.allSolution=allSolution;
		this.g=g;
		
		listePolyo=new ArrayList<Polyominos>();
		listeSolution=new HashSet<Plateau>();
		
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
		
		//Choix de l'affichage suivant si l'ont a une solution ou toutes les solution
		if(allSolution)
			g.createVisu(listeSolution); //on genere l'affichage pour toutes les solution
		else
			g.createVisu(plat.tabCoul); //on genere l'affichage pour une solution
		
		
		//Deblocage du bouton executer 
		g.executer.setEnabled(true);
		
	}
	
	/**
	 * Methode de resolution par backtracking
	 * Fonction recursive
	 */
	private boolean resolution(){
		Polyominos p;
		int indexPolyo;
		
		//double boucle de parcour du plateau
		for(int ligne=0;ligne<taille;ligne++){
			for(int col=0;col<taille;col++){
				
				/*
				 * Recuperation d'un polyominos qui n'est pas encore pose
				 * si l'index du polyo est un index valide pour la liste des polyo et si 
				 * le polyo n'est pas deja pose
				 */
				indexPolyo=0;
				while(indexPolyo<(listePolyo.size()-1) && listePolyo.get(indexPolyo).isPoser()){
					indexPolyo++;
				}
				
				//on stocke le polyominos trouve precedement
				p=listePolyo.get(indexPolyo);
				
				//on verifie si le polyominos est deja pose
				//si celui-ci est pose, cela indique que nous avons trouver une solution
				if(p.isPoser()){
					saveSolution(plat);
					//modification du nombre de solution trouver
					g.setNbSol(listeSolution.size());
					return true;
				}
				
				//si nous somme ici, c'est que le polyo n'est pas pose donc, on lance la pose
				
				while(indexPolyo<listePolyo.size()){
					//si le polyominos est pose
					if(plat.addBlock(p, ligne, col)){
						
						//on lance la prochaine etape du backtracking
						//et si on demande une solution et que resolution nous indique
						// que nous avons une solution, on quitte
						if(resolution() && !allSolution)
							return true;
						
						//si nous n'avons pas de solution, on retire le polyo
						plat.removeBlock(p,ligne,col);
						
					}
					
					//si nous arrivons ici, cela indique que le polyo n'as pas plus etre pose
					//ou que nous avons supprime un polyominos
					//Donc on effectue une transformation si c'est possible
					if(p.nbVarianteRestante>0)
						p.getVariante();
					else{
						//sinon on remet a zero le nombre de transformation possible
						p.resetNbVarianteRestante();
						
						//et on change de polyomino
						indexPolyo++;
						while(indexPolyo<listePolyo.size() && listePolyo.get(indexPolyo).isPoser()){
							indexPolyo++;
						}
						if(indexPolyo!=listePolyo.size())
							p=listePolyo.get(indexPolyo);
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Fonction qui sauvegarde les plateau solution
	 * @param p plateau solution a stocker
	 */
	private void saveSolution(Plateau p){
		listeSolution.add(new Plateau(p));
	}
	
}
