import java.util.function.Function;

public class Position {

	// L'origine est en bas à gauche du tableau
	// Si la ligne et la colonne sont entre 0 et L ou H, avec l'origine comme
	// ci-dessus,
	// la case (col, line) a pour adresse le bit col*(H+1) + line

	int L;
	int H;

	private long positionBlancs;
	private long positionNoirs;

	private int profondeur;

	public Position(int L, int H, long pB, long pN) {
		this.H = H;
		this.L = L;
		this.positionBlancs = pB;
		this.positionNoirs = pN;
		this.profondeur = 0;
	}

	public Position(int L, int H, long pB, long pN, int pr) {
		this.H = H;
		this.L = L;
		this.positionBlancs = pB;
		this.positionNoirs = pN;
		this.profondeur = pr;
	}

	public Position(int L, int H) {
		this.H = H;
		this.L = L;
		this.positionBlancs = 0;
		this.positionNoirs = 0;
		this.profondeur = 0;
	}

	public void augmenteProfondeur() {
		profondeur++;
	}

	public void diminueProfondeur() {
		profondeur--;
	}

	public int getProfondeur() {
		return this.profondeur;
	}

	public Position copy() {
		return new Position(this.L, this.H, this.positionBlancs,
				this.positionNoirs, this.profondeur);
	}

	public boolean isEmpty() {
		return (positionBlancs == 0 && positionNoirs == 0);
	}

	public Couleur quiEstLa(Coup coup) {
		long adresse = (long) (1) << (coup.colonne * (H + 1) + coup.line);
		if ((positionBlancs & adresse) != 0) {
			return Couleur.BLANC;
		} else if ((positionNoirs & adresse) == adresse) {
			return Couleur.NOIR;
		}

		return null;
	}

	public boolean changeCouleur(Coup coup) {
		Couleur coul = this.quiEstLa(coup);
		if (coul == null)
			return false;
		long adresse = (long) (1) << (coup.colonne * (H + 1) + coup.line);
		if (coul == Couleur.BLANC) {
			positionNoirs = (positionNoirs | adresse);
			positionBlancs = (positionBlancs & (~adresse));
			return true;
		}
		if (coul == Couleur.NOIR) {
			positionBlancs = (positionBlancs | adresse);
			positionNoirs = (positionNoirs & (~adresse));
			return true;
		}
		return false;
	}

	public boolean ajouteBlanc(Coup coup) {
		long adresse = (long) (1) << (coup.colonne * (H + 1) + coup.line);
		if (this.quiEstLa(coup) != null)
			return false;
		positionBlancs = (positionBlancs | adresse);
		return true;
	}

	public boolean ajouteNoir(Coup coup) {
		long adresse = (long) (1) << (coup.colonne * (H + 1) + coup.line);
		if (this.quiEstLa(coup) != null)
			return false;
		positionNoirs = (positionNoirs | adresse);
		return true;
	}

	public boolean remove(Coup coup) {
		long adresse = ~((long) (1) << coup.colonne * (H + 1) + coup.line);
		if (quiEstLa(coup) == null)
			return false;
		positionBlancs = (positionBlancs & adresse);
		positionNoirs = (positionNoirs & adresse);
		return true;
	}
	
