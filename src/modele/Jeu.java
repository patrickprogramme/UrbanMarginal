package modele;

import controleur.Controle;
import outils.connexion.Connection;

/**
 * Classe abstraite définissant les méthodes communes pour les jeux client et serveur.
 * Elle assure la gestion des connexions et de la communication entre les différentes instances du jeu.
 */
public abstract class Jeu {

	/**
	 * Instance du contrôleur permettant l'envoi d'informations et la gestion des événements du jeu.
	 */
	protected Controle controle;
	/**
	 * Établit la connexion avec un ordinateur distant.
	 *
	 * @param connection Connexion permettant les échanges entre le client et le serveur.
	 */
	public abstract void connexion(Connection connection) ;
	
	/**
	 * Réception d'une information provenant de l'ordinateur distant
	 * @param connection Connexion active avec l'ordinateur distant.
	 * @param info Donnée transmise par l'ordinateur distant.

	 */
	public abstract void reception(Connection connection, Object info) ;
	
    /**
     * Déconnecte l'ordinateur distant en utilisant la connexion spécifiée.
     *
     * @param connection La connexion à fermer pour déconnecter l'ordinateur distant.
     */
    public abstract void deconnexion(Connection connection);
	
	/**
	 * Envoi d'une information vers un ordinateur distant
	 * @param connection objet de connexion pour accéder à l'ordinateur distant
	 * @param info information à envoyer
	 */
	public void envoi(Connection connection, Object info) {
		this.controle.envoi(connection, info);
	}
	
}
