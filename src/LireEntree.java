import java.util.Scanner;

public class LireEntree {

	public static Coup suivant(Coup c, int H, int L) {
		if (c.colonne != (L - 1))
			return new Coup((byte) (c.colonne + 1), c.line);
		if (c.line == 0)
			return null;
		return new Coup((byte) 0, (byte) (c.line - 1));
	}

	public static Jeu LireEntreeStd(String titre) {
		Scanner sc = new Scanner(System.in);
		String ligneTaille = sc.nextLine();
		String[] dimension = ligneTaille.split("/");
		int L = -1;
		int H = -1;
		if (titre.equals("tictactoe")) {

			L = Integer.parseInt(dimension[1]);
			H = Integer.parseInt(dimension[2]);
		} else if (titre.equals("connect4") || titre.equals("othello")) {
			L = Integer.parseInt(dimension[0]);
			H = Integer.parseInt(dimension[1]);
		}
		Position p = new Position(L, H);
		StringBuffer sb = new StringBuffer();
		while (sc.hasNextLine()) {
			String a = sc.nextLine();
			sb.append(a);
		}

		String s = sb.toString();
		char[] table = s.toCharArray();
		Coup c = new Coup((byte) 0, (byte) (H - 1));
		int curseur = 0;
		while (c != null) {
			char x = table[curseur];
			if (x == '.') { // Case vide
				curseur++;
				c = suivant(c, H, L);
			} else if (x == '@') { // Case occupée par un noir
				p.ajouteNoir(c);
				curseur++;
				c = suivant(c, H, L);
			} else if (x == '0') { // Case occupée par un blanc
				p.ajouteBlanc(c);
				curseur++;
				c = suivant(c, H, L);
			} else { // Pas d'information
				curseur++;
			}
		}
		sc.close();
		if (titre.equals("tictactoe")) {
			int k = Integer.parseInt(dimension[0]);
			return new Morpion(L, H, (byte) k, p);
		} else if (titre.equals("connect4")) {
			System.err.println("Coucou je suis sorti");
			return new Puissance4(L, H, p);
		} else if (titre.equals("othello")) {
			return new Othello(L, H, p);
		}
		return null;
	}

	public static void main(String[] args) {
		String s = args[0];
		AfficheAffichage.affichePlateau(LireEntreeStd(s));
	}

}
