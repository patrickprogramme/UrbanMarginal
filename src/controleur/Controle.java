package controleur;
import java.awt.EventQueue;

import vue.ChoixJoueur;
import vue.EntreeJeu;
import vue.Arene;

import outils.connexion.AsyncResponse;
import outils.connexion.Connection;
import outils.connexion.ServeurSocket;
import outils.connexion.ClientSocket;

public class Controle implements AsyncResponse {
	
	private static final int PORT = 6666;
	private EntreeJeu frameEntreeJeu;
	private Arene frameArene;
	private ChoixJoueur frameChoixJoueur;
	private String typeJeu; 
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Controle();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private Controle() {
		this.frameEntreeJeu = new EntreeJeu(this);
		this.frameEntreeJeu.setVisible(true);
	}
	
	public void evenementEntreeJeu(String info) {
		if (info.equals("serveur")) {
			typeJeu = "serveur";
			new ServeurSocket(this, PORT);
			frameEntreeJeu.dispose();
			(new Arene()).setVisible(true);			
		}
		else {
			typeJeu = "client";
			new ClientSocket(this, info, PORT);
		}
	}
	
	public void evenementChoixJoueur(String pseudo, int numPerso) {
		(new Arene()).setVisible(true);
		frameChoixJoueur.dispose();
	}

	@Override
	public void reception(Connection connection, String ordre, Object info) {
		switch (ordre) {
		case "connexion":
			if (typeJeu.equals("client")) {
				frameEntreeJeu.dispose();
				frameArene = new Arene();
				frameChoixJoueur = new ChoixJoueur(this);
				frameChoixJoueur.setVisible(true);
			}
			break;
		case "reception":
			break;
		case "deconnexion":
			break;
		}
	}
}
