package zcy.zcy;

import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import zcy.zcy.models.ProblemSolving;
import zcy.zcy.models.TK;
import zcy.zcy.models.User;
import zcy.zcy.repository.ProblemSolvingRepository;
import zcy.zcy.repository.TKRepository;
import zcy.zcy.repository.UserRepository;

import javax.annotation.Resource;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@SpringBootTest
@Slf4j
class ZcyApplicationTests {
    @Resource
    TKRepository tkRepository;
    @Resource
    UserRepository userRepository;
    @Resource
    ProblemSolvingRepository problemSolvingRepository;
    
    @Test
    void contextLoads() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File("src/main/resources/fps-loj-small-pics.xml"));
        Element element = document.getRootElement();
        List<Element> elements = element.elements("item");
        List<TK> tks = new ArrayList<>();
        for (Element element1 : elements) {
            TK tk = new TK();
            Element element2 = element1.element("title");
            String s = element2.getText();
            String s1 = s.replace("&nbsp;", " ");
            tk.setTitle(s1);
            tks.add(tk);
        }
        document = saxReader.read(new File("src/main/resources/fps-www.educg.net-codeforce-1-2833.xml"));
        element = document.getRootElement();
        elements = element.elements("item");
        for (Element element1 : elements) {
            TK tk = new TK();
            Element element2 = element1.element("title");
            String s = element2.getText();
            String s1 = s.replace("&nbsp;", " ");
            tk.setTitle(s1);
            tks.add(tk);
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        tkRepository.batchInsert(tks);
        log.info("写入题目: " + Duration.between(localDateTime, LocalDateTime.now()));
        Faker faker = new Faker(Locale.CHINA);
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            User user = new User();
            user.setName(faker.name().fullName());
            users.add(user);
        }
        localDateTime = LocalDateTime.now();
        userRepository.batchInsert(users);
        log.info("写入用户: " + Duration.between(localDateTime, LocalDateTime.now()));
        tks = tkRepository.query();
        users = userRepository.query();
        List<ProblemSolving> problemSolvingList = new ArrayList<>();
        Random random = new Random();
        final int tkSize = tks.size();
        final int userSize = users.size();
        for (int i = 0; i < 1000000; i++) {
            ProblemSolving problemSolving = new ProblemSolving();
            problemSolving.setTKId(tks.get(random.nextInt(tkSize)).getId());
            problemSolving.setUserId(users.get(random.nextInt(userSize)).getId());
            problemSolvingList.add(problemSolving);
        }
        localDateTime = LocalDateTime.now();
        problemSolvingRepository.batchInsert(problemSolvingList);
        log.info("写入解题记录: " + Duration.between(localDateTime, LocalDateTime.now()));
        log.info(problemSolvingRepository.query().toString());
    }
    
}
