package introsde.dsantoro.endpoint;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.ws.Endpoint;

import introsde.dsantoro.blws.Blws;
import introsde.dsantoro.pcws.PcwsImpl;
import introsde.dsantoro.storagews.Dbws;
import introsde.dsantoro.storagews.DbwsService;

public class PcwsPublisher {
	public static void main(String[] args) throws IllegalArgumentException, IOException, URISyntaxException{
        String PROTOCOL = "http://";
        String HOSTNAME = InetAddress.getLocalHost().getHostAddress();
        if (HOSTNAME.equals("127.0.0.1"))
        {
            HOSTNAME = "localhost";
        }
        String PORT = "6903";
        String BASE_URL = "/ws/storagews";

        if (String.valueOf(System.getenv("PORT")) != "null"){
            PORT=String.valueOf(System.getenv("PORT"));
        }
        
        String endpointUrl = PROTOCOL+HOSTNAME+":"+PORT+BASE_URL;
        System.out.println("Starting storage Service...");
        
        // Check south-bound services
        Dbws dbws = getDbwsHandle();
        Blws adpws = getAdpwsHandle();
        if ( (dbws != null) && (adpws != null) ) {
        	// Proceed with startup
        	Endpoint.publish(endpointUrl, new PcwsImpl(dbws, adpws));        	
            System.out.println("--> Published. Check out "+endpointUrl+"?wsdl");	
        }
        else {
        	// Cannot start dependent services
        	System.out.println("--> ERROR: Not published. Check out dependent services:");
        	System.out.println("----> dbws: " + dbws);
        	System.out.println("----> adpws: " + adpws);
        }
        
    }
	
	private static Blws getAdpwsHandle() {
		final String ADPWS_ENDPOINT = System.getenv("ADPWS_ENDPOINT");
		final String ADPWS_PORT = System.getenv("ADPWS_PORT");
		return new Blws(ADPWS_ENDPOINT, ADPWS_PORT);		
	}

	private static Dbws getDbwsHandle() throws MalformedURLException {
		final String DBWS_ENDPOINT = System.getenv("DBWS_ENDPOINT");
		final String DBWS_PORT = System.getenv("DBWS_PORT");
		String dbwsURL = "http://"+DBWS_ENDPOINT+":"+DBWS_PORT+"/ws/dbws?wsdl";
		DbwsService dbwsService = new DbwsService(new URL(dbwsURL));		
		Dbws dbws = dbwsService.getDbwsImplPort();
		System.out.println("Dbws config: Got a valid endpoint: " + dbwsURL);
		return dbws;		
	}
}
