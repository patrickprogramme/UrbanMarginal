package controleur;
import java.awt.EventQueue;
import vue.EntreeJeu;

public class Controle {
	
	private EntreeJeu frame;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Controle();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private Controle() {
		this.frame = new EntreeJeu();
		this.frame.setVisible(true);
	}

}
