package Polyominos;

/**
 * Class qui gere les polyominos
 * @author David Ducatel
 *
 */
public class Polyominos {
	
	/**
	 * forme du polyominos
	 */
	public char name; 
	
	/**
	 * representation du polyominos
	 */
	public int[][] polyominos; 
	
	/**
	 * nombre de block contenue dans le polyominos
	 */
	public int nbBloc; 
	
	/**
	 * contient le nombre de variante possible
	 */
	public int nbVariante; 
	
	/**
	 * contient le nombre de variante encore possible
	 */
	public int nbVarianteRestante; 
	
	/**
	 * contient la couleur de representation du polyominos
	 */
	public String couleur; 
	
	/**
	 * boolean qui permet de savoir si le polyominos est pose sur un plateau
	 */
	public boolean pose;
	
	/**
	 * variable qui permet de savoir a quel point a ete pose le polyominos
	 */
	public String ptOrigin=null;
	
	/**
	 * Creer un objet polyominos compose d'un nombre bloc preci et definisant une forme precise
	 * @param nbBloc nombre de block de base (monomino) contenue dans le polyominos
	 * @param name nom de la forme que doit prendre le polyominos (suivant l'enonce du tp)
	 */
	public Polyominos(int nbBloc, char name){
		this.name=name;
		this.nbBloc=nbBloc;
		this.pose=false;
		
		switch (nbBloc){
		
		case 1:
			createMonomino();
			break;
		
		case 2:
			createDomino(name);
			break;
		
		case 3:
			createTriominos(name);
			break;
		
		case 4:
			createTetrominos(name);
			break;
		
		case 5:
			createPentaminos(name);
			break;
			
		default:
			System.out.println("Classe de polyominos non gere");
			System.exit(-1);
		
		}
		
		resetNbVarianteRestante();
		
	}
	
	/**
	 * Constructeur par recopie
	 * @param p polyominos a recopier
	 */
	public Polyominos(Polyominos p){
		name=p.name; 
		polyominos=new int[p.getHauteur()][p.getLargeur()];
		for(int i=0; i<p.getHauteur(); i++){
			for(int j=0; j<p.getLargeur(); j++){
				polyominos[i][j]=p.polyominos[i][j];
			}
		}
		nbBloc=p.nbBloc;
		nbVariante=p.nbVariante;
		nbVarianteRestante=p.nbVarianteRestante;
		couleur=p.couleur;
		pose=p.pose;
		ptOrigin=p.ptOrigin;
	}
	
	/**
	 * Fonction qui renvoie le nom de la forme du polyominos
	 * @return nom de la forme du polyominos
	 */
	public char getName(){
		return name;
	}
	
	/**
	 * Fonction qui retourne le tableau representant le polyominos
	 * @return tableau representant le polyominos
	 */
	public int[][] getPolyominos(){
		return polyominos;
	}
	
	/**
	 * Fonction qui renvoi la ligne de la pose de la boite
	 * du polyominos
	 * @return ligne ou a ete pose le polyo
	 */
	public int getLigneOrigin(){
		if(ptOrigin==null)
			return 0;
		
		String [] tmp=ptOrigin.split("_");
		return Integer.parseInt(tmp[0]);
	}
	
	/**
	 * Fonction qui renvoi la colonne de la pose de la boite
	 * du polyominos
	 * @return colonne ou a ete pose le polyo
	 */
	public int getColOrigin(){
		if(ptOrigin==null)
			return 0;
		
		String [] tmp=ptOrigin.split("_");
		return Integer.parseInt(tmp[1]);
	}
	
	/**
	 * Fonction qui replace le nombre de variante a essaye a la valeur du 
	 * nombre de variante possible pour le polyominos
	 */
	public void resetNbVarianteRestante(){
		nbVarianteRestante=nbVariante;
	}
	
	/**
	 * Fonction qui applique la variation suivante au polyominos
	 */
	public void getVariante(){
		if(nbVarianteRestante==4)
			polyominos=symetrieHorizontal();
		else
			polyominos=rotationGauche();
		
		nbVarianteRestante--;
	}
	
	/**
	 * Fonction qui effectue un nombre aleatoire de
	 * transformation au polyominos
	 */
	public void getVarianteAleat(){
		int nbVariation=(int)(Math.random()*this.nbVariante);
		
		for(int i=0;i<nbVariation;i++)
			this.getVariante();
	}
	
	/**
	 * Fonction qui renvoie la hauteur du Polyominos
	 * @return la hauteur du polyominos
	 */
	public int getHauteur(){
		return polyominos.length;
	}
	
	/**
	 * Fonction qui renvoie la largeur d'un polyominos
	 * @return la largeur du polyominos
	 */
	public int getLargeur(){
		return polyominos[0].length;
	}
	
	/**
	 * Fonciton qui retourne la couleur associe au polyominos
	 * @return la couleur du polyominos
	 */
	public String getCouleur(){
		return couleur;
	}
	
