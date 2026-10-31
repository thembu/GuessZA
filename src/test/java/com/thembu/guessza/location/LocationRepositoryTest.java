package com.thembu.guessza.location;


import com.thembu.guessza.user.User;
import com.thembu.guessza.location.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

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

        assertThatThrownBy(() -> locationRepository.saveVisited(location.getId(), alice.getId()))
                .isInstanceOf(DataIntegrityViolationException.class);
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


}
