
public class Main {

	public static void main(String args[]){
		// Eventually there will be arguments.
		
		// For testing purposes, for now, it only creates an empty morpion and solves it.
		
		Jeu jeuAResoudre = new Morpion(3,3,3);
		Recherche algoTeste = new Minimax(jeuAResoudre);
		byte result = algoTeste.recherche(Couleur.BLANC);
		
		switch(result){
		case StatusConstants.LOSE:
			System.out.println("LOSE");
			break;
		case StatusConstants.DRAW:
			System.out.println("DRAW");
			break;
		case StatusConstants.WIN:
			System.out.println("WIN");
			break;
		default:
			System.err.println("What are you trying to make me print ?");
			break;
		}		
	}
}
