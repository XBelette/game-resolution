import java.util.PriorityQueue;


public class alpha_beta extends Recherche {
	
	private byte winValue;
	private byte loseValue;
	private byte drawValue;
	
	public alpha_beta(Jeu j){
		super(j);
		winValue = StatusConstants.WIN;
		loseValue= StatusConstants.LOSE;
		drawValue= StatusConstants.DRAW;
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
		int meilleurScore;
		int scoreCourant;
		boolean gagnant = false;
		
		meilleurScore = loseValue;
		scoreCourant = loseValue;
		while(!gagnant){
			coupCourant = aJouer.poll();
			if(coupCourant == null){// Plus de coup à jouer : Il y a deux solutions
				// Primo, la partie est finie : à ce moment là il faut qu'on signale qu'on est à un match nul
				if (j.partieFinie()) return drawValue;
				// Secondo, on a exploré tous les coups de l'adversaire et on a fini notre exploration : on renvoie le meilleur score
				else return meilleurScore;
			}
			j.joueCoup(coupCourant, tour);
			gagnant = j.gagne(tour);
			if(gagnant){
				j.undo(coupCourant);
				return winValue;
			}
			else{
				// Bon, pas gagné. Je change les valeurs de victoire avant de faire rechercher pour la suite.
				loseValue *= -1;
				winValue *= -1;
				scoreCourant = rechercheAlphaBeta(tour.other(), -beta, -scoreCourant);
				if(scoreCourant > meilleurScore) meilleurScore = scoreCourant;
				// Before I go on : I must reset values to what they were before
				loseValue *= -1;
				winValue *= -1;
			}
			j.undo(coupCourant);
			if(meilleurScore > beta) return beta;
		}
		return meilleurScore;
	}

}
