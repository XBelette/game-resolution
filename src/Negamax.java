import java.util.LinkedList;
import java.util.PriorityQueue;


public class Negamax extends Recherche {
	
	LinkedList<Position> historique;
	
	public Negamax(Jeu j){
		super(j);
		historique = new LinkedList<>();
	}
	
	@Override
	public int recherche(Couleur tour) {
		
		// Only initial call : checking if the game is over.
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
		
		// If not, well, time to play !
		return rechercheAux(tour);
		
	}
	
	public int rechercheAux(Couleur tour){
		j.p.augmenteProfondeur();
		PriorityQueue<Coup> aJouer = j.GetCoupsPossibles(tour);
		Coup coupCourant = new Coup();
		boolean gagnant = false;
		boolean premierCoup = true;
		
		int meilleurScore = StatusConstants.LOSE;
		int scoreCourant = StatusConstants.LOSE;
		while((coupCourant = aJouer.poll()) != null){
			premierCoup = false;
			historique.add(j.p.copy());
			j.joueCoup(coupCourant, tour);
			gagnant = j.gagne(tour);
			if(gagnant){
				j.undo(historique.pollFirst());
				return StatusConstants.WIN;
			}
			else{
				// Bon, pas gagné.
				// Mais la partie est peut-être finie ?
				if(j.partieFinie()){
					if (meilleurScore == StatusConstants.LOSE)
						meilleurScore = StatusConstants.DRAW;
				}
				else{
					scoreCourant = -rechercheAux(tour.other());
					if(scoreCourant > meilleurScore) meilleurScore = scoreCourant;
				}
			}
			j.undo(historique.pollFirst());
		}
		if(premierCoup){// Othello specific case : if there is no move to play initially, the other player may play next
			meilleurScore= -rechercheAux(tour.other());
			j.p.diminueProfondeur();
			return meilleurScore;
		}
		j.p.diminueProfondeur();
		return meilleurScore;
	}
}
