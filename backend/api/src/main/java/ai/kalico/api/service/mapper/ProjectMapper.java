package ai.kalico.api.service.mapper;


import ai.kalico.api.data.postgres.entity.ProjectEntity;
import ai.kalico.api.data.postgres.entity.MediaContentEntity;
import ai.kalico.api.data.postgres.projection.UserProjectProjection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalico.model.*;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.ObjectUtils;

/**
 * @author Biz Melesse
 * created on 1/29/23
 */

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {JsonNullableMapper.class})
public interface ProjectMapper {

  default List<UserProject> mapProjections(List<UserProjectProjection> projections) {
    if (ObjectUtils.isEmpty(projections)) {
      return new ArrayList<>();
    }
    List<UserProject> projects = new ArrayList<>();
    for (UserProjectProjection projection : projections) {
      projects.add(map(projection));
    }
    return projects;
  }

  default UserProject map(UserProjectProjection projection) {
    return new UserProject()
        .projectId(projection.getProjectUid())
        .projectName(projection.getProjectName())
        .email(projection.getEmail())
        .contentUrl(projection.getContentUrl())
        .userFullName(projection.getFullName())
        .projectCreatedAt(projection.getProjectCreatedAt().toEpochSecond(ZoneOffset.UTC))
        .registeredOn(projection.getRegisteredOn().toEpochSecond(ZoneOffset.UTC));
  }

  default List<Project> map(List<ProjectEntity> projectEntities) {
    if (projectEntities == null) {
      return new ArrayList<>();
    }
    List<Project> projects = new ArrayList<>();
    for (ProjectEntity projectEntity : projectEntities) {
      projects.add(new Project()
          .projectName(projectEntity.getProjectName())
          .projectUid(projectEntity.getProjectUid()));
    }
    return projects;
  }

  default ProjectDetail map(ProjectEntity projectEntity, ObjectMapper objectMapper) {
    if (projectEntity == null) {
      return null;
    }
    TypeReference<List<ContentItem>> typeRef = new TypeReference<>() {};
    ProjectDetail detail = new ProjectDetail();
    detail.setId(projectEntity.getProjectUid());
    detail.setName(JsonNullable.of(projectEntity.getProjectName()));
    detail.setDateCreated(projectEntity.getCreatedAt().toEpochSecond(ZoneOffset.UTC));
    detail.setRawTranscript(projectEntity.getGetRawTranscript());
    if (projectEntity.getContent() != null) {
      try {
        detail.setContent(objectMapper.readValue(projectEntity.getContent().toString(), typeRef));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }
    return detail;
  }


  default String getCdnUrl(String cdn, String mediaId, String format) {
    return cdn + "/" + mediaId + "/" + mediaId + "." + format;
  }

  default MediaContent mapMediaContent(MediaContentEntity entity, String projectUid) {
    if (entity == null) {
      return null;
    }
    MediaContent content = new MediaContent();
    content.setProjectId(projectUid);
    content.setDescription(entity.getScrapedDescription());
    content.setTitle(entity.getScrapedTitle());
    content.setPermalink(entity.getPermalink());
    content.setTranscript(entity.getRawTranscript());
    content.setMediaId(entity.getMediaId());
    return content;
  }
}
