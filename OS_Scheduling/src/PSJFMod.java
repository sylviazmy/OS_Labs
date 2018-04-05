import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
//import java.util.Queue;

public class PSJFMod {
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
	public PSJFMod(ArrayList<Process> processlist, ArrayList<Integer> inputRandom) {
		// TODO Auto-generated constructor stub
		this.PL=processlist;
		this.RandomNum=inputRandom;
		this.runningTime=0;
		this.blockingTime=0;
		this.Throughput=0;
		this.aveTurn=0;
		this.aveWT=0;
		
	}

	public ArrayList<Process> PSJFSchedule(boolean verbFlag) {
		int totalCPUTime=0;
		int cycle=0;
		int num=PL.size();
		boolean runningFlag=false;
		Process runningP = null;
		ArrayList<Process> PL1=new ArrayList<Process>();
		ArrayList<Process> Readylist=new ArrayList<Process>();
		ArrayList<cycleRecord> records= new ArrayList<cycleRecord>();
		
	
		int id=0;
		for(Process p:PL) {
			totalCPUTime+=p.getCPUTime();			
			p.setUnstarted();
			p.id =id;
			id+=1;
			

		}
		cycleRecord rc= new cycleRecord(PL);

		records.add(rc);
		int i=0;

		int t=0;
		Comparator<Process> comparator = new Comparator<Process>() {

			@Override
			public int compare(Process o1, Process o2) {
				// TODO Auto-generated method stub
				if(o1.CpuTimeLeft!=o2.CpuTimeLeft) {
					return o1.CpuTimeLeft-o2.CpuTimeLeft;
				}
				else {
					return o1.id-o2.id;
				}
				
			}
			 
		 };
		 
		while((!checkStop(PL,cycle,verbFlag))) {
			cycle+=1;
			ArrayList <Process> recProcess=new ArrayList<Process>();
			int unterminated=num;
		
			for(int n=0;n<num;n++) {
//				Process rdProc;
				recProcess.add(PL.get(n));
			
				if(PL.get(n).getPosition()=="running") {
					
					if(PL.get(n).CpuTimeLeft>1){

						if(PL.get(n).getBlocksAfter()>1) {

						    PL.get(n).setBlcokedAfter(PL.get(n).getBlocksAfter()-1);
						    this.runningTime+=1;
						    runningFlag=true;
						    runningP=PL.get(n);
						    
					    }
					    else {
						    PL.get(n).setBlocked();						
//						    System.out.print("\nFind I/O burst when blocking a process "+RandomNum.get(i));

						    t=1+RandomNum.get(i)%PL.get(n).getIOBurst();

						    i+=1;
						    PL.get(n).setBlockFor(t);
						    runningFlag=false;
						    Readylist.remove(0);
						    
//						    runningP=null;

						
					}
						int CPUt=PL.get(n).CpuTimeLeft;
					    PL.get(n).setTimeLeft(CPUt-1);
					    
					
					}
					else {
						PL.get(n).setTerminate();
						Readylist.remove(0);
						PL.get(n).finishingT=cycle-1;
						PL.get(n).turnaroundT=PL.get(n).finishingT-PL.get(n).arrivalTime;
					    runningFlag=false;
					    unterminated-=1;
					    }
					
					
				}

				else if(PL.get(n).getPosition()=="unstarted") {//只有在剩余CPU时间最短并且已经是unstarted=1的时候就能抢断
					if(PL.get(n).getArrivalT()<cycle) {
						Readylist.add(PL.get(n));
						Collections.sort(Readylist,comparator);
//						System.out.print("    "+Readylist.get(0).id+"   "+Readylist.get(0).CpuTimeLeft);
						if(Readylist.get(0).id==n) {

							PL.get(n).setRunning();
							if(runningP!=null&&runningP.getPosition()=="running") {
								runningP.setReady();
								runningP.setBlcokedAfter(runningP.getBlocksAfter()+1);
								this.runningTime-=1;
							}
							
							runningP=PL.get(n);
							runningFlag=true;
							this.runningTime+=1;
//							System.out.print("\nFind burst when choosing ready process to run "+RandomNum.get(i)+"   "+Readylist.get(0).id);
							
							t=1+RandomNum.get(i)%PL.get(n).getCPUBurst();
							i+=1;
							PL.get(n).setBlcokedAfter(t);
						}else {
							PL.get(n).setReady();
						}
					}
					
					
				}
				else if(PL.get(n).getPosition()=="blocked") {//只要 block==1时并且CPU剩余时间小于当前所有进程的CPU		
					if(PL.get(n).getBlocksFor()<=1) {
						Readylist.add(PL.get(n));
						PL.get(n).setReady();
					}
					else {
						PL.get(n).setBlockFor(PL.get(n).getBlocksFor()-1);
					}
					
					
					
				}

				
				
			
			}
			Collections.sort(Readylist,comparator);
//			System.out.print("\n************size of readylist:     "+Readylist.size());
//			System.out.print("\n    Running Process is "+runningP.id+"   "+runningP.CpuTimeLeft);
//			for(Process p: Readylist) {
//				System.out.print("--"+p.id+" : "+p.CpuTimeLeft);
//			}
			if(Readylist.size()>0) {
			if((Readylist.get(0).getPosition()=="ready")&&Readylist.get(0).id!=runningP.id)
			{
				Readylist.get(0).setRunning();
				this.runningTime+=1;
				
				if(Readylist.get(0).getBlocksAfter()>1) {
					Readylist.get(0).setBlcokedAfter(Readylist.get(0).getBlocksAfter()-1);
					
				}
				else {
//					System.out.print("\nFind burst when choosing ready process to run "+RandomNum.get(i));
//					System.out.print("   This is :"+Readylist.get(0).id+"    Running Process is "+runningP.id);
					
					t=1+RandomNum.get(i)%Readylist.get(0).getCPUBurst();
					i+=1;
					Readylist.get(0).setBlcokedAfter(t);
				}
				if(runningP.getPosition()=="running") {
					runningP.setReady();
					this.runningTime-=1;
					runningP.setBlcokedAfter(runningP.getBlocksAfter()+1);
				}
				
				runningP=Readylist.get(0);
				runningFlag=true;
//				Readylist.remove(0);
			}
			else if((Readylist.get(0).getPosition()=="ready")&&runningFlag==false) {
//				System.out.println("\nunterminated");
				Readylist.get(0).setRunning();
				this.runningTime+=1;
				if(Readylist.get(0).getBlocksAfter()>1) {
					Readylist.get(0).setBlcokedAfter(Readylist.get(0).getBlocksAfter()-1);
					
				}
				else {
//					System.out.print("\nFind burst when choosing ready process to run "+RandomNum.get(i));
//					System.out.print("   This is :"+Readylist.get(0).id+"    Running Process is "+runningP.id);
					
					t=1+RandomNum.get(i)%Readylist.get(0).getCPUBurst();
					i+=1;
					Readylist.get(0).setBlcokedAfter(t);
				}
				
				
				runningP=Readylist.get(0);
				runningFlag=true;
			}
			
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
				+"\n         Throughput: "+this.ThroughputStr+" processes per hundred cycles"
				+"\n         Average turnaround time: "+aveTurnStr
				+"\n         Average waiting time: "+aveWTStr;
				
		return s;
	}
	
}
