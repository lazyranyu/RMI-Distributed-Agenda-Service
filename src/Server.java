import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
/**
 * [创建服务器]
 *
 * @author : [2021302747]
 * @version : [v1.0]
 * @createTime : [2023/11/6 20:33]
 */
public class Server {
    public static void main(String[] args) {
        try {
            SharedAgenda sharedAgenda = new SharedAgendaImpl();
            Registry registry = LocateRegistry.createRegistry(8000);
            registry.rebind("SharedAgenda", sharedAgenda);
            System.out.println("服务器已启动");
        } catch (Exception e) {
            System.err.println("服务器异常: " + e.toString());
            e.printStackTrace();
        }
    }
}
