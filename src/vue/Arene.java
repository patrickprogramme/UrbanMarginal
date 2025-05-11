package vue;

import java.awt.Dimension;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controleur.Global;
import modele.Mur;
import modele.Objet;

/**
 * frame de l'arène du jeu
 */
public class Arene extends JFrame implements Global {

	/**
	 * Panel général
	 */
	private JPanel contentPane;
	/**
	 * Zone de saisie du t'chat
	 */
	private JTextField txtSaisie;
	/**
	 * Panel contenant les murs
	 */
	private JPanel jpnMurs;
	/**
	 * Zone d'affichage du t'chat
	 */
	private JTextArea txtChat;
	
	/**
	 * Retourne le panel contenant les murs.
	 * @return le JPanel utilisé pour afficher les murs
	 */
	public JPanel getJpnMurs() {
	    return jpnMurs;
	}
	/**
	 * Définit le panel utilisé pour afficher les murs.
	 * @param jpnMurs le nouveau JPanel à assigner aux murs
	 */
	public void setJpnMurs(JPanel jpnMurs) {
	    this.jpnMurs.add(jpnMurs);
	    this.jpnMurs.repaint();
	}
	/**
	 * type Objet pour evenementJeuServeur dans controle (frameArene.ajoutMurs(info);)
	 * @param unMur
	 */
	public void ajoutMurs(Object unMur) {
		jpnMurs.add((JLabel)unMur);
		jpnMurs.repaint();
	}
	/**
	 * Affiche les infos sur l'Arene
	 */
	public void afficherInfosArene() {
	    System.out.println("L'arène a été créée avec succès !");
	    
	    StringBuilder message = new StringBuilder("Composants ajoutés : ");
	    for (java.awt.Component comp : contentPane.getComponents()) {
	        message.append(comp.getClass().getSimpleName()).append(", ");
	    }
	    
	    if (message.length() > 20) { // Vérifie qu'on a bien ajouté des composants
	        message.setLength(message.length() - 2); // Supprime la virgule et l'espace de fin
	    }
	    
	    System.out.println(message.toString());
	}
		/**
	 * Create the frame.
	 */
	public Arene() {
		// Dimension de la frame en fonction de son contenu
		this.getContentPane().setPreferredSize(new Dimension(LARGEURARENE, HAUTEURARENE + 25 + 140));
	    this.pack();
	    // interdiction de changer la taille
		this.setResizable(false);
		
		setTitle("Arena");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		jpnMurs = new JPanel();
		jpnMurs.setBounds(0, 0, LARGEURARENE, HAUTEURARENE);
		jpnMurs.setOpaque(false);
		jpnMurs.setLayout(null);
		contentPane.add(jpnMurs);
	
		txtSaisie = new JTextField();
		txtSaisie.setBounds(0, 600, 800, 25);
		contentPane.add(txtSaisie);
		txtSaisie.setColumns(10);
		
		JScrollPane jspChat = new JScrollPane();
		jspChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jspChat.setBounds(0, 625, 800, 140);
		contentPane.add(jspChat);
		
		txtChat = new JTextArea();
		jspChat.setViewportView(txtChat);
		
		JLabel lblFond = new JLabel("");
		URL resource = getClass().getClassLoader().getResource(FONDARENE);
		lblFond.setIcon(new ImageIcon(resource));		
		lblFond.setBounds(0, 0, 800, 600);
		contentPane.add(lblFond);
		
		afficherInfosArene();
	}
}