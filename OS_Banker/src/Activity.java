
public class Activity {
	private String action;
	private int taskNO;
	private int delay;
	private int resType;
	private int actedNum;
	Activity(String action,int taskNO,int delay,int resType,int actedNum){
		this.action=action;
		this.taskNO=taskNO;
		this.delay=delay;
		this.resType=resType;
		this.actedNum=actedNum;	
	}
	public String getAction(){
		return this.action;
	}
	public int getDelay() {
		return this.delay;
	}
	public void setDelay(int d) {
		this.delay=d;
	}
	public int getResType() {
		return this.resType;
	}
	public int getResNum() {
		return this.actedNum;
	}
	public void setResNum(int n) {
		this.actedNum=n;
	}
	
	public int getTaskNum() {
		return this.taskNO;
	}
	public void setWaiting() {
		this.action="waiting";
	}
	

}
