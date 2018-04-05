import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
//import java.util.Queue;

public class UniModel {
	ArrayList<Process> PL;
	ArrayList<Integer> RandomNum;
	int finishingT;
	int runningTime;
	int blockingTime;
	
	double aveTurn;
	String aveTurnStr;
	double aveWT;
	String aveWTStr;
	String CpuUti;
	String IoUti;
	double Throughput;
	String ThroughputStr;
	
	public UniModel(ArrayList<Process> processlist, ArrayList<Integer> inputRandom) {
		// TODO Auto-generated constructor stub
		this.PL=processlist;
		this.RandomNum=inputRandom;
		this.runningTime=0;
		this.blockingTime=0;
		this.Throughput=0;
		this.aveTurn=0;
		this.aveWT=0;
		
	}

	public ArrayList<Process> Unischedule(boolean verbFlag) {
//		int BlocksAfter=0;
//		int BlocksFor=0;
		boolean finishFlag=true;
		int totalCPUTime=0;
		int cycle=0;
		int num=PL.size();
		boolean runningFlag=false;
		ArrayList<Process> Readylist=new ArrayList<Process>();
		ArrayList<cycleRecord> records= new ArrayList<cycleRecord>();
		
//		System.out.print("\n\nThe scheduling algorithm used was First Come First Served\n");
		
		for(Process p:PL) {
			totalCPUTime+=p.getCPUTime();			
//			Readylist.add(p);
			p.setUnstarted();
//			Readylist.add(p);
//			System.out.print("\narrival time:"+p.arrivalTime);
		}
		cycleRecord rc= new cycleRecord(PL);
//		System.out.print("before 0 cycle: "+rc.toString());
		records.add(rc);
		int i=0;

		int t=0;

		while((!checkStop(PL,cycle,verbFlag))) {

			cycle+=1;
			ArrayList <Process> recProcess=new ArrayList<Process>();
			for(int n=0;n<num;n++) {
				recProcess.add(PL.get(n));
//				if(PL.get(n).getPosition()=="terminated") {
//					continue;
//				}
				if(PL.get(n).getPosition()=="running") {
					if(PL.get(n).CpuTimeLeft>1){
//						System.out.print("\nCPU time left:"+PL.get(n).CpuTimeLeft);
						if(PL.get(n).getBlocksAfter()>1) {

						    PL.get(n).setBlcokedAfter(PL.get(n).getBlocksAfter()-1);
						    this.runningTime+=1;
//						    int CPUt=PL.get(n).CpuTimeLeft;
//						    PL.get(n).setTimeLeft(CPUt-1);
						    runningFlag=true;
						    
					    }
					    else {
						    PL.get(n).setBlocked();						
//						    System.out.print("\nFind I/O burst when blocking a process "+RandomNum.get(i));
						    t=1+RandomNum.get(i)%PL.get(n).getIOBurst();
						    i+=1;
						    PL.get(n).setBlockFor(t);
						    runningFlag=false;
					}
						int CPUt=PL.get(n).CpuTimeLeft;
					    PL.get(n).setTimeLeft(CPUt-1);
					    
					
					}
					else {
						PL.get(n).setTerminate();
						PL.get(n).finishingT=cycle-1;
						PL.get(n).turnaroundT=PL.get(n).finishingT-PL.get(n).arrivalTime;
					    runningFlag=false;
					    finishFlag=true;
					    }
					
				}

				else if(PL.get(n).getPosition()=="unstarted") {
					if(PL.get(n).arrivalTime<cycle) {
						if(runningFlag==false&&finishFlag==true) {
//							if(PL.get(n).arrivalTime<cycle) {
							PL.get(n).setRunning();
							runningFlag=true;
							this.runningTime+=1;
//							System.out.print("\nFind burst when choosing ready process to run "+RandomNum.get(i));
							
							t=1+RandomNum.get(i)%PL.get(n).getCPUBurst();
							i+=1;
							PL.get(n).setBlcokedAfter(t);
							finishFlag=false;
						}
						else {
							PL.get(n).setReady();
							runningFlag=true;
							if(Readylist.size()==0) {
								PL.get(n).readyFirst=true;
							}
							Readylist.add(PL.get(n));
							
						}
					}
//					else {
//						PL.get(n).
//					}
				}
				else if(PL.get(n).getPosition()=="blocked") {
					
					if(PL.get(n).getBlocksFor()>1) {
						PL.get(n).setBlockFor(PL.get(n).getBlocksFor()-1);
//						System.out.print("\nhere!");
					}
					else {
//						if(runningFlag==false) {					
						PL.get(n).setRunning();
//						blockFlag=false;
						this.runningTime+=1;
//						System.out.print("\nFind burst when choosing ready process to run "+RandomNum.get(i));						
						t=1+RandomNum.get(i)%PL.get(n).getCPUBurst();
						i+=1;
						PL.get(n).setBlcokedAfter(t);
						runningFlag=true;						
//						}
					}
				}
				else if(PL.get(n).getPosition()=="ready") {
//					System.out.print("readyFirst:"+PL.get(n).readyFirst+"\n");
					if(runningFlag!=true&&finishFlag==true) {
						if(PL.get(n)==Readylist.get(0)) {						
						PL.get(n).setRunning();
						this.runningTime+=1;
//						System.out.print("\nFind burst when choosing ready process to run "+RandomNum.get(i));
						t=1+RandomNum.get(i)%PL.get(n).getCPUBurst();
						i+=1;
//						System.out.print(" "+t+"--------");
						PL.get(n).setBlcokedAfter(t);
						runningFlag=true;
						finishFlag=false;
//						PL.get(n).readyFirst=false;
						Readylist.remove(0);
//						System.out.print("\nsize after:"+Readylist.size());
//						System.out.print(Readylist.poll().toString());
//						Readylist.get(0).setRF();
//						System.out.print("890raicm:"+Readylist.get(0).toString());
						}
//						else {
//							PL.get(n).waitingT+=1;
//						}
					}
				}
				
			}

			if(runningFlag==false&&Readylist.size()>0&&finishFlag==true){
				Readylist.get(0).setRunning();
				runningFlag=true;
				finishFlag=false;
				this.runningTime+=1;
//				System.out.print("\nFind burst when choosing ready process to run "+RandomNum.get(i));						
				t=1+RandomNum.get(i)%Readylist.get(0).getCPUBurst();
				i+=1;
				Readylist.get(0).setBlcokedAfter(t);
				Readylist.remove(0);
						
//				for(Process p:PL) {
//					if(p.getPosition()=="ready"&&runningFlag==false) {
//						p.setRunning();
//						runningFlag=true;
//						this.runningTime+=1;
//						System.out.print("\nFind burst when choosing ready process to run "+RandomNum.get(i));						
//						t=1+RandomNum.get(i)%p.getCPUBurst();
//						i+=1;
//						p.setBlcokedAfter(t);
//						Readylist.remove(0);
//					}
//				}
			}
			
		}
		for(Process p:PL) {
			this.aveTurn+=p.turnaroundT;
			this.aveWT+=p.waitingT;
//			System.out.print(p.waitingT);
		}
		this.finishingT=cycle-1;
		DecimalFormat df = new DecimalFormat("0.000000");
		CpuUti=df.format((double)runningTime/(double)finishingT);
		IoUti=df.format((double)blockingTime/(double)finishingT);
		
		ThroughputStr=df.format(PL.size()*100/(double)finishingT);
		aveTurnStr=df.format((float)aveTurn/(float)PL.size());
		aveWTStr=df.format((float)aveWT/(float)PL.size());
	
		return PL;


		    
		}
		
