import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class Philosopher implements Runnable 
{
	private ReentrantLock LeftChop;		//Left Chopstick is mutually exclusive
    private ReentrantLock RightChop;	//Right Chopstick is mutually exclusive
    private int Id;

    public AtomicBoolean DoneEating = new AtomicBoolean(false); //Initially the philosophers are not done eating
    private Random EatThink = new Random();						//Randomise the eating and thinking times
    private int Turns = 0;										//Initially set number of times eaten to zero

    public int getId()
    {
          return this.Id;						//Return the philosopher ID when this method is called
    }
    
    public int getTurns()						//Return the number of times eaten when method is called
    {
    	return Turns;
    }

    public Philosopher(int id, ReentrantLock LeftChop, ReentrantLock RightChop) 
    {
            this.Id = id;						//Philosopher ID
            this.LeftChop = LeftChop;			//Philosopher's left chopstick
            this.RightChop = RightChop;			//Philosopher's right chopstick
     }

     public void run() 
     {
    	 while ( !DoneEating.get()) 					//While the philosophers are not done eating
    	 {
    		 try 										//Follow this procedure...
    		 {
    			 Think();								//Think for a random amount of time
                 if (UpLeftChop() && UpRightChop())		//Try to pick up both chopsticks
                 {
                	 EatRice();							//Eat rice for a random amount of time
                 }
                 
                 DownChopsticks();						//Put down the chopsticks after eating
                 
                 } 
    		 	 catch (Exception e) 
    		 	 {
                    e.printStackTrace();
                 }
            }
        }
     
        private void Think() throws InterruptedException 
        {
            System.out.println(String.format("Philosopher %s is thinking for a while...", this.Id));	//Print to console. this.Id = philosopher ID
            System.out.flush();																			//Flush the print writer
            Thread.sleep(EatThink.nextInt(500));														//Think for a random amount of time
        }

        private void EatRice() throws InterruptedException 
        {
            System.out.println(String.format("Philosopher %s is eating rice...", this.Id));				//Print to the console
            System.out.flush();																			//Flush the print writer
            Turns++;																					//Increase the number of times eaten by one
            Thread.sleep(EatThink.nextInt(500));		//Eat for a random amount of time
        }

        private boolean UpLeftChop() throws InterruptedException 
        {
            if (LeftChop.tryLock(500, TimeUnit.MILLISECONDS)) 
            {
                System.out.println(String.format("Philosopher %s has the Left Chopstick", this.Id));	//If the philosopher has the left chopstick for a second, print to console
                System.out.flush();																		//Flush the print writer
                return true;																			//Philosopher has the left chopstick
            }
            return false;																				//Philosopher no longer has the left chopstick
        }

        private boolean UpRightChop() throws InterruptedException 
        {
            if (RightChop.tryLock(500, TimeUnit.MILLISECONDS)) 
            {
                System.out.println(String.format("Philosopher %s has the Right ChopStick", this.Id));	//If the philosopher has the right chopstick for a second, print to console
                System.out.flush();																		//Flush the print writer
                return true;																			//Philosopher has the right chopstick
            }
            return false;																				//Philosopher no longer has the right chopstick
        }

        private void DownChopsticks() 
        {
            if (LeftChop.isHeldByCurrentThread()) 			//If the philosopher is currently holding the left chopstick
            {
                LeftChop.unlock();							//Release the left chopstick
                System.out.println(String.format("Philosopher %s drops the Left ChopStick", this.Id));
                System.out.flush();
            }
            
            if (RightChop.isHeldByCurrentThread()) 			//If the philosopher is currently holding the right chopstick
            {
                RightChop.unlock();							//Release the right chopstick
                System.out.println(String.format("Philosopher %s drops the Right ChopStick", this.Id));
                System.out.flush();
            }
            
        }
    }