package introsde.dsantoro.blws;

import java.io.StringReader;
import java.net.URI;
import java.util.Collection;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONObject;

import introsde.dsantoro.dbws.Meal;

public class Blws {
	
	private WebTarget service;
	private String blwsPort = null;
	private String blwsEndpoint = null;
	private final String SERVICE_BASEPATH = "ws/blws/";
	
	public Blws(String blwsEndpoint, String blwsPort) {
		this.blwsPort = blwsPort;
		this.blwsEndpoint = blwsEndpoint;
		
		// Check blws service settings
		if ( (this.blwsPort != null) && (this.blwsEndpoint != null) ) {
			// Setup service
			ClientConfig clientConfig = new ClientConfig();
	        Client client = ClientBuilder.newClient(clientConfig);
	        service = client.target(getBaseURI());
	        System.out.println("Blws config: Got a valid endpoint: " + getBaseURI().toString());
		}
		else {
			System.out.println("ERROR in Blws config: Wrong endpoint: " + getBaseURI().toString());
		}
	}
	
	private URI getBaseURI() {
        return UriBuilder.fromUri("http://" + blwsEndpoint + ":" + blwsPort).build();
    }
	
	public Collection<Meal> searchFoods(String key, int start, int num) {
		Collection<Meal> meals = null;
		String response = service.path(SERVICE_BASEPATH)
		        .queryParam("key",key)
		        .queryParam("start",start)
		        .queryParam("n",num)
		        .request().accept(MediaType.APPLICATION_XML).get().readEntity(String.class);		
		try {
			JAXBContext jc = JAXBContext.newInstance(MealStore.class);
			Unmarshaller um = jc.createUnmarshaller();
	        MealStore mealStore = (MealStore) um.unmarshal(new StringReader(response));
	        meals = mealStore.getData();
	        
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return meals;
	}
	
	public JSONObject checkGoal(JSONObject checkGoalJson) {
		JSONObject goalEval = null;
		Response response = service.path("ws/blws/goalcheck")		        
		        .request().accept(MediaType.APPLICATION_JSON).post(Entity.json(checkGoalJson.toString()));		
		if (response.getStatus() == 201){
			URI goalEvalURI = response.getLocation();
			goalEval = getGoalCheckResource(goalEvalURI);			
		}
		else {
			System.err.println("ERROR: Resource cehckGoal not created. Status returned: " + response.getStatus());		
		}
		return goalEval;
	}

	private JSONObject getGoalCheckResource(URI goalEvalURI) {
		Response response = service.path(goalEvalURI.getPath())		        
		        .request().accept(MediaType.APPLICATION_JSON).get();		
		String goalCheck = response.readEntity(String.class);
		JSONObject goalCheckJSon = new JSONObject(goalCheck);
		JSONObject link = (JSONObject) goalCheckJSon.get("link");
		String goalEvalPath = (String) link.get("uri");		
		//return getGoalEvalResource(goalEvalPath.replace(SERVICE_BASEPATH, ""));
		return getGoalEvalResource(goalEvalPath);
	}

	private JSONObject getGoalEvalResource(String goealEvalPath) {
		Response response = service.path(goealEvalPath)		        
		        .request().accept(MediaType.APPLICATION_JSON).get();		
		String goalEval = response.readEntity(String.class);
		JSONObject goalEvalJSon = new JSONObject(goalEval);			
		return goalEvalJSon;
	}
	
	
}
