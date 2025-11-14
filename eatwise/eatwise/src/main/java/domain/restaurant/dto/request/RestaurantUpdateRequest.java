package domain.restaurant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantUpdateRequest {

    private String name;
    private String address;
    private String phone;
    private Double latitude;
    private Double longitude;
    private String imageUrl;
}