	public int maxAlign(Coup coup, Couleur c){// Renvoie le plus long alignement dont le pion à la position coup fait partie
		long adresse = ((long)1 << coup.colonne * (H + 1) + coup.line);
		long grilleAlignementsVerticaux;
		long grilleAlignementsHorizontaux;
		long grilleAlignementsSlash;
		long grilleAlignementsAntislash;
		// Il y a plus d'un endroit où regarder pour vérifier les alignements
		// On sépare, comme pour les alignements, entre vertical (V), horizontal (H), etc.
		long placesH;
		long placesV;
		long placesS;
		long placesA;
		int maxLength = 1;
		boolean stillAligned = true;
		switch(c){
		case BLANC:
			grilleAlignementsVerticaux = this.positionBlancs;
			grilleAlignementsHorizontaux = this.positionBlancs;
			grilleAlignementsSlash = this.positionBlancs;
			grilleAlignementsAntislash = this.positionBlancs;
			break;
		case NOIR:
			grilleAlignementsVerticaux = this.positionNoirs;
			grilleAlignementsHorizontaux = this.positionBlancs;
			grilleAlignementsSlash = this.positionBlancs;
			grilleAlignementsAntislash = this.positionBlancs;
			break;
		default:
			throw new IllegalArgumentException(
					"Erreur dans les fonctions d'alignements : il y a une troisième couleur !");
		}
		// Maintenant, calculons cette longueur maximale
		grilleAlignementsVerticaux = grilleAlignementsVerticaux
				& (grilleAlignementsVerticaux >> (long) (1));
		grilleAlignementsHorizontaux = grilleAlignementsHorizontaux
				& (grilleAlignementsHorizontaux >> (long) (H + 1));
		grilleAlignementsAntislash = grilleAlignementsAntislash
				& (grilleAlignementsAntislash >> (long) H);
		grilleAlignementsSlash = grilleAlignementsSlash
				& (grilleAlignementsSlash >> (long) (H + 2));
		placesV = adresse | adresse >> (long) 1; // l'adresse peut être en haut d'un alignement vertical
		placesH = adresse | adresse >> (long) (H+1); // Ou à droite d'un alignement horizontal
		placesA = adresse | adresse >> (long) H;	// Ou en bas à droite d'un alignement comme \
		placesS = adresse | adresse >> (long) (H+2); // Ou en haut à droite d'un alignement comme /
		stillAligned = ((placesV & grilleAlignementsVerticaux) |(placesV & grilleAlignementsHorizontaux)
				|(placesA & grilleAlignementsAntislash) | (placesS & grilleAlignementsSlash)) >= 1;
		while(stillAligned){
			// Youpi ! L'alignement est plus long !
			maxLength++;
			// D'abord mettre à jour les endroits où regarder
			placesH=adresse;
			placesV=adresse;
			placesS=adresse;
			placesA=adresse;
			for(int i = 0; i < maxLength; i++){
				placesH = placesH | placesH >> (long) 1;
				placesV = placesV | placesV >> (long) H+1;
				placesS = placesS | placesS >> (long) H+2;
				placesA = placesA | placesA >> (long) H;
			}
			
			// Ensuite mettre à jour les alignements
			grilleAlignementsVerticaux = grilleAlignementsVerticaux
					& (grilleAlignementsVerticaux >> (long) (1));
			grilleAlignementsHorizontaux = grilleAlignementsHorizontaux
					& (grilleAlignementsHorizontaux >> (long) (H + 1));
			grilleAlignementsAntislash = grilleAlignementsAntislash
					& (grilleAlignementsAntislash >> (long) H);
			grilleAlignementsSlash = grilleAlignementsSlash
					& (grilleAlignementsSlash >> (long) (H + 2));
			// Et voir s'il y a un alignement plus long.
			stillAligned = ((placesV & grilleAlignementsVerticaux) |(placesV & grilleAlignementsHorizontaux)
					|(placesA & grilleAlignementsAntislash) | (placesS & grilleAlignementsSlash)) >= 1;
		}
		return maxLength;
	}

	public boolean alignementVertical(int n, Couleur color) {
		// Teste si un alignement vertical de l jetons du joueur color ou plus
		// existe
		// Notons que cette fonction ne peut fonctionner que pour n >= 1
		long grilleAlignements;
		// On crée la grille des alignements de longueur 1...
		switch (color) {
		case BLANC:
			grilleAlignements = this.positionBlancs;
			break;
		case NOIR:
			grilleAlignements = this.positionNoirs;
			break;
		default:
			throw new IllegalArgumentException(
					"Erreur dans les fonctions d'alignements : il y a une troisième couleur !");
		}

		// Puis on augmente la longueur !
		for (int i = 1; i < n; i++) {
			grilleAlignements = grilleAlignements
					& (grilleAlignements >> (long) (1));
		}

		// Il y a au moins un alignement cherché ssi il reste un 1 quelque part
		// dans la grille.
		return grilleAlignements > 0;
	}

	public boolean alignementHorizontal(int n, Couleur color) {
		// Cf supra avec des alignements horizontaux
		// Change le décalage.
		long grilleAlignements;
		// On crée la grille des alignements de longueur 1...
		switch (color) {
		case BLANC:
			grilleAlignements = this.positionBlancs;
			break;
		case NOIR:
			grilleAlignements = this.positionNoirs;
			break;
		default:
			throw new IllegalArgumentException(
					"Erreur dans les fonctions d'alignements : il y a une troisième couleur !");
		}

		// Puis on augmente la longueur !
		for (int i = 1; i < n; i++) {
			grilleAlignements = grilleAlignements
					& (grilleAlignements >> (long) (H + 1));
		}

		return grilleAlignements > 0;
	}

