import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class createDirsAndFiles {
    public createDirsAndFiles(){
        try {
            File progFiles = new File("progFiles/logs");

            if(progFiles.exists())
                System.out.println("Already exists");
            else
                progFiles.mkdirs();

            File settingsFile = new File("progFiles/settings.conf");
            if (settingsFile.createNewFile()) {
                System.out.println("File created: " + settingsFile.getName());
            } else {
                System.out.println("File already exists.");
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");
            LocalDateTime now = LocalDateTime.now();
            File logFile = new File("progFiles/logs/"+dtf.format(now)+".log");
            System.out.println(logFile.getPath());
            if (logFile.createNewFile()) {
                System.out.println("File created: " + logFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
