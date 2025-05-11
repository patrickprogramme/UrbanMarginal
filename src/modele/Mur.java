package modele;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controleur.Global;

/**
 * Gestion des murs
 *
 */
public class Mur extends Objet implements Global {

	/**
	 * Constructeur
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
