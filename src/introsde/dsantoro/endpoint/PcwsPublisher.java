package introsde.dsantoro.endpoint;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.ws.Endpoint;

import introsde.dsantoro.blws.Blws;
import introsde.dsantoro.pcws.PcwsImpl;
import introsde.dsantoro.storagews.Storagews;
import introsde.dsantoro.storagews.StoragewsService;

public class PcwsPublisher {
	public static void main(String[] args) throws IllegalArgumentException, IOException, URISyntaxException{
        String PROTOCOL = "http://";
        String HOSTNAME = InetAddress.getLocalHost().getHostAddress();
        if (HOSTNAME.equals("127.0.0.1"))
        {
            HOSTNAME = "localhost";
        }
        String PORT = "6905";
        String BASE_URL = "/ws/pcws";

        if (String.valueOf(System.getenv("PORT")) != "null"){
            PORT=String.valueOf(System.getenv("PORT"));
        }
        
        String endpointUrl = PROTOCOL+HOSTNAME+":"+PORT+BASE_URL;
        System.out.println("Starting process centric Service...");
        
        // Check south-bound services
        Storagews storagews = getStoragewsHandle();
        Blws blws = getBlwsHandle();
        if ( (storagews != null) && (blws != null) ) {
        	// Proceed with startup
        	Endpoint.publish(endpointUrl, new PcwsImpl(storagews, blws));        	
            System.out.println("--> Published. Check out "+endpointUrl+"?wsdl");	
        }
        else {
        	// Cannot start dependent services
        	System.out.println("--> ERROR: Not published. Check out dependent services:");
        	System.out.println("----> storagews: " + storagews);
        	System.out.println("----> blws: " + blws);
        }
        
    }
	
	private static Blws getBlwsHandle() {
		final String BLWS_ENDPOINT = System.getenv("BLWS_ENDPOINT");
		final String BLWS_PORT = System.getenv("BLWS_PORT");
		return new Blws(BLWS_ENDPOINT, BLWS_PORT);		
	}

	private static Storagews getStoragewsHandle() throws MalformedURLException {
		final String STORAGEWS_ENDPOINT = System.getenv("STORAGEWS_ENDPOINT");
		final String STORAGEWS_PORT = System.getenv("STORAGEWS_PORT");
		String storagewsURL = "http://"+STORAGEWS_ENDPOINT+":"+STORAGEWS_PORT+"/ws/storagews?wsdl";
		StoragewsService storagewsService = new StoragewsService(new URL(storagewsURL));		
		Storagews storagews = storagewsService.getStoragewsImplPort();
		System.out.println("Storagews config: Got a valid endpoint: " + storagewsURL);
		return storagews;		
	}
}
