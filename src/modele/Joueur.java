package modele;

import java.awt.Font;
import java.awt.event.KeyEvent;
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
	 * le personnage est-il tourné vers la droite ou la gauche ?
	 * @return orientation : 0 pour gauche, 1 pour droite.
	 */
	public int getOrientation() {
		return orientation;
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
	public void initPerso(String pseudo, int numPerso, Collection lesJoueurs, Collection lesMurs) {
		this.pseudo = pseudo;
		this.numPerso = numPerso;
		System.out.println("joueur "+pseudo+" - num perso "+numPerso+" créé");
		super.jLabel = new JLabel();
		
		this.message = new JLabel();
		message.setHorizontalAlignment(SwingConstants.CENTER);
		message.setFont(new Font("Dialog", Font.PLAIN, 8));
		
		this.boule = new Boule(this.jeuServeur);
		
		this.premierePosition(lesJoueurs, lesMurs);
		this.jeuServeur.ajoutJLabelJeuArene(jLabel);
		this.jeuServeur.ajoutJLabelJeuArene(message);
		this.jeuServeur.ajoutJLabelJeuArene(boule.getjLabel());
		
		this.affiche(MARCHE, this.etape);
	}

	/**
	 * Détermine la position initiale aléatoire du joueur dans l'arène.
	 * S'assure que la position générée ne chevauche pas un autre joueur ou un mur.
	 *
	 * @param lesJoueurs Liste des joueurs déjà présents dans l'arène.
	 * @param lesMurs Liste des murs pour éviter les collisions.
	 */
	private void premierePosition(Collection lesJoueurs, Collection lesMurs) {
		jLabel.setBounds(0, 0, LARGEURPERSO, HAUTEURPERSO);
		do {
			posX = (int) Math.round(Math.random() * (LARGEURARENE - LARGEURPERSO));
			posY = (int) Math.round(Math.random() * (HAUTEURARENE - HAUTEURPERSO - HAUTEURMESSAGE));
		} while(toucheCollectionObjetsAvecStream(lesJoueurs)!=null || toucheCollectionObjetsAvecStream(lesMurs)!=null);
	}
	
	/**
	 * Met à jour l'affichage du personnage et son message sous le pseudo pour tous les joueurs.
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
	 * Traite une action du joueur en fonction de la touche reçue.
	 * Effectue le déplacement si l'action correspond à une direction,
	 * met à jour l'orientation du personnage et actualise son affichage.
	 * 
	 * @param action Code de la touche pressée, représentant l'action à exécuter.
	 * @param lesJoueurs Collection des joueurs présents dans l'arène, utilisée pour vérifier les collisions.
	 * @param lesMurs Liste des murs présents dans l'arène, utilisée pour éviter les déplacements invalides.
	 */
	public void action(Integer action, Collection lesJoueurs, Collection lesMurs) {
		if (this.estMort()) {
			return;
		}
		switch (action) {
        case KeyEvent.VK_LEFT:
        	deplace(-PAS, 0, lesJoueurs, lesMurs);
            this.orientation = GAUCHE;
            break;
        case KeyEvent.VK_RIGHT:
        	deplace(PAS, 0, lesJoueurs, lesMurs);
            this.orientation = DROITE;
            break;
        case KeyEvent.VK_UP:
        	deplace(0, -PAS, lesJoueurs, lesMurs);
            break;
        case KeyEvent.VK_DOWN:
        	deplace(0, PAS, lesJoueurs, lesMurs);
            break;
        case KeyEvent.VK_SPACE:
        	if (!this.boule.jLabel.isVisible()) {
        		this.boule.tireBoule(this, lesMurs);
        		}
		}
		this.affiche(MARCHE, this.etape);
	}

	/**
	 * Gère le déplacement du personnage en fonction des coordonnées fournies.
	 * Met à jour la position et vérifie si le déplacement est valide.
	 * Si le joueur sort des limites de l'arène ou entre en collision avec un mur ou un autre joueur,
	 * le déplacement est annulé et la position précédente est restaurée.
	 *
	 * @param deltaX Déplacement horizontal du joueur (peut être positif ou négatif).
	 * @param deltaY Déplacement vertical du joueur (peut être positif ou négatif).
	 * @param lesJoueurs Collection des joueurs présents dans l'arène pour vérifier les collisions.
	 * @param lesMurs Liste des murs présents dans l'arène pour vérifier les collisions.
	 */
	private void deplace(Integer deltaX, Integer deltaY, Collection lesJoueurs, Collection lesMurs) {
		this.etape = (etape % NBETAPESMARCHE) + 1;
		
		int savePosX = posX;
		int savePosY = posY;
		posX += deltaX;
	    posY += deltaY;
		
		if (posX < 0 || 
			posX > (LARGEURARENE - LARGEURPERSO) ||
			posY < 0 ||
			posY > (HAUTEURARENE - HAUTEURPERSO)) {
			posX = savePosX;
			posY = savePosY;
			return;
		}
		if (toucheCollectionObjetsAvecStream(lesJoueurs)!=null || toucheCollectionObjetsAvecStream(lesMurs)!=null) {
			posX = savePosX;
			posY = savePosY;
		}
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
		this.vie += GAIN;
//		this.message.setText(pseudo+": "+vie);
//		this.jeuServeur.envoiJeuATous();
		affiche(MARCHE, etape);
	}
	
	/**
	 * Perte de points de vie après avoir été touché 
	 */
	public void perteVie() {
		this.vie = Math.max(0, this.vie - PERTE); //prend le max entre les deux = jamais en dessous de zéro.
		affiche(MARCHE, etape);
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
		return this.vie <= 0;
	}
	
	/**
	 * Le joueur se déconnecte et disparait
	 */
	public void departJoueur() {
		System.out.println(String.format("Joueur dit: départ du joueur %s", this.pseudo));
	    if (super.jLabel != null) {
			super.jLabel.setVisible(false);
			this.message.setVisible(false);
			this.boule.getjLabel().setVisible(false);
			this.jeuServeur.envoiJeuATous();
		}
	}
	
}
