import java.util.LinkedList;


public class alpha_beta extends Recherche {
	CustomHashMap visited;
	LinkedList<Position> historique; // Indispensable pour othello.
	boolean skipped;
	
	public alpha_beta(Jeu j){
		super(j);
		visited = new CustomHashMap(10000000, j);
		historique = new LinkedList<>();
		skipped = false;
	}
	
	@Override
	public int recherche(Couleur tour) {
		// J'ai besoin de passer alpha et beta en tant que paramètres modifiables de ma fonction de recherche
		// On a donc ici seulement le premier appel
		
		// Avant d'aller voir plus loin, il faut que je vérifie si la partie n'est pas finie dès le départ.
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
		
		LinkedList<Coup> aJouer = j.GetCoupsPossibles(tour);
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
			else{// Okay, pas de chance
				// Mais il n'y a peut-être plus de coups à jouer !
				if(j.partieFinie()){
					if (meilleurScore == StatusConstants.LOSE)
						meilleurScore = StatusConstants.DRAW;
				}
				else{
					// Ai-je déjà visité cette position ?
					if(visited.containsKey(j.p, tour)){
						if(visited.getBeta(j.p) >= beta) // Le résultat a déjà été calculé avec une meilleure précision
							scoreCourant = visited.get(j.p);
						else{// Sinon, je dois recalculer
							scoreCourant = -rechercheAlphaBeta(tour.other(), -beta, -meilleurScore);
							visited.put(j.p.copy(), scoreCourant, meilleurScore, beta, tour);
						}
						
					}
					else{
						// Super, une position inconnue !
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
		if(premierCoup){// Cas spécifique pour Othello : Si un joueur ne peut pas jouer, il rend la main a son adversaire
			// On a aussi peut-être mémoïsé le résultat.
			if(!skipped){
				skipped = true;
				if(visited.containsKey(j.p, tour)){
					if(visited.getBeta(j.p) >= beta) // Si le résultat a été calculé avec une meilleure précision
						scoreCourant = visited.get(j.p);
					else{// Sinon, il suffit de le recalculer
						scoreCourant = -rechercheAlphaBeta(tour.other(), -beta, -meilleurScore);
						visited.put(j.p.copy(), scoreCourant, meilleurScore, beta, tour);
					}
					
				}
				else{
					// Bon, on dirait qu'on avait pas mémoïsé le résultat en fait.
					scoreCourant = -rechercheAlphaBeta(tour.other(), -beta, -meilleurScore);
					visited.put(j.p.copy(), scoreCourant, alpha, beta, tour);
				}
				scoreCourant = -rechercheAlphaBeta(tour.other(), -beta, -alpha);
				j.p.diminueProfondeur();
				return (scoreCourant>meilleurScore)?scoreCourant:meilleurScore;
			}// Si on a déjà sauté un tour, il ne reste plus grand chose à faire. Simplement terminer.
		}
		j.p.diminueProfondeur();
		return meilleurScore;
	}

}
