# 🎮 ICoop - Jeu d'Aventure 2D en Java

![Status](https://img.shields.io/badge/Status-En_cours_de_développement-orange?style=for-the-badge)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

> 🚧 **Note : Ce projet est actuellement en cours de développement.** Le jeu est jouable, mais de nouvelles zones, fonctionnalités et corrections sont ajoutées régulièrement.

**ICoop** est un jeu vidéo 2D d'aventure et d'exploration développé entièrement en Java. Ce projet repose sur une architecture modulaire séparant le moteur de jeu personnalisé et la logique spécifique de l'aventure, offrant ainsi des mécaniques de combat, de résolution d'énigmes et d'exploration de donjons.

## 🚀 Fonctionnalités actuelles

* **Exploration multi-zones :** Voyagez à travers différentes cartes interactives telles que *Spawn*, *Maze* (Labyrinthe), *Arena* et *OrbWay*.
* **Système de Combat & Magie :** Affrontez une variété d'ennemis (DarkLord, BombMonster, LogMonster) en utilisant diverses armes (épée, arc) et de la magie élémentaire (bâtons d'eau et de feu).
* **Interactions et Énigmes :** Déverrouillez des portes avec des clés spécifiques, collectez des ressources (pièces, diamants), et interagissez avec l'environnement (leviers, rochers, explosifs).
* **Interface et Inventaire :** Suivez vos points de vie (HP), votre expérience (XP) et gérez les objets récoltés tout au long de votre quête.
* **Dialogues interactifs :** Discutez avec des PNJ pour faire avancer le scénario ou obtenir des indices.

## 📁 Architecture du Projet

Le dépôt est divisé en trois modules principaux gérés par Maven :

1.  **`game-engine`** : Le moteur physique et graphique du jeu. Il prend en charge le rendu visuel, le moteur physique (collisions, hitboxes), les mathématiques vectorielles et les entrées utilisateur.
2.  **`iccoop`** : Le cœur du jeu. Ce dossier contient toute la logique de l'aventure ICoop (comportements des acteurs, génération des zones, scénario, et IA des monstres).
3.  **`tutos`** : Un ensemble de mini-jeux permettant d'introduire les fonctionnalités du moteur de jeu.

## 🛠️ Prérequis et Installation

* **Java Development Kit (JDK)** (version 11 ou supérieure)
* **Maven** (pour la gestion des dépendances et le build)

**Étapes d'installation :**

1. Clonez ce dépôt sur votre machine locale :
   ```bash
   git clone [https://github.com/charaf-ech/icoop.git](https://github.com/charaf-ech/icoop.git)
   cd icoop/ICoop-charaf_branche
   ```
2. Compilez le projet à l'aide de Maven :
   ```bash
   mvn clean install
   ```

## ▶️ Comment lancer le jeu

Pour démarrer l'aventure principale, exécutez la méthode `main()` de la classe principale située dans le module `iccoop` depuis votre IDE (IntelliJ, Eclipse, etc.) :
* **Classe principale :** `ch.epfl.cs107.Play`

## ⌨️ Commandes du jeu

*(N'hésitez pas à modifier cette section si vos touches sont différentes)*
* **Flèches directionnelles** : Se déplacer
* **Espace** : Interagir (parler à un PNJ, lire un panneau)
* **Touche X** : Attaquer / Utiliser l'objet équipé
* **Touche I** : Ouvrir/Fermer l'inventaire
* **Échap** : Mettre le jeu en pause

## 🚧 À venir (To-Do)
* [ ] Ajout de nouveaux niveaux et boss.
* [ ] Amélioration de l'intelligence artificielle des ennemis.
* [ ] Ajout d'effets sonores et d'une musique d'ambiance.
* [ ] *Ajoutez ici vos futures idées de développement...*


## ✍️ Équipe de développement
* **Echchorfi Charaf**
* **EL Haddad Sajid**
