import java.util.PriorityQueue;




public abstract class Recherche {
	Jeu j;
	PriorityQueue<Coup> aJouer;
	
	public Recherche(Jeu j){
		this.j = j;
		aJouer = new PriorityQueue<Coup>();
	}
	abstract public byte recherche(Color tour);
}
