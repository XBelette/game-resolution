
public class Minimax extends Recherche{
	
	public Minimax(Jeu j){
		super(j);
	}
	
	@Override
	public byte recherche(Color tour) {
		
		this.aJouer = j.GetCoupsPossibles();
		Coup coupCourant = new Coup();
		byte meilleurScore;
		byte scoreCourant;
		Boolean gagnant = null;
		if(tour == Color.BLANC){// Blanc joue
			meilleurScore = StatusConstants.LOSE;
			scoreCourant = StatusConstants.LOSE;
			while(gagnant == null){
				coupCourant = this.aJouer.poll();
				if(coupCourant == null)// Plus de coup à jouer : J'appelle ça un match nul
					return StatusConstants.DRAW;
				j.joueCoup(coupCourant, tour);
				gagnant = j.blancGagne();
				if(gagnant == true) return StatusConstants.WIN;
				else{
					// Je n'ai pas pu perdre en jouant un coup
					// Donc gagnant == null
					scoreCourant = recherche(Color.NOIR);
					if(scoreCourant > meilleurScore) meilleurScore = scoreCourant;
				}
				j.undo(coupCourant);
			}
			return meilleurScore;
		}
		else if(tour == Color.NOIR){
			meilleurScore = StatusConstants.WIN;
			scoreCourant = StatusConstants.WIN;
			while(gagnant == null){
				coupCourant = this.aJouer.poll();
				if(coupCourant == null)// Plus de coup à jouer : J'appelle ça un match nul
					return StatusConstants.DRAW;
				j.joueCoup(coupCourant, tour);
				gagnant = j.blancGagne();
				if(gagnant == false) return StatusConstants.LOSE;
				else{
					// Je n'ai pas pu faire gagner blanc en jouant ce coup
					// Donc gagnant == null
					scoreCourant = recherche(Color.BLANC);
					if(scoreCourant < meilleurScore) meilleurScore = scoreCourant;
				}
				j.undo(coupCourant);
			}
			return meilleurScore;
		}
		else throw new IllegalArgumentException("Il y a un troisième joueur");
	}
	
	
}
