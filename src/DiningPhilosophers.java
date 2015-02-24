import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers 
{
	private static final int Philosophers_No = 5;				//This is the number of philosophers sat around the table
    private static final int DinnerTime = 1000*20;			//This is the length of the dinner ie. how long the simulation will run

    public static void main(String args[]) throws InterruptedException 
    {
    	ExecutorService executorService = null;								//Set execution of main th

        Philosopher[] philosophers = null;									//Set the array to null initially
        try 
        {
        	philosophers = new Philosopher[Philosophers_No];				//Create as many chopsticks as philosophers
        	ReentrantLock[] Chops = new ReentrantLock[Philosophers_No];		//Create reentrant lock array with five empty values.
            Arrays.fill(Chops, new ReentrantLock());						//Fill the array with the five chopsticks

            executorService = Executors.newFixedThreadPool(Philosophers_No);		//Execute the thread five times

            for (int x = 0; x < Philosophers_No; x++) 								//Start at zero, maximum of 4, increment by one 
            {
            	philosophers[x] = new Philosopher(x, Chops[x], Chops[(x + 1) % Philosophers_No]);
                
            	executorService.execute(philosophers[x]);			//Execute philosopher thread
            }

                Thread.sleep(DinnerTime);							//Main thread doesn't start until 'dinner time' has finished
                for (Philosopher philosopher : philosophers) 
                {
                    philosopher.DoneEating.set(true);				//All the philosophers have finished eating
                }

            } 
            finally 
            {
            	executorService.shutdown();						//Shutdown all other threads (philosophers)
            	
                Thread.sleep(1000);								//Wait for all threads to finish

                for (Philosopher philosopher : philosophers) 	//Print information ie. how many times eaten
                {
                    System.out.println("Philosopher " + philosopher.getId()+ " has eaten "+ philosopher.getTurns() + " times");
                    System.out.flush();
                }
            }
    }
}
