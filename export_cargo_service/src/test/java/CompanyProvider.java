import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CompanyProvider {

    public static void main(String[] args) throws Exception {
        //1.根据spring配置文件创建spring容器
        ClassPathXmlApplicationContext ac =
                new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        //2.启动spring容器
        ac.start();
        //3.在控制台输入任一项退出
        System.in.read();
    }
}