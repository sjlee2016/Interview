/*
 *
 * Se Jin Lee August 14, 2018
 * PlayPhone Internship Inteview Project
 * Description : A simple multithreading producer and consumer program
 * Producer produces random set of words of random size from the dictionary
 * Consumers concatenate the set, received from the producer 
 * Execute the program by running java "Paths to App.java" <num of threads>
 *
 * */


import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.Scanner;

interface Producer {
    Set<String> produce();
}

interface Consumer {
    String concat(Set<String> words);
}


class DefaultConsumer implements Consumer { 

	public String concat(Set<String> words) //receives Set String and concatenate it into a String
	{
		StringBuffer s = new StringBuffer();
		for(String element : words ) // looping through the Set
		{
			s.append(element); // add the element to the String Buffer
		}
		return s.toString(); // convert StringBuffer to String
	}
	
}

class DefaultProducer implements Producer
{
	private int wordCount;
	private static Set<String> fullDictionary = new HashSet<String>(); 
	public void fillIn() // initialize dictionary.
	{
		String [] wordList = {"banana", "apple", "pineapple", "grape", "pie", "cup", "starbucks", "play", "phone", "love", "orange", "world"};
		for(String word : wordList) // loop through the String Array
		{
			fullDictionary.add(word);  // add each element into the dictionary
		}
		wordCount = wordList.length;
	}
	public static String getRandomWord(int r) // get random word from the dictionary
	{
		int i = 0;
		for(String s : fullDictionary) // loop through dictionary
		{
			if(i==r)  // find rth element
			{
				return s; // return the element
			}
			i++;
		}
		return null;
	}
	public Set<String> produce() {

		Random random = new Random();
		Set<String> newDict = new HashSet<String>(); // create an empty set
		int n = random.nextInt(wordCount - 1) + 1;  // set the random size of Set 
		for(int i =0 ; i<n; i++)
		{
			int n2= random.nextInt(wordCount); // add a random element to the Set
			newDict.add(getRandomWord(n2));
		}
		return newDict; // return the set
	}
}

class Data // consist of Set String. Used to represent the data shared by the producer and consumer
{
	private Set<String> s;
	
	public Data(Set<String> setReceived) // initialize data. copy the elements from setReceived
	{
		s = new HashSet<String>();
		
		for(String ele : setReceived)
		{
			s.add(ele);
		}
	}
	public Set<String> getData() // return data
	{
		return s;
	}
}

public class App
{

	private static BlockingQueue <Data> queue;	
	static int numThread;
    DefaultConsumer [] c;
    
	public static void main(String [] args) throws InterruptedException
	{
        DefaultProducer p = new DefaultProducer();
        DefaultConsumer c = new DefaultConsumer();
        try
        {
          numThread = Integer.parseInt(args[0]); // receives the number of consumer threads through arguments
          queue = new ArrayBlockingQueue<Data>(numThread); // set the size of BlockingQueue
        }
        catch(NumberFormatException nfe) // catches when the argument is not a number
        {
            nfe.printStackTrace();
            System.out.println("The first argument must be the number of consumer threads");
            System.exit(1);
        }
        catch(ArrayIndexOutOfBoundsException e) // catches when no argument is passed
        {
            e.printStackTrace();
            System.out.println("The first argument must be the number of consumer threads");
            System.exit(1);
        }
        p.fillIn(); // initialize the value of dictionary
        Thread [] consumerThread = new Thread [numThread]; // create the array of consumer threads
		Thread producerThread = new Thread(new Runnable() 
		{
			public void run()
			{
				try {
					for(int i = 0; i<numThread; i++)
					{
						queue.put(new Data(p.produce())); // convert Set String(which is randomly selected from dictionary)
														   // into class Data.
														    // then enqueue
					
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
			}
		});
    
		for(int i = 0; i< numThread; i++) // using for loop set up the consumer threads
		{

			consumerThread[i] = new Thread(new Runnable() 
			{
				public void run()
				{
					try {
						Data newD = queue.take(); // dequeue 
						System.out.println(c.concat(newD.getData())); // concatenate the Set and print 
					} catch (InterruptedException e) {
		// TODO Auto-generated catch block
						e.printStackTrace();
						}
					}
				});
					
		}	
		
		producerThread.start();  
        producerThread.join(); 
		
        for(int i =0;i<numThread;i++)
		{
			consumerThread[i].start();
		}
		for(int i =0;i<numThread;i++)
		{
			consumerThread[i].join();
		}
	
	}
	
}
