
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
	public void coup(byte colonne, byte line, byte couleur){
		// On recalcule line de toute façon
		// L'argument est là pour hériter cette méthode de la super classe Jeu
		line = 0;
		while (line<H && p.quiEstLa(colonne,line)!=0)
			line++;
		if (line == H)
			throw new IllegalArgumentException("Colonne pleine");
		if (couleur == 1)
			p.ajouteBlanc(colonne,line);
		else if (couleur == -1)
			p.ajouteNoir(colonne,line);
		else
			throw new IllegalArgumentException("Couleur indeterminee");
	}

	
}
