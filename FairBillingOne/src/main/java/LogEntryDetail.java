import java.time.LocalTime;
import java.util.ArrayList;

public class LogEntryDetail {
  ArrayList<LogEntry> logEntries = new ArrayList<>();
  public LocalTime earliestStartTime;
  public LocalTime latestEndTime;
}