import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankerMain {
	private int numOfTask;
	private int numOfResource;
	public ArrayList<Activity> activities;
	
	private String file;
	FIFO fifo;
	Bank banker;
	
	
	public ArrayList<Task> readIn(String input) throws FileNotFoundException {
		
		activities= new ArrayList<Activity>();
		ArrayList<Task> tasks= new ArrayList<Task>();
		FileReader fr=new FileReader(input);
		Scanner scanner = new Scanner(fr);		
		String token;
		
		//read in the first line of assertion:
		String assertion[]= scanner.nextLine().split("\\s+");

	    numOfTask=Integer.parseInt(assertion[0]);
		numOfResource=Integer.parseInt(assertion[1]);
		fifo= new FIFO(numOfTask, numOfResource);
		banker=new Bank(numOfTask, numOfResource);
        //initialize tasks  	
		for(int i=0;i<numOfTask;i++) {
			Task t=new Task(i,numOfResource);
			tasks.add(t);
		}	
		
		//record the activities for each tasks
		while(scanner.hasNext()) {
			String activities = scanner.nextLine();
			if(!activities.contentEquals("")){
			int i=0;
			String acts[]=activities.split("\\s+");
		    String act=acts[0];
		    int taskNum=Integer.parseInt(acts[1]);
		    int delay=Integer.parseInt(acts[2]);
		    int resType=Integer.parseInt(acts[3]);
		    int numActed= Integer.parseInt(acts[4]);
		    Activity a= new Activity(act,taskNum,delay,resType,numActed);
		    tasks.get(taskNum-1).activities.add(a);
//		    System.out.print("\naction:"+act);
			}
		}
		//initialize the available resource in two management algorithms
		for(int i=0;i<numOfResource;i++) {
			fifo.availableRes[i]=Integer.parseInt(assertion[i+2]);
			banker.availableRes[i]=Integer.parseInt(assertion[i+2]);			
		}
		
		return tasks;

	}
	
	public BankerMain(String FilePath) throws FileNotFoundException  {
		ArrayList<Task> t1 =readIn(FilePath);
		this.file=FilePath;
		System.out.println("\n          FIFO");
		fifo.resAlloc(t1);
		
		ArrayList<Task> t2 =readIn(this.file);
		System.out.print("\n         BANKER'S");
	
		banker.resAlloc(t2);
		
		
		
//		
//		for (Activity a:tasks.get(2).activities) {
//			System.out.println(a.action);
//		}
		//start resource management process
		
//		System.out.println(activities.size());

	}
	
	

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		String FilePath="";
		if(args.length >0) {
			FilePath=args[0];
//			file=FilePath;
		}	
		else
			throw new IllegalArgumentException("\n input unfound\n");
		new  BankerMain(FilePath);


	}

}
