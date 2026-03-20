import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GuiTwo extends JPanel implements ActionListener {
    private static String checkBoxTestCode = "checkBox";
    private static String multipleChoiceCode = "choice";
    public JLayeredPane layeredPane;
    ArrayList<StringBuilder> inputs = new ArrayList<>();
    int currentLine = 0;
    int xCursorPos = 0;
    int fontSize = 24;
    int startX = 10;
    int startY = 30;
    Font font = new Font("Monospaced", Font.BOLD, fontSize);
    private JCheckBox checkBoxTest;
    private String[] choices = {"Yellow (0)", "Magenta (1)", "Cyan (2)", "Red (3)", "Green (4)"};
    private JComboBox multipleChoice;


    public GuiTwo() {
        inputs.add(new StringBuilder());
        // set layout for all panes
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        //create new pane
        layeredPane = new JLayeredPane();
        // set prefered size for pane
        layeredPane.setPreferredSize(new Dimension(550, 550));
        // set border and title
        layeredPane.setBorder(BorderFactory.createTitledBorder("Enter your text"));


        //addLabel();
        layeredPane.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyevent) {
                //controller.print.println("type: " + keyevent.getkeychar());
                if (keyevent.getKeyChar() != '\t') addKeyToList(keyevent);
            }

            @Override
            public void keyPressed(KeyEvent keyevent) {
                int keycode = keyevent.getKeyCode();
                if (keycode == KeyEvent.VK_LEFT) {
                    Controller.print.println("left");
                    cursorLeft();
                } else if (keycode == KeyEvent.VK_RIGHT) {
                    Controller.print.println("right");
                    cursorRight();
                } else if (keycode == KeyEvent.VK_UP) {
                    cursorUp();
                } else if (keycode == KeyEvent.VK_DOWN) {
                    cursorDown();
                } else if (keycode == KeyEvent.VK_TAB) {
                    tabPressed();
                } else {
                    Controller.print.println("pressed: " + keyevent.getKeyCode());
                }
            }


            @Override
            public void keyReleased(KeyEvent keyevent) {
                //controller.print.println("released: " + keyevent.getkeychar());
            }
        });
        layeredPane.setFocusable(true);
        layeredPane.grabFocus();

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(createControlPanel());
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(layeredPane);
        layeredPane.grabFocus();
        layeredPane.requestFocus();

    }

    public static void main() {
        JFrame frame = new JFrame("Text editor");
        JComponent newComponent = new GuiTwo();
        newComponent.setOpaque(true);
        frame.setContentPane(newComponent);
        frame.pack();
        frame.setVisible(true);

    }

    private void addLabel() {
        JPanel panel = new JPanel();
        panel.setVisible(true);
        panel.setBackground(Color.BLACK);
        layeredPane.add(panel);
    }


    private JPanel createControlPanel() {
        checkBoxTest = new JCheckBox("Top Position in Layer");
        checkBoxTest.setSelected(true);
        checkBoxTest.setActionCommand(checkBoxTestCode);
        checkBoxTest.addActionListener(this);

        multipleChoice = new JComboBox(choices);
        multipleChoice.setSelectedIndex(2);    //cyan layer
        multipleChoice.setActionCommand(multipleChoiceCode);
        multipleChoice.addActionListener(this);
        JPanel controls = new JPanel();
        controls.add(multipleChoice);
        controls.add(checkBoxTest);

        controls.setBorder(BorderFactory.createTitledBorder("Options"));
        return controls;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("aaa");
        layeredPane.grabFocus();
        layeredPane.requestFocus();
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
                inputs.add(currentLine + 1, new StringBuilder());
                currentLine++;
                xCursorPos = 0;
                break;
            case 65535:
            default:
                inputs.get(currentLine).insert(xCursorPos, e.getKeyChar());
                xCursorPos++;
        }
        paintText(layeredPane.getGraphics());
    }

    public void paintText(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        printCursor(g2d);
        paintInput(g2d);
    }

    private void printCursor(Graphics2D g2d) {
        //TODO: make it scaleable
        FontMetrics metrics = getFontMetrics(font);
        int[] withs = metrics.getWidths();
        System.out.println(metrics.getDescent());
        int startHeight = currentLine * fontSize + 15; //TODO: get reasoning for 50
        int startLeft = xCursorPos * withs[1] + startX; //TODO: get reasoning for 5
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(startLeft, startHeight, startLeft, startHeight + 20); //TODO: try to replace 60 with variable
    }

    private void paintInput(Graphics2D g2d) {
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < inputs.size(); i++) {
            g2d.drawString(inputs.get(i).toString(), startX, startY + (fontSize * i));
        }
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

    private void tabPressed() {
        inputs.get(currentLine).insert(xCursorPos, "    ");
        xCursorPos += 4;
        paint(this.getGraphics());
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
            paint(this.getGraphics());
        }
    }

    private void cursorLeft() {
        if (xCursorPos > 0) {
            xCursorPos--;
            paint(this.getGraphics());
        }
    }
}
