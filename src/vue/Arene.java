package vue;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controleur.Controle;
import controleur.Global;
import modele.Mur;
import modele.Objet;
import java.awt.event.KeyAdapter;
import java.util.Set;

/**
 * Classe représentant l'arène du jeu, gérant l'affichage des joueurs, des murs, 
 * des boules, et du tchat. Elle assure la gestion des événements et des interactions utilisateur.
 */
public class Arene extends JFrame implements Global {

	/**
	 * Panel général
	 */
	private JPanel contentPane;
	/**
	 * Instance du contrôleur pour communiquer avec lui
	 */
	private Controle controle;
	/**
	 * Ensemble des codes de touches représentant les flèches directionnelles.
	 * Un Set car : vérification rapide (O(1)) des touches valides + évite les doublons (ils sont interdits).
	 */
	private static final Set<Integer> TOUCHES_VALIDES = Set.of(
		    KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN
		);
	/**
	 * Zone de saisie du t'chat
	 */
	private JTextField txtSaisie;
	/**
	 * Panel contenant les murs
	 */
	private JPanel jpnMurs;
	/**
	 * Panel contenant les joueurs et les boules
	 */
	private JPanel jpnJeu;
	/**
	 * Indique si l'arène appartient au client ou non.
	 * Permet d'adapter certains éléments en fonction du type de jeu.
	 */
	private Boolean estClient;
	/**
	 * Zone d'affichage du t'chat
	 */
	private JTextArea txtChat;
	/**
	 * Retourne le texte actuel affiché dans le tchat du jeu.
	 *
	 * @return Texte du tchat.
	 */
	public String getTxtChat() {
		return txtChat.getText();
	}
	/**
	 * Met à jour le texte affiché dans le tchat et repositionne le curseur en fin de texte.
	 *
	 * @param text Nouvelle chaîne de caractères à afficher.
	 */
	public void setTxtChat(String text) {
		txtChat.setText(text);
		this.txtChat.setCaretPosition(this.txtChat.getDocument().getLength());
	}
	/**
	 * Ajoute une phrase à la zone d'affichage du t'chat,
	 * en ajoutant un saut de ligne à la fin pour séparer les messages.
	 * @param phrase la phrase à ajouter au t'chat.
	 */
	public void ajoutTchat(String phrase) {
		//txtChat.append(phrase + "\r\n");
		this.txtChat.setText(this.txtChat.getText()+phrase+"\r\n");
		this.txtChat.setCaretPosition(this.txtChat.getDocument().getLength());

	}
	/**
	 * Gère l'événement de frappe de touches dans la zone de saisie du tchat.
	 * Envoie le message lorsque la touche "Entrée" est pressée.
	 *
	 * @param e Informations sur la touche pressée.
	 */
	public void txtSaisie_KeyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (!txtSaisie.getText().isEmpty()) {
				System.out.println("Il y a du texte à envoyer");
				this.controle.evenementArene(this.txtSaisie.getText());
				txtSaisie.setText("");
				//txtSaisie.setCaretPosition(0);
			}
			this.contentPane.requestFocus();
		}
	}
	/**
	 * Retourne le panel contenant les joueurs et les boules affichées dans l'arène.
	 *
	 * @return JPanel du jeu.
	 */
	public JPanel getJpnJeu() {
		return jpnJeu;
	}
	/**
	 * Met à jour le panel contenant les joueurs et les boules affichées dans l'arène.
	 *
	 * @param jpnJeu Nouveau panel à afficher.
	 */
	public void setJpnJeu(JPanel jpnJeu) {
		this.jpnJeu.removeAll();
		this.jpnJeu.add(jpnJeu);
		this.jpnJeu.repaint();
		this.contentPane.requestFocus();
	}
	/**
	 * Retourne le panel contenant les murs de l'arène.
	 *
	 * @return JPanel des murs.
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
	 * Ajoute un mur dans le panel des murs de l'arène.
	 *
	 * @param unMur Mur à ajouter.
	 */
	public void ajoutMurs(Object unMur) {
		jpnMurs.add((JLabel)unMur);
		jpnMurs.repaint();
	}
	/**
	 * Ajoute un joueur, son message ou sa boule dans le panel de jeu.
	 *
	 * @param jlabel JLabel à ajouter.
	 */
	public void ajoutJLabelJeu(JLabel jlabel) {
		jpnJeu.add(jlabel);
		jpnJeu.repaint();
	}
	/**
	 * Affiche les infos sur l'Arene
	 */
	public void afficherInfosArene() {
		// Utilisation de l'opérateur ternaire pour choisir entre "client" et "serveur"
		String quiSuisJe = estClient ? "client" : "serveur"; 
		// Formatage élégant de la chaîne pour éviter la concaténation avec "+"
		String quiEstCe = String.format("L'arène du %s a été créée avec succès !", quiSuisJe); 
		System.out.println(quiEstCe);

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
	 * Gère l'événement de pression d'une touche.
	 * Vérifie si la touche appartient aux touches directionnelles valides.
	 * @param e L'événement KeyEvent contenant les informations sur la touche pressée.
	 */
	public void contentPane_keyPressed(KeyEvent e) {
		if (TOUCHES_VALIDES.contains(e.getKeyCode())) {
	        this.controle.evenementArene(e.getKeyCode());
	    }
	}
	/**
	 * Constructeur de la classe Arene.
	 * Initialise la fenêtre de jeu et ses éléments graphiques.
	 *
	 * @param controle Instance du contrôleur pour la gestion des événements.
	 * @param typeJeu Type du jeu (client ou serveur).
	 */
	public Arene(Controle controle, String typeJeu) {
		this.estClient = typeJeu.equals(CLIENT);
		// Dimension de la frame en fonction de son contenu
		this.getContentPane().setPreferredSize(new Dimension(LARGEURARENE, HAUTEURARENE + 25 + 140));
		this.pack();
		// interdiction de changer la taille
		this.setResizable(false);

		setTitle("Arena");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				contentPane_keyPressed(e);
			}
		});
		setContentPane(contentPane);
		contentPane.setLayout(null);

		jpnMurs = new JPanel();
		jpnMurs.setBounds(0, 0, LARGEURARENE, HAUTEURARENE);
		jpnMurs.setOpaque(false);
		jpnMurs.setLayout(null);
		contentPane.add(jpnMurs);

		jpnJeu = new JPanel();
		jpnJeu.setBounds(0, 0, LARGEURARENE, HAUTEURARENE);
		jpnJeu.setOpaque(false);
		jpnJeu.setLayout(null);
		contentPane.add(jpnJeu);

		if (this.estClient) {
			txtSaisie = new JTextField();
			txtSaisie.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					txtSaisie_KeyPressed(e);
				}
			});
			txtSaisie.setBounds(0, 600, 800, 25);
			contentPane.add(txtSaisie);
			txtSaisie.setColumns(10);
		}

		JScrollPane jspChat = new JScrollPane();
		jspChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jspChat.setBounds(0, 625, 800, 140);
		contentPane.add(jspChat);

		txtChat = new JTextArea();
		jspChat.setViewportView(txtChat);
		txtChat.setEditable(false);
		txtChat.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				contentPane_keyPressed(e);
			}
		});

		JLabel lblFond = new JLabel("");
		URL resource = getClass().getClassLoader().getResource(FONDARENE);
		lblFond.setIcon(new ImageIcon(resource));		
		lblFond.setBounds(0, 0, 800, 600);
		contentPane.add(lblFond);

		this.controle = controle;
		afficherInfosArene();
	}
}