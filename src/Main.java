
public class Main {

	public static void main(String args[]){
		Jeu jeuAResoudre;
		Recherche algoTeste;

		// S'il n'y a pas d'arguments, test par défaut
		if(args.length < 1){
			jeuAResoudre = new Morpion(3,3,3);
			algoTeste = new alpha_beta(jeuAResoudre);

		}
		else{
			jeuAResoudre = LireEntree.LireEntreeStd(args[0]);
			algoTeste = new alpha_beta(jeuAResoudre);
			
		}
		long start = System.currentTimeMillis();
		int result = algoTeste.recherche(Couleur.BLANC);
		long end = System.currentTimeMillis();
		switch(result){
		case StatusConstants.LOSE:
			System.out.println("LOSS");
			break;
		case StatusConstants.DRAW:
			System.out.println("DRAW");
			break;
		case StatusConstants.WIN:
			System.out.println("WIN");
			break;
		default:
			System.err.println("Que voulez-vous que j'imprime ?");
			break;
		}
		System.err.println(end-start + " ms");
	}
}
