package com.job;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.model.Job;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobControllerIT {

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @Test
    public void testWorkerJobs() throws Exception {
    	this.base = new URL("http://localhost:" + port + "/api/workers/8/newjobs");
    	
        ResponseEntity<String> response = template.getForEntity(base.toString(),
                String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<Job> myObjects = mapper.readValue(response.getBody(), new TypeReference<List<Job>>(){});
        
        // get 3 jobs for this worker.
        assertTrue(myObjects.size() == 3);
        
        // assert the order based on bill rate.
        assertEquals(17.6, myObjects.get(0).getBillRate(), 0.2);
        assertEquals(15.8, myObjects.get(1).getBillRate(), 0.2);
        assertEquals(15.3, myObjects.get(2).getBillRate(), 0.1);
        
    }
    
    @Test
    public void testWorkerNotFound() throws Exception {
    	//invalid worker id
    	this.base = new URL("http://localhost:" + port + "/api/workers/213/newjobs");
        ResponseEntity<String> response = template.getForEntity(base.toString(),
                String.class);
        
        assertEquals("404", response.getStatusCode().toString());
        assertThat(response.getBody(), 
        		equalTo("{\"status\":404,\"message\":\"Worker Not Found\"}"));
    }
}
