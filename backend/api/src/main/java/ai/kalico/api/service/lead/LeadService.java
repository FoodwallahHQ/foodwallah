package ai.kalico.api.service.lead;

import com.kalico.model.ChannelPageableResponse;
import com.kalico.model.CreateEmailCampaignRequest;
import com.kalico.model.EmailCampaignMetrics;
import com.kalico.model.GenericResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Biz Melesse created on 2/5/23
 */
public interface LeadService {

  /**
   * Get full channel details from YouTube for channels that show up for a given search
   * query. Take the resulting channel details and scrape the email from the public
   * Facebook Page of the channel, if available.
   *
   * @param query a YouTube search query, e.g. how to make egg fried rice
   * @return
   */
  ChannelPageableResponse getChannelInfo(String query);


  /**
   * Return a single-pixel image. The image is requested whenever the user opens a cold
   * email that we sent out. Decode the image hash and save the contents to the database for
   * analytics.
   *
   * NB: We generate the image url and embed it in the body of the email in the Python script
   * used for lead generation.
   *
   * @param imageHash a Base64-encoded recipient email address
   * @return
   */
  byte[] getUserEmailImage(String imageHash, HttpServletRequest httpServletRequest);

  /**
   * Get email tracking metrics for client-side visualization
   * @return
   */
  EmailCampaignMetrics getEmailCampaignMetrics();

  GenericResponse createEmailCampaign(CreateEmailCampaignRequest createEmailCampaignRequest);

}
