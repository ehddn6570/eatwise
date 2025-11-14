package domain.restaurant.api;

import domain.restaurant.application.RestaurantService;
import domain.restaurant.dto.request.RestaurantCreateRequest;
import domain.restaurant.dto.request.RestaurantUpdateRequest;
import domain.restaurant.dto.response.RestaurantResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(@RequestBody RestaurantCreateRequest request) {
        RestaurantResponse response = restaurantService.createRestaurant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable Long restaurantId) {
        RestaurantResponse response = restaurantService.getRestaurantById(restaurantId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants() {
        List<RestaurantResponse> responses = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<RestaurantResponse>> searchRestaurantsByName(@RequestParam String keyword) {
        List<RestaurantResponse> responses = restaurantService.searchRestaurantsByName(keyword);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search/address")
    public ResponseEntity<List<RestaurantResponse>> searchRestaurantsByAddress(@RequestParam String address) {
        List<RestaurantResponse> responses = restaurantService.searchRestaurantsByAddress(address);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<RestaurantResponse>> getRestaurantsNearby(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5.0") Double radius) {
        List<RestaurantResponse> responses = restaurantService.getRestaurantsNearby(latitude, longitude, radius);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> updateRestaurant(
            @PathVariable Long restaurantId,
            @RequestBody RestaurantUpdateRequest request) {
        RestaurantResponse response = restaurantService.updateRestaurant(restaurantId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
        return ResponseEntity.noContent().build();
    }
}
