package modele;

import javax.swing.JPanel;

import controleur.Controle;
import outils.connexion.Connection;
import controleur.Global;

/**
 * Classe représentant le jeu côté client.
 * Elle gère la connexion avec le serveur, la réception et l'envoi d'informations, ainsi que l'affichage du jeu.
 */
public class JeuClient extends Jeu implements Global {
	
	/**
	 * objet de connexion pour communiquer avec le serveur
	 */
	private Connection connection;

	/**
	 * si les murs sont déjà ajoutés ou pas
	 */
	private Boolean mursOk = false;
	/**
	 * Controleur
	 * @param controle: instance du contrôleur pour les échanges
	 */
	public JeuClient(Controle controle) {
		super.controle = controle;
	}
	/**
	 * Établit la connexion avec le serveur.
	 *
	 * @param connection Connexion permettant les échanges entre le client et le serveur.
	 */
	@Override
	public void connexion(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Traite les informations reçues du serveur.
	 * 
	 * - Si l'information est un `JPanel`, elle met à jour l'affichage du jeu ou ajoute les murs si nécessaire.
	 * - Si l'information est une `String`, elle met à jour le chat du jeu.
	 *
	 * @param connection Connexion avec le serveur.
	 * @param info Informations reçues (peut être un `JPanel` ou une `String`).
	 */
	@Override
	public void reception(Connection connection, Object info) {
		if (info instanceof JPanel) {
			if (!mursOk) {
				this.controle.evenementJeuClient(AJOUTPANELMURS, info);
				mursOk = true;
			}
			else {
				this.controle.evenementJeuClient(MODIFPANELJEU, info);
			}
		}
		if (info instanceof String) {
			this.controle.evenementJeuClient(MODIFTCHAT, info);
		}
	}
	/**
	 * Déconnecte le client du serveur et libère les ressources associées.
	 */
	@Override
	public void deconnexion() {
	}

	/**
	 * Envoie une information au serveur via la connexion établie.
	 * Utilise la méthode d'envoi définie dans la classe parent `Jeu`.
	 *
	 * @param info Information à envoyer au serveur.
	 */
	public void envoi(String info) {
		super.envoi(this.connection, info);
	}

}
