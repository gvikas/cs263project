package cs263project.cs263project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
/**
 * This is a REST API for other who want to use uChallenge-resources
 * I have not made a POST option for the REST API because the application is a closed application
 * for the users. 
 * @author Vikas
 *
 */
@Path("/json")
public class ChallengesRestAPI {
	
	/**
	 * I have a GET request for the all the challenges which will be in JSON-format when
	 * a developer want to it. 
	 * The idea behind this is to give the top or most popular challenges to the developers who 
	 * to use it for some interesting.
	 * @return
	 */
	@GET
	@Path("/challenges/")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllChallenges(){	
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("ChallengePost").addSort("date", Query.SortDirection.DESCENDING);
		List<Entity> entities = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
		
		List<Map<String,Object>> challenges = new ArrayList<Map<String,Object>>();
		for(Entity entity : entities){
			challenges.add(entity.getProperties());
		}

		return new Gson().toJson(challenges);
	}
}
