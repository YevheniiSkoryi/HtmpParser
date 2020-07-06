import domain.Post;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import service.PostParser;
import service.PostService;
import service.PostServiceImpl;

import java.util.List;

public class Driver {

    private static final PostParser postParser = new PostParser();
    private static final PostService postService = new PostServiceImpl();

    public static void main(String[] args) {

        for (int i = 0; i < 2; i++) {
            new MyThread().start();
        }
    }

    static class MyThread extends Thread {
        HttpClient httpclient = HttpClients.createDefault();

        @Override
        public void run() {
            List<Post> posts = postParser.parse(httpclient);

            postService.saveAll(posts);
            int repetitions = postService.calculateNumberOfRepetitions("body", "713221aa-f5c6-4fee-9dcf-f6e191882f83");
            System.out.println("REPETITIONS: " + repetitions); //expected 4
        }
    }


}
