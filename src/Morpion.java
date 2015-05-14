import java.util.PriorityQueue;


public class Morpion extends Jeu{


	final byte k;

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
	public void joueCoup(Coup coup, Couleur couleur){
		if (p.quiEstLa(coup)!=null){
			AfficheAffichage.affichePlateau(this);
			System.out.println("On jouait : " + coup.toString());
			throw new IllegalArgumentException("Coup interdit");
		}
		if (couleur == Couleur.BLANC)
			p.ajouteBlanc(coup);
		else if (couleur == Couleur.NOIR)
			p.ajouteNoir(coup);
		else
			throw new IllegalArgumentException("Couleur indeterminee");
	}
	
	@Override
	public void undo(Coup coup) {
		// Maybe add some checks ?
		p.remove(coup);
	}
	
	@Override
	public PriorityQueue<Coup> GetCoupsPossibles(){
		PriorityQueue<Coup> coupsPossibles = new PriorityQueue<Coup>();
		Coup coupCourant = new Coup();
		while(coupCourant.line < L){
			while(coupCourant.colonne < H){
				// Une priorité pourrait être calculée ici
				if(p.quiEstLa(coupCourant) == null) coupsPossibles.add(new Coup(coupCourant.colonne, coupCourant.line));
				coupCourant.colonne++;
			}
			coupCourant.line++;
			coupCourant.colonne = 0;
		}
		
		
		return coupsPossibles;
	}
	
	@Override
	public Boolean blancGagne(){
		// Rappel : retourne true si blanc gagne, false si noir gagne, et null si rien n'est joué
		Boolean test = null;
		// Blanc gagne-t-il ?
		test = p.alignementHorizontal(k, Couleur.BLANC) || p.alignementVertical(k, Couleur.BLANC)
				|| p.alignementDiagonaleAntislash(k, Couleur.BLANC) || p.alignementDiagonaleSlash(k, Couleur.BLANC);
		if(test) return true;
		// Okay, donc blanc ne gagne pas. Et noir ?
		test = p.alignementHorizontal(k, Couleur.NOIR) || p.alignementVertical(k, Couleur.NOIR)
				|| p.alignementDiagonaleAntislash(k, Couleur.NOIR) || p.alignementDiagonaleSlash(k, Couleur.NOIR);
		if(test) return false;
		
		// Si vraiment personne n'a gagné :
		return null;
	}
	
}
