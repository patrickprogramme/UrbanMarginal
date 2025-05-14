package modele;

import java.awt.Font;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import controleur.Global;

/**
 * Représente un joueur dans le jeu. 
 * Cette classe gère l'affichage, la position, la vie, les interactions et la communication avec le serveur de jeu.
 */

public class Joueur extends Objet implements Global {

	/**
	 * pseudo saisi
	 */
	private String pseudo ;
	/**
	 * n° correspondant au personnage (avatar) pour le fichier correspondant
	 */
	private int numPerso ; 
	/**
	 * instance de JeuServeur pour communiquer avec lui
	 */
	private JeuServeur jeuServeur ;
	/**
	 * numéro d'�tape dans l'animation (de la marche, touché ou mort)
	 */
	private int etape ;
	/**
	 * la boule du joueur
	 */
	private Boule boule ;
	/**
	* vie restante du joueur
	*/
	private int vie ; 
	/**
	* tourné vers la gauche (0) ou vers la droite (1)
	*/
	private int orientation ;
	/**
	 * message sous le pseudo
	 */
	private JLabel message;
	/**
	 * Retourne le pseudo du joueur.
	 * @return the pseudo
	 */
	public String getPseudo() {
		return pseudo;
	}

	/**
	 * Initialise un joueur avec une référence au serveur du jeu.
	 *
	 * @param jeuServeur Instance du jeu serveur permettant la gestion du joueur.
	 */
	public Joueur(JeuServeur jeuServeur) {
		this.jeuServeur = jeuServeur;
		this.vie = MAXVIE;
		this.etape = 1;
		this.orientation = DROITE;
	}

	/**
	 * Initialise un joueur avec son pseudo et son numéro de personnage. 
	 * Détermine sa première position sans chevaucher d'autres joueurs ou murs, 
	 * puis ajoute son affichage et son message dans l'arène du jeu.
	 *
	 * @param pseudo Nom du joueur.
	 * @param numPerso Numéro du personnage pour définir l'avatar.
	 * @param lesJoueurs Liste des joueurs existants pour éviter les chevauchements.
	 * @param lesMurs Liste des murs dans l'arène pour gérer les collisions.
	 */
	public void initPerso(String pseudo, int numPerso, Collection<Joueur> lesJoueurs, ArrayList<Mur> lesMurs) {
		this.pseudo = pseudo;
		this.numPerso = numPerso;
		System.out.println("joueur "+pseudo+" - num perso "+numPerso+" créé");
		super.jLabel = new JLabel();
		
		this.message = new JLabel();
		message.setHorizontalAlignment(SwingConstants.CENTER);
		message.setFont(new Font("Dialog", Font.PLAIN, 8));
		
		this.premierePosition(lesJoueurs, lesMurs);
		this.jeuServeur.ajoutJLabelJeuArene(jLabel);
		this.jeuServeur.ajoutJLabelJeuArene(message);
		this.affiche(MARCHE, this.etape);
	}

	/**
	 * Détermine la position initiale aléatoire du joueur dans l'arène.
	 * S'assure que la position générée ne chevauche pas un autre joueur ou un mur.
	 *
	 * @param lesJoueurs Liste des joueurs déjà présents dans l'arène.
	 * @param lesMurs Liste des murs pour éviter les collisions.
	 */
	private void premierePosition(Collection<Joueur> lesJoueurs, ArrayList<Mur> lesMurs) {
		jLabel.setBounds(0, 0, LARGEURPERSO, HAUTEURPERSO);
		do {
			posX = (int) Math.round(Math.random() * (LARGEURARENE - LARGEURPERSO));
			posY = (int) Math.round(Math.random() * (HAUTEURARENE - HAUTEURPERSO - HAUTEURMESSAGE));
		} while (this.toucheJoueur(lesJoueurs) || this.toucheMur(lesMurs));
	}
	
	/**
	 * Met à jour l'affichage du personnage et son message sous le pseudo.
	 * 
	 * @param etat État actuel du joueur (ex: "MARCHE", "TOUCHE", "MORT").
	 * @param etape Étape de l'animation pour afficher la bonne image du personnage.
	 */
	public void affiche(String etat, int etape) {
		super.jLabel.setBounds(posX, posY, LARGEURPERSO, HAUTEURPERSO);
		String chemin = CHEMINPERSONNAGES+PERSO+this.numPerso+etat+etape+"d"+this.orientation+EXTFICHIERPERSO;
		URL resource = getClass().getClassLoader().getResource(chemin);
		super.jLabel.setIcon(new ImageIcon(resource));
		
		this.message.setBounds(posX-10, this.posY+HAUTEURPERSO, LARGEURPERSO+10, HAUTEURMESSAGE);
		this.message.setText(pseudo+": "+vie);
		
		this.jeuServeur.envoiJeuATous();
	}

	/**
	 * Gère une action reçue et qu'il faut afficher (déplacement, tire de boule...)
	 */
	public void action() {
	}

	/**
	 * Gère le déplacement du personnage
	 */
	private void deplace() { 
	}

	/**
	 * Contrôle si le joueur touche un des autres joueurs
	 * @param lesJoueurs Liste des joueurs présents.
	 * @return true si deux joueurs se touchent
	 */
	private Boolean toucheJoueur(Collection<Joueur> lesJoueurs) {
		for (Joueur joueur : lesJoueurs) {
			if (!this.equals(joueur)) {
				if (super.toucheObjet(joueur)) {
					return true; // Collision détectée avec un joueur
				}
			}
		}
		return false; // Aucun joueur touché
	}

	/**
	 * Gain de points de vie après avoir touché un joueur
	 */
	public void gainVie() {
	}
	
	/**
	 * Perte de points de vie après avoir été touché 
	 */
	public void perteVie() {
	}
	
	/**
	* Contrôle si le joueur touche un des murs
	* @param lesMurs Liste des murs présents.
	* @return true si un joueur touche un mur
	*/
	private Boolean toucheMur(ArrayList<Mur> lesMurs) {
	    for (Mur mur : lesMurs) {
	        if (super.toucheObjet(mur)) {
	            return true; // Collision détectée avec un mur
	        }
	    }
	    return false; // Aucun mur touché
	}

	
	/**
	 * vrai si la vie est à 0
	 * @return true si vie = 0
	 */
	public Boolean estMort() {
		return null;
	}
	
	/**
	 * Le joueur se déconnecte et disparait
	 */
	public void departJoueur() {
	}
	
}
