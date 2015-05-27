import java.util.PriorityQueue;


public class alpha_beta extends Recherche {
	
	
	public alpha_beta(Jeu j){
		super(j);
	}
	
	@Override
	public int recherche(Couleur tour) {
		// Need alpha and beta to be mutable parameters of the function
		// Thus this will only be a starting call.
		return rechercheAlphaBeta(Couleur.BLANC, StatusConstants.LOSE, StatusConstants.WIN);
	}
	
	public int rechercheAlphaBeta(Couleur tour, int alpha, int beta){
		
		PriorityQueue<Coup> aJouer = j.GetCoupsPossibles(tour);
		Coup coupCourant = new Coup();
		int meilleurScore = alpha;
		int scoreCourant = alpha;
		boolean gagnant = false;
		
		while((coupCourant = aJouer.poll()) != null){
			
			j.joueCoup(coupCourant, tour);
			gagnant = j.gagne(tour);
			if(gagnant){
				j.undo(coupCourant);
				return StatusConstants.WIN;
			}
			else{// Okay, no luck
				// But there may be no more moves to play !
				if(j.partieFinie()){
					if (meilleurScore == StatusConstants.LOSE)
						meilleurScore = StatusConstants.DRAW;
				}
				else{
					scoreCourant = -rechercheAlphaBeta(tour.other(), -beta, -meilleurScore);
					if(scoreCourant > meilleurScore) meilleurScore = scoreCourant;
				}
			}
			j.undo(coupCourant);
			if(meilleurScore > beta) return meilleurScore;
		}
		return meilleurScore;
	}

}
