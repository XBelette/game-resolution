import java.util.PriorityQueue;


public class Negamax extends Recherche {
	
	private byte winValue;
	private byte loseValue;
	private byte drawValue;
	
	public Negamax(Jeu j){
		super(j);
		// White starts
		winValue = StatusConstants.WIN;
		loseValue= StatusConstants.LOSE;
		drawValue= StatusConstants.DRAW;
	}
	
	@Override
	public int recherche(Couleur tour) {
		// TODO Auto-generated method stub
		PriorityQueue<Coup> aJouer = j.GetCoupsPossibles(tour);
		Coup coupCourant = new Coup();
		int meilleurScore;
		int scoreCourant;
		boolean gagnant = false;
		
		meilleurScore = loseValue;
		scoreCourant = loseValue;
		while(!gagnant){
			coupCourant = aJouer.poll();
			if(coupCourant == null){// Plus de coup à jouer : On évalue la position
				gagnant = j.gagne(tour);
				if(gagnant) return winValue;
				gagnant = j.gagne(tour.other());
				if(gagnant) return loseValue;
				return drawValue;
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
				scoreCourant = recherche(tour.other());
				if(scoreCourant > meilleurScore) meilleurScore = scoreCourant;
				// Before I go on : I must reset values to what they were before
				loseValue *= -1;
				winValue *= -1;
			}
			j.undo(coupCourant);
		}
		return meilleurScore;
	}
}
