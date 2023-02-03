# PCA-Java-Application

============================


Projet d'exploration d'ACP à travers une implémentation Java .  Les données sont stockées sous forme de tableaux doubles à deux dimensions.  Les objets de données stockent des tableaux bidimensionnels de doubles.  Chaque objet de données peut se normaliser autour de la moyenne de chaque variable, calculer sa matrice de covariance et déterminer ses valeurs propres et ses vecteurs propres.  La détermination des valeurs propres et des vecteurs propres est effectuée par l'algorithme QR sur la matrice de covariance des données.  La décomposition QR d'une matrice A est A = QR, où Q est une matrice orthonormée et R est une matrice triangulaire supérieure.  L'algorithme QR effectue la décomposition A = QR, définit A' comme A' = RQ, et répète le processus sur A' comme souhaité.  Les valeurs sur la diagonale de A' convergent alors vers les valeurs propres de A. Dans le cas des matrices symétriques, les colonnes du produit des matrices Q forment un ensemble de vecteurs propres orthonormés de A correspondant à ces valeurs propres.  Les matrices de covariance étant symétriques, l'algorithme QR est idéal pour cette analyse.  
Dans cette implémentation, l'algorithme QR itère jusqu'à ce qu'aucune des valeurs propres ne change de plus de 1/100 000 entre les itérations.

## Table of Contents
-   [Getting Started](#getting-started)
-   [Prerequisites](#prerequisites)
-   [Usage](#usage)
-   [Authors](#authors)
-   [License](#license)
## Getting Started

Le fichier [Application JAVA d’analyse de données en utilisant_ACP .pdf](https://github.com/eddaouissam/PCA-Java-Application/blob/main/Application%20JAVA%20d’analyse%20de%20données%20en%20utilisant_ACP%20.pdf) donne une présentation générale du projet et ses cas d'utilisation.


### Prerequisites

Logiciels et outils nécessaires :

- Eclipse IDE for Java Developpers
- JavaFX Library

## Usage

1.  Interface console d'ACP.
2.  Interface graphique de visualisation d'ACP.

## Authors

-   [eddaouissam](https://github.com/eddaouissam)

## License

This project is licensed under the [MIT License](https://github.com/eddaouissam/GuestBook-Website/blob/main/LICENSE).
