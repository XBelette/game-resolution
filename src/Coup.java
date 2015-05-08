
public class Coup implements Comparable<Coup> {
	public byte colonne;
	public byte line;
	
	public Coup(){
		// Defaults to 0,0
		colonne = 0;
		line=0;
	}
	public Coup(byte col, byte l){
		colonne = col;
		line = l;
	}
	
	@Override
	public int compareTo(Coup arg0) {
		// There WILL be an heuristic for this later on
		// There will have some problem for making it dependent on the game, but time for that later.
		// For now, let's just say they are all equal.
		return 0;
	}
}
