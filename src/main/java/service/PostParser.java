package service;

import domain.Post;
import exception.ErrorException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PostParser implements Parser<Post> {


    private static final String URL = "https://ain.ua/post-list/";
    private static final String POST_ITEM_CLASS_NAME = "post-item ordinary-post";
    private static final String POST_LINK_CLASS_NAME = "post-link with-labels ";
    private static final String HREF_ATTRIBUTE_NAME = "href";
    private static final String POST_BODY_CLASS_NAME = "block-wrap-content";
    private static final String BODY_TAG = "p";
    private static final String TITLE_TAG = "h1";

    private static final Integer COUNT_LIMIT = 5;

    @Override
    public List<Post> parse(final HttpClient client) {
        HttpPost post = new HttpPost(URL);

        HttpEntity entity = execute(client, post);

        return handleEntity(entity, document -> {
            final Elements allPosts = document.body()
                    .getElementsByClass(POST_ITEM_CLASS_NAME);
            final List<String> hrefs = allPosts.stream()
                    .map(e -> {
                        Element href = e.getElementsByClass(POST_LINK_CLASS_NAME)
                                .get(0);
                        return getLinkOnPost(href);
                    })
                    .limit(COUNT_LIMIT)
                    .collect(Collectors.toList());

            return hrefs.stream()
                    .map(href -> {
                        final HttpGet getRequest = new HttpGet(href);
                        final HttpEntity postEntity = execute(client, getRequest);

                        return handleEntity(postEntity, doc -> {
                            Elements elements = doc.body()
                                    .getElementsByClass(POST_BODY_CLASS_NAME);
                            String title = extractTitle(elements);
                            String body = extractBody(elements);
                            return new Post(title, body);
                        });


                    })
                    .collect(Collectors.toList());
        });
    }

    private <B> B handleEntity(final HttpEntity entity, Function<Document, B> function) {
        try (InputStream inputStream = entity.getContent()) {
            final Document doc = parseInputStream(inputStream);
            return function.apply(doc);
        } catch (IOException e) {
            throw new ErrorException("Content not found.", e);
        }
    }

    private HttpEntity execute(final HttpClient client, final HttpRequestBase request) {
        HttpResponse response;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            throw new ErrorException("Can not execute request.", e);
        }
        return response.getEntity();
    }

    private String extractBody(final Elements elements) {
        final Elements bodies = elements.get(0).getElementsByTag(BODY_TAG);
        return bodies.stream()
                .map(Element::text)
                .collect(Collectors.joining());
    }

    private String extractTitle(final Elements elements) {
        final Element titleNode = elements.get(0).getElementsByTag(TITLE_TAG).get(0);
        return titleNode.text();
    }

    private Document parseInputStream(final InputStream inputStream) {
        final String result;
        try {
            result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ErrorException("Can not parse body.", e);
        }
        return Jsoup.parse(result);
    }

    private String getLinkOnPost(final Element element) {
        return element.attr(HREF_ATTRIBUTE_NAME);
    }
}
