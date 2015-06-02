import java.util.LinkedList;

public class CoupCompare extends Coup implements Comparable<Coup> {

	// Une classe héritée de Coup, mais munie d'une relation d'ordre
	Jeu j;
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
		if (j instanceof Morpion || j instanceof Puissance4) {
			// Pour le morpion, on préfère globalement jouer au centre.
			int H = j.getH();
			int L = j.getL();
			int dthis = (2 * this.line - H + 1) * (2 * this.line - H + 1)
					+ (2 * this.colonne - L + 1) * (2 * this.colonne - L + 1);
			int dthat = (2 * that.line - H + 1) * (2 * that.line - H + 1)
					+ (2 * that.colonne - L + 1) * (2 * that.colonne - L + 1);
			// On préfère aussi jouer des coups qui alignent un grand nombre de jetons.
			j.joueCoup(this, this.player);
			int alignThis = j.p.maxAlign(this, player);
			j.p.remove(this);
			j.joueCoup(that, player);
			int alignThat = j.p.maxAlign(that, player);
			j.p.remove(that);
			
			return alignThis-alignThat+dthis-dthat;
			
		} else if (j instanceof Othello) {
			// Pour othello, il est très important de jouer sur le coin et un peu moins sur les bords
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
			else{
				// Sinon, on peut toujours comparer le nombre de pions retournés.
				LinkedList<Coup> coupsThis = ((Othello) j).coupPossible(this, player);
				LinkedList<Coup> coupsThat = ((Othello) j).coupPossible(that, player);
				return coupsThis.size()-coupsThat.size();
			}
		}
		return 0;
	}
}

