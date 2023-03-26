package ai.kalico.api.service.utils;

import ai.kalico.api.props.ZenRowsProps;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import ai.kalico.api.props.InstagramProps;
import ai.kalico.api.service.scraper.ErrorListener;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;

/**
 * @author Bizuwork Melesse
 * created on 6/22/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScraperUtils {
    private final ErrorListener errorListener;
    private final InstagramProps instagramProps;

    private final ZenRowsProps zenRowsProps;

    public WebClient getWebClient() {
        WebClient webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setTimeout(60000);
        webClient.setJavaScriptErrorListener(errorListener.javaScriptErrorListener());

        String sessionName = "sessionid";
        webClient.getCookieManager().addCookie(new Cookie("www.instagram.com", sessionName, instagramProps.getSessionId()));
        webClient.getCookieManager().addCookie(new Cookie("i.instagram.com", sessionName, instagramProps.getSessionId()));
        webClient.addRequestHeader("user-agent", instagramProps.getWebUserAgent());
        webClient.addRequestHeader("pragma", "no-cache");
        webClient.addRequestHeader("cache-control", "no-cache");
        return webClient;
    }

    public URI getZenRowsUri(String url, boolean jsRender, boolean premiumProxy, boolean autoParse) {
        try {
            return new URIBuilder()
                .setScheme("https").setHost(zenRowsProps.getHost()).setPath(zenRowsProps.getPath())
                .setParameter("apikey", zenRowsProps.getApikey())
                .setParameter("url", url)
                .setParameter("js_render", jsRender ? "true" : "false")
                .setParameter("premium_proxy", premiumProxy ? "true": "false")
                .setParameter("autoparse", autoParse ? "true" : "false")
                .build();
        } catch (URISyntaxException e) {
            log.error("ScraperUtils.getIGZenRowsUri {}", e.getLocalizedMessage());
        }
        return null;
    }
}
