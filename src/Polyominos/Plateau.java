package Polyominos;

/**
 * Class qui gere la surface de pose des polyominos
 * @author David Ducatel
 *
 */
public class Plateau{
	
	/**
	 * Variable qui contient la taille du plateau de resolution
	 */
	public int taille;
	
	/**
	 * Variable contenant la representation du plateau
	 */
	public int[][] plateau;
	
	/**
	 * Variable qui contient le score optimal du plateau
	 */
	public int scoreOptimal;
	
	/**
	 * Variable qui contient les elements utile a la representation graphique du plateau
	 */
	public String[][] tabCoul;
	
	/**
	 * Constructeur qui creer le plateau de resolution
	 * @param taille taille que va avoir le plateau de résolution
	 */
	public Plateau(int taille) {
		this.taille = taille;
		plateau = new int[taille][taille];
		tabCoul = new String[taille][taille];
		scoreOptimal = taille * taille;
	}

	/**
	 * Constructeur par recopie
	 * 
	 * @param tmp
	 *            Plateau a reproduire
	 */
	public Plateau(Plateau tmp) {
		this.taille = tmp.taille;

		plateau = new int[taille][taille];
		tabCoul = new String[taille][taille];

		for (int i = 0; i < taille; i++) {
			for (int j = 0; j < taille; j++) {
				plateau[i][j] = tmp.plateau[i][j];
				tabCoul[i][j] = tmp.tabCoul[i][j];
			}
		}

		scoreOptimal = taille * taille;
	}

	/**
	 * Fonction qui calcul le score du plateau
	 * (score optimal == taille*taille)
	 * @return retourne le score du plateau
	 */
	public int getScore() {
		int score = 0;

		for (int i = 0; i < this.taille; i++)
			for (int j = 0; j < this.taille; j++)
				score += plateau[i][j];

		return score;
	}

	/**
	 * Fonction qui calcul le score du plateau
	 * (score optimal == 0)
	 * @return retourne le score du plateau
	 */
	public int getScore2(){
		int score = scoreOptimal;

		for (int i = 0; i < this.taille; i++)
			for (int j = 0; j < this.taille; j++)
				score -= plateau[i][j];

		return score;
	}
	
	/**
	 * Fonction qui pose un polyominos sur le plateau
	 * 
	 * @param p
	 *            Polyominos que l'on essaie de poser
	 * @return retourn vrai si le block a ete pose, sinon retourne false
	 */
	public boolean addBlock(Polyominos p) {
		int hauteur;
		int largeur;

		// tant que n'avons pas essaye toutes les possibilite (rotation,
		// symetrie) du polyominos
		while (p.nbVarianteRestante != 0) {

			hauteur = p.getHauteur();
			largeur = p.getLargeur();

			// boucle de parcour du tableau
			for (int ligne = 0; ligne <= taille; ligne++) {
				for (int col = 0; col <= taille; col++) {

					// si il y a de la place pour pose en largeur et en hauteur
					if (((ligne + hauteur) <= taille)
							&& ((col + largeur) <= taille)) {

						// on verifie si polyo est posable
						if (isPosable(p, ligne, col)) {
							// on pose le polyominos							
							for (int lignP = 0; lignP < p.getHauteur(); lignP++) {
								for (int colP = 0; colP < p.getLargeur(); colP++) {
									plateau[(lignP + ligne)][(colP + col)] += p.polyominos[lignP][colP];

									// si la valeur de la case du polyominos est
									// different de 0, on pose la couleur
									if (p.polyominos[lignP][colP] != 0)
										tabCoul[(lignP + ligne)][(colP + col)] = p
												.getCouleur()
												+ "_" + p.getName() + p.nbBloc;

								}
							}
							// le polyominos est pose
							p.pose=true;
							
							//on renseigne le point d'origine de la pose (le point x,y de la "boite" du polyominos)
							p.ptOrigin=ligne+"_"+col;
							
							return true;
						}
					}
				}
			}

			// on effectue une transformation
			p.getVariante();
		}

		// si nous arrivons ici, c'est que le polyominos n'as pas plus etre pose
		return false;
	}
	
