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
        }
        catch (Exception e){
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
        System.out.println("Started sendPoa() method in a separate thread");

        try {
            future.get(5, TimeUnit.SECONDS); // wait for x sec/days (up to you to decide) for the method to complete
            System.out.println("sendPoA method completed successfully");

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

        // We end by validating the PoA before terminating the thread
        System.out.println("Result check: ");
    }
    // Creating the correct "long" value for "i" days (86400000 = number of ms for a day)
    private static long Days(int i) {return i * 86400000;}
}