package acp;

import java.io.IOException;
import java.util.Scanner;



public class Program {
	public static void main(String[] args) throws IOException {
		try (Scanner scan = new Scanner(System.in)) {
			//1.	Charger les donn�es � partir d�un fichier
			System.out.println("Veuillez saisir le nom du fichier txt: (DATA.txt par d�faut)");
			String nomf=scan.next();
			double[][] dataset = Acp.uploadData(nomf);
			System.out.println("Donn�es initiales:");
			CalculMatriciel.printf(dataset);
			System.out.println("Veuillez saisir les axes � retenir: ");
			int axes=scan.nextInt();
			Acp data = new Acp(dataset);
			//2.	Centrer et r�duire les colonnes : X_reduced
			System.out.println("Tapez 1 pour centrer les donn�es(recommand�e pour des donn�es de m�me type)\nTapez 2 pour centrer et r�duire les donn�es: ");
			int choix=scan.nextInt();
			switch (choix) {
			case 1:
				data.centrer();
				System.out.println("Donn�es centr�es:");
				CalculMatriciel.printf(data.matrice);
				//3.	Calculer la matrice de covariance : cov_mat
				double[][] cov_mat = data.matricecov_cor();
				System.out.println("La matrice de covariance:");
				CalculMatriciel.printf(cov_mat);
				break;
			case 2:
				data.centrer_reduire();
				System.out.println("Donn�es centr�es et r�duits:");
				CalculMatriciel.printf(data.matrice);
				//3.	Calculer la matrice de coorelation : cor_mat
				double[][] cor_mat = data.matricecov_cor();
				System.out.println("La matrice de coor�lation:");
				CalculMatriciel.printf(cor_mat);
				break;
			default:
				System.out.println("ERREUR.....");
				System.exit(0);
			}
			//4.5.	Calculer les valeurs propres, ainsi les vecteurs propres associ�s
			Propr_Val_Vect val_vect = data.cov_corpropresget();
			double[][] vals = {val_vect.valeurs};
			System.out.println("Les valeurs propres:");
			CalculMatriciel.printf(vals);
			System.out.println("Les vecteurs propres associ�es:");
			CalculMatriciel.printf(val_vect.vecteurs);
			//6.	Identifier les composantes principales
			System.out.println("Les composants principales:");
			CalculMatriciel.printf(data.composantsprincipalesextract(axes, val_vect));
			//7.	Projeter les donn�es sur les nouveaux axes (composantes principales)
			double [][] x_PROJ=Acp.analysecomposantsprincipales(data.matrice, axes);
			System.out.println("La transformation ACP:");
			CalculMatriciel.printf(x_PROJ);
			//8.    Quantit� d'information(pourcentage) en chaque axe:
			System.out.println("La quantit� d'information en chaque axe retenue: ");
			for(int i=0;i<axes;i++) {
			System.out.println("Axe "+(i+1)+":  "+data.quantite_dinfo(val_vect.valeurs)[i]+"   %");
			}
		}
	}
}
