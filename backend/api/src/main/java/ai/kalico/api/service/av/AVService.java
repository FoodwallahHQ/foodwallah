package ai.kalico.api.service.av;


import ai.kalico.api.dto.VideoInfoDto;
import com.kalico.model.ContentPreviewResponse;
import com.kalico.model.CreateRecipeResponse;
import com.kalico.model.GenericResponse;

/**
 * @author Bizuwork Melesse
 * created on 6/12/22
 */
public interface AVService {
    void processRecipeContent(String url, VideoInfoDto dto, String contentId, String thumbnailUrl);
    void processMedia(String url, Long projectId, String file, String fileExtension);
    String extractYouTubeVideoId(String url);
    void processInstagramVideo(VideoInfoDto videoInfoDto, Long projectId, boolean recipeContent);
    void processYouTubeVideo(VideoInfoDto videoInfoDto, Long projectId, boolean recipeContent);
    void processUploadedVideo(String file, String fileExtension, String mediaId, Long projectId);
    void processUploadedAudio(String file, String fileExtension, String mediaId, Long projectId);
    ContentPreviewResponse downloadContentMetadata(String url);
    ContentPreviewResponse parseContentMetadata(VideoInfoDto dto);
    VideoInfoDto getContent(String url);
}
