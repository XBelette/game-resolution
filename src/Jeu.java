
public abstract class Jeu {
	
	protected byte H;
	protected byte L;
	protected Position p;
	
	public Jeu(byte L, byte H, Position p){
		this.L = L;
		this.H = H;
		this.p = p;
	}

	public byte getH() {
		return H;
	}
	public byte getL() {
		return L;
	}
	public Position getp() {
		return p;
	}
	
	abstract public void coup(byte colonne, byte line, byte couleur);

}
