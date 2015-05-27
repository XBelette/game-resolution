import java.util.LinkedList;
import java.util.PriorityQueue;

public class Othello extends Jeu {

	public Othello(int L, int H, Position p) {
		super(L, H, p);
	}

	public Othello(int L, int H) {
		super(L, H, new Position(L, H));
	}
	
	public void SituationDeDepart(){ // Place les 4 pions qui permettent de commencer a jouer
		if (H <=1 || L<=1)
			throw new IllegalArgumentException("Plateau trop petit");
		int l = L/2;
		int h = H/2;
		p.ajouteBlanc(new Coup(l,h));
		p.ajouteBlanc(new Coup(l-1,h-1));
		p.ajouteNoir(new Coup(l,h-1));
		p.ajouteNoir(new Coup(l-1,h));
	}

	@Override
	public PriorityQueue<Coup> GetCoupsPossibles(Couleur c) {
		PriorityQueue<Coup> coupsPossibles = new PriorityQueue<Coup>();
		Coup coupCourant = new Coup();
		while (coupCourant.line < L) {
			while (coupCourant.colonne < H) {
				// Une priorité pourrait être calculée ici
				if (p.quiEstLa(coupCourant) == null
						&& coupPossible(coupCourant, c)!=null)
					coupsPossibles.add(new Coup(coupCourant.colonne,
							coupCourant.line));
				coupCourant.colonne++;
			}
			coupCourant.line++;
			coupCourant.colonne = 0;
		}

		return coupsPossibles;

	}

	@Override
	public Boolean blancGagne() {
		if (!partieFinie())
			return null;
		int gain = this.getp().Nb(Couleur.BLANC) - this.getp().Nb(Couleur.NOIR);
		if (gain < 0)
			return false;
		if (gain > 0)
			return true;
		return null;
	}

	@Override
	public Boolean partieFinie() {
		if (GetCoupsPossibles(Couleur.BLANC).isEmpty()
				&& GetCoupsPossibles(Couleur.NOIR).isEmpty())
			return true;
		return false;
	}

	public LinkedList<Coup> coupPossible(Coup coup, Couleur couleur) {
		// calcule les consequences d'un coup, sous la forme d'une liste de
		// cases qui changent
		// renvoie null si le coup n'est pas possible
		// ne modifie pas l'etat du jeu
		int H = this.getH();
		int L = this.getL();
		if (p.quiEstLa(coup) != null) { // on ne peut pas jouer sur une case
										// deja occup�e
			AfficheAffichage.affichePlateau(this);
			throw new IllegalArgumentException("Coup interdit : "+ coup);
		}
		LinkedList<Coup> changent = new LinkedList<>();
		for (Voisinage v : Voisinage.values()) {
			LinkedList<Coup> candidats = new LinkedList<>();
			Coup voisin = coup.coupVoisin(v, H, L);
			while (voisin != null && this.getp().quiEstLa(voisin) != null
					&& this.getp().quiEstLa(voisin) != couleur) {
				candidats.add(voisin);
				voisin = voisin.coupVoisin(v, H, L);
			}
			if (voisin != null && this.getp().quiEstLa(voisin) == couleur)
				changent.addAll(candidats);
		}
		if (changent.isEmpty()) // aucun pion n'est modifie, le coup est
									// interdit
			return null;
		return changent;
	}

	@Override
	public void joueCoup(Coup coup, Couleur couleur) {
		LinkedList<Coup> changent = coupPossible(coup, couleur);
		if (changent == null)
			throw new IllegalArgumentException("Coup interdit, aucun gain");
		if (couleur == Couleur.BLANC)
			p.ajouteBlanc(coup);
		else if (couleur == Couleur.NOIR)
			p.ajouteNoir(coup);
		else
			throw new IllegalArgumentException("Couleur indeterminee");
		for (Coup c : changent)
			p.changeCouleur(c);
	}

	@Override
	public void undo(Coup c) {
		return;
	}

}
