import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class schedulerMain {
	int numofProcess=0;
	public schedulerMain(String verbose,String input) throws FileNotFoundException {
		String random="random-numbers";
		boolean verbFlag;
//		System.out.print(verbose);
		if(verbose.equals("-verbose")) {
			verbFlag=true;
		}
		else {
			verbFlag=false;
		}
		ArrayList<String> inputText= new ArrayList<String>();
		ArrayList<Integer> inputRandom= new ArrayList<Integer>();
		ArrayList<Process> Processlist=new ArrayList<Process>();
		ArrayList<Process> FcfsPL=new ArrayList<Process>();
		ArrayList<Process> RrPL=new ArrayList<Process>();
		ArrayList<Process> UniPL= new ArrayList<Process>();
		ArrayList<Process> PsjfPL=new ArrayList<Process>();
		String token;
		Scanner scanner1 = new Scanner(new File(input));		
		String content = scanner1.useDelimiter("\\A").next();//'\A' matches the beginning of the string

		Matcher m= Pattern.compile("[\\d\\w]+").matcher(content);		
		while(m.find()) {			
			token=m.group();
			inputText.add(token);
//			System.out.print(token+" :"+token.length()+"\n");
		}
		Scanner scanner2=new Scanner(new File(random));
		String randomNum=scanner2.useDelimiter("\\A").next();
		Matcher ma=Pattern.compile("[\\d\\w]+").matcher(randomNum);	
		while(ma.find()) {			
			token=ma.group();
			inputRandom.add(Integer.parseInt(token));
//			System.out.print(token+" :"+token.length()+"\n");
		}
		numofProcess=Integer.parseInt(inputText.get(0));
//		System.out.print("");
		int i=0;
		System.out.print("Select one scheduling method:"+
		"\n 1: First Come First Serve."+
		"\n 2: RR with quantum 2"+
		"\n 3: Uniprogrammed"+
		"\n 4: SRTN (PSJF)");
		int read = 0;
		try {
			   //read = System.in.read();
			   read = (char) System.in.read();
			  }catch(Exception e){
			   e.printStackTrace();
			  }
		System.out.print("The original input was: "+numofProcess+"   ");
		while(i<numofProcess) {
			int A=Integer.parseInt(inputText.get(i*4+1));
			int B=Integer.parseInt(inputText.get(i*4+2));
			int C=Integer.parseInt(inputText.get(i*4+3));
			int IO=Integer.parseInt(inputText.get(i*4+4));
			Process p= new Process(A,B,C,IO);
			
			p.getProcess();
			Processlist.add(p);
			i+=1;
		}
		 Comparator<Process> comparator = new Comparator<Process>() {

			@Override
			public int compare(Process o1, Process o2) {
				// TODO Auto-generated method stub
				
				return o1.arrivalTime-o2.arrivalTime;
			}
			 
		 };
		Collections.sort(Processlist,comparator);
		System.out.print("\nThe (sorted) input is:  "+numofProcess+"   ");
		display(Processlist);
		
		
		/*
		 * FCFS
		 */
		if(read=='1') {
		for(Process p:Processlist) {
			FcfsPL.add(p);
			
		}
//		FcfsPL=Processlist;
		FCFSModel fcfs=new FCFSModel(FcfsPL, inputRandom);
		fcfs.FCFSSchedule(verbFlag);
		int n1=0;
		for(Process p:FcfsPL) {
			System.out.print("\nProcess "+n1+":");
			System.out.print(p.toString());
			n1++;
		}
		System.out.print(fcfs.toString());
		}
		/*
		 * RR r=2
		 */
		else if(read =='2') {
		RrPL=Processlist;
		
		RRModel rr=new RRModel(RrPL, inputRandom);
		rr.RRchedule(verbFlag);
		int n2=0;
		System.out.print("\n\nThe scheduling algorithm used was Round Robbin\n");
		for(Process p:RrPL) {
			System.out.print("\n\nProcess "+n2+":");
			System.out.print(p.toString());
			n2++;
		}
		System.out.print(rr.toString());
		}
		/*
		 * Uniprogrammed 
		 */
		if(read=='3') {
		UniPL=Processlist;
		System.out.print("\n\nThe scheduling algorithm used was Uniprocessor\n");
		UniModel u=new UniModel(UniPL, inputRandom);
		u.Unischedule(verbFlag);
		int n3=0;
		
		for(Process p:UniPL) {
			System.out.print("\n\nProcess "+n3+":");
			System.out.print(p.toString());
			n3++;
		}
		System.out.print(u.toString());
		}
		/*
		 * PSJF
		 */
		else if(read=='4') {
		PsjfPL=Processlist;
		System.out.print("\n\nThe scheduling algorithm used was Preemptive Shortest Job First\n");
		PSJFMod Ps=new PSJFMod(PsjfPL, inputRandom);
		Ps.PSJFSchedule(verbFlag);
		
		int n4=0;
		
		for(Process p:PsjfPL) {
			System.out.print("\n\nProcess "+n4+":");
			System.out.print(p.toString());
			n4++;
		}
		System.out.print(Ps.toString());
		}


		
	}
	
	static void display(ArrayList<Process> P){  
        for(Process p:P)  
            p.getProcess(); 
    };

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		String verbose="";
		String FilePath="";
		if(args.length >0) {
			if(args.length==1) {
				FilePath=args[0];
			}
			else if(args.length==2&&args[0].equals("-verbose")) {
				verbose=args[0];
				FilePath=args[1];
			}
			
//		    randomPath=args[1];
		}
		else
			throw new IllegalArgumentException("\n input unfound\n");
		new schedulerMain(verbose,FilePath);

	}

}
