package domain.restaurant.dao;

import domain.restaurant.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByNameContaining(String keyword);

    List<Restaurant> findByAddressContaining(String address);

    List<Restaurant> findByLatitudeBetweenAndLongitudeBetween(
            Double minLat, Double maxLat, Double minLon, Double maxLon);
}
