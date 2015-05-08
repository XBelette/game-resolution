import java.util.PriorityQueue;
import java.util.Queue;


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

	
}
