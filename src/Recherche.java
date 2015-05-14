
public abstract class Recherche {
	Jeu j;
	
	public Recherche(Jeu j){
		this.j = j;
	}
	abstract public byte recherche(Couleur tour);
}
