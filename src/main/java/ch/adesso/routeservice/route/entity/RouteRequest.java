package ch.adesso.routeservice.route.entity;

import org.apache.avro.reflect.Nullable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class RouteRequest {

	private String id;
	private String passengerId;

	private LatitudeLongitude from;
	private LatitudeLongitude to;

	private int noOfPersons;
	private CarType carType;

	@Nullable
	private String passengerComment;

	@Nullable
	private String estimatedTime;

	@Nullable
	private String estimatedDistance;
}
