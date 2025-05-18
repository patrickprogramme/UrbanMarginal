package controleur;

import javax.swing.JLabel;
import javax.swing.JPanel;

import modele.Jeu;
import modele.JeuClient;
import modele.JeuServeur;

import vue.ChoixJoueur;
import vue.EntreeJeu;
import vue.Arene;

import outils.connexion.AsyncResponse;
import outils.connexion.Connection;
import outils.connexion.ServeurSocket;
import outils.connexion.ClientSocket;
/**
 * Contrôleur et point d'entrée de l'applicaton 
 *
 */
public class Controle implements AsyncResponse, Global {
	
	/**
	 * Frame représentant l'écran d'entrée du jeu
	 */
	private EntreeJeu frameEntreeJeu;

	/**
	 * Frame représentant l'arène de jeu
	 */
	private Arene frameArene;

	/**
	 * Frame permettant le choix du joueur
	 */
	private ChoixJoueur frameChoixJoueur;

	/**
	 * Instance du jeu (JeuServeur ou JeuClient)
	 */
	private Jeu leJeu;
	

	/**
	 * Méthode de démarrage
	 * @param args non utilisé
	 */
	public static void main(String[] args) {
		new Controle();
	}
	/**
	 * Constructeur privé de `Controle` pour empêcher l'instanciation externe.
	 */
	private Controle() {
		this.frameEntreeJeu = new EntreeJeu(this);
		this.frameEntreeJeu.setVisible(true);
	}
	/**
	 * Traite l'événement d'entrée dans le jeu (choix client/serveur).
	 *
	 * @param info Type de jeu sélectionné.
	 */
	public void evenementEntreeJeu(String info) {
		if (info.equals(SERVEUR)) {
			new ServeurSocket(this, PORT);
			leJeu = new JeuServeur(this);
			frameEntreeJeu.dispose();
			this.frameArene = new Arene(this, SERVEUR);
			((JeuServeur)this.leJeu).constructionMurs();
			this.frameArene.setVisible(true);		
		}
		else {
			new ClientSocket(this, info, PORT);
		}
	}
	/**
	 * Informations provenant de la vue ChoixJoueur
	 * @param pseudo le pseudo du joueur
	 * @param numPerso le numéro du personnage choisi par le joueur
	 */
	public void evenementChoixJoueur(String pseudo, int numPerso) {
		this.frameChoixJoueur.dispose();
		this.frameArene.setVisible(true);
		String info = PSEUDO+STRINGSEPARE+pseudo+STRINGSEPARE+ Integer.toString(numPerso);
		((JeuClient)leJeu).envoi(info);
	}
	/**
	 * Demande provenant de JeuServeur
	 * @param ordre ordre à exécuter
	 * @param info information à traiter
	 */
	public void evenementJeuServeur(String ordre, Object info) {
		switch (ordre) {
		case AJOUTMUR:
			this.frameArene.ajoutMurs(info);
			break;
		case AJOUTPANELMURS:
			this.leJeu.envoi((Connection)info, this.frameArene.getJpnMurs());
			break;
		case AJOUTJLABELJEU:
			this.frameArene.ajoutJLabelJeu((JLabel)info);
			break;
		case MODIFPANELJEU:
			this.leJeu.envoi((Connection)info, this.frameArene.getJpnJeu());
			break;
		case AJOUTPHRASE:
			this.frameArene.ajoutTchat((String)info);
			((JeuServeur)this.leJeu).envoi(this.frameArene.getTxtChat());
		}
	}
	/**
	 * Gère les événements côté client en fonction de l'ordre reçu.
	 * 
	 * - `AJOUTPANELMURS` : Ajoute le panneau des murs à l'arène.
	 * - `MODIFPANELJEU` : Met à jour le panneau du jeu avec de nouveaux éléments.
	 * - `MODIFTCHAT` : Modifie le texte du chat avec les nouvelles informations reçues.
	 * - `JOUESON` : Joue un son.
	 *
	 * @param ordre Type d'événement à traiter.
	 * @param info Informations associées à l'événement (peut être un `JPanel` ou une `String`).
	 */
	public void evenementJeuClient(String ordre, Object info) {
		switch(ordre) {
		case AJOUTPANELMURS:
			this.frameArene.setJpnMurs((JPanel)info);
			break;
		case MODIFPANELJEU:
			this.frameArene.setJpnJeu((JPanel)info);
			break;
		case MODIFTCHAT:
			this.frameArene.setTxtChat((String)info);
			break;
		case JOUESON:
			this.frameArene.joueSon((Integer)info);
			break;
		}
	}
	/**
	 * Gère un événement lié à l'arène (ex: mise à jour graphique).
	 *
	 * @param info Information sur l'événement.
	 */

	public void evenementArene(Object info) {
			if (info instanceof String) {
				String message = TCHAT + STRINGSEPARE + (String)info;
				((JeuClient) this.leJeu).envoi(message);
			}
			if (info instanceof Integer) {
				String message = ACTION + STRINGSEPARE + info;
				((JeuClient) this.leJeu).envoi(message);
			}
	}
	/**
	 * Envoie une information via la connexion active.
	 *
	 * @param connection Objet de connexion pour la communication.
	 * @param info Donnée envoyée.
	 */
	public void envoi(Connection connection, Object info) {
		connection.envoi(info);
	}
	@Override
	public void reception(Connection connection, String ordre, Object info) {
		switch (ordre) {
		case CONNEXION:
			if (!(leJeu instanceof JeuServeur)) {
				this.leJeu = new JeuClient(this);
				this.leJeu.connexion(connection);
				this.frameEntreeJeu.dispose();
				this.frameArene = new Arene(this, CLIENT);
				this.frameChoixJoueur = new ChoixJoueur(this);
				this.frameChoixJoueur.setVisible(true);
			}
			else {
				this.leJeu.connexion(connection);
			}
			break;
		case RECEPTION:
			this.leJeu.reception(connection, info);
			break;
		case DECONNEXION:
			System.out.println("Contrôle dit: déconnexion");
			this.leJeu.deconnexion(connection);
			break;
		}
	}
}
