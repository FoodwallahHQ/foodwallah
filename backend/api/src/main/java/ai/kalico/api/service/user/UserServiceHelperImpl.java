package ai.kalico.api.service.user;

import ai.kalico.api.data.postgres.entity.UserEntity;
import ai.kalico.api.data.postgres.repo.UserRepo;
import ai.kalico.api.service.utils.SeedData;
import com.kalico.model.UserProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author Biz Melesse created on 12/6/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceHelperImpl implements UserServiceHelper {
  private final UserRepo userRepo;
  private final SeedData seedData;

  @Async
  @Override
  public void createDbUserAsync(UserProfile userProfile) {
    createDbUserHelper(userProfile);
  }

  @Override
  public void createDbUser(UserProfile userProfile) {
    createDbUserHelper(userProfile);
  }

  private void createDbUserHelper(UserProfile userProfile) {
    if (userProfile != null && !ObjectUtils.isEmpty(userProfile.getFirebaseId())) {
      UserEntity entity = userRepo.findByFirebaseId(userProfile.getFirebaseId());
      if (entity == null) {
        UserEntity newUser = new UserEntity();
        newUser.setEmail(userProfile.getEmail());
        newUser.setFirebaseId(userProfile.getFirebaseId());
        newUser.setFullName(userProfile.getName());
        log.info("Creating new user with firebase ID = {}", newUser.getFirebaseId());
        userRepo.save(newUser);

        // Create a demo project for this user
        seedData.createProject(1, newUser.getFirebaseId());
      }
    } else {
      throw new RuntimeException("Please logout and login again to update your account");
    }
  }
}
