

public class Coup {
	public int colonne;
	public int line;

	public Coup() {
		// Par défaut à 0,
		colonne = 0;
		line = 0;
	}

	public Coup(int col, int l) {
		colonne = col;
		line = l;
	}

	@Override
	public String toString() {
		return "line : " + this.line + ", colonne : " + this.colonne;
	}

	public Coup coupVoisin(Voisinage v, int H, int L) {
		int nouvCol = colonne;
		int nouvLigne = line;
		if (v == Voisinage.N || v == Voisinage.NE || v == Voisinage.NO) {
			if (line == H-1)
				return null;
			nouvLigne++;
		}
		else if (v == Voisinage.S || v == Voisinage.SE || v == Voisinage.SO){
			if (line == 0)
				return null;
			nouvLigne--;
		}
		if (v == Voisinage.E || v == Voisinage.SE || v == Voisinage.NE){
			if (colonne == L-1)
				return null;
			nouvCol++;
		}
		else if (v == Voisinage.O || v == Voisinage.SO || v == Voisinage.NO){
			if (colonne == 0)
				return null;
			nouvCol--;
		}
		return new Coup(nouvCol, nouvLigne);
	}

}
