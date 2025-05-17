package vue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controleur.Controle;
import outils.son.Son;

import java.awt.Dimension;
import javax.swing.SwingConstants;
import java.awt.Cursor;

import controleur.Global;

/**
 * Fenêtre permettant au joueur de choisir son personnage et d'entrer son pseudo.
 * Elle offre une interface pour naviguer entre différents personnages et valider son choix.
 */
public class ChoixJoueur extends JFrame implements Global {

	/**
	 * Nombre total de personnages disponibles pour le joueur.
	 */
	private static final int NBPERSOS = 3;
	/**
	 * Instance du contrôleur permettant de gérer la sélection du joueur et l'envoi des informations.
	 */
	private Controle controle;
	/**
	 * Panel général
	 */
	private JPanel contentPane;
	/**
	 * Zone de saisie du pseudo
	 */
	private JTextField txtPseudo;
	
	/**
	 * Affichage du personnage
	 */
	private JLabel lblPersonnage;

	/**
	 * numéro du perso sélectionné
	 */	
	private int numPerso;
	/** 
	 * Son de bienvenue joué lors de l'affichage de la fenêtre de sélection du personnage.
	 */
	private Son welcome;

	/** 
	 * Son joué lors du retour à la sélection précédente.
	 */
	private Son precedent;

	/** 
	 * Son joué lors du passage à la sélection suivante.
	 */
	private Son suivant;

	/** 
	 * Son joué lorsque le jeu est lancé.
	 */
	private Son go;
	/**
	 * Clic sur la flèche "précédent" pour afficher le personnage précédent
	 */
	private void lblPrecedent_clic() {
		precedent.play();
		numPerso = (numPerso + NBPERSOS - 2) % NBPERSOS + 1;
		affichePerso();
	}
	
	/**
	 * Clic sur la flèche "suivant" pour afficher le personnage suivant
	 */
	private void lblSuivant_clic() {
		suivant.play();
		numPerso = numPerso % NBPERSOS + 1;
		affichePerso();
	}
	
	/**
	 * Clic sur GO pour envoyer les informations
	 */
	private void lblGo_clic() {
		if (txtPseudo.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "La saisie d'un pseudo est obligatoire");
			txtPseudo.requestFocus();
		}
		else {
			go.play();
			controle.evenementChoixJoueur(txtPseudo.getText(), numPerso);
		}
		
	}
	/**
	 * Change le pointeur de la souris en mode standard (curseur par défaut).
	 */
	private void sourisNormale() {
		contentPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	/**
	 * Change le pointeur de la souris en mode "main" pour indiquer une zone interactive.
	 */
	private void sourisDoigt() {
		contentPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	/**
	 * Met à jour l'affichage du personnage sélectionné en fonction de son numéro.
	 */
	private void affichePerso() {
		//String chemin = "personnages/perso"+numPerso+"marche1d1.gif";
		String chemin = CHEMINPERSONNAGES+PERSO+this.numPerso+MARCHE+1+"d"+1+EXTFICHIERPERSO;
		URL resource = getClass().getClassLoader().getResource(chemin);
		lblPersonnage.setIcon(new ImageIcon(resource));
	}


	/**
	 * Constructeur de la fenêtre de sélection du joueur.
	 * Initialise les éléments graphiques et affiche le premier personnage par défaut.
	 *
	 * @param controle Instance du contrôleur permettant d'envoyer les informations sélectionnées.
	 */
	public ChoixJoueur(Controle controle) {
		// Dimension de la frame en fonction de son contenu
		this.getContentPane().setPreferredSize(new Dimension(400, 275));
	    this.pack();
	    // interdiction de changer la taille
		this.setResizable(false);
		 
		setTitle("Choice");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblPersonnage = new JLabel("");
		lblPersonnage.setHorizontalAlignment(SwingConstants.CENTER);
		lblPersonnage.setBounds(142, 115, 120, 120);
		contentPane.add(lblPersonnage);
		
		JLabel lblPrecedent = new JLabel("");
		lblPrecedent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				lblPrecedent_clic();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				sourisDoigt();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				sourisNormale();
			}
			
		});
		
		JLabel lblSuivant = new JLabel("");
		lblSuivant.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblSuivant_clic();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				sourisDoigt();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				sourisNormale();
			}
			
		});
		
		JLabel lblGo = new JLabel("");
		lblGo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblGo_clic();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				sourisDoigt();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				sourisNormale();
			}
			
		});
		
		txtPseudo = new JTextField();
		txtPseudo.setBounds(142, 245, 120, 20);
		contentPane.add(txtPseudo);
		txtPseudo.setColumns(10);
		
		lblGo.setBounds(311, 202, 65, 61);
		contentPane.add(lblGo);
		lblSuivant.setBounds(301, 145, 25, 46);
		contentPane.add(lblSuivant);
		lblPrecedent.setBounds(65, 146, 31, 45);
		contentPane.add(lblPrecedent);
		
		JLabel lblFond = new JLabel("");
		lblFond.setBounds(0, 0, 400, 275);
		URL resource = getClass().getClassLoader().getResource(FONDCHOIX);
		lblFond.setIcon(new ImageIcon(resource));		
		contentPane.add(lblFond);
		
		//Affichage du personnage par défaut
		numPerso = 1;
		affichePerso();

		// positionnement sur la zone de saisie
		txtPseudo.requestFocus();

		this.controle = controle;
		
		precedent = new Son(getClass().getClassLoader().getResource(SONPRECEDENT));
		suivant = new Son(getClass().getClassLoader().getResource(SONSUIVANT));
		go = new Son(getClass().getClassLoader().getResource(SONGO));
		welcome = new Son(getClass().getClassLoader().getResource(SONWELCOME));
	    
	    welcome.play();
	}
}