# Project-D0020E POA lib
The library is intended to be published to some public java Library site ex Maven.
Currently, the library code is just in a separate repository to emulate having it as an import for our test applications.

The implementation is object based, where you can generate a PoA object.
```java
    public class PoA {

    //sets default values
    private int resourceOwnerID = 0;
    private int transferable = 0;
    private String principalPublicKey = "default";
    private String principalName = "default";
    private String agentPublicKey = "default";
    private String agentName = "default";
    private String signingAlogrithm = "RS256";
    private Date issuedAt;
    private Date expiredAt;
    private String path = "";
    private String metaData = "default";
    }
```
   Using build in methods in ``PoAGen`` to generate the object and using setters to set the variables in the generated PoA object.
   
### Exaples for generating PoA´s
````java
public class example {
    
    PoA poa = PoAGen
            .generateDefault()
            .setTransferable(1)
            .setPrincipalPublicKey(KeyEncodeDecode.stringEncodedKey(PrivateKey))
            .setPrincipalName("bob")
            .setAgentPublicKey(KeyEncodeDecode.stringEncodedKey(PublicKey))
            .setAgentName("agent1");
}
````

This example generates a default PoA and sets the required variables using the setter methods.
Since the setters return the PoA u can call them in one after another like shown above.

````java
public class PoAGen {
    
    public static PoA generate(
            int resourceOwnerID,
            int transferable,
            String principalPublicKey,
            String principalName,
            String agentKey,
            String agentName,
            Date expiredAt,
            String[] metaData) {

        return new PoA(
                resourceOwnerID,
                transferable,
                principalPublicKey,
                principalName,
                agentKey,
                agentName,
                expiredAt,
                metaData);
    }
}
public class example{
    
    PoA testPoA = PoAGen.generate(
            someID,
            transferable,
            "principalPublicKey",
            "principalName",
            "agentKey",
            "agentName",
            new Date(System.currentTimeMillis() + Days(5)),
            SomeStringArray);
}
````
This method in ``PoAGen`` is also an option but since it requires all the variables at once its less flexible and annoying to use.
The roundabout implementation is due to the intended scope of the PoA methods.
This is to prevent direct accuses to the PoA methods to block un-intended utilization of them.

We implemented both the constructor and setter methods to enable developers to use whatever method they find the easiest.

### Example PoA Validation

 Attempts to parse the PoA token using the Key,
 if the key is wrong, or the token has been modified this will false otherwise true.
 This method will also check the PoA path and validate each step.

````java
class PoAValid {
    public static boolean validateRecursively(String PoAToken, key PublicKey) {...};
    public static boolean validate(String PoAToken, key PublicKey) {...};
}

public class example {

    PoA poa = PoAGen
            .generateDefault()
            .setTransferable(1)
            .setPrincipalPublicKey(KeyEncodeDecode.stringEncodedKey(PrivateKey))
            .setPrincipalName("bob")
            .setAgentPublicKey(KeyEncodeDecode.stringEncodedKey(PublicKey))
            .setAgentName("agent1");
    String JWT = poa.exportJWT(PrivateKey);
    
    //both these would in this case return true
    PoAValid.validate(JWT, PublicKey);
    PoAValid.validateRecursively(JWT, PublicKey);
}
````

One method for a simple 2 party validation and one for validating a transferred PoA token.
Both methods take the JWT, and the senders public key.

The intended use of this is for a Resource-owner to validate the PoAs validity before grating whatever resource the Agent is requesting.

### KeyEncodeDecode
Is a class containing helper methods to encode and decode sha256 keys to a string format and back.
_**Encoding**_ works the same for both Private and Public Keys, but _**Decoding**_ requires a different method for the different Key types

# Project-D0020E POA Test application

To understand the testcases you need to understand the Agent class. 
````java
public class Agent  extends Thread{
 private final String agentName;
 private final int agentID;
 private final Key agentPrivateKey;
 private final Key agentPublicKey;
 private final String agentIP;
 private Communications com;
 private final int lastAgent;
 

