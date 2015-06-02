public class Position {

	// L'origine est en bas a gauche du tableau
	// Si la ligne et la colonne sont entre 0 et L/H, avec l'origine comme
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
	
	public void augmenteProfondeur(){
		profondeur++;
	}
	
	public void diminueProfondeur(){
		profondeur--;
	}
	
	public int getProfondeur(){
		return this.profondeur;
	}

	public Position copy(){
		return new Position(this.L, this.H, this.positionBlancs, this.positionNoirs, this.profondeur);
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
	
	public int maxAlign(Coup coup, Couleur c){// Returns longest alignment of which Coup coup is part of.
		long adresse = ((long)1 << coup.colonne * (H + 1) + coup.line);
		long grilleAlignementsVerticaux;
		long grilleAlignementsHorizontaux;
		long grilleAlignementsSlash;
		long grilleAlignementsAntislash;
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
		// Now to get that maximum length.
		grilleAlignementsVerticaux = grilleAlignementsVerticaux
				& (grilleAlignementsVerticaux >> (long) (1));
		grilleAlignementsHorizontaux = grilleAlignementsHorizontaux
				& (grilleAlignementsHorizontaux >> (long) (H + 1));
		grilleAlignementsAntislash = grilleAlignementsAntislash
				& (grilleAlignementsAntislash >> (long) H);
		grilleAlignementsSlash = grilleAlignementsSlash
				& (grilleAlignementsSlash >> (long) (H + 2));
		long places = adresse; //There are more than one place to look at to see if the alignment gets longer
		places = places | places >> (long) 1 // address might be the upper limit in a vertical alignment
						| places >> (long) (H+1) // Or the right hand limit in a horizontal alignment
						| places >> (long) H	// Or the lower-right limit in a \ like alignment
						| places >> (long) (H+2); // Or the upper-right limit in a / like alignment
		stillAligned = (places & (grilleAlignementsVerticaux | grilleAlignementsHorizontaux
								| grilleAlignementsAntislash | grilleAlignementsSlash)) >= 1;
		while(stillAligned){
			maxLength++;
			// I'll need to split places the same way I split it for alignments
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
			places = adresse | placesH | placesV | placesS | placesA;
			
			// Also updating alignments :
			grilleAlignementsVerticaux = grilleAlignementsVerticaux
					& (grilleAlignementsVerticaux >> (long) (1));
			grilleAlignementsHorizontaux = grilleAlignementsHorizontaux
					& (grilleAlignementsHorizontaux >> (long) (H + 1));
			grilleAlignementsAntislash = grilleAlignementsAntislash
					& (grilleAlignementsAntislash >> (long) H);
			grilleAlignementsSlash = grilleAlignementsSlash
					& (grilleAlignementsSlash >> (long) (H + 2));
			// Okay, reasonable places ?
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

	// renvoie 1 si deux positions sont �quivalentes
	// renvoie -1 si deux positions sont �quivalentes en �changeant les blancs
	// et les noirs
	// renvoie 0 sinon
	public int equals(Position p) {
		if (this.positionBlancs == p.positionBlancs
				&& this.positionNoirs == p.positionNoirs)
			return 1;
		if (this.positionBlancs == p.positionNoirs
				&& this.positionNoirs == p.positionBlancs)
			return -1;
		// symetrie verticale
		boolean meme = true;
		boolean oppose = true;
		for (int col = 0; col < L; col++)
			for (int line = 0; line < H; line++) {
				Couleur c1 = this.quiEstLa(new Coup(col, line));
				Couleur c2 = p.quiEstLa(new Coup(L - 1 - col, line));
				if ((c1 == null && c2 != null) || (c1 != null && c2 == null)) {
					meme = false;
					oppose = false;
					break;
				} else if (c1 == c2 && c1 != null)
					oppose = false;
				else if (c1 != c2)
					meme = false;
				if (!meme && !oppose)
					break;
			}
			
		if (meme)
			return 1;
		if (oppose)
			return -1;
		// symetrie horizontale
		meme = true;
		oppose = true;
		for (int col = 0; col < L; col++)
			for (int line = 0; line < H; line++) {
				Couleur c1 = this.quiEstLa(new Coup(col, line));
				Couleur c2 = p.quiEstLa(new Coup(col, L - 1 - line));
				if ((c1 == null && c2 != null) || (c1 != null && c2 == null)) {
					meme = false;
					oppose = false;
					break;
				} else if (c1 == c2 && c1 != null)
					oppose = false;
				else if (c1 != c2)
					meme = false;
				if (!meme && !oppose)
					break;
			}
		if (meme)
			return 1;
		if (oppose)
			return -1;
		// combinaison des deux symetries
		meme = true;
		oppose = true;
		for (int col = 0; col < L; col++)
			for (int line = 0; line < H; line++) {
				Couleur c1 = this.quiEstLa(new Coup(col, line));
				Couleur c2 = p.quiEstLa(new Coup(L - 1 - col, L - 1 - line));
				if ((c1 == null && c2 != null) || (c1 != null && c2 == null)) {
					meme = false;
					oppose = false;
					break;
				} else if (c1 == c2 && c1 != null)
					oppose = false;
				else if (c1 != c2)
					meme = false;
				if (!meme && !oppose)
					break;
			}
		if (meme)
			return 1;
		if (oppose)
			return -1;
		// Si H == L
		if (H == L) {
			// Quart de tour anti-horaire
			meme = true;
			oppose = true;
			for (int col = 0; col < L; col++)
				for (int line = 0; line < H; line++) {
					Couleur c1 = this.quiEstLa(new Coup(col, line));
					Couleur c2 = p.quiEstLa(new Coup(L - 1 - line, col));
					if ((c1 == null && c2 != null)
							|| (c1 != null && c2 == null)) {
						meme = false;
						oppose = false;
						break;
					} else if (c1 == c2 && c1 != null)
						oppose = false;
					else if (c1 != c2)
						meme = false;
					if (!meme && !oppose)
						break;
				}
			if (meme)
				return 1;
			if (oppose)
				return -1;
			// Quart de tour horaire
			meme = true;
			oppose = true;
			for (int col = 0; col < L; col++)
				for (int line = 0; line < H; line++) {
					Couleur c1 = this.quiEstLa(new Coup(col, line));
					Couleur c2 = p.quiEstLa(new Coup(line, H - 1 - col));
					if ((c1 == null && c2 != null)
							|| (c1 != null && c2 == null)) {
						meme = false;
						oppose = false;
						break;
					} else if (c1 == c2 && c1 != null)
						oppose = false;
					else if (c1 != c2)
						meme = false;
					if (!meme && !oppose)
						break;
				}
			if (meme)
				return 1;
			if (oppose)
				return -1;
			// Sym�trie slash
			meme = true;
			oppose = true;
			for (int col = 0; col < L; col++)
				for (int line = 0; line < H; line++) {
					Couleur c1 = this.quiEstLa(new Coup(col, line));
					Couleur c2 = p.quiEstLa(new Coup(line, col));
					if ((c1 == null && c2 != null)
							|| (c1 != null && c2 == null)) {
						meme = false;
						oppose = false;
						break;
					} else if (c1 == c2 && c1 != null)
						oppose = false;
					else if (c1 != c2)
						meme = false;
					if (!meme && !oppose)
						break;
				}
			if (meme)
				return 1;
			if (oppose)
				return -1;
			// Sym�trie antislash
			meme = true;
			oppose = true;
			for (int col = 0; col < L; col++)
				for (int line = 0; line < H;line++) {
					Couleur c1 = this.quiEstLa(new Coup(col, line));
					Couleur c2 = p
							.quiEstLa(new Coup(L - 1 - line, H - 1 - col));
					if ((c1 == null && c2 != null)
							|| (c1 != null && c2 == null)) {
						meme = false;
						oppose = false;
						break;
					} else if (c1 == c2 && c1 != null)
						oppose = false;
					else if (c1 != c2)
						meme = false;
					if (!meme && !oppose)
						break;
				}
			if (meme)
				return 1;
			if (oppose)
				return -1;
		}
		return 0;

	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		for (int line = H-1;line>=0;line--){
			for (int col = 0;col <L;col++){
				Couleur c = quiEstLa(new Coup(col,line));
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
