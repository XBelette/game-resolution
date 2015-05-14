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
		Couleur couleur;
		while (pionPlace.colonne < L) {
			while (pionPlace.line < H) {
				couleur = p.quiEstLa(pionPlace);
				if (couleur == Couleur.BLANC || couleur == Couleur.NOIR) {
					if (couleur == Couleur.BLANC)
						g.setColor(Color.WHITE);
					else
						g.setColor(Color.BLACK);

					g.fillOval(
							(int) (50 + pas * pionPlace.colonne + pas / 6),
							(int) (50 + (H - pionPlace.line - 1) * pas + pas / 6),
							(int) (2 * pas / 3), (int) (2 * pas / 3));
				}
				pionPlace.line++;
			}
			pionPlace.colonne++;
			pionPlace.line=0;
		}

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		dessiner(g);
	}

}