 // Receive a poa, reconstruct to be able to send it again, decrement transferable and validate
 public PoA recivePoA(int socketNumber, Key previousAgentPubKey){
  //System.out.println(this.agentName + " calling reciveCom");
  String message = this.com.receiveCom(Integer.valueOf(Getters.getPort(this.agentName)));
  //System.out.println(this.agentName + " recived from reciveCom");
  PoA poa = PoAGen.reconstruct(message, previousAgentPubKey);
  print(poa);


  if(poa.getTransferable() > 1 && this.lastAgent == 0){
   PoA encapsulatedPoA = PoAGen.transfer(message, previousAgentPubKey);
   String nextAgent = "agent" + (Integer.parseInt(this.agentName.substring(5, 6)) + 1);
   sendPoA(encapsulatedPoA, this.agentIP, Integer.parseInt(Getters.getPort(nextAgent)));
  }
  System.out.println( "-Result from " + this.agentName + " validating the PoA: " + validatePoA(message, previousAgentPubKey));
  return(poa);
 }

 // Send to recipient
 public void sendPoA(PoA poa,
                     String ip,
                     int portNumber){

  // Converts the PoA to a JasonWebToken
  String jwt = poa.exportJWT(agentPrivateKey);
  // Sends the token to the destination specified by ip and portnumber
  //System.out.println(this.agentName + " Calling transmittCom");
  this.com.transmitCom(jwt, ip, portNumber);
  //System.out.println(this.agentName + " transmittCom finished");
 }
}
````

This is the testcase tests the transferability of the PoA. As you can see in the code we create 4 agents. Where agent0 is the prinicpal 
````java
public class TransferableTest {


    public static void main(String[] args) throws InterruptedException {


        // creating instances of agent0 and agent
        Agent agent0 = new Agent(
                "agent0",
                0,
                "localhost",
                0);

        Agent agent1 = new Agent(
                "agent1",
                1,
                "localhost",
                0);

        Agent agent2 = new Agent(
                "agent2",
                2,
                "localhost",
                0);

        Agent agent3 = new Agent(
                "agent3",
                3,
                "localhost",
                1);

        try {
            agent1.start();
        }catch (Exception e){
            System.out.println("Error when starting agent1: (\"bingo bango det funkar inte\") " + e);
            System.exit(0);
        }

        String[] metadata = {};
        Date date =  new Date(System.currentTimeMillis()+ Days(1));

        // Setting valus for the PoA first handed out by the Agent0
        PoA poa = agent0.setValues(0,
                3,
                "agent1",
                date,
                metadata);

        // Send the PoA from the agent0
        agent0.sendPoA(poa, "localhost", 889);

        try {
            agent2.start();
        }catch (Exception e){
            System.out.println("Error when starting agent2: " + e);
            System.exit(0);
        }
        try {
            agent3.start();
        }catch (Exception e){
            System.out.println("Error when starting agent3: " + e);
            System.exit(0);
        }
    }
````
### Application test case: Time out
This test case tests the timeout scenario for the Poa to see whether it can be timed out or not, with the given time.
````java
package Application.TestCases;
import Application.Agent;
import Library.PoA;

import java.util.Date;
import java.util.concurrent.*;

public class TimeOutTest {

    public static void main(String[] args) throws InterruptedException {

        // creating instances of agent0 and agent
        Agent agent0 = new Agent("agent0", 0, "localhost", 0);
        Agent agent1 = new Agent("agent1", 1, "localhost", 1);
        
        try {
            agent1.start();
        }catch (Exception e){
            System.out.println("Error when starting agent1: (\"bingo bango det funkar inte ;(\") " + e);
            System.exit(0);
        }

        String[] metadata = {};
        Date date =  new Date(System.currentTimeMillis()+ Days(0));

        // Setting values for the PoA first handed out by the Agent0
        PoA poa = agent0.setValues(0, 0, "agent0", date, metadata);

        // Run sendPoA() method in a separate thread with a timeout of x seconds
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future= executor.submit(() -> agent0.sendPoA(poa, "localhost", 889));

        try {
            // wait for x sec/days (up to you to decide) for the method to complete
            future.get(5, TimeUnit.SECONDS);
            
        } catch (TimeoutException e) {
            // Handle timeout exception
            System.out.println("send PoA() method time out after x seconds ");
            future.cancel(true); // cancel the task if it's still running
            executor.shutdown(); // Shutdown the executor service
            return; // Exit program


        } catch (InterruptedException | ExecutionException e) {
            // Handle other exceptions
            e.printStackTrace();
            executor.shutdown(); // Shutdown the executor service
            return; // Exit program
        }

        executor.shutdown(); // Shutdown the executor service
    }
    // Creating the correct "long" value for "i" days (86400000 = number of ms for a day)
    private static long Days(int i) {
        return i * 86400000;
    }
}
````


