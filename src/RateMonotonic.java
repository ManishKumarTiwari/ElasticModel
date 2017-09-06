import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import javax.swing.JFrame;
public class RateMonotonic
{

                static int sumCPUBurstTime;
                static int lengthOfEachBlock;
                static final int SCREEN_WIDTH=700,SCREEN_HEIGHT=280;
                static final int rectangleUpperPadding=50,rectangleHeight=100;
                static int numberOfProcesses;
                static float CPUBurstTime[],priority[],Time[];
                static BufferedReader br;
                static RateMonotonic obj;
                RateMonotonic(int n,float c[],float time[])
                {
                	numberOfProcesses=n;
                	CPUBurstTime=new float[n];
                	for(int i=0;i<n;i++)
                	CPUBurstTime[i]=c[i];
                	Time=new float[n];
                	priority=new float[n];
                	for(int i=0;i<n;i++){
                    	Time[i]=time[i];
                    	priority[i]=time[i];
                	}
                	
                }
                RateMonotonic(){
                	this.obj=obj;
                }
                public void fun() throws NumberFormatException, IOException
                {
                                drawGanttChart();

                }
               
                public static void drawGanttChart() throws NumberFormatException, IOException
                {
                    sumCPUBurstTime=0;


                    /* calculating the sum of all cpu burst time */
                    for(int i=0;i<numberOfProcesses;i++)
                    {
                        sumCPUBurstTime+=CPUBurstTime[i];
                    }

                    /* now the total width of the screen is to be divided into sumCPUBurstTime equal parts */
                    lengthOfEachBlock=SCREEN_WIDTH/sumCPUBurstTime;

                    drawGanttChartForPriorityScheduling();
                }
                public static void drawGanttChartForPriorityScheduling()
                {
                                new FrameForPriorityScheduling(obj);
                }
}

class FrameForPriorityScheduling extends JFrame
{
                RateMonotonic obj;
                float priority[],Time[],comp[],temp[];
                FrameForPriorityScheduling(RateMonotonic obj)
                {
                                super("Rate Monotonic");
                                this.obj=obj;
                               
                                this.setVisible(true);
                                this.setSize(obj.SCREEN_WIDTH+100, obj.SCREEN_HEIGHT);
                                repaint();
                }
               
                @Override
                public void paint(Graphics g)
                {
                                super.paint(g);
                                priority=obj.priority.clone();
                                Time=obj.Time.clone();
                                comp=obj.CPUBurstTime.clone();
                                temp=obj.CPUBurstTime.clone();
                                this.getContentPane().setBackground(Color.white);
                                int currentTime=0;
                                int leftStart=50;
                   
                                float min;
                                int mini = 0;
                                g=this.getContentPane().getGraphics();
                                g.drawString(""+currentTime,leftStart,obj.rectangleUpperPadding+obj.rectangleHeight+20);
                                int prev=-1,p1=1;
                                int flag=0;
                                for(int j=1;j<=300;j++)
                                {
                                	min=Integer.MAX_VALUE;
                                	for(int i=0;i<obj.numberOfProcesses;i++){
                                		if(priority[i]==Integer.MAX_VALUE&&(j%Time[i]==1)){
                                			priority[i]=Time[i];
                                			comp[i]=temp[i];
                                		}
                                	}
                                	for(int i=0;i<obj.numberOfProcesses;i++)
                                	{
                                		if(min>priority[i])
                                		{
                                			min=priority[i];
                                			mini=i;
                                		}
                                	}
                   
                                	if(min!=Integer.MAX_VALUE){
                                		if(flag==1){
                                			flag=0;
                                			g=this.getContentPane().getGraphics();
                                        	g.drawRect(leftStart,obj.rectangleUpperPadding,obj.lengthOfEachBlock*(j-p1)/2,obj.rectangleHeight);
                                        
                                        	g.drawString("    ",leftStart+5,obj.rectangleUpperPadding+50);
                                        	
                                        	leftStart+=obj.lengthOfEachBlock*(j-p1)/2;
                                        	currentTime+=(j-p1);
                                        	g.drawString(""+currentTime,leftStart,obj.rectangleUpperPadding+obj.rectangleHeight+20);
                                        	p1=j;
                                        	prev=mini;
                                		}
                                		if(prev==-1)
                                    		prev=mini;
                                		
                                	comp[mini]--;
                                	if(comp[mini]==0)
                                	priority[mini]=Integer.MAX_VALUE;
                                	if(prev!=mini){
                                	g=this.getContentPane().getGraphics();
                                	g.drawRect(leftStart,obj.rectangleUpperPadding,obj.lengthOfEachBlock*(j-p1),obj.rectangleHeight);
                                
                                	g.drawString("P"+(prev+1),leftStart+5,obj.rectangleUpperPadding+50);
                                	
                                	leftStart+=obj.lengthOfEachBlock*(j-p1);
                                	currentTime+=(j-p1);
                                	g.drawString(""+currentTime,leftStart,obj.rectangleUpperPadding+obj.rectangleHeight+20);
                                	p1=j;
                                	prev=mini;
                                }
                                	}
                                	else{
                                		if(mini!=-1){
                                			g.drawRect(leftStart,obj.rectangleUpperPadding,obj.lengthOfEachBlock*(j-p1),obj.rectangleHeight);
                                            
                                        	g.drawString("P"+(prev+1),leftStart+5,obj.rectangleUpperPadding+50);
                                        	
                                        	leftStart+=obj.lengthOfEachBlock*(j-p1);
                                        	currentTime+=(j-p1);
                                        	g.drawString(""+currentTime,leftStart,obj.rectangleUpperPadding+obj.rectangleHeight+20);
                                		}
                                		
                                		if(flag==0)
                                			p1=j;
                                		flag=1;
                                		mini=-1;
                                		prev=-1;
                                	}
                                }
                }
}