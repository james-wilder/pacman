package co.uk.handmadetools.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyState implements KeyListener {

    private static boolean up = false;
    private static boolean down = false;
    private static boolean left = false;
    private static boolean right = false;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        synchronized(this) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                up = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                down = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                left = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                right = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        synchronized(this) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                up = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                down = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                left = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                right = false;
            }
        }
    }

    public static synchronized boolean isUp() {
        return up;
    }

    public static synchronized boolean isDown() {
        return down;
    }

    public static synchronized boolean isLeft() {
        return left;
    }

    public static synchronized boolean isRight() {
        return right;
    }

}
