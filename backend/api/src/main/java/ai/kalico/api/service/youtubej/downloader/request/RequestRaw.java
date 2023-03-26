package ai.kalico.api.service.youtubej.downloader.request;

public abstract class RequestRaw<T extends RequestRaw<T>> extends Request<T, String> {

    public abstract String getDownloadUrl();

}
