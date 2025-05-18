package modele;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controleur.Global;


/**
 * Gestion de la boule
 *
 */
public class Boule extends Objet implements Global, Runnable {

	/**
	 * Collection de murs
	 */
	private Collection lesMurs;
	/**
	 * joueur qui lance la boule
	 */
	private Joueur attaquant ;
	/**
	 * instance de JeuServeur pour la communication
	 */
	private JeuServeur jeuServeur ;
	
	/**
	 * Constructeur de la classe Boule.
	 * Initialise une nouvelle instance de Boule en associant le jeu serveur,
	 * créant un JLabel pour l'affichage de la boule, et définissant ses propriétés visuelles.
	 *
	 * @param jeuServeur L'instance du jeu serveur permettant la communication des événements liés à la boule.
	 */
	public Boule(JeuServeur jeuServeur) {
		this.jeuServeur = jeuServeur;
		super.jLabel = new JLabel();
		super.jLabel.setVisible(false);
		URL resource = getClass().getClassLoader().getResource(BOULE);
		super.jLabel.setIcon(new ImageIcon(resource));
		super.jLabel.setBounds(0, 0, LARGEURBOULE, HAUTEURBOULE);
	}
	
	/**
	 * Initialise le tir d'une boule par un joueur donné, en définissant sa position
	 * et en lançant un thread pour gérer son déplacement et ses interactions.
	 *
	 * @param attaquant Le joueur qui effectue le tir.
	 * @param lesMurs La collection d'objets représentant les murs dans l'arène.
	 */
	public void tireBoule(Joueur attaquant, Collection<Objet> lesMurs) {
		this.attaquant = attaquant;
		this.lesMurs = lesMurs;
		
		super.posY = attaquant.getPosY() + (HAUTEURPERSO / 2);
		
		if (attaquant.getOrientation() == DROITE) {
			super.posX = attaquant.getPosX() + LARGEURPERSO + 1;
		}
		else {
			super.posX = attaquant.getPosX() - 1 - LARGEURBOULE;
		}		
		
		new Thread(this).start();
	}

	/**
	 * Met en pause l'exécution du thread actuel pendant la durée spécifiée.
	 * Gère les éventuelles interruptions et affiche un message d'erreur en cas d'échec.
	 *
	 * @param millisecondes Durée de la pause en millisecondes.
	 * @param nanosecondes Durée supplémentaire en nanosecondes.
	 */
	public void pause(long millisecondes, int nanosecondes) {
		try {
			Thread.sleep(millisecondes, nanosecondes);
		} catch (InterruptedException e) {
			System.out.println("erreur pause");
		}
	}
	
	/**
	 * Gère le déplacement de la boule après son lancement, en détectant les collisions
	 * avec les joueurs et les murs, et en déclenchant les effets correspondants.
	 * Met à jour l'affichage et communique les événements au serveur de jeu.
	 */
	@Override
	public void run() {
		// envoi du son "FIGHT"
		this.jeuServeur.envoi(FIGHT);
		this.attaquant.affiche(MARCHE, 1);
		super.jLabel.setVisible(true);
		Joueur victime = null;
		int lePas = (attaquant.getOrientation() == DROITE) ? PAS : -PAS;
		
		do {
			posX += lePas;
			super.jLabel.setBounds(posX, posY, LARGEURBOULE, HAUTEURBOULE);
			this.jeuServeur.envoiJeuATous();
			Collection lesJoueurs = this.jeuServeur.getLesJoueurs();
			victime = (Joueur)super.toucheCollectionObjetsAvecStream(lesJoueurs);
			pause(10, 0);
		} while (posX > 0 && 
				posX <= (LARGEURARENE - LARGEURBOULE) && 
				victime == null && 
				super.toucheCollectionObjetsAvecStream(lesMurs) == null);
				// La boucle continue tant que la boule reste dans l'arène et ne touche ni un joueur ni un mur
		
		if (victime != null && !victime.estMort()) {
			this.jeuServeur.envoi(HURT);
			victime.perteVie();
			this.attaquant.gainVie();
			
			for (int i = 1; i <= NBETAPESTOUCHE; i++) {
				victime.affiche(TOUCHE, i);
				pause(80, 0);
			}
			if (victime.estMort()) {
				this.jeuServeur.envoi(DEATH);
				for (int i = 1; i <= NBETAPESMORT; i++) {
					victime.affiche(MORT, i);
					pause(80, 0);
				}
			}
			else {
				victime.affiche(MARCHE, 1);
			}
		}
		
		super.jLabel.setVisible(false);
		this.jeuServeur.envoiJeuATous();
	}	
	
}
