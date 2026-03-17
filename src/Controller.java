import javax.swing.*;

public class Controller extends ny.Controller {

    public void run(){
        Gui gui = new Gui();
        gui.printComponents(gui.getGraphics());
    }


}
