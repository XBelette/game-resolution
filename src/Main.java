
public class Main {

	public static void main(String args[]){
		// Eventually there will be arguments.
//		Jeu jeuAResoudre;
//		Recherche algoTeste;
//
//		// If no option : here is a default test
//		if(args.length < 1){
//			jeuAResoudre = new Othello(6,6);
//			((Othello) jeuAResoudre).SituationDeDepart();
//			algoTeste = new alpha_beta(jeuAResoudre);
//
//		}
//		else{
//			jeuAResoudre = LireEntree.LireEntreeStd(args[0]);
//			algoTeste = new alpha_beta(jeuAResoudre);
//			
//		}
//		long start = System.currentTimeMillis();
//		int result = algoTeste.recherche(Couleur.BLANC);
//		long end = System.currentTimeMillis();
//		switch(result){
//		case StatusConstants.LOSE:
//			System.out.println("LOSS");
//			break;
//		case StatusConstants.DRAW:
//			System.out.println("DRAW");
//			break;
//		case StatusConstants.WIN:
//			System.out.println("WIN");
//			break;
//		default:
//			System.err.println("What are you trying to make me print ?");
//			break;
//		}
//		System.err.println(end-start + " ms");
		
		// Just testing maxAlignment function
		Jeu test = new Morpion(8,8,8);
		test.joueCoup(new Coup(3,5), Couleur.BLANC);
		test.joueCoup(new Coup(3,6), Couleur.BLANC);
		test.joueCoup(new Coup(3,7), Couleur.BLANC);
		test.joueCoup(new Coup(3,4), Couleur.BLANC);
		System.out.println(test.p.maxAlign(new Coup(3,4), Couleur.BLANC));
		test.p.remove(new Coup(3,4));
		test.joueCoup(new Coup(3,4), Couleur.NOIR);
		System.out.println(test.p.maxAlign(new Coup(3,4), Couleur.NOIR));
		test.p.remove(new Coup(3,4));
		test.joueCoup(new Coup(4,6), Couleur.BLANC);
		System.out.println(test.p.maxAlign(new Coup(4,6), Couleur.BLANC));
	}
}
