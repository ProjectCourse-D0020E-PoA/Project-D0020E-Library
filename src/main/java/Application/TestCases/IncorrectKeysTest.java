package Application.TestCases;

import Application.Agent;

public class IncorrectKeysTest {

    public static void main(String[] args) throws InterruptedException {
        Agent agent0 = new Agent(
                "agent0",
                0,
                "localhost",
                0);

        Agent agent1 = new Agent(
                "agent1",
                1,
                "localhost",
                1);
        try {
            agent1.start();
        }catch (Exception e){
            System.out.println("Error when starting agent1: (\"bingo bango det funkar inte ;(\") " + e);
            System.exit(0);
        }
    }

}
