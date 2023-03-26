package ai.kalico.api.service.utils;

import ai.kalico.api.data.postgres.entity.EmailTrackingEntity;
import ai.kalico.api.data.postgres.repo.EmailTrackingRepo;
import ai.kalico.api.props.LeadsProps;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Biz Melesse created on 2/18/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeadServiceHelper {

  private final EmailTrackingRepo emailTrackingRepo;
  private final LeadsProps leadsProps;

  @Async
  public void logImageRequest(String imageHash, String ipAddress) {
    byte[] decodedBytes = Base64.getDecoder().decode(imageHash.replace(".png", ""));
    String decodedHash = new String(decodedBytes);
    // An image hash is made up of user email address and campaign id
    String[] tokens = decodedHash.split(":");
    if (tokens.length > 1) {
      String email = tokens[0];
      String campaignId = tokens[1];
      EmailTrackingEntity entity;
      // Locate the existing tracked emails  by campaign id and ip address. We need to do the lookup
      // using these two parameters so each device can only create a single record even if they're
      // opening multiple emails for the same campaign. This is the case when we are opening the
      // drafts and sending them.
      List<EmailTrackingEntity> trackingEntities = emailTrackingRepo.findByEmailAndCampaignId(email, campaignId);
      if (!trackingEntities.isEmpty()) {
        entity = trackingEntities.get(0);

        // Update number of times opened based on the threshold. The default value is 5 minutes. Only consider
        // multiple opens if more than five minutes has elapsed since the last open
        if (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - entity.getUpdatedAt().toEpochSecond(ZoneOffset.UTC)
            >= leadsProps.getOpenRateThreshold()) {
          entity.setNumOpened(entity.getNumOpened() + 1);
          entity.setUpdatedAt(LocalDateTime.now());
          emailTrackingRepo.save(entity);
          log.info("Email opened campaignId={}\t email={}", campaignId, email);
        }

      } else {
        entity = new EmailTrackingEntity();
        entity.setCampaignId(campaignId);
        entity.setEmail(email);
        if (ipAddress != null) {
          entity.setIpAddress(ipAddress);
          emailTrackingRepo.save(entity);
        }
      }
    } else {
      log.warn(this.getClass().getSimpleName()+ ".getUserEmailImage: Failed to parse image hash '{}'",
          decodedHash);
    }
  }

}
