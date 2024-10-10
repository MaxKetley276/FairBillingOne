import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.time.LocalTime;

public class Main {
  public static void main(String[] args) {
    System.out.println(outputReport("src/main/resources/logfile.log"));

  }

  public static LogEntryDetail loadLogEntriesArrayFromLogFile(String fileName)  {
    LogEntryDetail o = new LogEntryDetail();
    o.logEntries = new ArrayList<>();
    
    try (Scanner scanner = new Scanner(Paths.get(fileName))) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (LineValidation.isValid(line) == true) {
          if (o.earliestStartTime == null) {
            o.earliestStartTime = LocalTime.parse(line.split(" ")[0]);
          }
          o.latestEndTime = LocalTime.parse(line.split(" ")[0]);
          LogEntry logEntry = new LogEntry();
          logEntry.userName = line.split(" ")[1];
          logEntry.start = LocalTime.parse(line.split(" ")[0]);
          logEntry.action = line.split(" ")[2];
          o.logEntries.add(logEntry);
        } else {
          System.out.println("Invalid line: " + line);
        }
      } 
      
    } catch (FileNotFoundException e) {
      System.out.println("File not found");      
    } catch (IOException e) {
      e.printStackTrace();
    }
    return o;
  }
  
  public static String outputReport(String fileName) {
    // load file
    LogEntryDetail logEntryDetail = loadLogEntriesArrayFromLogFile(fileName);
    
    if (logEntryDetail.logEntries.size() == 0) {
      return "No log entries found";
    }
    System.out.println("Start time: " + logEntryDetail.earliestStartTime.toString());
    System.out.println("End time: " + logEntryDetail.latestEndTime.toString());

    // Iterator iterator = logEntryDetail.logEntries.iterator();
    // while(iterator.hasNext()){
    //   LogEntry s = (LogEntry) iterator.next();
    //     System.out.println(s.start.toString() + " " + s.userName + " " + s.action);
    // }


    Iterator iterator = SessionProcessing.process(logEntryDetail).iterator();
    while(iterator.hasNext()){
      Session s = (Session) iterator.next();
        System.out.println(s.start.toString() + " " + s.userName + " " + s.state + " " + s.durationSeconds);
    }
    
    /*
    
      walk each line
        is it valid?
          Are there 3 parts AND are all parts valid
            <HH:mm:ss> <alpha-numeric username> <Start|End>
          yes: process the line
            have we seen this user before?
              yes: What's the action?
                Start:
                  Already started session?: start running parralel session
                  No open session: Start session
                End:
                  Already started session: close the most recent session (and calculate duration)
                  No open session: create a session and charge for time based off the earliest file time
              no: What is the action?
                Start: new session (i.e. no duration.  The time is the start time))
                End: calculate duration and end the session
                  option 1: User had an open session (and we knew the start time)
                    add duration
                  option 2: There is no previous start so use the earliest time seen in the file.
                    add duration
          no: reject
      close any 'open' sessions - based on the last seen log time.

      Group up all the sessions by username and calculate the total duration for each user.
      Output list (by user / duration?)
    */

    return "";
  }
}