	/**
	 * Fonction qui essaie de pose un polyominos a un endroit precis du plateau
	 * 
	 * @param p
	 *            polyominos a pose
	 * @param ligne
	 *            index de la ligne pour la localisation de la place de pose
	 * @param col
	 *            index de la colonne pour la localisation de la place de pose
	 * @return retourne true si le polyominos est posé, sinon, retourne false
	 */
	public boolean addBlock(Polyominos p, int ligne, int col) {
		int hauteur = p.getHauteur();
		int largeur = p.getLargeur();

		// si il y a de la place pour pose en largeur et en hauteur
		if (((ligne + hauteur) <= taille) && ((col + largeur) <= taille)) {

			// on verifie si polyo est posable
			if (isPosable(p, ligne, col)) {
				// on pose le polyominos
				p.pose = true;

				for (int lignP = 0; lignP < p.getHauteur(); lignP++) {
					for (int colP = 0; colP < p.getLargeur(); colP++) {
						plateau[(lignP + ligne)][(colP + col)] += p.polyominos[lignP][colP];

						// si la valeur de la case du polyominos est different
						// de 0, on pose la couleur
						if (p.polyominos[lignP][colP] != 0)
							tabCoul[(lignP + ligne)][(colP + col)] = p
									.getCouleur()
									+ "_" + p.getName() + p.nbBloc;

					}
				}
				// le polyominos est pose
				return true;
			}
		}
		// si nous arrivons ici, c'est que le polyominos n'est pas posable ici
		return false;
	}
	
	
	/**
	 * Methode qui ajoute un polyominos au tableau 
	 * sans lui faire subir de transformation
	 * @param p polyominos a insérer
	 * @return true sie le polyominos est posé, sinon false
	 */
	public boolean simpleAddBlock(Polyominos p) {
		int hauteur=p.getHauteur();
		int largeur= p.getLargeur();

		// boucle de parcour du tableau
		for (int ligne = 0; ligne <= taille; ligne++) {
			for (int col = 0; col <= taille; col++) {

				// si il y a de la place pour pose en largeur et en hauteur
				if (((ligne + hauteur) <= taille)
						&& ((col + largeur) <= taille)) {

					// on verifie si polyo est posable
					if (isPosable(p, ligne, col)) {
						// on pose le polyominos							
						for (int lignP = 0; lignP < p.getHauteur(); lignP++) {
							for (int colP = 0; colP < p.getLargeur(); colP++) {
								plateau[(lignP + ligne)][(colP + col)] += p.polyominos[lignP][colP];

								// si la valeur de la case du polyominos est
								// different de 0, on pose la couleur
								if (p.polyominos[lignP][colP] != 0)
									tabCoul[(lignP + ligne)][(colP + col)] = p
											.getCouleur()
											+ "_" + p.getName() + p.nbBloc;

							}
						}
						// le polyominos est pose
						p.pose=true;
						
						//on renseigne le point d'origine de la pose (le point x,y de la "boite" du polyominos)
						p.ptOrigin=ligne+"_"+col;
						
						return true;
					}
				}
			}
		}

		// si nous arrivons ici, c'est que le polyominos n'as pas plus etre pose
		return false;
	}
	
	/**
	 * Fonction qui supprime un polyominos du plateau
	 * @param p polyominos a supprimer
	 */
	public void removeBlock(Polyominos p){
		
		// ligne d'origine de la pose du polyominos
		int ligne=p.getLigneOrigin();
		
		// col d'origine de la pose du polyominos
		int col=p.getColOrigin();
		
		//suppression du polyominos
		removeBlock(p, ligne, col);
	}

