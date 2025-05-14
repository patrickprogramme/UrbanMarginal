package modele;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controleur.Global;

/**
 * Classe représentant un mur dans l'arène du jeu.
 * Les murs servent d'obstacles pour limiter les déplacements des joueurs et des objets.
 */
public class Mur extends Objet implements Global {

	/**
	 * Constructeur de la classe Mur.
	 * Génère une position aléatoire dans l'arène sans dépasser les dimensions autorisées.
	 * Initialise l'affichage graphique du mur.
	 */
	public Mur() {
		// Utilisation de Math.round() pour un arrondi classique au lieu d'une simple troncature
		posX = (int) Math.round(Math.random() * (LARGEURARENE - LARGEURMUR));
		posY = (int) Math.round(Math.random() * (HAUTEURARENE - HAUTEURMUR));
		jLabel = new JLabel();
		URL resource = getClass().getClassLoader().getResource(MUR);
		jLabel.setIcon(new ImageIcon(resource));
		jLabel.setBounds(posX, posY, LARGEURMUR, HAUTEURMUR);
	}
	
}
