import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FIFO {
	int numOfTask;//the number of Task
	int numOfRes;//the number of types of resource
	int[] availableRes;//Available resource array
	int[] tempRes;//to store temporary released resource in each cycle	
	int delayT;//delayed time
	int runningTask;//the number of running Tasks
	
	/*
	 * Constructor
	 */
	FIFO(int numOfTask,int numOfRes)
	{
		this.numOfTask=numOfTask;
		this.numOfRes=numOfRes;
		this.availableRes=new int[numOfRes];
		this.tempRes=new int[numOfRes];	
		this.runningTask=numOfTask;
	}
	
	/*
	 * Check if there is a deadlock
	 */
	private boolean isDeadlock(ArrayList<Activity> blockedAct)
	{
		int i;
		int count=0;
		for(i=0;i<blockedAct.size();i++){
			//only check unfinished or unaborted
			if(blockedAct.get(i).getResNum()>availableRes[blockedAct.get(i).getResType()-1]){
				count+=1;
			}
			
		}		
		//if all tasks cannot be allocated then it is deadlock
		if(count>=this.runningTask&&(blockedAct.size()>1)) {
			return true;
			
		}
		else return false;
	}
	/*
	 * Main logic function
	 */		
	public void resAlloc(ArrayList<Task> tasks) {		
		int cycle=0;
		int waitT=0;
		int timeS=0;
		ArrayList<Task> finishedT= new ArrayList<Task>();//store the tasks which are terminated
		ArrayList<Activity> blockedAct=new ArrayList<Activity>();
		
		while(finishedT.size()<tasks.size())	{//	if terminated tasks are less than original
			cycle+=1;
//			System.out.print("\nDuring "+ (cycle-1)+"-"+cycle);
			for(Task t:tasks) {
				t.setFlag(false);
			}
			
			/*RETURN RESOURCE TO MANAGER*/
			for(int i=0;i<numOfRes;i++) {
				availableRes[i]+=tempRes[i];//add
				tempRes[i]=0;
			}
			
			Activity a;

			int blockedN=blockedAct.size();
			//Check if there is task in block list and could be satisfied by banker
			if(!blockedAct.isEmpty()) {
//				System.out.print("\nfirst check blocked tasks:"+blockedAct.size());
				int temp=0;
				for(int c=0;c<blockedN;c++) {
					int resTyp=blockedAct.get(temp).getResType();
					int taskN=blockedAct.get(temp).getTaskNum();
					if(availableRes[resTyp-1]<blockedAct.get(temp).getResNum()) {
						//manager can't satisfy request then fail to grant
//							System.out.print("\n      Task "+taskN+"'s request cannot be granted");
							temp+=1;
							tasks.get(taskN-1).timeSpending+=1;
							tasks.get(taskN-1).timeWaiting+=1;
							
//						}
					}
					else {
						//manager can satisfy request then grant
						availableRes[resTyp-1]-=blockedAct.get(temp).getResNum();
//						System.out.print("\n      Task "+taskN+"'s pending request is granted       "+blockedAct.get(temp).getAction());
						tasks.get(taskN-1).resource[resTyp-1]+=blockedAct.get(temp).getResNum();
						tasks.get(taskN-1).timeSpending+=1;
						tasks.get(taskN-1).activities.remove(0);
						blockedAct.remove(temp);
					}
					tasks.get(taskN-1).setFlag(true);//this task has been checked in block list

				}
				
			}
			
			
	
			for(Task t :tasks) {	
//				System.out.print("\ntask ID:  "+t.taskId+"    ");
				if(t.activities.isEmpty()) {
					//if the task has terminated
					continue;
				}
				else {
					
					a = t.activities.get(0);
					String action=a.getAction();
					/*INITIATE CLAIMS FOR RESOURCES*/
					if(action.equals("initiate")&&(t.status=="")&&(t.getFlag()==false)) {
						t.setFlag(true);
						t.timeSpending+=1;
//						System.out.print("\n Task "+(t.taskId+1)+" completes its initiate.");
			    	        t.activities.remove(0);
//						}
				     }
					/*REQUEST FOR RESOURCES*/
			         else if(a.getAction().equals("request")&&(t.status=="")&&(t.getFlag()==false)) {
			        	 t.timeSpending+=1;
			        	 t.setFlag(true);
			    	     int d=a.getDelay();
			    	     
		    	         if(d!=0) {
		    	        	//if delay,then wait
//		    	        	 System.out.print("\nTask "+(t.taskId+1)+" computes");
		    	    	     a.setDelay(d-1);
		    	         }
			    	     else {
			    	      	 if(availableRes[a.getResType()-1]>=a.getResNum()) {
			    	      		//if the manager can satisfy the request then grant 
			    	    	    	   availableRes[a.getResType()-1]-=a.getResNum();		
			    	    	    	   t.resource[a.getResType()-1]+=a.getResNum();
//			    	    	    	   System.out.print("\nTask "+(t.taskId+1)+" completes its request.(i.e. the request is granted)    "+a.getAction());
			    	    	    	   t.activities.remove(0);
			    	    	     }
			    	    	     else {
			    	    	    	//if the manager can satisfy the request then add the task activity into blocked list and add up waiting time
//			    	    	    	 System.out.print("\nTask "+(t.taskId+1)+"'s request cannot be granted     ");			    	  
			    	    	    	 blockedAct.add(a);
			    	    	    	 t.timeWaiting+=1;
			    	    	    	 
			    	    	     }

			    	    }
			    }
				/*RELEASE*/
			    else if(a.getAction().equals("release")&&(t.status=="")&&(t.getFlag()==false)) {
			        	t.timeSpending+=1;
			        	t.setFlag(true);
			       	 int d=a.getDelay();
			    	     if(d!=0) {		
//			    	    	 System.out.print("\nDuring "+ (cycle-1)+"-"+cycle+" task "+(t.taskId+1)+" computes");
			    	    	 a.setDelay(d-1);
			    	     }
			    	     else {
			    	    	 
			    	    	 tempRes[a.getResType()-1]+=t.resource[a.getResType()-1];
			    	    	 t.resource[a.getResType()-1]=0;
//			    	    	 System.out.print("\nTask "+(t.taskId+1)+" releases and is finished (at "+cycle+")");
			    	    	 t.activities.remove(0);
			    	     }
			    	     
			    	
			    }
				/*TERMINATE*/
			    else if(a.getAction().equals("terminate")&&(t.status=="")&&(t.getFlag()==false)) {			  
			    	int d=a.getDelay();
		    	    if(d!=0) {
		    	    	//if delay then wait
		    	    	 t.timeSpending+=1;
				 t.setFlag(true);
//		    	    	 System.out.print("\nDuring "+ (cycle-1)+"-"+cycle+" task "+(t.taskId+1)+" computes");
		    	    	 a.setDelay(d-1);
		    	     }
		    	     else {
			    	t.setFlag(true);
			    	t.activities.remove(0);
			    	finishedT.add(t);
			    	this.runningTask-=1;
		    	     }
			    }
				
				
			}
		     }
			
			 Comparator<Activity> comparator = new Comparator<Activity>() {
				 //sorting activity
					@Override
					public int compare(Activity o1, Activity o2) {
						// TODO Auto-generated method stub						
						return o1.getTaskNum()-o2.getTaskNum();
					}
			 };
			/*
			 *  Check if it's deadlock
			 */
			if(!blockedAct.isEmpty()) {				
//				System.out.print("\n****check blocked activity****");
				int numOfBA=blockedAct.size();
				for(int i=0;i<numOfBA;i++) {					
				if(isDeadlock(blockedAct)) {
					//if there's deadlock, then sort the task according to the task number, and then abort them until the deadlock removed
					Collections.sort(blockedAct,comparator);
//					System.out.print("\nAccording to the spec task "+blockedAct.get(0).getTaskNum()+" is aborted now and its resources are\n" + 
//    	      		 		"available next cycle " + (cycle-1)+"-"+cycle);
					tasks.get(blockedAct.get(0).getTaskNum()-1).setAborted();
					finishedT.add(tasks.get(blockedAct.get(0).getTaskNum()-1));					
					this.runningTask-=1;
					tasks.get(blockedAct.get(0).getTaskNum()-1).timeSpending=0;
					tasks.get(blockedAct.get(0).getTaskNum()-1).timeWaiting=0;
					//add back the released resource to temporary array
					tempRes[blockedAct.get(0).getResType()-1]+=tasks.get(blockedAct.get(0).getTaskNum()-1).resource[blockedAct.get(0).getResType()-1];
					tasks.get(blockedAct.get(0).getTaskNum()-1).resource[blockedAct.get(0).getResType()-1]=0;
					blockedAct.remove(0);
				}
				}
			}
			
//			System.out.print("\nfinish this round");
		}
		for(Task p:tasks) {
		timeS+=p.timeSpending;
		waitT+=p.timeWaiting;
		p.print();
	}
		
		long percent=Math.round(((double)waitT/(double)timeS)*100);
		System.out.print("Total     "+timeS+"    "+waitT+"    "+percent+"%\n");
	}
}
