package ai.kalico.api.service.user;

import ai.kalico.api.utils.security.firebase.SecurityFilter;
import com.kalico.model.UserProfile;
import com.kalico.model.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Biz Melesse
 * created on 10/17/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final SecurityFilter securityFilter;
  private final UserServiceHelper userServiceHelper;

  @Override
  public UserProfileResponse getOrCreateUserprofileAsync() {
    UserProfile userProfile = securityFilter.getUser();
    userServiceHelper.createDbUserAsync(userProfile);
    return new UserProfileResponse().profile(userProfile);
  }

  @Override
  public UserProfileResponse getOrCreateUserprofile() {
    UserProfile userProfile = securityFilter.getUser();
    userServiceHelper.createDbUser(userProfile);
    return new UserProfileResponse().profile(userProfile);
  }

  @Override
  public void createUser() {
    userServiceHelper.createDbUser(securityFilter.getUser());
  }
}
