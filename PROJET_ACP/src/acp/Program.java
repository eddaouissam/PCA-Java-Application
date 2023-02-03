package acp;

import java.io.IOException;
import java.util.Scanner;



public class Program {
	public static void main(String[] args) throws IOException {
		try (Scanner scan = new Scanner(System.in)) {
			//1.	Charger les données à partir d’un fichier
			System.out.println("Veuillez saisir le nom du fichier txt: (DATA.txt par défaut)");
			String nomf=scan.next();
			double[][] dataset = Acp.uploadData(nomf);
			System.out.println("Données initiales:");
			CalculMatriciel.printf(dataset);
			System.out.println("Veuillez saisir les axes à retenir: ");
			int axes=scan.nextInt();
			Acp data = new Acp(dataset);
			//2.	Centrer et réduire les colonnes : X_reduced
			System.out.println("Tapez 1 pour centrer les données(recommandée pour des données de même type)\nTapez 2 pour centrer et réduire les données: ");
			int choix=scan.nextInt();
			switch (choix) {
			case 1:
				data.centrer();
				System.out.println("Données centrées:");
				CalculMatriciel.printf(data.matrice);
				//3.	Calculer la matrice de covariance : cov_mat
				double[][] cov_mat = data.matricecov_cor();
				System.out.println("La matrice de covariance:");
				CalculMatriciel.printf(cov_mat);
				break;
			case 2:
				data.centrer_reduire();
				System.out.println("Données centrées et réduits:");
				CalculMatriciel.printf(data.matrice);
				//3.	Calculer la matrice de coorelation : cor_mat
				double[][] cor_mat = data.matricecov_cor();
				System.out.println("La matrice de coorélation:");
				CalculMatriciel.printf(cor_mat);
				break;
			default:
				System.out.println("ERREUR.....");
				System.exit(0);
			}
			//4.5.	Calculer les valeurs propres, ainsi les vecteurs propres associés
			Propr_Val_Vect val_vect = data.cov_corpropresget();
			double[][] vals = {val_vect.valeurs};
			System.out.println("Les valeurs propres:");
			CalculMatriciel.printf(vals);
			System.out.println("Les vecteurs propres associées:");
			CalculMatriciel.printf(val_vect.vecteurs);
			//6.	Identifier les composantes principales
			System.out.println("Les composants principales:");
			CalculMatriciel.printf(data.composantsprincipalesextract(axes, val_vect));
			//7.	Projeter les données sur les nouveaux axes (composantes principales)
			double [][] x_PROJ=Acp.analysecomposantsprincipales(data.matrice, axes);
			System.out.println("La transformation ACP:");
			CalculMatriciel.printf(x_PROJ);
			//8.    Quantité d'information(pourcentage) en chaque axe:
			System.out.println("La quantité d'information en chaque axe retenue: ");
			for(int i=0;i<axes;i++) {
			System.out.println("Axe "+(i+1)+":  "+data.quantite_dinfo(val_vect.valeurs)[i]+"   %");
			}
		}
	}
}
