package modele;

import java.util.Collection;
import java.util.stream.Collectors;

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
	 * retourne la coordonnée sur X de l'objet
	 * @return the posX
	 */
	public Integer getPosX() {
		return posX;
	}

	/**
	 * retourne la coordonnée sur Y de l'objet
	 * @return the posY
	 */
	public Integer getPosY() {
		return posY;
	}
	/**
	 * Définit la position X de l'objet.
	 * <p>
	 * Ce setter n'est présent que pour des **tests unitaires** et ne devrait 
	 * pas être utilisé en production.
	 * </p>
	 * @param posX Nouvelle position X de l'objet.
	 */
	public void setPosX(Integer posX) {
		this.posX = posX;
	}
	/**
	 * Définit la position Y de l'objet.
	 * <p>
	 * Ce setter est uniquement utilisé pour les **tests unitaires** et n'a 
	 * pas d'usage en dehors du cadre de test.
	 * </p>
	 * @param posY Nouvelle position Y de l'objet.
	 */
	public void setPosY(Integer posY) {
		this.posY = posY;
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
	/**
	 * Vérifie Vérifie si l'objet actuel entre en collision avec un des objets d'une collection.
	 * @param lesObjets collection d'objet qui seront testés
	 * @return bolléen : collision ou pas ?
	 */
	public Boolean toucheCollectionObjets(Collection<Objet> lesObjets) {
		for (Objet objet : lesObjets) {
			if (!objet.equals(this)) {
				if (this.toucheObjet(objet)) {
					return true;
				} 
			}
		}
		return false;
	}
	/**
	 * Vérifie si l'objet actuel entre en collision avec un des objets d'une collection.
	 * 
	 * Cette méthode utilise l'API Stream pour :
	 * - Parcourir efficacement la collection sans boucle explicite.
	 * - Filtrer directement les objets en collision en excluant l'objet lui-même.
	 * - Récupérer le premier objet trouvé en collision, réduisant ainsi le nombre d'opérations.
	 * 
	 * L'utilisation de Stream rend le code plus concis, lisible et optimisé pour la gestion des collisions.
	 * 
	 * @param lesObjets Collection des objets avec lesquels tester la collision.
	 * @return Le premier objet détecté en collision ou `null` si aucune collision n'est trouvée.
	 */
	public Objet toucheCollectionObjetsAvecStream(Collection<Objet> lesObjets) {
	    return lesObjets.stream()
	                    .filter(objet -> !objet.equals(this) && this.toucheObjet(objet)) // Filtrer les objets en collision
	                    .findFirst() // Prendre le premier trouvé
	                    .orElse(null); // Retourner null si aucune collision n'est détectée
	}
}
