
public class Coup {
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
}
