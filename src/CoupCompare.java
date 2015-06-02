public class CoupCompare extends Coup implements Comparable<Coup> {

	Jeu j; // m pour Morpion, o pour Othello, p pour Puissance4
	Couleur player;

	public CoupCompare(int col, int l, Jeu j, Couleur player) {
		super(col, l);
		this.j = j;
		this.player = player;
	}

	public CoupCompare(Jeu j, Couleur player) {
		super();
		this.j = j;
		this.player = player;
	}

	public static boolean isACorner(Coup c, Jeu j) {
		int L = j.getL();
		int H = j.getH();
		if ((c.colonne == 0 || c.colonne == L - 1)
				&& (c.line == 0 || c.line == H - 1))
			return true;
		return false;
	}
	
	public static boolean isOnTheEdge(Coup c, Jeu j){
		int L = j.getL();
		int H = j.getH();
		if (c.colonne == 0 || c.colonne == L - 1
				||c.line == 0 || c.line == H - 1)
			return true;
		return false;
	}

	@Override
	public int compareTo(Coup that) {
		if (j instanceof Morpion || j instanceof Puissance4) { // Instinctively,
																// we'd rather
																// play near the
																// center
			int H = j.getH();
			int L = j.getL();
			int dthis = (2 * this.line - H + 1) * (2 * this.line - H + 1)
					+ (2 * this.colonne - L + 1) * (2 * this.colonne - L + 1);
			int dthat = (2 * that.line - H + 1) * (2 * that.line - H + 1)
					+ (2 * that.colonne - L + 1) * (2 * that.colonne - L + 1);
			// Also we want to play someplace where we line up tokens.
			j.joueCoup(this, this.player);
			int alignThis = j.p.maxAlign(this, player);
			j.p.remove(this);
			j.joueCoup(that, player);
			int alignThat = j.p.maxAlign(that, player);
			j.p.remove(that);
			
			return alignThis-alignThat+dthis-dthat;
			
		} else if (j instanceof Othello) { // Instinctively, we'd rather play first in
											// the corners then on the edges
			if (isACorner(this, j) && isACorner(that,j))
				return 0;
			else if (isACorner(this,j))
				return -1;
			else if (isACorner(that, j))
				return 1;
			else if (isOnTheEdge(this,j) && isOnTheEdge(that,j))
				return 0;
			else if (isOnTheEdge(this,j))
				return -1;
			else if (isOnTheEdge(that,j))
				return 1;
			else
				return 0;
		}
		return 0;
	}

}
