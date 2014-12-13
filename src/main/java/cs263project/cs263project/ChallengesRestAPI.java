package cs263project.cs263project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;

@Path("/json")
public class ChallengesRestAPI {
	
	/**
	 * Get all the challenges in Json-format
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
