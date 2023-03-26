package ai.kalico.api.service.instagram4j.actions.feed;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.requests.IGPageRankTokenRequest;
import ai.kalico.api.service.instagram4j.requests.IGRequest;
import ai.kalico.api.service.instagram4j.responses.IGPageRankTokenResponse;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PageRankIterable<T extends IGRequest<R> & IGPageRankTokenRequest, R extends IGResponse & IGPageRankTokenResponse>
        implements Iterable<R> {
    @NonNull
    private IGClient client;
    @NonNull
    private Supplier<T> requestSupplier;

    @Override
    public Iterator<R> iterator() {
        return new PageRankTokenIterator<T, R>(client, requestSupplier.get());
    }

    @Override
    public Spliterator<R> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), Spliterator.IMMUTABLE);
    }

    public Stream<R> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    public static <T extends IGRequest<R> & IGPageRankTokenRequest, R extends IGResponse & IGPageRankTokenResponse> PageRankIterable<T, R> of(
            IGClient client, T t) {
        return new PageRankIterable<>(client, () -> t);
    }

}
