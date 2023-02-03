package acp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
;
/**
 * Transformation d'un dataset chargée à partir d'un fichier DATA.txt prédéfini
 */
public class Acp{
	double[][] matrice; //Attribut de la classe
	/**
	 * Constructeur de classe Data
	 * @param valeurs
	 */
	Acp(double[][] valeurs) {
		matrice = CalculMatriciel.copier(valeurs);
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	
	static double[][] analysecomposantsprincipales(double[][] in, int nombrecomposants) {
		Acp data = new Acp(in);
		data.centrer();
		Propr_Val_Vect propres = data.cov_corpropresget();
		double[][] vecteur = data.composantsprincipalesextract(nombrecomposants, propres);
		double[][] pc = CalculMatriciel.transposer(vecteur);
		double[][] inputTranspose = CalculMatriciel.transposer(in);
		return CalculMatriciel.transposer(CalculMatriciel.multiplier(pc, inputTranspose));
	}
	
	double[][] composantsprincipalesextract(int nombrecomposants, Propr_Val_Vect propres) {
		double[] valeurs = propres.valeurs;
		if(nombrecomposants > valeurs.length) {
			throw new MatrixException("Ne peut pas produire plus de composants principaux que ceux fournis.");
		}
		boolean[] choisis = new boolean[valeurs.length];
		double[][] vecteurs = propres.vecteurs;
		double[][] pc = new double[nombrecomposants][];
		for(int i = 0; i < pc.length; i++) {
			int max = 0;
			while(choisis[max]) {
				max++;
			}
			for(int j = 0; j < valeurs.length; j++) {
				if(Math.abs(valeurs[j]) > Math.abs(valeurs[max]) && !choisis[j]) {
					max = j;
				}
			}
			choisis[max] = true;
			pc[i] = vecteurs[max];
		}
		return pc;
	}
	
	/**
* Utilise l'algorithme QR pour déterminer les valeurs propres et les vecteurs propres de la covariance/coorelation
* matrice pour cet ensemble de données. L'itération continue jusqu'à ce qu'aucune valeur propre ne change de plus de
* 1/10000.
* @return retourne un EigenSet contenant les valeurs propres et les vecteurs propres de la matrice de covariance/coorelation
	 */
	Propr_Val_Vect cov_corpropresget() {
		double[][] data = matricecov_cor();
		return CalculMatriciel.decompositionpropres(data);
	}
	
	/**
	 * Construire la matrice de covariance ou coorelation du dataset
	 * @return	Retourner la matrice de covariance ou coorelation
	 */
	double[][] matricecov_cor() {
		double[][] cov_cor = new double[matrice.length][matrice.length];
		for(int i = 0; i < cov_cor.length; i++) {
			for(int j = 0; j < cov_cor.length; j++) {
				double[] dataA = matrice[i];
				double[] dataB = matrice[j];
				cov_cor[i][j] = cov_coor(dataA, dataB);
				}
		}
		return cov_cor;
	}
	
	/**
	* Renvoie la covariance ou la coorelation de deux vecteurs de données.
	* @param un double[] de données
	* @param b double[] de données
	* @return la covariance/coorelation de a et b, cov(a,b)
	*/
	static double cov_coor(double[] a, double[] b) {
		if(a.length != b.length) {
			throw new MatrixException("Vecteurs de taille différente.");
		}
		double div = a.length - 1;
		double somme = 0;
		for(int i = 0; i < a.length; i++) {
			somme += a[i] * b[i];
		}
		return somme/div;
	}
	double[] quantite_dinfo(double[] in) {
		double[] quantite=new double[matrice.length];
		double somme=0;
		for(int i=0;i<matrice.length;i++) {
			somme+=in[i];
		}
		for(int i=0;i<matrice.length;i++) {
			quantite[i]=(in[i]/somme)*100;
		}
		return quantite;
	}
	/**
	* Centre et réduire chaque colonne de la matrice de données sur sa moyenne.
	*/
	void centrer_reduire() {
		matrice = normaliser(matrice);
		for(int i=0;i<matrice.length;i++) {
			double std=ecart_type(matrice[i]);
			for(int j=0;j<matrice[i].length;j++) {
				matrice[i][j]=matrice[i][j]/std;
			}
		}
		
	}
	
	/**
	* Centre chaque colonne de la matrice de données sur sa moyenne.
	*/
	void centrer() {
		matrice = normaliser(matrice);
	}
	
	/**
	* Normalise la matrice d'entrée afin que chaque colonne soit centrée sur 0.
	*/
	double[][] normaliser(double[][] in) {
		double[][] output = new double[in.length][in[0].length];
		for(int i = 0; i < in.length; i++) {
			double moy = moyenne(in[i]);
			for(int j = 0; j < in[i].length; j++) {
				output[i][j] = in[i][j] - moy;
			}
		}
		return output;
	}
	/**
	* Calcule la moyenne d'un tableau de doubles.
	* @param entrées tableau d'entrée de doubles
	*/
	static double moyenne(double[] entrees) {
		double output = 0;
		for(double d: entrees) {
			output += d/entrees.length;
		}
		return output;
	}
	/**
	* Calcule l'écart type d'un tableau de doubles.
	* @param entrées tableau d'entrée de doubles
	*/
	static double ecart_type(double[] entrees) {
		double output = 0;
		for(double d: entrees) {
			output += Math.pow(d-moyenne(entrees),2)/entrees.length;
		}
		output=Math.sqrt(output);
		return output;
	}

	/**
	* Utilise le fichier donné pour construire une table à utiliser par l'application.
	* Tous les nombres doivent être délimités par des tabulations.
	* La deuxieme ligne des données doit être composée de deux nombres : le nombre de lignes R suivi du
	* nombre de colonnes C. A partir de la quatrieme ligne, il devrait y avoir R lignes de valeurs C délimitées par des tabulations.
	* @param fichier le nom du fichier contenant les données
	* @return un double[][] contenant les données du nom de fichier
	* @throws IOException si une erreur se produit lors de la lecture du fichier
	*/
static double[][] uploadData(String fichier) throws IOException{
	BufferedReader in = null;
	try {
		in = new BufferedReader(new FileReader(new File(fichier)));
	} catch(FileNotFoundException e) {
		System.err.println("Fichier " + fichier + " non trouvé.");
	}
	in.readLine();
	
	
	String secondLine = in.readLine();
	String[] dims = secondLine.split(",");
	double[][] data = new double[Integer.parseInt(dims[1])][Integer.parseInt(dims[0])];
	in.readLine();
	for(int j = 0; j < data[0].length; j++) {
		String text = in.readLine();
		String[] vals = text.split(" ");
		for(int i = 0; i < data.length; i++)  {
			data[i][j] = Double.parseDouble(vals[i]);
		}
	}
	try {
		in.close();
	} catch(IOException e) {
		System.err.println(e);
	}
	return data;
}
static double[][] uploadDataFx(File fichier) throws IOException{
	BufferedReader in = null;
	try {
		in = new BufferedReader(new FileReader(fichier));
	} catch(FileNotFoundException e) {
		System.err.println("Fichier " + fichier + " non trouvé.");
	}
	in.readLine();
	String secondLine = in.readLine();
	String[] dims = secondLine.split(",");
	double[][] data = new double[Integer.parseInt(dims[1])][Integer.parseInt(dims[0])];
	in.readLine();
	for(int j = 0; j < data[0].length; j++) {
		String text = in.readLine();
		String[] vals = text.split(" ");
		for(int i = 0; i < data.length; i++)  {
			data[i][j] = Double.parseDouble(vals[i]);
		}
	}
	try {
		in.close();
	} catch(IOException e) {
		System.err.println(e);
	}
	return data;
}
}