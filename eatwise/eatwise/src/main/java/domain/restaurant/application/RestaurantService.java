package domain.restaurant.application;

import domain.restaurant.dao.RestaurantRepository;
import domain.restaurant.domain.Restaurant;
import domain.restaurant.dto.request.RestaurantCreateRequest;
import domain.restaurant.dto.request.RestaurantUpdateRequest;
import domain.restaurant.dto.response.RestaurantResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Transactional
    public RestaurantResponse createRestaurant(RestaurantCreateRequest request) {
        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .imageUrl(request.getImageUrl())
                .build();

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return RestaurantResponse.from(savedRestaurant);
    }

    public RestaurantResponse getRestaurantById(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with id: " + restaurantId));
        return RestaurantResponse.from(restaurant);
    }

    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(RestaurantResponse::from)
                .collect(Collectors.toList());
    }

    public List<RestaurantResponse> searchRestaurantsByName(String keyword) {
        return restaurantRepository.findByNameContaining(keyword).stream()
                .map(RestaurantResponse::from)
                .collect(Collectors.toList());
    }

    public List<RestaurantResponse> searchRestaurantsByAddress(String address) {
        return restaurantRepository.findByAddressContaining(address).stream()
                .map(RestaurantResponse::from)
                .collect(Collectors.toList());
    }

    public List<RestaurantResponse> getRestaurantsNearby(Double latitude, Double longitude, Double radius) {
        // Simple bounding box calculation (approximate)
        Double latDelta = radius / 111.0; // 1 degree latitude ≈ 111 km
        Double lonDelta = radius / (111.0 * Math.cos(Math.toRadians(latitude)));

        return restaurantRepository.findByLatitudeBetweenAndLongitudeBetween(
                        latitude - latDelta, latitude + latDelta,
                        longitude - lonDelta, longitude + lonDelta)
                .stream()
                .map(RestaurantResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public RestaurantResponse updateRestaurant(Long restaurantId, RestaurantUpdateRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with id: " + restaurantId));

        Restaurant updatedRestaurant = Restaurant.builder()
                .restaurantId(restaurant.getRestaurantId())
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .imageUrl(request.getImageUrl())
                .build();

        Restaurant savedRestaurant = restaurantRepository.save(updatedRestaurant);
        return RestaurantResponse.from(savedRestaurant);
    }

    @Transactional
    public void deleteRestaurant(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new IllegalArgumentException("Restaurant not found with id: " + restaurantId);
        }
        restaurantRepository.deleteById(restaurantId);
    }
}
