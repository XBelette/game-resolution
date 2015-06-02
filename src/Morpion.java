import java.util.LinkedList;
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
	public LinkedList<Coup> GetCoupsPossibles(Couleur c){
		PriorityQueue<CoupCompare> coupsPossibles = new PriorityQueue<>();
		CoupCompare coupCourant = new CoupCompare(this, c);
		while(coupCourant.line < H){
			while(coupCourant.colonne < L){
				if(p.quiEstLa(coupCourant) == null) coupsPossibles.add(new CoupCompare(coupCourant.colonne, coupCourant.line,this,c));
				coupCourant.colonne++;
			}
			coupCourant.line++;
			coupCourant.colonne = 0;
		}
		LinkedList<Coup> coups = new LinkedList<>();
		for(CoupCompare co:coupsPossibles)
			coups.add(new Coup(co));
		
		return coups;
	}
	
	public boolean partieFinie(){ // La partie est finie si :
		if (gagne(Couleur.BLANC) || gagne(Couleur.NOIR)) // Noir ou Blanc a aligné ses pions
			return true;
		LinkedList<Coup> l = GetCoupsPossibles(Couleur.BLANC);
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
