import java.util.PriorityQueue;


public class Morpion extends Jeu{


	final int k;

	public Morpion(int L, int H, int k, Position p){
		super(L, H, p);
		this.k = k;
	}
	
	public Morpion(int L, int H, int k){
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
	
	public void undo(Coup coup) {
		p.remove(coup);
	}
	
	@Override
	public PriorityQueue<Coup> GetCoupsPossibles(Couleur c){
		PriorityQueue<CoupCompare> coupsPossibles = new PriorityQueue<>();
		CoupCompare coupCourant = new CoupCompare(this, c);
		while(coupCourant.line < H){
			while(coupCourant.colonne < L){
				// Une priorité pourrait être calculée ici
				if(p.quiEstLa(coupCourant) == null) coupsPossibles.add(new CoupCompare(coupCourant.colonne, coupCourant.line,this,c));
				coupCourant.colonne++;
			}
			coupCourant.line++;
			coupCourant.colonne = 0;
		}
		PriorityQueue<Coup> coups = new PriorityQueue<>();
		coups.addAll(coupsPossibles);
		
		return coups;
	}
	
	/*
	public static void main(String[] args) {
		Jeu j = new Morpion(2,2,2);
		PriorityQueue<Coup> p = j.GetCoupsPossibles(Couleur.BLANC);
		for (Coup c : p)
			System.out.println(c);
	}
	*/
	
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
	
	public boolean partieFinie(){ // La partie est finie si :
		if (blancGagne()!=null) // Noir ou Blanc a align� ses pions
			return true;
		PriorityQueue<Coup> l = GetCoupsPossibles(Couleur.BLANC);
		if (l.isEmpty()) // Le plateau est plein
			return true;
		return false;
		
	}

	@Override
	public boolean gagne(Couleur tour) {
		return p.alignementHorizontal(k, tour) || p.alignementVertical(k, tour)
		|| p.alignementDiagonaleAntislash(k, tour) || p.alignementDiagonaleSlash(k, tour);
	}

	
}
