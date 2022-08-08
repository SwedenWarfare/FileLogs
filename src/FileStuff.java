import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static java.nio.file.StandardWatchEventKinds.*;

public class FileStuff extends Thread {

    public FileStuff(String f){
        try {

            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(f);
            path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE, OVERFLOW);
            boolean poll = true;
            while (poll) {
                System.out.println("iam alive");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");
                LocalDateTime now = LocalDateTime.now();
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    if(event.context() == null)
                        return;
                    if(event.kind() == ENTRY_MODIFY){
                        String data = "";
                        if(event.context().toString().endsWith(".txt"))
                        {

                            File toRead = new File(path.resolve((Path) event.context()).toString());
                            Scanner sc = new Scanner(toRead);
                            while (sc.hasNextLine()){
                                data += sc.nextLine();
                            }
                            sc.close();
                            writeToLog(dtf.format(now)+" File: "+event.context()+" Type: "+event.kind()+" Path: "+path.resolve((Path) event.context()).toString()+" Content: "+data+"\n");
                        }
                    }else{
                        writeToLog(dtf.format(now)+" File: "+event.context()+" Type: "+event.kind()+" Path: "+path.resolve((Path) event.context()).toString()+"\n");

                    }
                }
                poll = key.reset();

                }
            } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return;
    }
    void writeToLog(String text) throws FileNotFoundException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");
        LocalDateTime now = LocalDateTime.now();
        try{
            File f = new File("progFiles/logs/"+dtf.format(now)+".log");
            if(f.exists()){
                String data = "";
                Scanner sc = new Scanner(f);
                while (sc.hasNextLine()){
                    data += sc.nextLine()+" \n";
                }
                sc.close();
                data += text;
                String[] toWrite = data.split(" \n");
                FileWriter writer = new FileWriter("progFiles/logs/"+dtf.format(now)+".log");
                for(String s : toWrite){
                    writer.write(s+" \n");
                }
                writer.close();
            }else{
                if(f.createNewFile()){
                    FileWriter writer = new FileWriter("progFiles/logs/"+dtf.format(now)+".log");
                    writer.write(text);
                    writer.close();
                }else{

                }
            }
        }catch (Exception e){

        }
        Gui.historyText.setText("");
        Gui.readLogs();
    }


}

