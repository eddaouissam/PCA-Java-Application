package acp;

public class CalculMatriciel {
	/**
	* Détermine les valeurs propres et les vecteurs propres d'une matrice en utilisant l'algorithme QR. Répétitions
	* jusqu'à ce qu'aucune valeur propre ne change de plus de 1/100000.
	* Matrice d'entrée d'entrée @param ; doit être carré
	*/
	static Propr_Val_Vect decompositionpropres(double[][] matr) {
		if(matr.length != matr[0].length) {
			throw new MatrixException("Veuillez saisir un matrice carrée.");
		}
		double[][] c = copier(matr);
		double[][] q = new double[c.length][c.length];
		for(int i = 0; i < q.length; i++) {
			q[i][i] = 1; //Matrice d'identité en debut
			}
		boolean d = false;
		while(!d) {
			double[][][] fact = CalculMatriciel.factorisationQR(c); //Fct à définir
			double[][] newMat = CalculMatriciel.multiplier(fact[1], fact[0]); //[A_k+1] := [R_k][Q_k]
			q = CalculMatriciel.multiplier(fact[0], q);
			//Arrêter la boucle si aucune valeur propre ne change de plus de 1/100000
			for(int i = 0; i < c.length; i++) {
				if(Math.abs(newMat[i][i] - c[i][i]) > 0.00001) {
					c = newMat;
					break;
				} else if(i == c.length - 1) { 
					d = true;
				}
			}
		}
		Propr_Val_Vect out = new Propr_Val_Vect();
		out.valeurs = CalculMatriciel.diagValeurs(c); //Les valeurs propres en diagonal
		out.vecteurs = q; //Les vecteurs propres sont les colonnes de q
		return out;
	}
	
	/**
	* Produit un tableau des entrées diagonales dans la matrice d'entrée.
	* Matrice d'entrée d'entrée @param
	* @return les entrées sur la diagonale d'entrée
	*/
	static double[] diagValeurs(double[][] matr) {
		double[] output = new double[matr.length];
		for(int i = 0; i<matr.length; i++) {
			output[i] = matr[i][i];
		}
		return output;
	}
	
	/**
	* Effectue une factorisation QR sur la matrice d'entrée.
	* Matrice d'entrée @param
	* @return {Q, R}, la factorisation QR de l'entrée.
	*/
	static double[][][] factorisationQR(double[][] matr) {
		double[][][] output = new double[2][][];
		double[][] ortho = gram_Schmidt_process(matr);
		output[0] = ortho;
		double[][] r = new double[ortho.length][ortho.length];
		for(int i = 0; i < r.length; i++) {
			for(int j = 0; j <= i; j++) {
				r[i][j] = prod_scal(matr[i], ortho[j]);
			}
		}
		output[1] = r;
		return output;
	}
	
	/**
* Convertit la liste d'entrée de vecteurs en une liste orthonormée avec la même étendue.
* @param entrée liste de vecteurs
* @return liste orthonormée avec le même span en entrée
	 */
	static double[][] gram_Schmidt_process(double[][] input) {
		double[][] output = new double[input.length][input[0].length];
		for(int i = 0; i < output.length; i++) {
			double[] v = input[i];
			for(int j = i - 1; j >= 0; j--) {
				double[] sub = projection(v, output[j]);
				v = soustraire(v, sub); //Soustraire les composants non orthonormées
			}
			output[i] = vect_norm(v); //retourner la liste orthonormée
		}
		return output;
	}
	
	/**
	* Renvoie la transposition de la matrice d'entrée.
	* @param matrice double[][] matrice de valeurs
	* @return la matrice transposée de la matrice
	*/
	static double[][] transposer(double[][] matrix) {
		double[][] output = new double[matrix[0].length][matrix.length];
		for(int i = 0; i < output.length; i++) {
			for(int j = 0; j < output[0].length; j++) {
				output[i][j] = matrix[j][i];
			}
		}
		return output;
	}
	
	/**
	* Renvoie la différence entre a et b.
	* @param a un vecteur double[] de valeurs
	* @param b double[] vecteur de valeurs
	* @retourne la différence vectorielle a - b
	*/
	static double[] soustraire(double[] a, double[] b) {
		if(a.length != b.length) {
			throw new MatrixException("Vecteurs de taille différente.");
		}
		double[] output = new double[a.length];
		for(int i = 0; i < output.length; i++) {
			output[i] = a[i] - b[i];
		}
		return output;
	}
	
