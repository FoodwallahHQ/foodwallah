package ai.kalico.api.service.youtubej.downloader;

public interface YoutubeCallback<T> {

    void onFinished(T data);

    void onError(Throwable throwable);
}