	/**
	 * Fonction qui dit si le polyominos est pose
	 * @return true si le polyo a deja ete pose, false sinon
	 */
	public boolean isPoser(){
		return pose;
	}
	
	/**
	 * Fonction qui permet d'afficher un polyominos dans le shell
	 */
	public String toString(){
		String s="";
		for(int y=polyominos[0].length-1;y>=0;y--){
			for(int x=0;x<polyominos.length;x++){
				if(polyominos[x][y]==1)
					s+="X";
				else
					s+="O";
			}
			s+="\n";
		}
		return s;
	}
	
	/**
	 * Fonction qui creer un Monomino
	 */
 	private void createMonomino() {
		nbVariante=1;
		polyominos=new int[1][1];
		polyominos[0][0]=1;
		couleur="-16777216";
	}
	
	/**
	 * Fonction qui creer un domino de forme precise en fonction de son argument
	 * @param name forme a donnee au domino
	 */
	private void createDomino(char name) {
		nbVariante=2;
		polyominos=new int[2][1];
		polyominos[0][0]=1;
		polyominos[1][0]=1;
		couleur="-16776961";
		
	}
	
	/**
	 * Fonction qui creer un triominos de forme precise en fonction de son argument
	 * @param name forme a donnee au triominos
	 */
	private void createTriominos(char name) {
		switch(name){
		case 'A':
			nbVariante=2;
			polyominos=new int[3][1];
			polyominos[0][0]=1;
			polyominos[1][0]=1;
			polyominos[2][0]=1;
			couleur="-16711681";
			break;
			
		case 'B':
			nbVariante=9;
			polyominos=new int[2][2];
			polyominos[0][0]=1;
			polyominos[0][1]=1;
			polyominos[1][1]=1;
			couleur="-12566464";
			break;
		}
	}
	
	/**
	 * Fonction qui creer un tetrominos de forme precise en fonction de son argument
	 * @param name forme a donnee au tetrominos
	 */
	private void createTetrominos(char name) {
		switch(name){
		case 'A':
			nbVariante=2;
			polyominos=new int[4][1];
			polyominos[0][0]=1;
			polyominos[1][0]=1;
			polyominos[2][0]=1;
			polyominos[3][0]=1;
			couleur="-8355712";
			break; 
		case 'B':
			nbVariante=1;
			polyominos=new int[2][2];
			polyominos[0][0]=1;
			polyominos[1][0]=1;
			polyominos[0][1]=1;
			polyominos[1][1]=1;
			couleur="-16711936";
			break; 
		case 'C':
			nbVariante=9;
			polyominos=new int[2][3];
			polyominos[0][0]=1;
			polyominos[1][0]=1;
			polyominos[1][1]=1;
			polyominos[1][2]=1;
			couleur="-4144960";
			break; 
		case 'D':
			nbVariante=9;
			polyominos=new int[2][3];
			polyominos[0][0]=1;
			polyominos[1][1]=1;
			polyominos[0][1]=1;
			polyominos[0][2]=1;
			couleur="-65281";
			break; 
		case 'E':
			nbVariante=9;
			polyominos=new int[2][3];
			polyominos[0][0]=1;
			polyominos[0][1]=1;
			polyominos[1][1]=1;
			polyominos[1][2]=1;
			couleur="-14336";
			break; 
		}
	}
	
