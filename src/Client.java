import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.text.SimpleDateFormat;
//2023/11/16/10:25
//add b 2023/11/16/10:25 2023/11/16/11:25 ab2
//add b 2023/11/15/10:25 2023/11/15/11:25 ab1
//add a 2023/11/17/5:25 2023/11/17/6:25 ba1
//add d 2023/11/15/10:25 2023/11/15/11:25 cd1
//add c 2023/11/15/14:25 2023/11/15/15:25 bc1

//add c 2023/11/15/13:25 2023/11/15/15:00 ac1
//query 2023/11/15/0:00 2023/11/17/10:25
/**
 * [一句话描述该类的功能]
 *
 * @author : [Lenovo]
 * @version : [v1.0]
 * @createTime : [2023/11/6 20:37]
 */
public class Client {
    private static SharedAgenda sharedAgenda;
    private static Scanner scanner;
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd/HH:mm");
//    RMIclien localhost 8000 register a a
    public static void main(String[] args) {
        if (args.length<6)
        {
            System.out.println("参数不足！");
            return;
        }
//        String command = "RMIClient localhost 8000 register c a";
//        args = command.split(" ");
        String clientname = args[0];
        String servername = args[1];
        int portNumber = Integer.parseInt(args[2]);
        try {
            Registry registry = LocateRegistry.getRegistry(servername,portNumber);
            sharedAgenda = (SharedAgenda) registry.lookup("SharedAgenda") ;
            String action = args[3];
            String username = args[4];
            String password = args[5];
            boolean tage =true;
            while (tage)
            {
                while (action.equals("register"))
                {
                    if(registerUser(username,password)){
                        System.out.println(username+"注册成功");
                        tage = false;
                        break;
                    }else {
                        System.out.println("注册失败：用户名已存在\n");
                        System.out.println("请输入新的参数：");
                        Scanner sc1 = new Scanner(System.in);
                        String newArgs = sc1.nextLine();
                        args = newArgs.split(" ");
                        username = args[4];
                        password = args[5];
                    }
                }
                if (!action.equals("register")) {
                    System.out.println("请先注册：");
                    Scanner sc1 = new Scanner(System.in);
                    String newArgs = sc1.nextLine();
                    args = newArgs.split(" ");
                    action = args[3];
                    username = args[4];
                    password = args[5];
                }
            }
            scanner = new Scanner(System.in);

            displayMenu();

            while (true)
            {
                System.out.println("\n"+username+"Input an operation:");
                String choice = scanner.nextLine();
                String[] choices = choice.split(" ");
                String option = choices[0];
                switch (option)
                {
                    case "add":
                        String[] ups0 = yanZhen();
                        String otherName = choices[1];
                        String start = choices[2];
                        String end = choices[3];
                        String title = choices[4];
                        Date startDate = format.parse(start);
                        Date endDate = format.parse(end);
                        addMeeting(ups0[0], ups0[1],otherName,startDate,endDate,title);
                        break;
                    case "delete":
                        String[] ups1 = yanZhen();
                        String meetId = choices[1];
                        deleteMeeting(ups1[0], ups1[1],meetId);
                        break;
                    case "clear":
                        String[] ups2 = yanZhen();
                        clearMeeting(ups2[0], ups2[1]);
                        break;
                    case "query":
                        String[] ups3 = yanZhen();
                        String q_start = choices[1];
                        String q_end = choices[2];
                        Date QstartDate = format.parse(q_start);
                        Date QendDate = format.parse(q_end);
                        queryMeeting(ups3[0], ups3[1],QstartDate,QendDate);
                        break;
                    case "help":
                        help();
                        break;
                    case "quit":
                        System.out.println("系统退出");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("命令错误");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void displayMenu() {
        System.out.println("RMI Menu:");
        System.out.println("\t\t1.add\n\t\t\t<argument>: <username> <start> <end> <title>");
        System.out.println("\t\t2.delete\n\t\t\t<argument>: <meeting id>");
        System.out.println("\t\t3.clear\n\t\t\t<argument>: no args");
        System.out.println("\t\t4.query\n\t\t\t<argument>: <start> <end>");
        System.out.println("\t\t5.help\n\t\t\t<argument>: no args");
        System.out.println("\t\t6.quit\n\t\t\t<argument>: no args");
    }

    private static boolean registerUser(String username, String password) {
        try {
            boolean registerResult = sharedAgenda.registerUser(username,password );
            return registerResult;
        } catch (Exception e) {
            System.err.println("注册用户异常！");
            e.printStackTrace();
        }
        return false;
    }
    private  static void addMeeting(String username, String password, String otherName, Date start, Date end, String title) throws RemoteException {
        String response = sharedAgenda.addMeet(username,password,otherName,start,end,title);
        System.out.println(response);
    }

    private static void deleteMeeting(String username, String password, String meetId) {
        try {
            boolean deleteResult = sharedAgenda.deleteMeeting(username,password,meetId);
            if (deleteResult)
            {
                System.out.println("会议："+meetId+"删除成功！");
            }else {
                System.out.println("会议："+meetId+"删除失败！");
            }
        } catch (RemoteException e) {
            System.out.println("会议："+meetId+"删除失败！");
            e.printStackTrace();
        }
    }

    private static void clearMeeting(String username, String password) {
        try {
            boolean clearResult = sharedAgenda.clearMeetings(username,password);
            if (clearResult)
            {
                System.out.println("已清除："+username+"创建的会议\n");
            }else {
                System.out.println("无法清除："+username+"创建的会议\n");
            }
        } catch (RemoteException e) {
            System.out.println("无法清除："+username+"创建的会议\n");
            e.printStackTrace();
        }
    }

    private static void queryMeeting(String username, String password, Date qStart, Date qEnd) {
        //String meetings=null;
        List<Meeting> meetingList = new Vector<>();
        try {
            meetingList = sharedAgenda.queryMeetings(username,password,qStart,qEnd);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //System.out.println(meetings);

        if (meetingList.size()==0)
        {
            System.out.println("目前没有任何会议");
        }else if (meetingList.get(0).getCreator().equals("0")&&meetingList.get(0).getOtherUsername().equals("0")){
            System.out.println("查询会议失败：用户名或密码错误");
        }else {
            for (Meeting meeting :meetingList) {
                System.out.println(meeting.toString());
            }
        }
    }
    private static void help() {
        System.out.println("add\t\t\t\t已注册的用户可以添加会议。会议必须涉及到两个已注册的用户，一个只涉及单个用户的会议无法被创建。会议的信息包括开始时间、结束时间、会议标题、会议参与者。当一个会议被添加之后，它必须出现在创建会议的用户和参加会议的用户的议程中。如果一个用户的新会议与已经存在的会议出现时间重叠，则无法创建会议。最终，用户收到会议添加结果的提示。");
        System.out.println("query\t\t\t已注册的用户通过给出特定时间区间（给出开始时间和结束时间）在议程中查询到所有符合条件的会议记录。返回的结果列表按时间排序。在列表中，包括开始时间、结束时间、会议标题、会议参与者等信息。");
        System.out.println("delete\t\t\t已注册的用户可以根据会议的信息删除由该用户创建的会议。");
        System.out.println("clear\t\t\t已注册的用户可以清除所有由该用户创建的会议。");
    }
    private static String[] yanZhen(){
        System.out.println("请输入账号密码，验证身份：");
        String up = scanner.nextLine();
        String[] ups = up.split(" ");
        return ups;
    }
}
