# M1SocialNetwork

## Membres :

* WU Sébastien
* DUDEK Tomasz

## Utilisation

**Attention !**  Il faut utiliser une version 8 de java.

### Utilisation Windows :

* Tout les commandes sont a executer dans le répertoire `src`
* Compiler le projet en lançant la commande `javac *.java`
* Démarrer le RMIRegistry avec la commande `start rmiregistry.exe`
* Démarrer le serveur depuis un autre terminal avec la commande `java Server`
* Démarrer le client depuis un dernier terminal avec la commande `java Client`

### Utilisation Linux :

* Tout les commandes sont a executer dans le répertoire `src`
* Compiler le projet en lançant la commande `javac *.java`
* Démarrer le RMIRegistry avec la commande `rmiregistry`
* Démarrer le serveur depuis un autre terminal avec la commande `java Server`
* Démarrer le client depuis un dernier terminal avec la commande `java Client`

## Info utile

Lorsque le serveur est lancé, vous pouvez voir dans le terminal les lignes qui sont lues par le programme dans le fichier resesausocial.txt ainsi que les 3 meilleurs messages mis à jour en direct. 

Vous pouvez désactiver cet affichage en metant en commentaires les lignes 38 et 56 dans le fichier ReadFile.java.
