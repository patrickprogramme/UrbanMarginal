package modele;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import javax.swing.JLabel;

import controleur.Controle;
import controleur.Global;
import outils.connexion.Connection;

/**
 * Classe gérant le jeu côté serveur.
 * Elle prend en charge la gestion des joueurs, l'envoi et la réception des informations,
 * ainsi que la génération des éléments de jeu comme les murs.
 */
public class JeuServeur extends Jeu implements Global {

	/**
	 * Collection de murs
	 */
	private ArrayList<Mur> lesMurs = new ArrayList<Mur>() ;
	/**
	 * Hashtable associant chaque connexion à un joueur.
	 * Permet de gérer les interactions des joueurs connectés au serveur.
	 */

	private Hashtable<Connection, Joueur> lesJoueurs = new Hashtable<Connection, Joueur>() ;
	/**
	 * @return the lesJoueurs
	 */
	public Collection getLesJoueurs() {
		return lesJoueurs.values();
	}
	/**
	 * Constructeur de la classe JeuServeur.
	 * Initialise le contrôleur du jeu pour gérer les événements et la communication avec les clients.
	 *
	 * @param controle Instance du contrôleur pour gérer les interactions serveur-client.
	 */
	public JeuServeur(Controle controle) {
		super.controle = controle;
	}
	/**
	 * Ajoute un nouveau joueur au jeu à partir de la connexion reçue.
	 * 
	 * @param connection Connexion représentant le joueur à ajouter.
	 */
	@Override
	public void connexion(Connection connection) {
		this.lesJoueurs.put(connection, new Joueur(this));
	}

	/**
	 * Traite une information reçue d'un client.
	 * 
	 * - Si l'ordre reçu est `PSEUDO`, le serveur initialise le joueur et affiche un message de bienvenue.
	 * - Si l'ordre reçu est `TCHAT`, le serveur ajoute un message au chat du jeu.
	 *
	 * @param connection Connexion avec le client.
	 * @param info Information envoyée par le client sous forme de chaîne de caractères.
	 */
	@Override
	public void reception(Connection connection, Object info) {
		//info = info.toString();
		String[] infos = ((String)info).split(STRINGSEPARE);
		String ordre = infos[0];
		switch (ordre) {
		case PSEUDO:
			this.controle.evenementJeuServeur(AJOUTPANELMURS, connection);
			String pseudo = infos[1];
			int numPerso = Integer.parseInt(infos[2]);
			this.lesJoueurs.get(connection).initPerso(pseudo, numPerso, this.lesJoueurs.values(), this.lesMurs);
			String bienvenue = "*** " + pseudo +" vient de se connecter ***";
			this.controle.evenementJeuServeur(AJOUTPHRASE, bienvenue);
			break;
		case TCHAT:
			String auteur = this.lesJoueurs.get(connection).getPseudo();
			String message = auteur + " > " + infos[1];
			this.controle.evenementJeuServeur(AJOUTPHRASE, message);
			break;
		case ACTION:
			Integer action = Integer.parseInt(infos[1]);
			this.lesJoueurs.get(connection).action(action, this.lesJoueurs.values(), this.lesMurs);
		}
	}
	/**
	 * Déconnecte un joueur du serveur et supprime ses données du jeu.
	 * À implémenter pour gérer la suppression propre des joueurs.
	 */
	@Override
	public void deconnexion() {
	}

	/**
	 * Envoi d'une information vers tous les clients
	 * fais appel plusieurs fois à l'envoi de la classe Jeu
	 * @param info Information à transmettre aux joueurs.
	 */
	public void envoi(Object info) {
		for (Connection K : this.lesJoueurs.keySet()) {
			super.envoi(K, info);
		}
	}
	/**
	 * Ajoute un élément graphique (`JLabel`) à l'arène de jeu sur le serveur.
	 *
	 * @param jLabel Élément graphique à ajouter.
	 */
	public void ajoutJLabelJeuArene(JLabel jLabel) {
		this.controle.evenementJeuServeur(AJOUTJLABELJEU, jLabel);
	}
	/**
	 * Génère et ajoute les murs à l'arène du jeu.
	 * Chaque mur est instancié puis transmis à l'événement serveur.
	 */
	public void constructionMurs() {
		for (int i = 0; i < NBMURS; i++) {
			this.lesMurs.add(new Mur());
			this.controle.evenementJeuServeur(AJOUTMUR, lesMurs.get(lesMurs.size()-1).getjLabel());
		}
		System.out.println(lesMurs.size() + " murs dans lesMurs");
	}
	/**
	 * Met à jour l'affichage du jeu pour tous les joueurs connectés.
	 */
	public void envoiJeuATous() {
		for (Connection K : this.lesJoueurs.keySet()) {
			this.controle.evenementJeuServeur(MODIFPANELJEU, K);
		}
	}

}
