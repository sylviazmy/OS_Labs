import java.util.ArrayList;

public class cycleRecord {
	boolean blFlag;
	private ArrayList<Process> processRec;
	public cycleRecord(ArrayList<Process> Rec){
		this.processRec=Rec;
//		this.blFlag=false;
		
	}
	public String toString() {
		String res= new String();
		blFlag=false;
		for(Process p:this.processRec) {
			
			if(p.getPosition()=="running") {
				p.turnaroundT+=1;
				res+="      "+p.getPosition()+"  "+(p.getBlocksAfter());
			}
			else if(p.getPosition()=="blocked") {
				blFlag=true;
				p.IOT+=1;
			res+="      "+p.getPosition()+"  "+(p.getBlocksFor());
			}
			else if(p.getPosition()=="ready")
			{
				p.waitingT+=1;
				res+="      "+p.getPosition()+"    "+0;
			}
			else {
				res+="      "+p.getPosition()+"  "+0;
			}
		}
		return res+".";
		
	}

}
