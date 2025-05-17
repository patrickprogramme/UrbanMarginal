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
	 * Constructeur
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
	 * Tir d'une boule
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

	public void pause(long millisecondes, int nanosecondes) {
		try {
			Thread.sleep(millisecondes, nanosecondes);
		} catch (InterruptedException e) {
			System.out.println("erreur pause");
		}
	}
	
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
