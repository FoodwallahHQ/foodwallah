package ai.kalico.api.service.project;

import com.kalico.model.ContentPreviewResponse;
import com.kalico.model.CreateProjectRequest;
import com.kalico.model.CreateProjectResponse;
import com.kalico.model.GenericResponse;
import com.kalico.model.GifRequest;
import com.kalico.model.GifResponse;
import com.kalico.model.MediaContent;
import com.kalico.model.PageableResponse;
import com.kalico.model.ProjectDetail;
import com.kalico.model.ProjectJobStatus;
import com.kalico.model.UpdateProjectContentRequest;
import com.kalico.model.UserProjectsResponse;
import java.util.List;

/**
 * @author Biz Melesse created on 1/29/23
 */
public interface ProjectService {

  /**
   * Create a new project
   *
   * @param createProjectRequest project metadata and an optional uploaded media content
   * @return
   */
  CreateProjectResponse createProject(CreateProjectRequest createProjectRequest);
  GenericResponse deleteProject(String projectUid);
  PageableResponse getAllProjects();
  ProjectDetail getProjectById(String projectUid);

  GenericResponse updateProjectContent(
      UpdateProjectContentRequest updateProjectContentRequest);

  GenericResponse deleteGif(String url);

  GifResponse generateGif(GifRequest gifRequest);

  List<String> getSampledImages(String projectUid);

  MediaContent getMediaContent(String projectUid);

  ProjectJobStatus getProjectJobStatus(String projectUid);

  /**
   * Get OpenGraph social preview links for the given content
   * @param url
   * @return
   */
  ContentPreviewResponse getContentPreview(String url);

  /**
   * Get all user projects for an admin
   *
   * @param page
   * @param limit
   * @return
   */
  UserProjectsResponse getAllUserProjects(Integer page, Integer limit);

}
