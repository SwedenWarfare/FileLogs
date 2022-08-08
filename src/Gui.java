import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class Gui implements  ActionListener {
    JFrame frame = new JFrame();
    public static JTextArea historyText = new JTextArea(20,20);
    JTextArea dirsTxt = new JTextArea(20,20);
    JButton addDirs = new JButton("Add Dirs");
    JPanel historyPanel = new JPanel();
    JSplitPane pane;
    JSplitPane dirsPane;
    JTextField inField = new JTextField();
    boolean isRunning = false;
    public static List<String> dirsList = new ArrayList<>();

    public Gui() throws FileNotFoundException {
        JScrollPane historyPane = new JScrollPane(historyText);
        historyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        historyPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane dirs = new JScrollPane(dirsTxt);
        historyText.setEditable(false);
        dirs.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        dirs.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        dirsTxt.setEditable(false);
        dirsPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,historyPane,dirs);
        historyPanel.setLayout(new GridLayout(0,2));
        historyPanel.add(inField);
        historyPanel.add(addDirs);
        pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,dirsPane,historyPanel);

        addDirs.addActionListener(this);
        frame.setVisible(true);
        frame.add(pane);
        frame.setSize(500,500);
        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                stopTasks();
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        readLogs();
        readFile();


        frame.pack();
    }
    void stopTasks(){
        System.out.println("I did a funny");
    }
    void readFile()
    {
        String data = "";
        try {
            File toRead = new File("progFiles/setting.conf");
            Scanner sc = new Scanner(toRead);
            while (sc.hasNextLine()){
                data += sc.nextLine();
                System.out.println(data);
            }
            sc.close();
        }catch (Exception e){

        }
        String[] dirs = data.split("&");
        System.out.println(dirs.length);
        dirsTxt.setText("");
        try {
            for(String s : dirs){
            dirsTxt.append(s+"\n");
                Thread thread = new Thread()
                {
                    public void run()
                    {
                        FileStuff stuff = new FileStuff(s);
                        stuff.run();
                    }
                };
                thread.start();
            }

        }catch (Exception e){
            e.printStackTrace();
        }



    }
    static void readLogs() throws FileNotFoundException {
        File logsDir = new File("progFiles/logs/");
        File[] logs = logsDir.listFiles();

        for(File f : logs){
            if(f.isFile()){
                File toRead = f;
                Scanner sc = new Scanner(toRead);
                String data = "";
                while (sc.hasNextLine()){
                    data += sc.nextLine()+"\n";

                }
                sc.close();
                String[] logData = data.split("\n");
                for(String s : logData){
                    historyText.append(s+"\n");
                }
                if(logs.length > 1)
                    historyText.append("----End of Hour----");
            }
        }
    }



    void writeSettings(String text){
        try {
            FileWriter writer = new FileWriter("progFiles/setting.conf");
            File f = new File("progFiles/setting.conf");
            if(f.exists()) {
                String data = "";
                Scanner sc = new Scanner(f);
                while (sc.hasNextLine()) {
                    data += sc.nextLine() + "\n";
                }
                sc.close();
                data += text;

                String[] toWrite = data.split("\n");

                for (String s : toWrite) {
                    writer.write(s + "&\n");
                }
                writer.close();
            }else{
                for(String s : dirsTxt.getText().split("\n")){
                    writer.write(s+"&\n");
                    System.out.println(s);
                }
                writer.close();
            }

        }catch (Exception e){

        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addDirs){

            String[] tmpArr = inField.getText().split(",");
            for(String s : tmpArr){
                dirsTxt.append(s+"\n");
            }
            writeSettings(dirsTxt.getText());
            Thread thread = new Thread()
            {
                public void run()
                {
                    readFile();
                }
            };
            thread.start();
            //readFile();
            inField.setText("");

        }
    }

}
