## BUGS
- [ ] Sous Linux, l'export d'une image fonctionne, mais bloque le logiciel (le logiciel freeze et devient grisé et non répondant).

 - [ ] Quand on configure A*T, mais pas A*A, et que l'on demande l'affichage (bouton `Simuler`) de A*T et A*A, le logiciel plante. Quand on ne définit pas AxA mais qu'on demande l'affichage de A*A, cela ne fait juste rien (comportement attendu)
	- NB : A*T et A*E ont bien le comportement attendu aussi dans ce cas : pas de réglage, pas de graphique, mais ça ne plante pas.
 
 - [ ] En A*A, l'opacité permet de représenter le temps. Elle est définie comme minimale au pas de temps 0, et maximale au pas de temps maximum. Un problème se pose quand on n'affiche (via un filtre) qu'une seule date : le point est quasiment invisible. Il vaudrait donc mieux définir l'opacité de manière décroissante, avec le point le plus "récent" en opacité = 1.

## FEATURES

- [ ] Filtrage : On devrait pouvoir choisir le "dernier pas de temps", qui peut varier d'une expérience/réplication à l'autre (pas dans le jeu de données utilisé aussi). Donc par exemple ajouter une option "max" (et "min" tant qu'à faire) qui irait chercher le max/min pour chaque combinaison ID_experience/ID_replication.

- [ ] En A*A :
	- [ ] Quand on définit un filtre, le slider ne fonctionne plus : ni en mode "courbe unique", ni en mode "superposition".

    - [ ] De plus, quand on définit un filtre, il n'est plus possible d'ajouter de point sur le graphique courant (avec le bouton `Ajouter une courbe` (N.B. : sur Linux, le bouton se grise, mais pas sous Windows).
        - N.B : Quand on est en A*A "bonus", c'est à dire un A*Experimentations ou A*Replications, ces boutons (avec filtre) fonctionnent correctement.


## GUI FIX

- [ ] Renommer le bouton `Simuler` (en haut à droite) en Afficher

- [ ] Dans la BDD, renommer les champs :
    - [ ] num_step en "Temps"
    - [ ] id_Replication en "ID_replication"

- [ ] Dans la configuration A*A, la première variable (en haut) définit l'ordonnée (Y), et la seconde (en bas) l’abscisse (X). Il serait plus intuitif d'inverser cela.

- [ ] Le crédit (en bas à gauche) "CNRS Géocité" devrait être "UMR Géographie-cités"

- [ ] Le logo VisuAgent en haut à droite apparaît déformé, et peu lisible, sans doute à reprendre à la taille fixée voulue.