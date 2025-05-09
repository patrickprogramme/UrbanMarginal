package controleur;

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

public class Controle implements AsyncResponse, Global {
	
	private EntreeJeu frameEntreeJeu;
	private Arene frameArene;
	private ChoixJoueur frameChoixJoueur;
	private Jeu leJeu;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new Controle();
	}
	
	private Controle() {
		this.frameEntreeJeu = new EntreeJeu(this);
		this.frameEntreeJeu.setVisible(true);
	}
	
	public void evenementEntreeJeu(String info) {
		if (info.equals("serveur")) {
			new ServeurSocket(this, PORT);
			leJeu = new JeuServeur(this);
			frameEntreeJeu.dispose();
			this.frameArene = new Arene();
			this.frameArene.setVisible(true);
			//(new Arene()).setVisible(true);			
		}
		else {
			new ClientSocket(this, info, PORT);
		}
	}
	
	public void evenementChoixJoueur(String pseudo, int numPerso) {
		this.frameChoixJoueur.dispose();
		//(new Arene()).setVisible(true);
		this.frameArene.setVisible(true);
		String info = PSEUDO+STRINGSEPARE+pseudo+STRINGSEPARE+ Integer.toString(numPerso);
		((JeuClient)leJeu).envoi(info);
	}

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
				this.frameArene = new Arene();
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
			break;
		}
	}
}
