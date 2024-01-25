/*
创建共享议程接口
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public interface SharedAgenda extends Remote {
    boolean registerUser(String username,String password)throws RemoteException;
    String addMeet(String username, String password, String otherUsername, Date start, Date end, String title)throws RemoteException;
    List<Meeting> queryMeetings(String username,String password,Date start,Date end)throws RemoteException;
    boolean deleteMeeting(String username,String password,String meetingId)throws  RemoteException;
    boolean clearMeetings(String username,String password)throws RemoteException;
}
