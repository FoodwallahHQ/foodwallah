package ai.kalico.api.service.youtubej.downloader;

public interface YoutubeProgressCallback<T> extends YoutubeCallback<T> {

    void onDownloading(int progress);

}
