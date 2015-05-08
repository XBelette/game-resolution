import java.util.PriorityQueue;


public class Puissance4 extends Jeu{
	
	byte L;
	byte H;
	Position p;

	public Puissance4(byte L, byte H, Position p){
		super(L, H, p);
	}
	
	public Puissance4(byte L, byte H){
		super(L, H, new Position(L,H));
	}
	
	// 1 pour les blancs, -1 pour les noirs
	@Override
	public void joueCoup(Coup coup, Color couleur){
		// On recalcule line de toute façon
		// L'argument est là pour hériter cette méthode de la super classe Jeu
		coup.line = 0;
		while (coup.line<H && p.quiEstLa(coup)!=0)
			coup.line++;
		if (coup.line == H)
			throw new IllegalArgumentException("Colonne pleine");
		if (couleur == Color.BLANC)
			p.ajouteBlanc(coup);
		else if (couleur == Color.NOIR)
			p.ajouteNoir(coup);
		else
			throw new IllegalArgumentException("Couleur indeterminee");
	}
	
	public PriorityQueue<Coup> GetCoupsPossibles(){
		PriorityQueue<Coup> coupsPossibles = new PriorityQueue<Coup>();
		Coup coupCourant = new Coup();
		// On ignore les lignes, chic au p4 !
		while(coupCourant.colonne < H){
			// Une priorité pourrait être calculée ici
			if(p.quiEstLa(coupCourant) == 0) coupsPossibles.add(coupCourant);
			coupCourant.colonne++;
		}
		return coupsPossibles;
	}
	
	@Override
	public Boolean blancGagne(){
		// Rappel : retourne true si blanc gagne, false si noir gagne, et null si rien n'est joué
		Boolean test = null;
		// Blanc gagne-t-il ?
		test = p.alignementHorizontal((byte)4, Color.BLANC) || p.alignementVertical((byte) 4, Color.BLANC)
				|| p.alignementDiagonaleAntislash((byte) 4, Color.BLANC) || p.alignementDiagonaleSlash((byte) 4, Color.BLANC);
		if(test) return true;
		// Okay, donc blanc ne gagne pas. Et noir ?
		test = p.alignementHorizontal((byte)4, Color.NOIR) || p.alignementVertical((byte) 4, Color.NOIR)
				|| p.alignementDiagonaleAntislash((byte) 4, Color.NOIR) || p.alignementDiagonaleSlash((byte) 4, Color.NOIR);
		if(test) return false;
		
		// Si vraiment personne n'a gagné :
		return null;
	}

	@Override
	public void undo(Coup coup) {
		coup.line = H;
		while(coup.line >= 0 && p.quiEstLa(coup) == 0)
			coup.line--;
		if(coup.line < 0)
			throw new IllegalArgumentException("Colonne vide");
		p.remove(coup);
	}

	
}
