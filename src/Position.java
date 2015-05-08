
public class Position {

	// L'origine est en bas à gauche du tableau
	// Si la ligne et la colonne sont entre 0 et L/H, avec l'origine comme ci-dessus,
	// la case (col, line) a pour adresse le bit col*(H+1) + line
	
	byte L;
	byte H;
	
	private long positionBlancs;
	private long positionNoirs;
	
	public Position(byte L, byte H, long pB, long pN){
		this.H = H;
		this.L = L;
		this.positionBlancs = pB;
		this.positionNoirs = pN;
	}
	
	public Position(byte L, byte H){
		this.H = H;
		this.L = L;
		this.positionBlancs = 0;
		this.positionNoirs = 0;
	}
	
	public boolean isEmpty(){
		return (positionBlancs == 0 && positionNoirs == 0);
	}
	
	// 1 pour les blancs, -1 pour les noirs, 0 pour inoccupé
	
	public byte quiEstLa(byte col, byte line){
		long adresse = (long)(Math.pow(2, col*(H+1)+line));
		if ((positionBlancs & adresse) != 0)
			return 1;
		else if ((positionNoirs & adresse) != 0)
			return -1;
		return 0;
	}
	
	public boolean ajouteBlanc(byte col, byte line){
		long adresse = (long)(Math.pow(2, col*(H+1)+line));
		if (quiEstLa(col,line) != 0)
			return false;
		positionBlancs = (positionBlancs | adresse);
		return true;
	}
	
	public boolean ajouteNoir(byte col, byte line){
		long adresse = (long)(Math.pow(2, col*(H+1)+line));
		if (quiEstLa(col,line) != 0)
			return false;
		positionNoirs= (positionNoirs | adresse);
		return true;
	}

	
}
