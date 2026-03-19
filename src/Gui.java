import javax.swing.*;
import java.awt.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.cert.CertPath;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Gui extends JPanel {
    JFrame mainFrame;
    ArrayList<StringBuilder> inputs = new ArrayList<>();
    int currentLine = 0;
    int xCursorPos = 0;
    int fontSize = 78;
    int startX = 100;
    int startY = 100;
    Font font = new Font("Monospaced", Font.PLAIN, fontSize);

    public Gui() {
        mainFrame = new JFrame();
        mainFrame.setResizable(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.add(this);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        inputs.add(new StringBuilder());
        mainFrame.setFocusTraversalKeysEnabled(false);
        mainFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                //Controller.print.println("type: " + keyEvent.getKeyChar());
                addKeyToList(keyEvent);

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                int keyCode = keyEvent.getKeyCode();
                if (keyCode == KeyEvent.VK_LEFT) {
                    Controller.print.println("left");
                    cursorLeft();
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    Controller.print.println("right");
                    cursorRight();
                } else if (keyCode == KeyEvent.VK_UP) {
                    cursorUp();
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    cursorDown();
                }  else {
                    Controller.print.println("pressed: " + keyEvent.getKeyChar());
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                //Controller.print.println("released: " + keyEvent.getKeyChar());
            }
        });
    }

    private void cursorUp() {
        if (currentLine >= 1) {
            currentLine--;
            if (xCursorPos > inputs.get(currentLine).length()) {
                xCursorPos = inputs.get(currentLine).length();
            }
            paint(this.getGraphics());
        }
    }

    private void cursorDown() {
        Controller.print.println(currentLine);
        Controller.print.println(inputs.size());
        if (currentLine + 1 < inputs.size()) {
            currentLine++;
            if (xCursorPos > inputs.get(currentLine).length()) {
                xCursorPos = inputs.get(currentLine).length();
            }
            paint(this.getGraphics());
        }
    }

    private void cursorRight() {
        if (xCursorPos < inputs.get(currentLine).length()) {
            xCursorPos++;
            Controller.print.println(xCursorPos);
            paint(this.getGraphics());
        }
    }

    private void cursorLeft() {
        if (xCursorPos > 0) {
            xCursorPos--;
            Controller.print.println(xCursorPos);
            paint(this.getGraphics());
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        paintInput(g2d);
        printCursor(g2d);
    }

    private void printCursor(Graphics2D g2d) {
        //TODO: don't make it semi hardcoded
        FontMetrics metrics = getFontMetrics(font);
        int[] withs = metrics.getWidths();
        System.out.println(metrics.getDescent());
        int startHeight = currentLine * fontSize + 40;
        int startLeft = xCursorPos * withs[1] + startX + 5;
        g2d.setColor(Color.white);
        g2d.setStroke(new BasicStroke(10));
        g2d.drawLine(startLeft, startHeight, startLeft, startHeight + 70);
    }

    private void paintInput(Graphics2D g2d) {
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < inputs.size(); i++) {
            g2d.drawString(inputs.get(i).toString(), startX, startY + (fontSize * i));
        }
    }

    private void addKeyToList(KeyEvent e) {
        Controller.print.println(e.getKeyChar());
        switch (e.getKeyChar()) {
            case 8:
                deleteAtChar(0);
                break;
            case 127:
                deleteAtChar(1);
                break;
            case 10:
                inputs.add(new StringBuilder());
                currentLine++;
                xCursorPos = 0;
                break;
            case 65535:
            default:
                if (inputs.get(currentLine).charAt(xCursorPos) == ' '){
                    //TODO: fix input when last key is " "
                    inputs.get(currentLine).insert(xCursorPos, e.getKeyChar());
                    break;
                }
                inputs.get(currentLine).insert(xCursorPos, e.getKeyChar());
                xCursorPos++;
        }
        print(this.getGraphics());
    }


    private void deleteAtChar(int mod) {
        if (xCursorPos == 0 && currentLine >= 1 && mod == 0) {
            Controller.print.println("up");
            inputs.remove(currentLine);

            currentLine -= 1;
            xCursorPos = inputs.get(currentLine).length();
            return;
        }
        if (mod == 1 && xCursorPos == inputs.get(currentLine).length() && currentLine != inputs.indexOf(inputs.getLast())) {
            inputs.get(currentLine).append(inputs.get(currentLine + 1));
            inputs.remove(currentLine + 1);
            return;
        }

        if (xCursorPos == 0 && currentLine == 0 && mod == 0) {
            return;
        }

        if (xCursorPos + mod <= inputs.get(currentLine).length()) {
            inputs.get(currentLine).replace(xCursorPos - 1 + mod, xCursorPos + mod, "");
            if (mod == 0) {
                xCursorPos--;
            }
        }
    }
}