	/**
	 * Fonction qui creer un Pentaminos de forme precise en fonction de son argument
	 * @param name forme a donnee au Pentaminos
	 */
	private void createPentaminos(char name) {
		switch(name){
		case 'A':
			nbVariante=2;
			polyominos=new int[5][1];
			polyominos[0][0]=1;
			polyominos[1][0]=1;
			polyominos[2][0]=1;
			polyominos[3][0]=1;
			polyominos[4][0]=1;
			couleur="-20561";
			break; 
		case 'B':
			nbVariante=9;
			polyominos=new int[4][2];
			polyominos[0][1]=1;
			polyominos[1][1]=1;
			polyominos[2][1]=1;
			polyominos[3][1]=1;
			polyominos[3][0]=1;
			couleur="-65536";
			break; 
		case 'C':
			nbVariante=9;
			polyominos=new int[2][4];
			polyominos[0][0]=1;
			polyominos[0][1]=1;
			polyominos[0][2]=1;
			polyominos[0][3]=1;
			polyominos[1][2]=1;
			couleur="-1";
			break; 
		case 'D':
			nbVariante=9;
			polyominos=new int[3][3];
			polyominos[0][2]=1;
			polyominos[0][1]=1;
			polyominos[1][1]=1;
			polyominos[2][1]=1;
			polyominos[2][0]=1;
			couleur="-256";
			break; 
		case 'E':
			nbVariante=1;
			polyominos=new int[3][3];
			polyominos[0][1]=1;
			polyominos[1][0]=1;
			polyominos[1][1]=1;
			polyominos[1][2]=1;
			polyominos[2][1]=1;
			couleur="#8FBC8F";
			break; 
		case 'F':
			nbVariante=9;
			polyominos=new int[3][3];
			polyominos[0][0]=1;
			polyominos[0][1]=1;
			polyominos[0][2]=1;
			polyominos[1][0]=1;
			polyominos[2][0]=1;
			couleur="#9400D3";
			break; 
		case 'G':
			nbVariante=9;
			polyominos=new int[3][2];
			polyominos[0][0]=1;
			polyominos[0][1]=1;
			polyominos[1][1]=1;
			polyominos[2][1]=1;
			polyominos[1][0]=1;
			couleur="#B22222";
			break; 
		case 'H':
			nbVariante=9;
			polyominos=new int[4][2];
			polyominos[0][0]=1;
			polyominos[1][0]=1;
			polyominos[2][0]=1;
			polyominos[2][1]=1;
			polyominos[3][1]=1;
			couleur="#7CFC00";
			break; 
		case 'I':
			nbVariante=9;
			polyominos=new int[3][3];
			polyominos[0][0]=1;
			polyominos[1][0]=1;
			polyominos[2][0]=1;
			polyominos[1][1]=1;
			polyominos[1][2]=1;
			couleur="#DEB887";
			break; 
		case 'J':
			nbVariante=9;
			polyominos=new int[2][3];
			polyominos[0][0]=1;
			polyominos[0][1]=1;
			polyominos[0][2]=1;
			polyominos[1][0]=1;
			polyominos[1][2]=1;
			couleur="#008080";
			break; 
		case 'K':
			nbVariante=9;
			polyominos=new int[3][3];
			polyominos[0][1]=1;
			polyominos[1][1]=1;
			polyominos[1][2]=1;
			polyominos[2][1]=1;
			polyominos[2][0]=1;
			couleur="#87CEEB";
			break; 
		case 'L':
			nbVariante=9;
			polyominos=new int[3][3];
			polyominos[0][2]=1;
			polyominos[1][2]=1;
			polyominos[1][1]=1;
			polyominos[2][1]=1;
			polyominos[2][0]=1;
			couleur="#800080";
			break; 
		}
	}
	
	/**
	 * Fonction qui effectue une rotation droite sur le polyominos courant
	 * @return un tableau representatif du polyominos qui a subit une rotation droite
	 */
	@SuppressWarnings("unused")
	private int[][] RotationDroite(){
		int hauteur=polyominos[0].length;
		int largeur=polyominos.length;
		int[][] nouvPoly=new int[hauteur][largeur];

		int x2=hauteur-1;
		int y2;
		for(int y=hauteur-1;y>=0;y--){
			y2=largeur-1;
			for(int x=0;x<largeur;x++){
				nouvPoly[x2][y2--]=polyominos[x][y];
			}
			x2--;
		}
		return nouvPoly;
	}
	
	/**
	 * Fonction qui effectue une rotation gauche sur le polyominos courant
	 * @return un tableau representatif du polyominos qui a subit une rotation gauche
	 */
	private int[][] rotationGauche(){
		int hauteur=polyominos[0].length;
		int largeur=polyominos.length;
		int[][] nouvPoly=new int[hauteur][largeur];

		int x2=0;
		int y2=0;
		for(int y=hauteur-1;y>=0;y--){
			y2=0;
			for(int x=0;x<largeur;x++){
				nouvPoly[x2][y2++]=polyominos[x][y];
			}
			x2++;
		}
		return nouvPoly;
	}
	
	/**
	 * Fonction qui effectue une symetrie horizontal sur le polyominos courant
	 * @return un tableau representatif du polyominos qui a subit une symetrie horizontal
	 */
	private int[][] symetrieHorizontal(){
		int hauteur=polyominos[0].length;
		int largeur=polyominos.length;
		int[][] nouvPoly=new int[largeur][hauteur];

		int y2=0;
		for(int x=0;x<largeur;x++){
			y2=0;
			for(int y=hauteur-1;y>=0;y--){
				nouvPoly[x][y2++]=polyominos[x][y];
			}
		}
		return nouvPoly;
	}
	
	/**
	 * Fonction qui effectue une symetrie vertical sur le polyominos courant
	 * @return un tableau representatif du polyominos qui a subit une symetrie vertical
	 */
	@SuppressWarnings("unused")
	private int[][] symetrieVertical(){
		int hauteur=polyominos[0].length;
		int largeur=polyominos.length;
		int[][] nouvPoly=new int[largeur][hauteur];
		
		int x2;
		for(int y=hauteur-1;y>=0;y--){
			x2=largeur-1;
			for(int x=0;x<largeur;x++){
				nouvPoly[x2--][y]=polyominos[x][y];
			}
		}
		return nouvPoly;
	}
	
}