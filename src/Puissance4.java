import java.util.PriorityQueue;


public class Puissance4 extends Jeu{

	public Puissance4(int L, int H, Position p){
		super(L, H, p);
	}
	
	public Puissance4(int L, int H){
		super(L, H, new Position(L,H));
	}
	
	// 1 pour les blancs, -1 pour les noirs
	@Override
	public void joueCoup(Coup coup, Couleur couleur){
		// On recalcule line de toute façon
		// L'argument est là pour hériter cette méthode de la super classe Jeu
		coup.line = 0;
		while (coup.line<this.H && p.quiEstLa(coup)!=null)
			coup.line++;
		if (coup.line == this.H){
			AfficheAffichage.affichePlateau(this);
			throw new IllegalArgumentException("Colonne pleine");
		}
		if (couleur == Couleur.BLANC)
			p.ajouteBlanc(coup);
		else if (couleur == Couleur.NOIR)
			p.ajouteNoir(coup);
		else
			throw new IllegalArgumentException("Couleur indeterminee");
	}

	
	public PriorityQueue<Coup> GetCoupsPossibles(Couleur c){
		PriorityQueue<Coup> coupsPossibles = new PriorityQueue<Coup>();
		PriorityQueue<CoupCompare> coupsOrdonnes = new PriorityQueue<CoupCompare>();
		CoupCompare coupCourant = new CoupCompare(this,c);
		// On ignore les lignes, chic au p4 !
		while(coupCourant.colonne < L){
			coupCourant.line = 0;
			// Une priorité pourrait être calculée ici
			while(p.quiEstLa(coupCourant) != null) 
				coupCourant.line++;
			// Okay, donc je suis sur la première case où il n'y a personne
			// Si je suis trop haut en fait je ne peux pas jouer dans cette colonne
			if(coupCourant.line < H)
				coupsOrdonnes.add(new CoupCompare(coupCourant.colonne, coupCourant.line, this, c));
			coupCourant.colonne++;
		}
		coupsPossibles.addAll(coupsOrdonnes);
		return coupsPossibles;
	}
	
	@Override
	public Boolean blancGagne(){
		// Rappel : retourne true si blanc gagne, false si noir gagne, et null si rien n'est joue
		Boolean test = null;
		// Blanc gagne-t-il ?
		test = p.alignementHorizontal((byte)4, Couleur.BLANC) || p.alignementVertical((byte) 4, Couleur.BLANC)
				|| p.alignementDiagonaleAntislash((byte) 4, Couleur.BLANC) || p.alignementDiagonaleSlash((byte) 4, Couleur.BLANC);
		if(test) return true;
		// Okay, donc blanc ne gagne pas. Et noir ?
		test = p.alignementHorizontal((byte)4, Couleur.NOIR) || p.alignementVertical((byte) 4, Couleur.NOIR)
				|| p.alignementDiagonaleAntislash((byte) 4, Couleur.NOIR) || p.alignementDiagonaleSlash((byte) 4, Couleur.NOIR);
		if(test) return false;
		
		// Si vraiment personne n'a gagné :
		return null;
	}
	
	public boolean partieFinie(){
		if (blancGagne()!=null) // Noir ou Blanc a align� ses pions
			return true;
		PriorityQueue<Coup> l = GetCoupsPossibles(Couleur.BLANC);
		if (l.isEmpty()) // Le plateau est plein
			return true;
		return false;
	}
	
	@Override
	public boolean gagne(Couleur tour) {
		return p.alignementHorizontal(4, tour) || p.alignementVertical(4, tour)
		|| p.alignementDiagonaleAntislash(4, tour) || p.alignementDiagonaleSlash(4, tour);
	}

	public void undo(Coup coup) {
		coup.line = H;
		while(coup.line >= 0 && p.quiEstLa(coup) == null)
			coup.line--;
		if(coup.line < 0)
			throw new IllegalArgumentException("Colonne vide");
		p.remove(coup);
	}

}
