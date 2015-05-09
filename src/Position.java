
public class Position {

	// L'origine est en bas � gauche du tableau
	// Si la ligne et la colonne sont entre 0 et L/H, avec l'origine comme ci-dessus,
	// la case (col, line) a pour adresse le bit col*(H+1) + line
	
	byte L;
	byte H;
	
	private long positionBlancs;
	private long positionNoirs;
	
	public Position(byte L, byte H, long pB, long pN){
		this.H = H;
		this.L = L;
		this.positionBlancs = pB;
		this.positionNoirs = pN;
	}
	
	public Position(byte L, byte H){
		this.H = H;
		this.L = L;
		this.positionBlancs = 0;
		this.positionNoirs = 0;
	}
	
	public boolean isEmpty(){
		return (positionBlancs == 0 && positionNoirs == 0);
	}
	
	// 1 pour les blancs, -1 pour les noirs, 0 pour inoccup�
	
	public byte quiEstLa(Coup coup){
		long adresse = 1 << coup.colonne*(H+1)+coup.line;
		if ((positionBlancs & adresse) != 0)
			return 1;
		else if ((positionNoirs & adresse) != 0)
			return -1;
		return 0;
	}
	
	public boolean ajouteBlanc(Coup coup){
		long adresse = 1 << coup.colonne*(H+1)+coup.line;
		if (quiEstLa(coup) != 0)
			return false;
		positionBlancs = (positionBlancs | adresse);
		return true;
	}
	
	public boolean ajouteNoir(Coup coup){
		long adresse = 1 << coup.colonne*(H+1)+coup.line;
		if (quiEstLa(coup) != 0)
			return false;
		positionNoirs= (positionNoirs | adresse);
		return true;
	}
	
	public boolean remove(Coup coup){
		long adresse = ~(1 << coup.colonne*(H+1)+coup.line);
		if(quiEstLa(coup) == 0)
			return false;
		positionBlancs = (positionBlancs & adresse);
		positionNoirs = (positionNoirs & adresse);
		return true;
	}

	public boolean alignementVertical(byte n, Color color){ 
		// Teste si un alignement vertical de l jetons du joueur color ou plus existe
		// Notons que cette fonction ne peut fonctionner que pour n >= 1
		long grilleAlignements;
		// On crée la grille des alignements de longueur 1...
		switch(color){
		case BLANC:
			grilleAlignements = this.positionBlancs;
			break;
		case NOIR:
			grilleAlignements = this.positionNoirs;
			break;
		default:
			throw new IllegalArgumentException("Erreur dans les fonctions d'alignements : il y a une troisième couleur !");
		}
		
		// Puis on augmente la longueur !
		for(int i = 1; i < n; i++){
			grilleAlignements = grilleAlignements & (grilleAlignements >> 1);
		}
		
		// Il y a au moins un alignement cherché ssi il reste un 1 quelque part dans la grille.
		return grilleAlignements > 0;
	}
	
	public boolean alignementHorizontal(byte n, Color color){
		// Cf supra avec des alignements horizontaux
		// Change le décalage.
		long grilleAlignements;
		// On crée la grille des alignements de longueur 1...
		switch(color){
		case BLANC:
			grilleAlignements = this.positionBlancs;
			break;
		case NOIR:
			grilleAlignements = this.positionNoirs;
			break;
		default:
			throw new IllegalArgumentException("Erreur dans les fonctions d'alignements : il y a une troisième couleur !");
		}
		
		// Puis on augmente la longueur !
		for(int i = 1; i < n; i++){
			grilleAlignements = grilleAlignements & (grilleAlignements >> H+1);
		}
		
		return grilleAlignements > 0;
	}
	
	public boolean alignementDiagonaleAntislash(byte n, Color color){
		// Cf supra avec des alignements comme ceci : \
		// Change le décalage.
		long grilleAlignements;
		// On crée la grille des alignements de longueur 1...
		switch(color){
		case BLANC:
			grilleAlignements = this.positionBlancs;
			break;
		case NOIR:
			grilleAlignements = this.positionNoirs;
			break;
		default:
			throw new IllegalArgumentException("Erreur dans les fonctions d'alignements : il y a une troisième couleur !");
		}
		
		// Puis on augmente la longueur !
		for(int i = 1; i < n; i++){
			grilleAlignements = grilleAlignements & (grilleAlignements >> H);
		}
		
		return grilleAlignements > 0;
	}
	
	public boolean alignementDiagonaleSlash(byte n, Color color){
		// Cf supra avec des alignements comme ceci : /
		// Change le décalage.
		long grilleAlignements;
		// On crée la grille des alignements de longueur 1...
		switch(color){
		case BLANC:
			grilleAlignements = this.positionBlancs;
			break;
		case NOIR:
			grilleAlignements = this.positionNoirs;
			break;
		default:
			throw new IllegalArgumentException("Erreur dans les fonctions d'alignements : il y a une troisième couleur !");
		}
		
		// Puis on augmente la longueur !
		for(int i = 1; i < n; i++){
			grilleAlignements = grilleAlignements & (grilleAlignements >> H+2);
		}
		
		return grilleAlignements > 0;
	}
}
