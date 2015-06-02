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
		// TODO Auto-generated method stub
		PriorityQueue<Coup> aJouer = j.GetCoupsPossibles(tour);
		Coup coupCourant = new Coup();
		int meilleurScore;
		int scoreCourant;
		boolean gagnant = false;
		
		meilleurScore = StatusConstants.LOSE;
		scoreCourant = StatusConstants.LOSE;
		while(!gagnant){
			coupCourant = aJouer.poll();
			if(coupCourant == null){// Plus de coup à jouer : On a, pour ainsi dire, fait le tour
				return meilleurScore;
			}
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
					scoreCourant = -recherche(tour.other());
					if(scoreCourant > meilleurScore) meilleurScore = scoreCourant;
				}
			}
			j.undo(historique.pollFirst());
		}
		return meilleurScore;
	}
}