	public boolean alignementDiagonaleAntislash(int n, Couleur color) {
		// Cf supra avec des alignements comme ceci : \
		// Change le décalage.
		long grilleAlignements;
		// On crée la grille des alignements de longueur 1...
		switch (color) {
		case BLANC:
			grilleAlignements = this.positionBlancs;
			break;
		case NOIR:
			grilleAlignements = this.positionNoirs;
			break;
		default:
			throw new IllegalArgumentException(
					"Erreur dans les fonctions d'alignements : il y a une troisième couleur !");
		}

		// Puis on augmente la longueur !
		for (int i = 1; i < n; i++) {
			grilleAlignements = grilleAlignements
					& (grilleAlignements >> (long) H);
		}

		return grilleAlignements > 0;
	}

	public boolean alignementDiagonaleSlash(int n, Couleur color) {
		// Cf supra avec des alignements comme ceci : /
		// Change le décalage.
		long grilleAlignements;
		// On crée la grille des alignements de longueur 1...
		switch (color) {
		case BLANC:
			grilleAlignements = this.positionBlancs;
			break;
		case NOIR:
			grilleAlignements = this.positionNoirs;
			break;
		default:
			throw new IllegalArgumentException(
					"Erreur dans les fonctions d'alignements : il y a une troisième couleur !");
		}

		// Puis on augmente la longueur !
		for (int i = 1; i < n; i++) {
			grilleAlignements = grilleAlignements
					& (grilleAlignements >> (long) (H + 2));
		}

		return grilleAlignements > 0;
	}

	public int Nb(Couleur color) {
		if (color == Couleur.BLANC)
			return Long.bitCount(positionBlancs);
		if (color == Couleur.NOIR)
			return Long.bitCount(positionNoirs);
		return -1;
	}
	
	public boolean estInvariantParBij(Position p, Function<Coup,Coup> bij){
		boolean meme = true;
		for (int col = 0; col < L; col++)
			for (int line = 0; line < H; line++) {
				Coup cp = new Coup(col,line);
				Couleur c1 = this.quiEstLa(cp);
				Couleur c2 = p.quiEstLa(bij.apply(cp));
				if ((c1 == null && c2 != null) || (c1 != null && c2 == null)
						|| c1 != c2) {
					meme = false;
					break;
				}
			}
		if (meme)
			return true;
		return false;
	}

	public boolean equals(Position p) {
		if (this.positionBlancs == p.positionBlancs
				&& this.positionNoirs == p.positionNoirs)
			return true;
		// symétrie verticale
		Function<Coup,Coup> symVert = new Function<Coup,Coup>() {
			public Coup apply(Coup c){
				return new Coup(L-1-c.colonne,c.line);
			}
		};
		if (estInvariantParBij(p,symVert))
			return true;
		// symetrie horizontale
		Function<Coup,Coup> symHor = new Function<Coup,Coup>() {
			public Coup apply(Coup c){
				return new Coup(c.colonne,L-1-c.line);
			}
		};
		if (estInvariantParBij(p,symHor))
			return true;
		// combinaison des deux symétries
		Function<Coup,Coup> demiTr = new Function<Coup,Coup>() {
			public Coup apply(Coup c){
				return new Coup(L-1-c.colonne,L-1-c.line);
			}
		};
		if (estInvariantParBij(p,demiTr))
			return true;
		return false;
	}

	public boolean equalsPasPuissance4(Position p) {
		if(equals(p)) return true;
		if (H != L)
			return false;
		// Quart de tour anti-horaire
		Function<Coup,Coup> quartTrGauche = new Function<Coup,Coup>() {
			public Coup apply(Coup c){
				return new Coup(L-1-c.line,c.colonne);
			}
		};
		if (estInvariantParBij(p,quartTrGauche))
			return true;
		// Quart de tour horaire
		Function<Coup,Coup> quartTrDroite = new Function<Coup,Coup>() {
			public Coup apply(Coup c){
				return new Coup(c.line,H-1-c.colonne);
			}
		};
		if (estInvariantParBij(p,quartTrDroite))
			return true;
		// Symétrie slash
		Function<Coup,Coup> symSlash = new Function<Coup,Coup>() {
			public Coup apply(Coup c){
				return new Coup(c.line,c.colonne);
			}
		};
		if (estInvariantParBij(p,symSlash))
			return true;
		// Symétrie antislash
		Function<Coup,Coup> symAntiSlash = new Function<Coup,Coup>() {
			public Coup apply(Coup c){
				return new Coup(L-1-c.line,H-1-c.colonne);
			}
		};
		if (estInvariantParBij(p,symAntiSlash))
			return true;
		return false;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int line = H - 1; line >= 0; line--) {
			for (int col = 0; col < L; col++) {
				Couleur c = quiEstLa(new Coup(col, line));
				if (c == Couleur.BLANC)
					sb.append("O");
				else if (c == Couleur.NOIR)
					sb.append("@");
				else
					sb.append(".");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
