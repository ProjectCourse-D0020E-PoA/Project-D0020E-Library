package Application.TestCases;

import Application.Agent;
import Library.PoA;

import java.util.Date;

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
                "agent0",
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
    // Creating the correct "long" value for "i" days (86400000 = number of ms for a day)
    private static long Days(int i) {
        return i * 86400000;
    }
}
