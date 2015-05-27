import java.util.PriorityQueue;


public abstract class Jeu {
	
	protected int H;
	protected int L;
	protected Position p;
	
	public Jeu(int L, int H, Position p){
		this.L = L;
		this.H = H;
		this.p = p;
	}

	public int getH() {
		return H;
	}
	public int getL() {
		return L;
	}
	public Position getp() {
		return p;
	}
	
	abstract public PriorityQueue<Coup> GetCoupsPossibles(Couleur c);
	abstract public Boolean blancGagne(); // renvoie true si blanc a gagné, false si noir a gagné, et null sinon (match nul ou pas encore s�r) 
	abstract public Boolean partieFinie();
	abstract public void joueCoup(Coup coup, Couleur couleur);
	public void undo(Position q){
		p = q;
	}; // Défait un coup.
	abstract public void undo(Coup c);
	
}
