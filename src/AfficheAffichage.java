import javax.swing.JFrame;

public class AfficheAffichage {
	public static void affichePlateau(Position p) {	
		JFrame cadre = new javax.swing.JFrame("Plateau"); //nom de la fenêtre
		cadre.setContentPane(new Affichage(p));
		cadre.setLocation(300, 50); // position du cadre dans l'ecran
		cadre.pack();
		cadre.setVisible(true);
		cadre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    }	
	
	public static void affichePlateau(Jeu j) { affichePlateau(j.getp()); }
	
}
