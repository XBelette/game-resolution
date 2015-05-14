import java.util.PriorityQueue;


public class Minimax extends Recherche{
	
	public Minimax(Jeu j){
		super(j);
	}
	
	@Override
	public byte recherche(Couleur tour) {
		
		PriorityQueue<Coup> aJouer = j.GetCoupsPossibles();
		Coup coupCourant = new Coup();
		byte meilleurScore;
		byte scoreCourant;
		Boolean gagnant = null;
		if(tour == Couleur.BLANC){// Blanc joue
			meilleurScore = StatusConstants.LOSE;
			scoreCourant = StatusConstants.LOSE;
			while(gagnant == null){
				coupCourant = aJouer.poll();
				if(coupCourant == null){// Plus de coup à jouer : J'appelle ça un match nul
					return StatusConstants.DRAW;
				}
				j.joueCoup(coupCourant, tour);
				gagnant = j.blancGagne();
				if(gagnant != null && gagnant == true){
					j.undo(coupCourant);
					return StatusConstants.WIN;
				}
				else{
					// Je n'ai pas pu perdre en jouant un coup
					// Donc gagnant == null
					scoreCourant = recherche(Couleur.NOIR);
					if(scoreCourant > meilleurScore) meilleurScore = scoreCourant;
				}
				j.undo(coupCourant);
			}
			return meilleurScore;
		}
		else if(tour == Couleur.NOIR){
			meilleurScore = StatusConstants.WIN;
			scoreCourant = StatusConstants.WIN;
			while(gagnant == null){
				coupCourant = aJouer.poll();
				if(coupCourant == null)// Plus de coup à jouer : J'appelle ça un match nul
					return StatusConstants.DRAW;
				j.joueCoup(coupCourant, tour);
				gagnant = j.blancGagne();
				if(gagnant != null && gagnant == false){
					j.undo(coupCourant);
					return StatusConstants.LOSE;
				}
				else{
					// Je n'ai pas pu faire gagner blanc en jouant ce coup
					// Donc gagnant == null
					scoreCourant = recherche(Couleur.BLANC);
					if(scoreCourant < meilleurScore) meilleurScore = scoreCourant;
				}
				j.undo(coupCourant);
			}
			return meilleurScore;
		}
		else throw new IllegalArgumentException("Il y a un troisième joueur");
	}
	
	
}