    public boolean checkStop(ArrayList<Process> Processes,int cycle,boolean verbFlag) {
    	    boolean flag=true;
    	    for(Process p:Processes) {
    	    	if (p.getPosition()=="terminated") {
    	    		flag=flag&true;
    	    	}
    	    	else {
    	    		flag=flag&false;
    	    	}
    	    		
    	    }
    	    if(!flag) {
//    	    	cycleRecord rc= new cycleRecord(PL);
    	      	cycleRecord 	rc= new cycleRecord(Processes);
    	      	/*
    	      	 * -verbose
    	      	 */
    	    
    	      	if(verbFlag==true) {
    	      		System.out.print("\nBefore cycle   "+cycle+": "+rc.toString());
    	      	}
    	      	else {
    	      		rc.toString();
    	      	}
    			/*
    			 * without -verbose
    			 */
//    	    	rc.toString();
    			if(rc.blFlag) {
    				this.blockingTime+=1;
    			}
    	    }
    	    return flag;//return true if all the CPU left time is zero
		
	
	}
    public String toString() {
		String s="\nSummary Data:"
				+"\n         Finishing time: "+this.finishingT
				+"\n         CPU Utilization: "+this.CpuUti
				+"\n         I/O Utilization: "+this.IoUti
				+"\n         Throughput: "+this.ThroughputStr+" process per hundred cycles"
				+"\n         Average turnaround time: "+aveTurnStr
				+"\n         Average waiting time: "+aveWTStr;
				
		return s;
	}
	
}
