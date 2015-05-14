import java.util.PriorityQueue;


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
	
	abstract public PriorityQueue<Coup> GetCoupsPossibles();
	abstract public Boolean blancGagne(); // renvoie true si blanc a gagné, false si noir a gagné, et null sinon 
	abstract public void joueCoup(Coup coup, Couleur couleur);
	abstract public void undo(Coup coup); // Défait un coup.
}
