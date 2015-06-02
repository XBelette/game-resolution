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
	abstract public Boolean blancGagne(); // renvoie true si blanc a gagné, false si noir a gagné, et null sinon (match nul ou pas encore sûr)
	abstract public boolean gagne(Couleur tour); // renvoie true si le joueur a gagn�, false sinon
	abstract public boolean partieFinie();
	abstract public void joueCoup(Coup coup, Couleur couleur);
	public void undo(Position q){
		this.p = q;
	}; // Défait un coup.
	
	public String toString(){
		return this.getp().toString();
	}
	
}
