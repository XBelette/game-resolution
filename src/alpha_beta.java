import java.util.LinkedList;
import java.util.PriorityQueue;


public class alpha_beta extends Recherche {
	CustomHashMap visited;
	LinkedList<Position> historique; // Indispensable pour othello.
	boolean skipped; // true if a turn was skipped
	
	public alpha_beta(Jeu j){
		super(j);
		visited = new CustomHashMap(10000000);
		historique = new LinkedList<>();
		skipped = false;
	}
	
	@Override
	public int recherche(Couleur tour) {
		// J'ai besoin de passer alpha et beta en tant que paramètres modifiables de ma fonction de recherche
		// On a donc ici seulement le premier appel
		
		// Avant d'aller voir plus loin, il faut que je checke si la partie n'est pas finie dès le départ.
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
		
		// Et sinon, je vais chercher récursivement le reste.
		return rechercheAlphaBeta(Couleur.BLANC, StatusConstants.LOSE, StatusConstants.WIN);
	}
	
	public int rechercheAlphaBeta(Couleur tour, int alpha, int beta){
		
		PriorityQueue<Coup> aJouer = j.GetCoupsPossibles(tour);
		j.p.augmenteProfondeur(); // Je suis plus profond qu'avant !
		Coup coupCourant = new Coup();
		int meilleurScore = alpha;
		int scoreCourant = alpha;
		boolean gagnant = false;
		boolean premierCoup = true; // Utile pour déterminer si le joueur tour a effectivement un coup à jouer (Othello)
		
		while((coupCourant = aJouer.poll()) != null){
			premierCoup = false;
			skipped = false;
			historique.addFirst(j.getp().copy());
			j.joueCoup(coupCourant, tour);
			gagnant = j.gagne(tour);
			if(gagnant){
				j.undo(historique.pollFirst());
				j.p.diminueProfondeur();
				return StatusConstants.WIN;
			}
			else{// Okay, no luck
				// But there may be no more moves to play !
				if(j.partieFinie()){
					if (meilleurScore == StatusConstants.LOSE)
						meilleurScore = StatusConstants.DRAW;
				}
				else{
					// Have I been in this position yet ?
					if(visited.containsKey(j.p) && visited.getTour(j.p)==tour){
						if(visited.getBeta(j.p) >= beta) // Result already computed with better precision
							scoreCourant = visited.get(j.p);
						else{// Sinon, ben je dois recalculer
							scoreCourant = -rechercheAlphaBeta(tour.other(), -beta, -meilleurScore);
							visited.put(j.p.copy(), scoreCourant, meilleurScore, beta, tour);
						}
						
					}
					else{
						// Great, new place !
						scoreCourant = -rechercheAlphaBeta(tour.other(), -beta, -meilleurScore);
						visited.put(j.p.copy(), scoreCourant, alpha, beta, tour);
					}
					if(scoreCourant > meilleurScore) meilleurScore = scoreCourant;
				}
			}
			j.undo(historique.pollFirst());
			if(meilleurScore > beta){
				j.p.diminueProfondeur();
				return meilleurScore;
			}
		}
		if(premierCoup){// Othello specific case : player may replay if opponent has no move left.
			// Also, result might have been memoized.
			if(!skipped){
				skipped = true;
				if(visited.containsKey(j.p) && visited.getTour(j.p)==tour){
					if(visited.getBeta(j.p) >= beta) // Result already computed with better precision
						scoreCourant = visited.get(j.p);
					else{// otherwise, I'll just compute it again
						scoreCourant = -rechercheAlphaBeta(tour.other(), -beta, -meilleurScore);
						visited.put(j.p.copy(), scoreCourant, meilleurScore, beta, tour);
					}
					
				}
				else{
					// Okay, so result was not memoized (or memoized for the other guy)
					scoreCourant = -rechercheAlphaBeta(tour.other(), -beta, -meilleurScore);
					visited.put(j.p.copy(), scoreCourant, alpha, beta, tour);
				}
				scoreCourant = -rechercheAlphaBeta(tour.other(), -beta, -alpha);
				j.p.diminueProfondeur();
				return (scoreCourant>meilleurScore)?scoreCourant:meilleurScore;
			}// Nothing to do really if I've already skipped a turn, game is over. Swinging back next score.
		}
		j.p.diminueProfondeur();
		return meilleurScore;
	}

}
