
public class Process {
	public int arrivalTime;
	public int id;
	public boolean readyFirst;
	public boolean controled;
	public int CpuTimeLeft;
	public int finishingT;
	public int turnaroundT;
	public int IOT;
	public int waitingT;
	private int CPUburst;
	private int CPUtime;
	private int IOburst;	
	private int BlocksAfter;
	private int BlocksFor;
	public int realBlocksAfter;
	private String position;
	public Process(int A,int B,int C,int IO) {
		this.arrivalTime=A;
		this.CPUburst=B;
		this.CPUtime=C;
		this.IOburst=IO;
		this.CpuTimeLeft=C;
		this.position=null;
		this.BlocksAfter=0;
		this.BlocksFor=0;
		this.readyFirst=false;
		this.controled=false;
		this.realBlocksAfter=0;
	}
	public void getProcess() {
		System.out.print(this.arrivalTime+" "+this.CPUburst+" "+this.CPUtime+" "+this.IOburst+"   ");
		
	}
	public int getArrivalT() {
		return this.arrivalTime;
	}
	public int getCPUBurst() {
		return this.CPUburst;
	}
	public int getIOBurst() {
		return this.IOburst;
	}
	
	public int getCPUTime() {
		return this.CPUtime;
	}
	public String getPosition() {
		return this.position;
	}
	public int getBlocksAfter() {
		return this.BlocksAfter;
	}
	public int getBlocksFor() {
		return this.BlocksFor;
	}
	public int setArrivalT(int t) {
		return this.arrivalTime=t;
	}
	public String setUnstarted() {
		return this.position="unstarted";
	}
	public String setRunning() {
		return this.position="running";
		
	}
	public String setBlocked() {
		return this.position="blocked";
		
	}
	public String setReady() {
		return this.position="ready";
		
	}
	public int setTimeLeft(int Tleft) {
		return this.CpuTimeLeft=Tleft;
	}
	public int setBlcokedAfter(int t) {
		return this.BlocksAfter=t;
	}
	public int setBlockFor(int t) {
		return this.BlocksFor=t;
	}
	public String setTerminate() {
		return this.position="terminated";
	}
	public boolean setRF() {
		return this.readyFirst=true;
	}
	public int setRealBlockedAfter(int t) {
		return this.realBlocksAfter=t;
	}
	public String toString() {
		String s="\n         (A,B,C,IO) = ("+this.arrivalTime+","+this.CPUburst+","+this.CPUtime+","
				+this.IOburst+")"+
				"\n         Finishing time: "+this.finishingT+
				"\n         Turnaround time: "+this.turnaroundT+
				"\n         I/O time: "+this.IOT+
				"\n         Waiting time: "+this.waitingT+"\n";
		return s;
	}

}
