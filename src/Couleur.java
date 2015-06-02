
public enum Couleur {
	BLANC, NOIR;
	
	public Couleur other(){
		switch(this){
		case BLANC:
			return NOIR;
		case NOIR:
			return BLANC;
		default:
			throw new IllegalArgumentException("Il y a un troisième joueur");
		}
	}
}
