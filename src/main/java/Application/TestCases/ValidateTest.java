package Application.TestCases;

import Application.Agent;
import Library.PoA;

import java.util.Date;

public class ValidateTest extends Thread{

    public static void main(String[] args) throws InterruptedException {




        // creating instances of agent0 and agent
        Agent agent0 = new Agent("agent0",
                0,
                "localhost");
        Agent agent1 = new Agent("agent1",
                1,
                "localhost");
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
                1,
                "agent0",
                date,
                metadata);
        //agent1.wait(1000);
        // Send the PoA from the agent0
        agent0.sendPoA(poa, "localhost", 888);

        // We end by validating the PoA before terminating the thread
        System.out.println("Result check: ");
    }
    // Creating the correct "long" value for "i" days (86400000 = number of ms for a day)
    private static long Days(int i) {
        return i * 86400000;
    }
}
