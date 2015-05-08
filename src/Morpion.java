
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
	public void coup(byte colonne, byte line, byte couleur){
		if (p.quiEstLa(colonne, line)!=0)
			throw new IllegalArgumentException("Coup interdit");
		if (couleur == 1)
			p.ajouteBlanc(colonne,line);
		else if (couleur == -1)
			p.ajouteNoir(colonne,line);
		else
			throw new IllegalArgumentException("Couleur indeterminee");
	}
}
