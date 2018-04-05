import java.util.ArrayList;

public class Bank {
	int[] availableRes;//Available resource array
	int[][] maxNeed;//Max needed resource matrix
	int[] tempRes;//to store temporary released resource in each cycle
	int numOfTask;//the number of Task
	int numOfRes;//the number of types of resource
	int delayT;//delayed time
	int runningTaskNum;//the number of running Tasks

	/*
	 * Constructor
	 */
	Bank(int numOfTask,int numOfRes)
	{
		this.numOfTask=numOfTask;
		this.numOfRes=numOfRes;
		this.availableRes=new int[numOfRes];
		this.tempRes=new int[numOfRes];
		this.runningTaskNum=numOfTask;
	}
/*Banker, to check if the available resources could satisfy task*/
	public boolean banker(int taskid,ArrayList<Task> taskList) {
		Task t=taskList.get(taskid-1);
		for(int i=0;i<t.resource.length;i++) {
			if((maxNeed[taskid-1][i]-t.resource[i])>availableRes[i]) {
				return false;
			}
		}
		return true;
	}
/*Main logic function*/	
	public void resAlloc(ArrayList<Task> taskList) {
		int cycle=0;
		int waitT=0;
		int timeS=0;
		ArrayList<Task> finishedT=new ArrayList<Task>();
		ArrayList<Activity> blockedAct=new ArrayList<Activity>();
		Task[] tasks= new Task[numOfTask];
		for(int i=0;i<taskList.size();i++) {
			tasks[i]=taskList.get(i);

		}
		maxNeed=new int[numOfTask][numOfRes];//record the resource the task need	
		while(finishedT.size()<numOfTask) {
//			System.out.print("\n\nDuring "+cycle+"-"+(cycle+1));
			cycle+=1;
			int[] releasedResource=new int[numOfRes];//to store temporary released resource in each cycle	
			for(int r:releasedResource) {
				r=0;
			}
			for(Task t:tasks) {
				t.setFlag(false);//assume the task hasn't been checked in blocked list
			}

			Activity a;
			int blockedN=blockedAct.size();//block list
			
			//Check if there is task in block list and could be satisfied by banker
			if(!blockedAct.isEmpty()) {
//				System.out.print("\nfirst check blocked tasks:"+blockedAct.size());
				int temp=0;
				for(int c=0;c<blockedN;c++) {
					int resTyp=blockedAct.get(temp).getResType();
					int taskN=blockedAct.get(temp).getTaskNum();
					if(!banker(taskN,taskList)) {
						//the status is unsafe then fail to grant
//							System.out.print("\n      Task "+taskN+"'s request cannot be granted  "+availableRes[resTyp-1]);
							temp+=1;
							taskList.get(taskN-1).timeSpending+=1;
							taskList.get(taskN-1).timeWaiting+=1;
							
//						}
					}
					else {
						//the status is safe then grant
						availableRes[resTyp-1]-=blockedAct.get(temp).getResNum();
//						System.out.print("\n      Task "+taskN+"'s pending request is granted       ");
						taskList.get(taskN-1).resource[resTyp-1]+=blockedAct.get(temp).getResNum();
						taskList.get(taskN-1).timeSpending+=1;
						taskList.get(taskN-1).activities.remove(0);
						blockedAct.remove(temp);
					}
					taskList.get(taskN-1).setFlag(true);//this task has been checked in block list
				}
				
			}
			for(Task t:taskList) {
				if(t.activities.isEmpty()) {
					//if the task has terminated
					continue;
				}
				else {
					a= t.activities.get(0);
					String action=a.getAction();
					
					/*INITIATE CLAIMS FOR RESOURCES*/
					if(action.equals("initiate")&&(t.status=="")) {
						 t.setFlag(true);
						if(a.getResNum()>availableRes[a.getResType()-1]) {
							//Initiating fails if it request for more resource than banker has
//							System.out.print("\nTask"+(t.taskId+1)+"  is aborted (claim exceeds total in system)");
							taskList.get(a.getTaskNum()-1).setAborted();
							finishedT.add(t);
							//release all the resource assigned to this task
						}
						else {
							//Initiating succeeds														
//							System.out.print("\nTask "+(t.taskId+1)+" completes its initiate.");
							t.timeSpending+=1;
							maxNeed[a.getTaskNum()-1][a.getResType()-1]=a.getResNum();//initial claim							
							t.activities.remove(0);
						}
					}
					/*REQUEST FOR RESOURCES*/
					else if(action.equals("request")&&(t.status=="")&&(t.getFlag()==false)) {
						int delay=a.getDelay();
						t.timeSpending+=1;
						int taskId=a.getTaskNum();
						int rType=a.getResType();
						t.setFlag(true);
						if(delay>0) {
							//if delay,then wait
//							 System.out.print("\nTask "+(t.taskId+1)+" computes");
							 a.setDelay(delay-1);

						}
						else {
							if(a.getResNum()	+t.resource[a.getResType()-1]>maxNeed[taskId-1][rType-1]) {
								//if the task request for more resource than it had claimed,
								//release all the resource assigned to this task
//								System.out.print("\nrefused");
								t.timeSpending=0;
								taskList.get(a.getTaskNum()-1).setAborted();
								finishedT.add(t);
								availableRes[a.getResType()-1]+=t.resource[a.getResType()-1];
								
								
							}
							else if(!banker(taskId,taskList)) {
								//if banker can't satisfy requests then refuse
								//allocate the resource
//								System.out.print("\nTask "+(t.taskId+1)+"'s request cannot be granted(state would not be safe)");								
								 t.timeWaiting+=1;
								 blockedAct.add(a);
							
							}
							else {
//								System.out.print("\n allocate it");
								availableRes[a.getResType()-1]-=a.getResNum();
								t.resource[a.getResType()-1]+=a.getResNum();
//								System.out.print("\nTask "+(t.taskId+1)+" completes its request.(i.e. the request is granted)    ");
		    	    	    	            t.activities.remove(0);
							}
							
						}
						
					
					}
					/*RELEASE*/
					else if(action.equals("release")&&(t.status=="")&&(t.getFlag()==false)) {
						t.timeSpending+=1;
						t.setFlag(true);
						int delay=a.getDelay();
						if(delay!=0) {
//							System.out.print("\nDuring "+ (cycle-1)+"-"+cycle+" task "+(t.taskId+1)+" computes");
							a.setDelay(delay-1);
						}
						else {
							tempRes[a.getResType()-1]+=t.resource[a.getResType()-1];
							t.resource[a.getResType()-1]=0;
//							System.out.print("\nTask "+(t.taskId+1)+" releases. (at "+cycle+")");
			    	    	        t.activities.remove(0);
						}
						
					}
					/*TERMINATE*/
					else if(action.equals("terminate")&&(t.status=="")&&(t.getFlag()==false)) {
						t.setFlag(true);
						int delay=a.getDelay();
						if(delay!=0) {
							//if delay,then wait
							t.timeSpending+=1;
//							System.out.print("\nDuring "+ (cycle-1)+"-"+cycle+" task "+(t.taskId+1)+" computes");
			    	    	        a.setDelay(delay-1);
						}
						else {
					    	t.setFlag(true);
					    	t.activities.remove(0);
					    	finishedT.add(t);
					   // 	this.runningTask-=1;
				    	     }
						
					}				
				}
				
			}
			
			
			
			/*RETURN RESOURCE TO MANAGER*/
			for(int i=0;i<numOfRes;i++) {
				availableRes[i]+=tempRes[i];
				tempRes[i]=0;
			}
			
		}
		System.out.print("\n");
		for(Task p:tasks) {
		timeS+=p.timeSpending;
		waitT+=p.timeWaiting;
		p.print();
	}
		long percent=Math.round(((double)waitT/(double)timeS)*100);
		System.out.print("Total     "+timeS+"    "+waitT+"    "+percent+"%\n");
		
	}
	

}
