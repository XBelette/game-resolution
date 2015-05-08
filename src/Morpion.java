import java.util.PriorityQueue;
import java.util.Queue;


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
	public void joueCoup(Coup coup, byte couleur){
		if (p.quiEstLa(coup)!=0)
			throw new IllegalArgumentException("Coup interdit");
		if (couleur == 1)
			p.ajouteBlanc(coup);
		else if (couleur == -1)
			p.ajouteNoir(coup);
		else
			throw new IllegalArgumentException("Couleur indeterminee");
	}
	
	@Override
	public Queue<Coup> GetCoupsPossibles(){
		Queue<Coup> coupsPossibles = new PriorityQueue<Coup>();
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
}
