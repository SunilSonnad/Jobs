# Jobs

## Problem
Given a REST API – /workers, which provides the list of all available Workers and /jobs which provides a list of all available Jobs. The task is to produce a REST API match that will take a workerId and return no more than three appropriate jobs for that Worker.

## Solution
SpringBoot application is used as the framework for the REST apis. Spring's RestTemplate client is used to perform the rest calls against the /workers and /jobs. RestTemplate is run in Async mode so that the two calls can be performed parallely. Executors for Rest client calls are configured and the number of threads can be controlled in the AppConfig.

<em>Note:</em> AsyncRestTemplate with ListenableFuture is not used as we have to wait for both the calls to complete anyway. A CountDownLatch is used instead to wait for the completion of the rest calls and then process the results.
Also there is no caching of results from the two calls.

## REST Api
The rest api exposed by this application is <br>
                http://localhost:8080/api/workers/workerid/newjobs <br>
  
