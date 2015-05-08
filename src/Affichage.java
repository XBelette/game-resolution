import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Affichage extends JPanel {

	byte L; // Largeur
	byte H; // Hauteur

	Position p;
	int TP = 500; // Taille du plateau

	public Affichage(Position p) {
		setBackground(Color.LIGHT_GRAY);
		setPreferredSize(new Dimension(600, 600)); // taille du cadre
		this.p = p;
		this.L = p.L;
		this.H = p.H;
	}
	
	public Affichage(Jeu j) {
		setBackground(Color.LIGHT_GRAY);
		setPreferredSize(new Dimension(600, 600)); // taille du cadre
		this.p = j.getp();
		this.L = j.getL();
		this.H = j.getH();
	}
	

	public void dessiner(Graphics g) {

		double pas = (double) (TP) / (Math.max(L, H));
		
		// Trace des lignes du cube
		g.setColor(Color.BLACK);
		g.drawLine(50, 50, (int) Math.round(50 + L * pas), 50);
		for (int i = 0; i < H; i++)
			g.drawLine(50, (int) Math.round(50 + (i + 1) * pas),
					(int) Math.round(50 + L * pas),
					(int) Math.round(50 + (i + 1) * pas));
		g.drawLine(50, 50, 50, (int) Math.round(50 + H * pas));
		for (int i = 0; i < L; i++)
			g.drawLine((int) Math.round(50 + (i + 1) * pas), 50,
					(int) Math.round(50 + (i + 1) * pas),
					(int) Math.round(50 + H * pas));

		// Placement des pions
		Coup pionPlace = new Coup();
		byte couleur = 0;
		while(pionPlace.line < L){
			while(pionPlace.colonne < H){
				couleur = p.quiEstLa(pionPlace);
				if (couleur == 1 || couleur == -1) {
					if (couleur == 1)
						g.setColor(Color.WHITE);
					else 
						g.setColor(Color.BLACK);
					
					g.fillOval((int)(50+pas*pionPlace.line+pas/6),(int)(50+(H-pionPlace.colonne-1)*pas+pas/6),(int)(2*pas/3),(int)(2*pas/3));
				}
				pionPlace.colonne++;
			}
			pionPlace.line++;
		}

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		dessiner(g);
	}

}
