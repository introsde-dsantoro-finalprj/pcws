package introsde.dsantoro.blws;

import java.io.StringReader;
import java.net.URI;
import java.util.Collection;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;

import introsde.dsantoro.dbws.Meal;

public class Blws {
	
	private WebTarget service;
	private String adpwsPort = null;
	private String adpwsEndpoint = null;
	
	public Blws(String adpwsEndpoint, String adpwsPort) {
		this.adpwsPort = adpwsPort;
		this.adpwsEndpoint = adpwsEndpoint;
		
		// Check adpws service settings
		if ( (this.adpwsPort != null) && (this.adpwsEndpoint != null) ) {
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
        return UriBuilder.fromUri("http://" + adpwsEndpoint + ":" + adpwsPort).build();
    }
	
	public Collection<Meal> searchFoods(String key, int start, int num) {
		Collection<Meal> meals = null;
		String response = service.path("ws/adpws/meals")
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
}
