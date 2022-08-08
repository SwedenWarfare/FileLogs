import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        if(args.length > 0)
        {
            try {
                boolean idk = Boolean.parseBoolean(args[0]);
                Gui.shouldShow = idk;
            }catch (Exception e){}

        }
        try {
                //weird one javax.swing.plaf.nimbus.NimbusLookAndFeel
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                //
                //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");


            } catch(Exception ignored){}
        new createDirsAndFiles();
        new Gui();
        }
    }
