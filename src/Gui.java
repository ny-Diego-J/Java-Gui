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

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Gui extends JPanel {
    JFrame mainFrame;
    ArrayList<StringBuilder> inputs = new ArrayList<>();
    int currentLine = 0;
    int xCursorPos = 0;

    public Gui() {
        mainFrame = new JFrame();
        mainFrame.setResizable(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.add(this);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        inputs.add(new StringBuilder());
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
                } else {
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
        }
    }

    private void cursorRight() {
        if (xCursorPos < inputs.get(currentLine).length()) {
            xCursorPos++;
            Controller.print.println(xCursorPos);
        }
    }

    private void cursorLeft() {
        if (xCursorPos > 0) {
            xCursorPos--;
            Controller.print.println(xCursorPos);

        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        paintInput(g2d, 100, 100, 100);
    }

    private void paintInput(Graphics2D g2d, int xPos, int yPos, int fontSize) {
        g2d.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < inputs.size(); i++) {
            g2d.drawString(inputs.get(i).toString(), xPos, yPos + (fontSize * i));
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
                String newText = appendAtPoint(inputs.get(currentLine).toString(), e);
                inputs.get(currentLine).delete(0, inputs.get(currentLine).length());
                inputs.get(currentLine).append(newText);
        }
        print(this.getGraphics());
    }

    private void deleteAtChar(int mod) {
        if (xCursorPos == 0 && currentLine >= 1) {
            Controller.print.println("up");
            currentLine -= 1;
            xCursorPos = inputs.get(currentLine).length();
            return;
        }
        if (xCursorPos == 0 && currentLine == 0) {
            return;
        }
        if (xCursorPos + mod <= inputs.get(currentLine).length()) {
            inputs.get(currentLine).replace(xCursorPos - 1 + mod, xCursorPos + mod, "");
            if (mod == 0) {
                xCursorPos--;
            }
        }
    }

    private String appendAtPoint(String s, KeyEvent e) {
        // TODO: function can be replaced with inpusts.get(currentLine).insert(xCursorPos, e.getKeyChar)
        String start = s.substring(0, xCursorPos);
        String end = "";
        Controller.print.println("-------------");
        Controller.print.println("String length: " + s.length());

        if (xCursorPos != s.length()) {
            end = s.substring(xCursorPos);
            Controller.print.println("aaaaaaaaaaaaa");
        }

        Controller.print.println("Start: " + start);
        Controller.print.println("Current point: " + xCursorPos);
        Controller.print.println("End: " + end);
        Controller.print.println("-------------");

        xCursorPos++;

        return start + e.getKeyChar() + end;
    }
}