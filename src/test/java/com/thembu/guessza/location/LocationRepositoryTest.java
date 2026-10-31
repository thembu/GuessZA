package com.thembu.guessza.location;


import com.thembu.guessza.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LocationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LocationRepository locationRepository;

    @Test
    void visitedLocationIsExcludedOnlyForThatUser() {
        User alice = makeUser("alice");
        User bob = makeUser("bob");
        Location location = anyLocation();

        locationRepository.saveVisited(location.getId(), alice.getId());

        int all = (int) locationRepository.count();   // ask for every location so random() + LIMIT can't hide anything

        assertThat(locationRepository.findRandomActiveLocations(all, alice.getId()))
                .extracting(Location::getId)
                .doesNotContain(location.getId());

        assertThat(locationRepository.findRandomActiveLocations(all, bob.getId()))
                .extracting(Location::getId)
                .contains(location.getId());
    }


    @Test
    void sameLocationCannotBeVisitedTwiceBySameUser() {
        User alice = makeUser("alice");
        Location location = anyLocation();
        locationRepository.saveVisited(location.getId(), alice.getId());
        locationRepository.saveVisited(location.getId(), alice.getId());   // no exception

        assertThat(visitedCount(alice.getId())).isEqualTo(1);
    }

    @Test
    void  deleteVisitedOnlyResetsThatUser() {
        User alice = makeUser("alice");
        User bob = makeUser("bob");
        Location location = anyLocation();

        locationRepository.saveVisited(location.getId(), alice.getId());
        locationRepository.saveVisited(location.getId(), bob.getId());

        //delete visited for alice

        locationRepository.deleteVisited(alice.getId());

        int all = (int) locationRepository.count();   // ask for every location so random() + LIMIT can't hide anything

        //alice has location available
        assertThat(locationRepository.findRandomActiveLocations(all, alice.getId()))
                .extracting(Location::getId)
                .contains(location.getId());

        //bob does not have active locations
        assertThat(locationRepository.findRandomActiveLocations(all, bob.getId()))
                .extracting(Location::getId)
                .doesNotContain(location.getId());
    }

    @Test
    void visitedLocationIsExcludedOnlyForThatUserInProvinceMode() {
        User alice = makeUser("alice");
        User bob = makeUser("bob");
        Location location = anyLocation();
        String province = location.getProvince().name();   // e.g. "GAUTENG", the location's own province

        locationRepository.saveVisited(location.getId(), alice.getId());

        int all = (int) locationRepository.count();   // more than any province has, so LIMIT can't hide anything

        // alice visited it, so it's gone from her province pool
        assertThat(locationRepository.findRandomLocationsByProvince(province, all, alice.getId()))
                .extracting(Location::getId)
                .doesNotContain(location.getId());

        // bob never visited it, so he still gets it
        assertThat(locationRepository.findRandomLocationsByProvince(province, all, bob.getId()))
                .extracting(Location::getId)
                .contains(location.getId());
    }


    private User makeUser(String nickname) {
        User u = new User();
        u.setNickname(nickname);
        entityManager.persist(u);
        return u;
    }

    private Location anyLocation() {
        return locationRepository.findAll().getFirst();
    }

    private long visitedCount(UUID userId) {
        return ((Number) entityManager.getEntityManager()
                .createNativeQuery("SELECT COUNT(*) FROM visited_locations WHERE user_id = :userId").setParameter("userId", userId)
                .getSingleResult()).longValue();
    }


}
