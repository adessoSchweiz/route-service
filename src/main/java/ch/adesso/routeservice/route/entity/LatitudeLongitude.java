package ch.adesso.routeservice.route.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class LatitudeLongitude {
	private Double latitude;
	private Double longitude;
}