	/**
	* Renvoie le produit matriciel de a et b ; si la longueur horizontale de a n'est pas égale à la
	* longueur verticale de b, lève une exception.
	* @param une matrice double[][] de valeurs
	* @param b double[][] matrice de valeurs
	* @return le produit matriciel ab
	*/
	static double[][] multiplier(double[][] a, double[][] b) {
		if(a.length != b[0].length) {
			throw new MatrixException("Matrices not compatible for multiplication.");
		}
		double[][] output = new double[b.length][a[0].length];
		for(int i = 0; i < output.length; i++) {
			for(int j = 0; j < output[0].length; j++) {
				double[] ligne = getLigne(a, j);
				double[] colonne = getColonne(b, i);
				output[i][j] = prod_scal(ligne, colonne);
			}
		}
		return output;
	}
	

	
	/**
	* Prend le produit scalaire de deux vecteurs, {a[0]b[0], ..., a[n]b[n]}.
	* @param un double[] de valeurs
	* @param b double[] de valeurs
	* @return le produit scalaire de a avec b
	*/
	static double prod_scal(double[] a, double[] b) {
		if(a.length != b.length) {
			throw new MatrixException("Longuers de vecteurs différentes: " + a.length + "=/=" + b.length);
		}
		double sum = 0;
		for(int i = 0; i < a.length; i++) {
			sum += a[i] * b[i];
		}
		return sum;
	}
	
	/**
	* Renvoie une copie de la matrice d'entrée.
	* @param en double[][] à copier
	*/
	public static double[][] copier(double[][] in) {
		double[][] copie = new double[in.length][in[0].length];
		for(int i = 0; i < copie.length; i++) {
			for(int j = 0; j < copie[i].length; j++) {
				copie[i][j] = in[i][j];
			}
		}
		return copie;
	}
	
	/**
	 * Retourner la ième colonne
	 */
	static double[] getColonne(double[][] matrix, int i) {
		return matrix[i];
	}
	
	/**
	 * Retourner la ième ligne
	 */
	static double[] getLigne(double[][] matrix, int i) {
		double[] vals = new double[matrix.length];
		for(int j = 0; j < vals.length; j++) {
			vals[j] = matrix[j][i];
		}
		return vals;
	}
	
	/**
	* Renvoie la projection de vec sur le sous-espace couvert par proj
	* @param vec vecteur à projeter
	* @param proj vecteur couvrant le sous-espace cible
	* @return proj_proj(vec)
	*/
	static double[] projection(double[] vec, double[] proj) {
		double constant = prod_scal(proj, vec)/prod_scal(proj, proj);
		double[] projection = new double[vec.length];
		for(int i = 0; i < proj.length; i++) {
			projection[i] = proj[i]*constant;
		}
		return projection;
	}
	
	/**
	 * Calculer le vecteur normé
	 * @return	vecteur/||vecteur||
	 */
	static double[] vect_norm(double[] vecteur) {
		double[] newV = new double[vecteur.length];
		double norm = norme(vecteur);
		for(int i = 0; i < vecteur.length; i++) {
			newV[i] = vecteur[i]/norm;
		}
		return newV;
	}
	
	/**
	 * Calculer la norme d'un vecteur
	 * @return ||vecteur||
	 */
	static double norme(double[] vecteur) {
		return Math.sqrt(prod_scal(vecteur,vecteur));
	}
	
	/**
	 * Afficher la matrice matrix
	 */
	
	static void printf(double[][] matrix) {
		for(int j = 0; j < matrix[0].length; j++) {
			for(int i = 0; i < matrix.length; i++) {
				double formattedValue = Double.parseDouble(String.format("%.4g%n", matrix[i][j]));
				if(Math.abs(formattedValue) < 0.00001) { //Hide negligible values
					formattedValue = 0;
				}
				System.out.print(formattedValue + "\t");
			}
			System.out.print("\n");
		}
		System.out.println("");
	}
}

/**
 *Gestion d'erreur.
 */
@SuppressWarnings("serial")
class MatrixException extends RuntimeException {
	MatrixException(String string) {
		super(string);
	}
}
/**
 * Classe afin de stocker les valeur propres ainsi les vecteurs propres associées.
 */
class Propr_Val_Vect {
	double[] valeurs;
	double[][] vecteurs;
}