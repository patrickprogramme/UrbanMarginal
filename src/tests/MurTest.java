package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import modele.Mur;
import controleur.Global;

class MurTest implements Global {
    Mur m1 = new Mur();
    Mur m2 = new Mur();
    int coordDefaut = 50;

    /**
     * Met les deux murs à la même position, la position par défaut
     */
    void setPos() {
        m1.setPosX(coordDefaut);
        m1.setPosY(coordDefaut);
        m2.setPosX(coordDefaut);
        m2.setPosY(coordDefaut);
    }
    @Test
    void testToucheObjetTotalement() {
        setPos(); // Les deux murs sont exactement au même endroit
        assertTrue(m1.toucheObjet(m2), "Échec: m1 et m2 devraient se toucher totalement");
    }
    @Test
    void testToucheObjetGauche() {
        setPos();
        m2.setPosX(coordDefaut - 1); // Légèrement à gauche, donc touche encore
        assertTrue(m1.toucheObjet(m2), "Échec: m1 et m2 devraient se toucher sur la gauche");
    }
    @Test
    void testToucheObjetDroite() {
        setPos();
        m2.setPosX(coordDefaut + 1); // Légèrement à droite, donc touche encore
        assertTrue(m1.toucheObjet(m2), "Échec: m1 et m2 devraient se toucher sur la droite");
    }
    @Test
    void testToucheObjetHaut() {
        setPos();
        m2.setPosY(coordDefaut - 1); // Légèrement au-dessus, donc touche encore
        assertTrue(m1.toucheObjet(m2), "Échec: m1 et m2 devraient se toucher en haut");
    }
    @Test
    void testToucheObjetBas() {
        setPos();
        m2.setPosY(coordDefaut + 1); // Légèrement en dessous, donc touche encore
        assertTrue(m1.toucheObjet(m2), "Échec: m1 et m2 devraient se toucher en bas");
    }
    @Test
    void testNeTouchePasObjetGauche() {
        setPos();
        m2.setPosX(coordDefaut - LARGEURMUR - 1); // Trop à gauche pour toucher
        assertFalse(m1.toucheObjet(m2), "Échec: m1 et m2 ne devraient pas se toucher (gauche)");
    }
    @Test
    void testNeTouchePasObjetDroite() {
        setPos();
        m2.setPosX(coordDefaut + LARGEURMUR + 1); // Trop à droite pour toucher
        assertFalse(m1.toucheObjet(m2), "Échec: m1 et m2 ne devraient pas se toucher (droite)");
    }
    @Test
    void testNeTouchePasObjetHaut() {
        setPos();
        m2.setPosY(coordDefaut - HAUTEURMUR - 1); // Trop en haut pour toucher
        assertFalse(m1.toucheObjet(m2), "Échec: m1 et m2 ne devraient pas se toucher (haut)");
    }
    @Test
    void testNeTouchePasObjetBas() {
        setPos();
        m2.setPosY(coordDefaut + HAUTEURMUR + 1); // Trop en bas pour toucher
        assertFalse(m1.toucheObjet(m2), "Échec: m1 et m2 ne devraient pas se toucher (bas)");
    }
    @Test
    void testFroleGauche() {
        setPos();
        m2.setPosX(coordDefaut - LARGEURMUR); // M2 est exactement à côté, mais ne touche pas
        assertFalse(m1.toucheObjet(m2), "Échec: m1 et m2 ne devraient pas se toucher (frôlement gauche)");
    }
    @Test
    void testFroleDroite() {
        setPos();
        m2.setPosX(coordDefaut + LARGEURMUR); // M2 est exactement à côté, mais ne touche pas
        assertFalse(m1.toucheObjet(m2), "Échec: m1 et m2 ne devraient pas se toucher (frôlement droite)");
    }
    @Test
    void testFroleHaut() {
        setPos();
        m2.setPosY(coordDefaut - HAUTEURMUR); // M2 est juste au-dessus, mais ne touche pas
        assertFalse(m1.toucheObjet(m2), "Échec: m1 et m2 ne devraient pas se toucher (frôlement haut)");
    }
    @Test
    void testFroleBas() {
        setPos();
        m2.setPosY(coordDefaut + HAUTEURMUR); // M2 est juste en dessous, mais ne touche pas
        assertFalse(m1.toucheObjet(m2), "Échec: m1 et m2 ne devraient pas se toucher (frôlement bas)");
    }
}