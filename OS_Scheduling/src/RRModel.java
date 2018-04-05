import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
//import java.util.Queue;

public class RRModel {
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
	
	public RRModel(ArrayList<Process> processlist, ArrayList<Integer> inputRandom) {
		// TODO Auto-generated constructor stub
		this.PL=processlist;
		this.RandomNum=inputRandom;
		this.runningTime=0;
		this.blockingTime=0;
		this.Throughput=0;
		this.aveTurn=0;
		this.aveWT=0;
		
	}

	public ArrayList<Process> RRchedule(boolean verbFlag) {
//		int BlocksAfter=0;
//		int BlocksFor=0;
//		boolean blockFlag=false;
		int totalCPUTime=0;
		int cycle=0;
		int num=PL.size();
//		int r=2;
		boolean runningFlag=false;
		ArrayList<Process> Readylist=new ArrayList<Process>();
		ArrayList<cycleRecord> records= new ArrayList<cycleRecord>();
		
//		System.out.print("\n\nThe scheduling algorithm used was RR\n");
		
		for(Process p:PL) {
			totalCPUTime+=p.getCPUTime();			
			p.setUnstarted();

		}
		cycleRecord rc= new cycleRecord(PL);
//		System.out.print("before 0 cycle: "+rc.toString());
		records.add(rc);
		int i=0;
//		Process p =Readylist.poll();
//		BlocksAfter=1+RandomNum.get(i)%p.getCPUBurst();
//		
//		int CBurst=p.setBlcokedAfter(BlocksAfter);
		
		int t=0;
		

		while((!checkStop(PL,cycle,verbFlag))) {
			
//			rc= new cycleRecord(PL);
			
//			System.out.print("\nBefore cycle   "+cycle+": "+rc.toString());
			cycle+=1;
			ArrayList <Process> recProcess=new ArrayList<Process>();
			for(int n=0;n<num;n++) {
//				System.out.print("============real blocks after:"+PL.get(n).realBlocksAfter);	
				
				recProcess.add(PL.get(n));

				if(PL.get(n).getPosition()=="running") {
					int rAft=PL.get(n).realBlocksAfter;
					int r=PL.get(n).getBlocksAfter();
					
					if(PL.get(n).CpuTimeLeft>1){
						
//						System.out.print(rAft+"\n");
						if(rAft<=1) {							
							PL.get(n).setBlocked();														
//							System.out.print("\nFind I/O burst when blocking a process "+RandomNum.get(i));
							t=1+RandomNum.get(i)%PL.get(n).getIOBurst();							
							i+=1;
							PL.get(n).setBlockFor(t);
//							PL.get(n).setRealBlockedAfter(rAft-1);
//							PL.get(n).realBlocksAfter=t;
//							PL.get(n).realBlocksAfter=rAft;
//							r=2;
//							PL.get(n).controled=false;
							runningFlag=false;
							
						}
						else {//rAft>1
							if(r<=1) {
								PL.get(n).setReady();
								Readylist.add(PL.get(n));							
//								PL.get(n).realBlocksAfter=rAft;
//								PL.get(n).setRealBlockedAfter(rAft-1);
//								r=2;
								PL.get(n).controled=false;
								runningFlag=false;
							}
							else if(r>1) {	
//								r-=1;
								PL.get(n).setBlcokedAfter(r-1);
								PL.get(n).setRealBlockedAfter(rAft-1);								
								this.runningTime+=1;
								runningFlag=true;
							}
						}
						int CPUt=PL.get(n).CpuTimeLeft;
					    PL.get(n).setTimeLeft(CPUt-1);
				}
				
					else {
						PL.get(n).setTerminate();
						PL.get(n).finishingT=cycle-1;
						PL.get(n).turnaroundT=PL.get(n).finishingT-PL.get(n).arrivalTime;
					    runningFlag=false;
//					    r=2;
//						PL.get(n).realBlocksAfter-=1;
					    }
					
				}
//					else {
//						PL.get(n).setBlocked();
//					}
					
			

				else if(PL.get(n).getPosition()=="unstarted") {
					if(PL.get(n).arrivalTime<cycle) {
						if(runningFlag==false) {
//							if(PL.get(n).arrivalTime<cycle) {
							PL.get(n).setRunning();
							runningFlag=true;
							this.runningTime+=1;
//							System.out.print("\nFind burst when choosing ready process to run -----------------------"+RandomNum.get(i)+"     "+i);
							
							t=1+RandomNum.get(i)%PL.get(n).getCPUBurst();				
//							System.out.print("\nFind burst when choosing ready process to run  "+RandomNum.get(i)+"     "+t);
							i+=1;
							if(t>2) {
							PL.get(n).setBlcokedAfter(2);
							PL.get(n).controled=true;
							PL.get(n).realBlocksAfter=t;
							}
							else {
								PL.get(n).setBlcokedAfter(t);
								PL.get(n).realBlocksAfter=t;
							}
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
//						blockFlag=true;
						PL.get(n).setBlockFor(PL.get(n).getBlocksFor()-1);
//						PL.get(n).waitingT+=1;
//					
					}
					else {
						if((runningFlag!=true)&&Readylist.size()==0) {
					
						PL.get(n).setRunning();
//						blockFlag=false;
						this.runningTime+=1;
//						System.out.print("\nFind burst when choosing ready process to run -----------------------"+RandomNum.get(i));
						
						t= 1+RandomNum.get(i)%PL.get(n).getCPUBurst();
//						System.out.print("\nFind burst when choosing ready process to run  "+RandomNum.get(i)+"     "+t);
						i+=1;
						if(t>2) {
							PL.get(n).setBlcokedAfter(2);
							PL.get(n).realBlocksAfter=t;
							PL.get(n).controled=true;
						}
						else{
							PL.get(n).setBlcokedAfter(t);
							PL.get(n).realBlocksAfter=t;
						}
						runningFlag=true;
						
						}
						else {
							PL.get(n).setReady();
//							if(Readylist.size()==0) {
////								PL.get(n).readyFirst=true;
//							}
//							System.out.print("\nsize before:"+Readylist.size());
//							PL.get(n).waitingT+=1;
							Readylist.add(PL.get(n));
							
						}
					}
				}
				else if(PL.get(n).getPosition()=="ready") {
//					System.out.print("readyFirst:"+PL.get(n).readyFirst+"\n");					
					if(runningFlag!=true) {						
						if(PL.get(n)==Readylist.get(0)) {
							PL.get(n).setRunning();
//							System.out.print("\n Transfer from ready to running, the blocks after flag is :"+PL.get(n).getBlocksAfter());
							this.runningTime+=1;
							runningFlag=true;
							
							Readylist.remove(0);
//							System.out.print("\n real Blocks after:"+PL.get(n).realBlocksAfter);
							if((PL.get(n).realBlocksAfter<=1)) {								
//								System.out.print("\nFind burst when choosing ready process to run -----------------------"+RandomNum.get(i));
								t=(RandomNum.get(i)%PL.get(n).getCPUBurst())+1;
//								System.out.print("\nFind burst when choosing ready process to run  "+RandomNum.get(i)+"     ");
								i+=1;						
					
							}
							else {
								t=PL.get(n).realBlocksAfter-1;
								
							}
							if(t>2) {
								PL.get(n).setBlcokedAfter(2);
								PL.get(n).setRealBlockedAfter(t);
								PL.get(n).controled=true;
								}
							else {
								PL.get(n).setBlcokedAfter(t);
								PL.get(n).setRealBlockedAfter(t);
							}
						

					}
//						else {
//							if(PL.get(n)==Readylist.get(0)) {	
//							PL.get(n).setRunning();
//							runningFlag=true;
//							this.runningTime+=1;
//							t=PL.get(n).realBlocksAfter;
//							if(t>2) {
//								PL.get(n).setBlcokedAfter(2);
//								PL.get(n).controled=true;
//								PL.get(n).realBlocksAfter=t;
//							}
//							else {
//								PL.get(n).setBlcokedAfter(t);
//								PL.get(n).realBlocksAfter=t;
//							}
//							runningFlag=true;
////							PL.get(n).readyFirst=false;
//							Readylist.remove(0);
//							}
//						}
					}
										
					
				}
				
			}

			if(runningFlag==false&&Readylist.size()>0){
				Readylist.get(0).setRunning();
				runningFlag=true;
				this.runningTime+=1;
//				System.out.print("\nFind burst when choosing ready process to run----------------------- "+RandomNum.get(i));	
				if((Readylist.get(0).realBlocksAfter<=1)) {								
//					System.out.print("\nFind burst when choosing ready process to run -----------------------"+RandomNum.get(i));
					t=(RandomNum.get(i)%Readylist.get(0).getCPUBurst())+1;
//					System.out.print("\nFind burst when choosing ready process to run  "+RandomNum.get(i)+"     "+t);
					i+=1;						
		
				}
				else {
					t=Readylist.get(0).realBlocksAfter-1;
					
				}
//				t=1+RandomNum.get(i)%Readylist.get(0).getCPUBurst();
//				System.out.print("\nFind burst when choosing ready process to run  "+RandomNum.get(i)+"     "+t);
//				i+=1;
				if(t>2) {
					Readylist.get(0).setBlcokedAfter(2);
					Readylist.get(0).controled=true;
					Readylist.get(0).setRealBlockedAfter(t);
				}
				else {
					Readylist.get(0).setBlcokedAfter(t);
					Readylist.get(0).setRealBlockedAfter(t);
				}
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
