package ch.adesso.routeservice.route.boundary;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.adesso.routeservice.route.entity.CarType;
import ch.adesso.routeservice.route.entity.LatitudeLongitude;
import ch.adesso.routeservice.route.entity.RouteRequest;

@Path("route")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)

public class RouteResource {

	private final String TOPIC = "route-request-topic";

	@Inject
	private KafkaProducer<String, Object> producer;

	@GET
	@Path("/requests/test")
	public RouteRequest createTestRouteRequest() {
		RouteRequest request = new RouteRequest();
		request.setCarType(CarType.ECONOMIC);
		request.setEstimatedDistance("2 km");
		request.setEstimatedTime("30 min");

		LatitudeLongitude l = new LatitudeLongitude();
		l.setLatitude(123.22);
		l.setLongitude(12121.2);
		request.setFrom(l);
		request.setTo(l);
		request.setId("12222");

		request.setPassengerId("2222");
		request.setNoOfPersons(2);
		request.setPassengerComment("comments");

		return request;
	}

	@POST
	@Path("/requests")
	public Response createRouteRequest(RouteRequest routeRequest) {
		routeRequest.setId(UUID.randomUUID().toString());

		ProducerRecord<String, Object> record = new ProducerRecord<String, Object>(TOPIC, routeRequest.getId(),
				routeRequest);
		Future<RecordMetadata> response = producer.send(record);

		try {
			response.get(1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			return Response.serverError().entity(e.getMessage()).build();
		}

		ObjectMapper mapper = new ObjectMapper();

		try {
			String json = mapper.writeValueAsString(routeRequest);
			return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} catch (JsonProcessingException e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

}
