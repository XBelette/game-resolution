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
		Coup coupCourant = new Coup();
		// On ignore les lignes, chic au p4 !
		while(coupCourant.line < H){
			// Une priorité pourrait être calculée ici
			if(p.quiEstLa(coupCourant) == null) coupsPossibles.add(coupCourant);
			coupCourant.colonne++;
		}
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
	
	public Boolean partieFinie(){
		if (blancGagne()!=null) // Noir ou Blanc a align� ses pions
			return true;
		PriorityQueue<Coup> l = GetCoupsPossibles(Couleur.BLANC);
		if (l.isEmpty()) // Le plateau est plein
			return true;
		return false;
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
