import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
class Task{
	private float Ci;
	private float Tmin;
	private float Tmax;
	private float Ti;
	private float elasticCoeff;
	private float Uio;
	private float Umin;
	private float Ui;
	public float getUi() {
		return Ui;
	}
	public void setUi(float ui) {
		Ui = ui;
	}
	public float getUmin() {
		return Umin;
	}
	public void setUmin(float umin) {
		Umin = umin;
	}
	public float getUio() {
		return Uio;
	}
	public void setUio(float uio) {
		Uio = uio;
	}
	Task(float Ci,float Tmin,float Tmax,float elasticCoeff,float Ti){
		this.Ci=Ci;
		this.Tmin=Tmin;
		this.Tmax=Tmax;
		this.elasticCoeff=elasticCoeff;
		this.Ti=Ti;
	}
	public float getTi() {
		return Ti;
	}
	public void setTi(float ti) {
		Ti = ti;
	}
	public float getCi() {
		return Ci;
	}
	public void setCi(float ci) {
		Ci = ci;
	}
	public float getTmin() {
		return Tmin;
	}
	public void setTmin(float tmin) {
		Tmin = tmin;
	}
	public float getTmax() {
		return Tmax;
	}
	public void setTmax(float tmax) {
		Tmax = tmax;
	}
	public float getElasticCoeff() {
		return elasticCoeff;
	}
	public void setElasticCoeff(float elasticCoeff) {
		this.elasticCoeff = elasticCoeff;
	}
}

public class Elastic {
	public static boolean solve(Task task[],float Ud){
		float Umin=0;
		HashSet<Task> set = new HashSet<Task>();
		for(int i=0;i<task.length;i++){
			Umin+=(task[i].getCi()/task[i].getTmax());
			task[i].setUio(task[i].getCi()/task[i].getTmin());
			task[i].setUmin(task[i].getCi()/task[i].getTmax());
			set.add(task[i]);
		}
		if(Ud<Umin)
		return false;
		int flag;
		do{
			float Uf=0,Uvo=0,Ev=0;
			for (int i=0;i<task.length;i++){
				if((task[i].getElasticCoeff()== 0)||(task[i].getTi()==task[i].getTmax())){
				Uf =Uf+task[i].getUmin() ;
				set.remove(task[i]);
				}
				else{
				Ev =Ev + task[i].getElasticCoeff() ;
				Uvo= Uvo+ task[i].getUio() ;
				}
			}
			flag=1;
			Iterator<Task> it=set.iterator();
			while(it.hasNext()){
				Task temp=it.next();
				if ((temp.getElasticCoeff() > 0)&&(temp.getTi() < temp.getTmax())){
					temp.setUi(temp.getUio()-(Uvo-Ud+Uf)*temp.getElasticCoeff()/Ev);
					temp.setTi(temp.getCi()/temp.getUi()) ;
				if (temp.getTi() > temp.getTmax() ){
					temp.setTi(temp.getTmax());
					flag= 0;
				}
				}
			}
		}while(flag==0);
		return true;
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		int n;
		float c=-1,t1=-1,t2=-1,e=-1,Ud=-1,t3,Umin;
		Scanner in = new Scanner(System.in);
		System.out.println("Enter task set (Γ) ");
		System.out.println("No of tasks : ");
		n=in.nextInt();
		float cp[]=new float[n];
		float tp[]=new float[n];
		Task task[]=new Task[n];
		for(int i=0;i<n;i++){
				System.out.println("Enter details of task "+(i+1));
				c=t1=t2=t3=e=Ud=-1;
			while(c<=0){
				System.out.println("Enter Computation Time : ");
				c=in.nextFloat();
			if(c<=0){
				System.out.println("Computation Time cannot be negative or zero");
			}
			}
			while(t1<=0){
				System.out.println("Enter Nominal Period ( Minimum Period ) Tmin : ");
				t1=in.nextFloat();
			if(t1<=0){
				System.out.println("Nominal period cannot be negative or zero");
			}
			}
			while(t2<=0){
				System.out.println("Enter Maximum Period (Tmax) : ");
				t2=in.nextFloat();
			if(t2<=0){
				System.out.println("Maximum period cannot be negative or zero");
			}
			}
			while(t3<=0){
				System.out.println("Enter Ti ( actual peroiod ) : ");
				t3=in.nextFloat();
			if(t3<=0&&!(t3>=t1&&t3<=t2)){
				System.out.println("Maximum period is not correct");
			}
			}
			while(e<=0){
				System.out.println("Enter Elastic Coefficient: ");
				e=in.nextFloat();
			if(e<0){
				System.out.println("Elasti Coefficient cannot be negative ");
			}
			}
				task[i]=new Task(c,t1,t2,e,t3);
		}
			while(Ud<0||Ud>1){
			System.out.println("Enter Desired Utilization : ");
			Ud=in.nextFloat();
			if(Ud<0&&Ud>1){
				System.out.println("Desired Utilization must be in range of 0 to 1 ");
			}
			}
			if(solve(task,Ud)){
				System.out.println("Scheduling is possible with time period :");
				for(int i=0;i<n;i++){
				System.out.println("Ti"+(i+1)+"\t"+task[i].getTi());
				tp[i]=task[i].getTi();
				cp[i]=task[i].getCi();
				}
				RateMonotonic m = new RateMonotonic(n,cp,tp);
				m.fun();
			}
			else{
				Umin=0;
				System.out.println("No scheduling possible for "+Ud+" desired utilization");
				for(int i=0;i<n;i++){
					Umin+=task[i].getUmin();
				}
				System.out.println(" because minimum utilization is "+Umin);
			}
	}

}
