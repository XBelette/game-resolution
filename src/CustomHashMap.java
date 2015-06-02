
public class CustomHashMap {
	private Pair<Position, Integer>[] informations;
	boolean forteSymetrie;
	
	@SuppressWarnings("unchecked")
	public CustomHashMap(Jeu j){
		informations = new Pair[10000000];
		for(int i =0; i < 10000000; i++)
			informations[i] = null;
		forteSymetrie = (!(j instanceof Puissance4) && (j.getH()==j.getL()));
	}
	
	@SuppressWarnings("unchecked")
	public CustomHashMap(int size, Jeu j){
		informations = new Pair[size];
		for(int i =0; i < size; i++)
			informations[i] = null;
		forteSymetrie = (!(j instanceof Puissance4) && (j.getH()==j.getL()));
	}
	
	public void put(Position p, Integer score, int alpha, int beta, Couleur tour){
		int hash = p.hashCode();
		int index = hash % (informations.length);
		// Prépare les informations : 
		// Les deux premiers bits contiennent alpha
		// Les deux suivants contiennent beta
		// Le suivant contient le tour
		// Convention : BLANC est 0 (premier joueur)
		// Convention : -1 == 2
		// Les bits de gauche contiennent le score
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
		// Suppose que la position p a été stockée précédemment, soit containsKey(p) == true
		return (informations[p.hashCode()%informations.length].second >> 5);
	}
	
	public Couleur getTour(Position p){
		// Suppose que la position p a été stockée précédemment, soit containsKey(p) == true
		int couleur = (informations[p.hashCode()%informations.length].second >> 4)&1;
		return (couleur==0)?Couleur.BLANC:Couleur.NOIR;
	}
	
	public int getAlpha(Position p){
		// Renvoie le alpha au moment du stockage dans la table
		// Suppose que la position p a été stockée précédemment, soit containsKey(p) == true
		int alpha = informations[p.hashCode()%informations.length].second & 3;
		return (alpha==2)?-1:alpha;
	}
	
	public int getBeta(Position p){
		// Renvoie le béta au moment du stockage dans la table
		// Suppose que la position p a été stockée précédemment, soit containsKey(p) == true
		int beta = (informations[p.hashCode()%informations.length].second >> 2)&3;
		return (beta==2)?-1:beta;
	}
	
	public boolean containsKey(Position p){
		// Renvoie vrai ssi la position p est encore présente dans la table (elle a pu être remplacée).
		if(forteSymetrie)
			return informations[p.hashCode()%informations.length] != null 
				&& informations[p.hashCode()%informations.length].first.equalsPasPuissance4(p);
		else
			return informations[p.hashCode()%informations.length] != null 
			&& informations[p.hashCode()%informations.length].first.equals(p);
	}
}