	/**
	 * Fonction qui supprime un polyominos du plateau
	 * @param p polyominos a supprimer
	 * @param ligne ligne ou est placer le polyominos
	 * @param col colonne ou est placer le polyominos
	 */
	public void removeBlock(Polyominos p, int ligne, int col) {

		//Boucle de parcour du polyominos
		for (int lignP = 0; lignP < p.getHauteur(); lignP++) {
			for (int colP = 0; colP < p.getLargeur(); colP++) {
				
				//On Supprime le polyo du plateau
				plateau[(lignP + ligne)][(colP + col)] -= p.polyominos[lignP][colP];

				// si la valeur de la case du polyominos est different de 0, on
				// supprimme la couleur
				if (p.polyominos[lignP][colP] != 0)
					tabCoul[(lignP + ligne)][(colP + col)] = null;

			}
		}

		// une fois le polyominos enlever, on change le boolean qui indique si
		// le polyo est posé
		p.pose = false;
		
		//on supprime le point d'origine de la boite du polyominos
		p.ptOrigin=null;
	}
	
	/**
	 * redefinition de la methode equals pour l'insertion des solution sans
	 * doublons
	 */
	public boolean equals(Object obj) {
		// Vérification de l'égalité des références
		if (obj == this) {
			return true;
		}

		// On vérifie si le Object est une instance de plateau
		if (obj instanceof Plateau) {
			Plateau p = (Plateau) obj;

			// si la taille est differente (ne devrai jamais arriver)
			if (this.taille != p.taille)
				return false;

			// On vérifie chaque case de la représentation du plateau
			for (int i = 0; i < this.taille; i++) {
				for (int j = 0; j < this.taille; j++) {
					if (!this.tabCoul[i][j].equals(p.tabCoul[i][j]))
						return false;
				}
			}

		}
		return true;

	}

	/**
	 * redefinition de la methode hashCode pour l'insertion des solution sans
	 * doublons
	 */
	public int hashCode() {
		// variable qui va contenir le hashCode (placer a 7 arbitrairement (7==
		// nombre premier))
		int result = 7;

		// Varaible qui va multiplier le hashCode a chaque ajout de parametre
		// (placer a 17 arbitrairement (17== nombre premier))
		final int multiplier = 17;

		//Calcul du HashCode sur le tableau représentatif du plateau
		for (int i = 0; i < this.taille; i++)
			for (int j = 0; j < this.taille; j++)
				result = multiplier * result + this.tabCoul[i][j].hashCode();

		return result;
	}
	
	/**
	 * Fonction qui va permettre un affichage du plateau dans le shell
	 */
	public String toString() {
		String s = "";
		for (int y = plateau[0].length - 1; y >= 0; y--) {
			for (int x = 0; x < plateau.length; x++) {
				if (plateau[x][y] == 1)
					s += "X ";
				else if (plateau[x][y] == 0)
					s += "O ";
				else
					s += "" + plateau[x][y];
			}
			s += "\n";
		}
		return s;
	}
	
	/**
	 * Fonction qui test si un polyominos est posable
	 * 
	 * @param p
	 *            Polyominos a poser
	 * @param ligne
	 *            ligne de depart pour le test de pose
	 * @param col
	 *            colonne de depart pour le test de pose
	 * @return retourne vrai si le polyominos est posable, sinon false
	 */
	private boolean isPosable(Polyominos p, int ligne, int col) {
		/*
		 * pour chaque case du tableau du polyominos, on verifie si la somme de
		 * la case du plateau + la case du polyominos est different de 0 ou 1
		 */
		for (int lignP = 0; lignP < p.getHauteur(); lignP++) {
			for (int colP = 0; colP < p.getLargeur(); colP++) {
				// si il y a deja un morceau de polyominos de pose, on retourne
				// false
				if (plateau[(lignP + ligne)][(colP + col)]
						+ p.polyominos[lignP][colP] != 0
						&& plateau[(lignP + ligne)][(colP + col)]
								+ p.polyominos[lignP][colP] != 1)
					return false;
			}
		}

		return true;
	}

}
