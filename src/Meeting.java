import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.io.*;

/**
 * [一句话描述该类的功能]
 *
 * @author : [Lenovo]
 * @version : [v1.0]
 * @createTime : [2023/11/6 19:36]
 */
public class Meeting implements Serializable {
    private String creator;
    private String otherUsername;
    private Date start;
    private Date end;
    private String title;
    private String meetingId;

    public Meeting(String username, String otherUsername, Date start, Date end, String title) {
        this.creator = username;
        this.otherUsername = otherUsername;
        this.start = start;
        this.end = end;
        this.title = title;
        this.meetingId = generateMeetingId();
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public String getCreator() {
        return creator;
    }

    public String getOtherUsername() {
        return otherUsername;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return meetingId;
    }
    private String generateMeetingId() {
        Set<String> existingIds = new HashSet<>();
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        while (true) {
            // Generate a random nine-digit number
            int randomNumber = random.nextInt(900_000_000) + 100_000_000;
            sb.setLength(0);
            sb.append(randomNumber);

            // Format the number as "111-111-111"
            String formattedId = sb.insert(3, '-').insert(7, '-').toString();

            // Check if the generated ID is unique
            if (!existingIds.contains(formattedId)) {
                existingIds.add(formattedId);
                return formattedId;
            }
        }
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "creator='" + creator + '\'' +
                ", otherUsername='" + otherUsername + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", title='" + title + '\'' +
                ", meetingId='" + meetingId + '\'' +
                '}';
    }
}
