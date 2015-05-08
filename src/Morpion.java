import java.util.PriorityQueue;


public class Morpion extends Jeu{


	final byte k;
	Position p;

	public Morpion(byte L, byte H, byte k, Position p){
		super(L, H, p);
		this.k = k;
	}
	
	public Morpion(byte L, byte H, byte k){
		super(L, H, new Position(L,H));
		this.k = k;
	}
	
	// 1 pour les blancs, -1 pour les noirs
	@Override
	public void joueCoup(Coup coup, Color couleur){
		if (p.quiEstLa(coup)!=0)
			throw new IllegalArgumentException("Coup interdit");
		if (couleur == Color.BLANC)
			p.ajouteBlanc(coup);
		else if (couleur == Color.NOIR)
			p.ajouteNoir(coup);
		else
			throw new IllegalArgumentException("Couleur indeterminee");
	}
	
	@Override
	public PriorityQueue<Coup> GetCoupsPossibles(){
		PriorityQueue<Coup> coupsPossibles = new PriorityQueue<Coup>();
		Coup coupCourant = new Coup();
		while(coupCourant.line < L){
			while(coupCourant.colonne < H){
				// Une priorité pourrait être calculée ici
				if(p.quiEstLa(coupCourant) == 0) coupsPossibles.add(coupCourant);
				coupCourant.colonne++;
			}
			coupCourant.line++;
		}
		
		
		return coupsPossibles;
	}
	
	@Override
	public Boolean blancGagne(){
		// Rappel : retourne true si blanc gagne, false si noir gagne, et null si rien n'est joué
		Boolean test = null;
		// Blanc gagne-t-il ?
		test = p.aligneHorizontal((byte)3, Color.BLANC) || p.alignementVertical((byte) 3, Color.BLANC)
				|| p.aligneDiagonaleAntislash((byte) 3, Color.BLANC) || p.aligneDiagonaleSlash((byte) 3, Color.BLANC);
		if(test) return true;
		// Okay, donc blanc ne gagne pas. Et noir ?
		test = p.aligneHorizontal((byte)3, Color.NOIR) || p.alignementVertical((byte) 3, Color.NOIR)
				|| p.aligneDiagonaleAntislash((byte) 3, Color.NOIR) || p.aligneDiagonaleSlash((byte) 3, Color.NOIR);
		if(test) return false;
		
		// Si vraiment personne n'a gagné :
		return null;
	}
	
}
