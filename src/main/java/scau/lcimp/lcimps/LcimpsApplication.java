package scau.lcimp.lcimps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LcimpsApplication {

    public static void main(String[] args) {
        //  解决循环依赖
        SpringApplication springApplication = new SpringApplication(LcimpsApplication.class);
        springApplication.setAllowCircularReferences(Boolean.TRUE);
        springApplication.run(args);

//        try {
//            //  通过cmd自动打开接口文档
//            //  Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start http://localhost:8080/doc.html"});
//            //  通过cmd自动打开前端界面
//            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start http://localhost:8008/"});
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
