package service;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;

import java.util.List;

public interface Parser<T> {

    List<T> parse(final HttpClient client);
}
