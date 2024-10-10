import java.util.ArrayList;
import java.time.LocalTime;


public class SessionProcessing {
  public static ArrayList<Session> process(LogEntryDetail detail) {
    ArrayList<Session> processedSessions = new ArrayList<>();
    for (LogEntry logEntry : detail.logEntries) {
      if (logEntry.action.equals("Start")) {
        Session session = new Session();
        session.userName = logEntry.userName;
        session.start = logEntry.start;
        session.state = "Active";
        session.durationSeconds = 0;
        processedSessions.add(session);
      } else if (logEntry.action.equals("End")) {
        boolean existingSessionFound = false;
        for (Session existingSession : processedSessions) {
          if (existingSession.userName.equals(logEntry.userName)) {
            existingSession.start = logEntry.start;
            existingSession.state = "Inactive";
            existingSession.durationSeconds = (int) (logEntry.start.toSecondOfDay() - existingSession.start.toSecondOfDay());
            existingSessionFound = true;
            break;
          }
        }
        
        if (existingSessionFound == false) {
          System.out.println("No existing session found for user: " + logEntry.userName);
          Session session = new Session();
          session.start = detail.earliestStartTime;
          session.userName = logEntry.userName;
          session.state = "Inactive";
          session.durationSeconds = (int) (logEntry.start.toSecondOfDay() - detail.earliestStartTime.toSecondOfDay());
          processedSessions.add(session);
        }
      }
    }

    boolean hasActiveSessions = false;
    for (Session session : processedSessions) {
      if (session.state.equals("Active")) {
        hasActiveSessions = true;
        session.state = "Inactive";
        session.durationSeconds = (int) (detail.latestEndTime.toSecondOfDay() - session.start.toSecondOfDay());
        processedSessions.add(session);
      }
    }
    return processedSessions;
  }
}

