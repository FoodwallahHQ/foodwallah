package ai.kalico.api.service.youtubej.parser;

import ai.kalico.api.service.youtubej.downloader.request.RequestChannelUploads;
import ai.kalico.api.service.youtubej.downloader.request.RequestPlaylistInfo;
import ai.kalico.api.service.youtubej.downloader.request.RequestSearchContinuation;
import ai.kalico.api.service.youtubej.downloader.request.RequestSearchResult;
import ai.kalico.api.service.youtubej.downloader.request.RequestSearchable;
import ai.kalico.api.service.youtubej.downloader.request.RequestSubtitlesInfo;
import ai.kalico.api.service.youtubej.downloader.request.RequestVideoInfo;
import ai.kalico.api.service.youtubej.model.search.SearchResult;
import ai.kalico.api.service.youtubej.model.videos.VideoInfo;
import java.util.List;

import ai.kalico.api.service.youtubej.downloader.request.*;
import ai.kalico.api.service.youtubej.downloader.response.Response;
import ai.kalico.api.service.youtubej.model.playlist.PlaylistInfo;
import ai.kalico.api.service.youtubej.model.subtitles.SubtitlesInfo;

public interface Parser {

    /* Video */

    Response<VideoInfo> parseVideo(RequestVideoInfo request);

    /* Playlist */

    Response<PlaylistInfo> parsePlaylist(RequestPlaylistInfo request);

    /* Channel uploads */

    Response<PlaylistInfo> parseChannelsUploads(RequestChannelUploads request);

    /* Subtitles */

    Response<List<SubtitlesInfo>> parseSubtitlesInfo(RequestSubtitlesInfo request);

    /* Search */

    Response<SearchResult> parseSearchResult(RequestSearchResult request);

    Response<SearchResult> parseSearchContinuation(RequestSearchContinuation request);

    Response<SearchResult> parseSearcheable(RequestSearchable request);

}
