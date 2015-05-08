import javax.swing.JFrame;


public class AfficheAffichage {
	public static void affichePlateau(Position p) {	
		JFrame cadre = new javax.swing.JFrame("System"); //nom de la fenetre
		
		cadre.setContentPane(new Affichage(p));
		cadre.setLocation(300, 50); // position du cadre dans l'ecran
		cadre.pack();
		cadre.setVisible(true);
		cadre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    }
	
	public static void affichePlateau(Jeu j) {	
		JFrame cadre = new javax.swing.JFrame("System"); //nom de la fenetre
		cadre.setContentPane(new Affichage(j));
		cadre.setLocation(300, 50); // position du cadre dans l'ecran
		cadre.pack();
		cadre.setVisible(true);
		cadre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    }
	
	public static void main(String[] args) {
		byte L = 7;
		byte H = 5;
		Jeu j = new Puissance4(L,H);
		((Puissance4) j).joueCoup(new Coup((byte)3,(byte)0),Color.BLANC);
		((Puissance4) j).joueCoup(new Coup((byte)1,(byte)0),Color.NOIR);
		((Puissance4) j).joueCoup(new Coup((byte)3,(byte)0),Color.BLANC);
		((Puissance4) j).joueCoup(new Coup((byte)3,(byte)0),Color.NOIR);
		((Puissance4) j).joueCoup(new Coup((byte)3,(byte)0),Color.BLANC);
		affichePlateau(j);
	}
}
