import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.*;

/**
 * [实现共享议程服务]
 *
 * @author : [2021302747]
 * @version : [v1.0]
 * @createTime : [2023/11/6 19:20]
 */
public class SharedAgendaImpl extends UnicastRemoteObject implements SharedAgenda {
    private HashMap<String,String> users;
    private Vector<Meeting> meetings;

    protected SharedAgendaImpl() throws RemoteException {
        super();
        users = new HashMap<>();
        meetings = new Vector<>();
    }

    @Override
    public boolean registerUser(String username, String password) throws RemoteException {
        if(users.containsKey(username))
        {
            System.out.println("注册失败：用户名已存在！");
            return false;
        }else {
            users.put(username, password);
            System.out.println("客户"+username+"注册成功！");
            return true;
        }
    }

    @Override
    public String addMeet(String username, String password, String otherUsername, Date start, Date end, String title) throws RemoteException {
        if (!users.get(username).equals(password)) {
            String out = "删除会议失败：用户名或密码错误！";
            System.out.println("删除会议失败：用户名或密码错误");
            return out;
        }
        if (!users.containsKey(username)||!users.containsKey(otherUsername))
        {
            String out = "添加会议失败：用户不存在！";
            System.out.println("添加会议失败：用户不存在！");
            return out;
        }
        if (start.compareTo(end) > 0) {
            String out = "添加会议失败：请检查时间是否合理!";
            System.out.println("添加会议失败：会议开始时间在结束时间后面!");
            return out;
        }
        //检查会议时间是否会重叠
        for (Meeting meeting:meetings) {
            if ((meeting.getCreator().equals(username)||meeting.getOtherUsername().equals(username))&&((meeting.getStart().compareTo(start) <= 0 && meeting.getEnd().compareTo(start) > 0) || (meeting.getStart().compareTo(end) < 0 && meeting.getEnd().compareTo(end) >= 0))) {
                String out = "添加会议失败：时间冲突!";
                System.out.println("添加会议失败：时间冲突!");
                return out;
            }
        }
        //创建会议
        Meeting meeting = new Meeting(username,otherUsername,start,end,title);
        meetings.add(meeting);
        System.out.println("添加会议"+meeting.getTitle()+"成功！");
        return "添加会议成功：会议ID为"+meeting.getId();
    }

    @Override
    public List<Meeting> queryMeetings(String username, String password, Date start, Date end) throws RemoteException {
        List<Meeting> result = new Vector<>();
        if (!users.get(username).equals(password))
        {
            System.out.println("查询会议失败：用户名或密码错误");
            Date now = new Date();
            Meeting meeting0 = new Meeting("0","0",now,now,"0");
            result.add(meeting0);
            return result;
        }

        for (Meeting meeting:meetings) {
            if ((meeting.getCreator().equals(username)||meeting.getOtherUsername().equals(username))&&(meeting.getStart().compareTo(start)>=0&&meeting.getEnd().compareTo(end)<=0)){
                result.add(meeting);
            }
        }
        Comparator<Meeting> startTimeComparator = new Comparator<Meeting>() {
            @Override
            public int compare(Meeting meeting1,Meeting meeting2) {
                return meeting1.getStart().compareTo(meeting2.getStart());
            }
        };
        result.sort(startTimeComparator);
//        StringBuilder out = new StringBuilder();
//        for (Meeting meeting:result) {
//            out.append(meeting.toString()).append("\n");
//        }
//        String output = out.toString();
//        System.out.println(output);
//        return output;
        return result;
    }

    @Override
    public boolean deleteMeeting(String username, String password, String meetingId) throws RemoteException {
        if (!users.get(username).equals(password)) {
            System.out.println("删除会议失败：用户名或密码错误");
            return false;
        }

        for (Meeting meeting : meetings) {
            if (meeting.getId().equals(meetingId) && meeting.getCreator().equals(username)) {
                meetings.remove(meeting);
                System.out.println("会议号为"+meeting.getId()+"d会议删除成功");
                return true;
            }else {
                System.out.println("删除会议失败：会议不存在或您无权删除");
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean clearMeetings(String username, String password) throws RemoteException {
        if (!users.get(username).equals(password)) {
            System.out.println("清除会议失败：用户名或密码错误");
            return false;
        }

        meetings.removeIf(meeting -> meeting.getCreator().equals(username));
        System.out.println("用户:"+username+"所创建的会议清除成功");
        return true;
    }

}
