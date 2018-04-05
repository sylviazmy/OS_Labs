import java.text.DecimalFormat;
import java.util.ArrayList;

public class Task {
	private int delayT;
	private int runningT;
//	public ArrayList<String> Activity=new ArrayList<String>();
	public int taskId;
	public int delay;
	public int resType;
	public int timeSpending;
	public int timeWaiting;
	public int waitingT;
	public String status;
	public int resource[];
	private int initialClaim[];
	public boolean checkedflag;//record if in this cycle the task has already been checked.
	
	public ArrayList<Activity> activities= new ArrayList<Activity>();
	Task(int id,int numofResource){
		this.taskId+=id;
		this.delay=0;
		this.timeSpending=0;
		this.timeWaiting=0;
		this.status="";
		this.resource= new int[numofResource];
		this.initialClaim=new int[numofResource];
		this.checkedflag=false;
		
	}
	public void setClaim(int i,int n) {
		this.initialClaim[i]=n;
	}
	public int getClaim(int i) {
		return this.initialClaim[i];
	}
	public String getStatus() {
		return this.status;
	}
	public void setTimeSpending(int t) {
		this.timeSpending=t;
	}
	public int getDelayT() {
		for(Activity a:activities) {
			this.delayT+=a.getDelay();
		}
		return this.delayT;
	}
	public void setAborted() {
		this.status="aborted";
	}
	public void setFlag(boolean flag) {
		this.checkedflag=flag;
	}
	public boolean getFlag() {
		return this.checkedflag;
	}
	public void print() {
		if(this.status=="aborted") {
			System.out.print("Task "+(this.taskId+1)+"    aborted\n");
		}
		else {
			DecimalFormat df = new DecimalFormat("####");
			long percent=Math.round(((double)this.timeWaiting/this.timeSpending)*100);
			System.out.print("Task "+(this.taskId+1)+"    "+this.timeSpending+"    "+this.timeWaiting+"    "+percent+"%\n");
		}
	}
	
	
	
}
