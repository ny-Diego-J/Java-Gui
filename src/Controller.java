import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Controller extends ny.Controller {

    public void run() {
        Gui gui = new Gui();
        gui.printComponents(gui.getGraphics());
    }
}
