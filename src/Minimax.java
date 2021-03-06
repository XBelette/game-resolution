import java.util.LinkedList;

public class Minimax extends Recherche {

	LinkedList<Position> historique;

	public Minimax(Jeu j) {
		super(j);
		historique = new LinkedList<>();
	}

	public String toString() {
		return j.toString();
	}

	@Override
	public int recherche(Couleur tour) {

		// La partie est peut-être déjà finie
		if (j.partieFinie()) {
			boolean BlancGagne = j.gagne(Couleur.BLANC);
			boolean NoirGagne = j.gagne(Couleur.NOIR);
			if (BlancGagne && NoirGagne)
				throw new IllegalArgumentException("Invalid Position");
			if (BlancGagne)
				return StatusConstants.WIN;
			if (NoirGagne)
				return StatusConstants.LOSE;
			return StatusConstants.DRAW;
		}

		return rechercheAux(tour);
	}

	public int rechercheAux(Couleur tour) {
		LinkedList<Coup> aJouer = j.GetCoupsPossibles(tour);
		Coup coupCourant = new Coup();
		int meilleurScore;
		int scoreCourant;
		Boolean gagnant = null;
		if (tour == Couleur.BLANC) {// Blanc joue
			meilleurScore = StatusConstants.LOSE;
			scoreCourant = StatusConstants.LOSE;
			while (gagnant == null) {
				coupCourant = aJouer.poll();
				if (coupCourant == null) {
						return meilleurScore;
				}
				historique.addFirst(j.getp().copy());
				j.joueCoup(coupCourant, tour);
				if (j.gagne(Couleur.BLANC))
					gagnant = true;
				else if (j.gagne(Couleur.NOIR))
					gagnant = false;
				else
					gagnant = null;
				if (gagnant != null && gagnant == true) {
					j.undo(historique.pollFirst());
					return StatusConstants.WIN;
				} else if (gagnant == null && j.partieFinie() == true) {
					if (meilleurScore == StatusConstants.LOSE)
						meilleurScore = StatusConstants.DRAW;
				} else {
					// Je n'ai pas pu perdre en jouant un coup
					// Donc gagnant == null
					scoreCourant = rechercheAux(Couleur.NOIR);
					if (scoreCourant > meilleurScore)
						meilleurScore = scoreCourant;
				}
				j.undo(historique.pollFirst());

			}
			return meilleurScore;
		} else if (tour == Couleur.NOIR) {
			meilleurScore = StatusConstants.WIN;
			scoreCourant = StatusConstants.WIN;
			while (gagnant == null) {
				coupCourant = aJouer.poll();
				if (coupCourant == null) {// Plus de coup à jouer
						return meilleurScore;
				}
				historique.addFirst(j.getp().copy());
				j.joueCoup(coupCourant, tour);
				if (j.gagne(Couleur.BLANC))
					gagnant = true;
				else if (j.gagne(Couleur.NOIR))
					gagnant = false;
				else
					gagnant = null;
				if (gagnant != null && gagnant == false) {
					j.undo(historique.pollFirst());
					return StatusConstants.LOSE;
				} else if (gagnant == null && j.partieFinie() == true) {
					if (meilleurScore == StatusConstants.WIN)
						meilleurScore = StatusConstants.DRAW;
				} else {
					// Je n'ai pas pu faire gagner blanc en jouant ce coup
					// Donc gagnant == null
					scoreCourant = rechercheAux(Couleur.BLANC);
					if (scoreCourant < meilleurScore)
						meilleurScore = scoreCourant;
				}
				j.undo(historique.pollFirst());

			}
			return meilleurScore;
		} else
			throw new IllegalArgumentException("Il y a un troisième joueur");
	}
}
