package modele;

import javax.swing.JLabel;

/**
 * Classe abstraite représentant les objets du jeu (joueurs, murs, boules).
 * Elle gère la position des objets et les interactions entre eux, notamment les collisions.
 */
public abstract class Objet {

	/**
	 * position X de l'objet
	 */
	protected Integer posX ;
	/**
	 * position Y de l'objet
	 */
	protected Integer posY ;
	/**
	 * Élément graphique permettant d'afficher l'objet dans l'interface utilisateur.
	 */
	protected JLabel jLabel;
	/**
	 * Retourne l'élément graphique (`JLabel`) représentant l'objet.
	 *
	 * @return JLabel utilisé pour afficher l'objet.
	 */
	public JLabel getjLabel() {
		return jLabel;
	}

	/**
	 * Vérifie si l'objet actuel entre en collision avec un autre objet.
	 *
	 * La collision est détectée lorsque les zones occupées par les deux objets
	 * se chevauchent dans l'espace de jeu.
	 *
	 * @param objet Objet avec lequel tester la collision.
	 * @return `true` si les deux objets se touchent, `false` sinon.
	 */
	public Boolean toucheObjet (Objet objet) {
		if ((objet == null)) {return false;} //Un objet null ne peut pas provoquer de collision
		
		//position max des objet (position + largeur/hauteur)
		int thisPosXMax = this.posX + this.jLabel.getWidth();
		int objPosXMax = objet.posX + objet.getjLabel().getWidth();
		int thisPosYMax = this.posY + this.jLabel.getHeight();
		int objPosYMax = objet.posY + objet.getjLabel().getHeight();
		
		return (thisPosXMax > objet.posX &&
				this.posX < objPosXMax &&
				thisPosYMax > objet.posY &&
				this.posY < objPosYMax);
	}
}
