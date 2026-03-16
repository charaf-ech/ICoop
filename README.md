# 🎮 ICoop - 2D Adventure Game in Java

![Status](https://img.shields.io/badge/Status-Work_in_Progress-orange?style=for-the-badge)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

> 🚧 **Note: This project is currently under development.** The game is playable, but new zones, features, and bug fixes are being added regularly.

**ICoop** is a 2D adventure and exploration video game developed entirely in Java. This project is built on a modular architecture that separates a custom game engine from the specific game logic, offering combat mechanics, puzzle-solving, and dungeon exploration.

## 🚀 Current Features

* **Multi-zone Exploration:** Travel through various interactive maps such as *Spawn*, *Maze*, *Arena*, and *OrbWay*.
* **Combat & Magic System:** Fight various enemies (DarkLord, BombMonster, LogMonster) using different weapons (sword, bow) and elemental magic (fire and water staffs).
* **Interactions & Puzzles:** Unlock doors with specific keys, collect resources (coins, diamonds), and interact with the environment (levers, rocks, explosives).
* **Interface & Inventory:** Track your Hit Points (HP) and Experience Points (XP), and manage items collected throughout your quest.
* **Interactive Dialogues:** Talk to NPCs to progress the story or obtain clues.

## 📁 Project Architecture

The repository is divided into three main modules managed by Maven:

1.  **`game-engine`**: The game's physical and graphical engine. It handles visual rendering, physics (collisions, hitboxes), vector mathematics, and user input.
2.  **`iccoop`**: The core of the game. This folder contains all the specific ICoop adventure logic (actor behaviors, zone generation, storyline, and enemy AI).
3.  **`tutos`**: A set of mini-games and tutorials to gradually introduce the features of the game engine.

## 🛠️ Prerequisites & Installation

* **Java Development Kit (JDK)** (version 11 or higher)
* **Maven** (for dependency management and building)

**Installation Steps:**

1. Clone this repository to your local machine:
   ```bash
   git clone [https://github.com/charaf-ech/icoop.git](https://github.com/charaf-ech/icoop.git)
   cd icoop/ICoop-charaf_branche
   ```
2. Build the project using Maven:
   ```bash
   mvn clean install
   ```

## ▶️ How to Run the Game

To start the main adventure, run the `main()` method of the primary class located in the `iccoop` module from your IDE (IntelliJ, Eclipse, etc.):
* **Main Class:** `ch.epfl.cs107.Play`

## ⌨️ Game Controls

*(Feel free to modify this section if your key bindings are different)*
* **Arrow Keys**: Move
* **Spacebar**: Interact (talk to NPC, read sign)
* **X Key**: Attack / Use equipped item
* **I Key**: Open/Close Inventory
* **Esc**: Pause the game

## 🚧 Upcoming Features (To-Do)
* [ ] Addition of new levels and bosses.
* [ ] Improvement of enemy artificial intelligence.
* [ ] Addition of sound effects and ambient music.


## ✍️ Development Team
* **Echchorfi Charaf**
* **EL Haddad Sajid**
