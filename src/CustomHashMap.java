
public class CustomHashMap {
	private Pair<Position, Integer>[] informations;
	
	public CustomHashMap(){
		informations = new Pair[10000000];
		for(int i =0; i < 10000000; i++)
			informations[i] = null;
	}
	
	public CustomHashMap(int size){
		informations = new Pair[size];
		for(int i =0; i < size; i++)
			informations[i] = null;
	}
	
	public void put(Position p, Integer score, int alpha, int beta, Couleur tour){
		int hash = p.hashCode();
		int index = hash % (informations.length);
		// Preparing informations : 
		// The two first bits contain alpha
		// The two next bits contain  beta
		// The next bit contains player turn
		// Convention : white is 0 (premier joueur)
		// Convention : -1 == 2
		// Big end bits contain the score
		score = score << 1;
		score += (tour == Couleur.BLANC)?0:1;
		score = score << 2;
		score += (beta==-1)?2:beta;
		score = score << 2;
		score += (alpha==-1)?2:alpha;
		
		if(informations[index] == null){
			// Si on n'avait pas déjà enregistré de coup
			informations[index] = new Pair<Position, Integer>(p, score);
		}
		else{// Sinon
			// Je remplace le coup le plus profond.
			if(informations[index].first.getProfondeur() > p.getProfondeur()){
				informations[index].first = p.copy();
				informations[index].second = score;
			}
		}
	}
	
	public int get(Position p){
		// Renvoie le résultat stocké dans la fonction
		// Assumes position p was previously added. That is, containsKey(p) == true
		return (informations[p.hashCode()%informations.length].second >> 5);
	}
	
	public Couleur getTour(Position p){
		// Assumes position p was previously added. That is, containsKey(p) == true
		int couleur = (informations[p.hashCode()%informations.length].second >> 4)&1;
		return (couleur==0)?Couleur.BLANC:Couleur.NOIR;
	}
	
	public int getAlpha(Position p){
		// Renvoie le alpha au moment du stockage dans la table
		// Assumes position p was previously added. That is, containsKey(p) == true
		int alpha = informations[p.hashCode()%informations.length].second & 3;
		return (alpha==2)?-1:alpha;
	}
	
	public int getBeta(Position p){
		// Renvoie le béta au moment du stockage dans la table
		// Assumes position p was previously added. That is, containsKey(p) == true
		int beta = (informations[p.hashCode()%informations.length].second >> 2)&3;
		return (beta==2)?-1:beta;
	}
	
	public boolean containsKey(Position p){
		// Renvoie vrai ssi la position p est encore présente dans la table (elle a pu être remplacée).
		return informations[p.hashCode()%informations.length] != null 
				&& informations[p.hashCode()%informations.length].first.equals((Object)p);
	}
}
