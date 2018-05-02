// Enter no. of lines through console and draw lines using mouse

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;

public class LineClipping extends JFrame implements MouseListener  
{
	ArrayList<Line> lines;
	int count,noOfLines,state;
	int xSt,ySt,width,height;
	int xL,yA,xR,yB;
	int prevX,prevY;
	Scanner sc=new Scanner(System.in);
	public LineClipping() 
	{
		count=0;
		state=0;
		lines=new ArrayList<Line>();
		System.out.println(" Enter X Start ");
		xSt=sc.nextInt();
		System.out.println(" Enter Y Start ");
		ySt=sc.nextInt();
		System.out.println(" Enter Width  ");
		width=sc.nextInt();
		System.out.println(" Enter Height  ");
		height=sc.nextInt();
		System.out.println(" Enter no. of Lines");
		noOfLines=sc.nextInt();
		xL=xSt;
		yB=ySt;
		xR=xL+width;
		yA=yB+height;
		prevX=0;
		prevY=0;
	}
	public static void main(String args[])throws IOException
	{
		LineClipping l=new LineClipping();
		l.setSize(800,600);
		l.setVisible(true);
		l.addMouse();
		l.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void paint(Graphics g)
	{
		try 
		{
			getInput(g);
		} 
		catch (Exception e) 
		{
		}
	}
	public void getInput(Graphics g)throws IOException
	{
		g.drawRect(xSt, ySt, width, height);
	}
	public int[] getRegionCodes(int x,int y) // ABRL convention
	{
		int abrl[]= {0,0,0,0}; // follow screen convention
		if( x < xL )// L
			abrl[3]=1;
		if( x > xR )// R
			abrl[2]=1;
		if( y < yB )// B 
			abrl[1]=1;
		if( y > yA) //A
			abrl[0]=1;
		return abrl;
	}
	public void clip(Graphics g)
	{
		delay(100);
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);
		g.drawRect(xSt, ySt, width, height);
		g.setColor(Color.RED);
		for(Line l : lines)
		{
			clipline(g,l);
		}
	}
	public void clipline(Graphics g,Line l)
	{
		int x1=l.x1,y1=l.y1,x2=l.x2,y2=l.y2;
		int dx=l.dx,dy=l.dy;
		int reg1[]=getRegionCodes(x1,y1);
		int reg2[]=getRegionCodes(x2, y2);
		int res[]={ reg1[0]&reg2[0] ,reg1[1]&reg2[1],reg1[2]&reg2[2],reg1[3]&reg2[3]};
		while(true)
		{
			if(checkVisibilty(reg1) && checkVisibilty(reg2) )
			{
				g.drawLine(x1, y1, x2, y2);
				break;
			}
			else if(!checkVisibilty(res))
				break;
			else if(checkVisibilty(res))
			{
				int reg[];
				int x=0,y=0;
					
				if(!checkVisibilty(reg1))
					reg=reg1;
				else
					reg=reg2;
				if(reg[0] == 1)
				{
					y=yA;
					if(y2==y1)
					{
						x=x1;
					}
					else
					{
						x=x1+(y-y1)*(x2-x1)/(y2-y1);
					}
				}
				else if(reg[1] == 1)
				{
					y=yB;
					if(y2==y1)
					{
						x=x1;
					}
					else
					{
						x=x1+(y-y1)*(x2-x1)/(y2-y1);
					}
				}
				else if(reg[2]==1)
				{
					x=xR;
					if(x2==x1)
						x=x1;
					else
						y=y1+(x-x1)*(y2-y1)/(x2-x1);
				}
				if(reg[3] == 1)
				{
					x=xL;
					if(x2==x1)
						x=x1;
					else
						y=y1+(x-x1)*(y2-y1)/(x2-x1);
				}
				if(reg == reg1)
				{
					x1=x;
					y1=y;
					reg1=getRegionCodes(x1, y1);
				}
				else
				{
					x2=x;
					y2=y;
					reg2=getRegionCodes(x2, y2);
				}
			}
		}
	}
	public boolean checkVisibilty(int a[])
	{
		for (int i = 0; i < a.length; i++) 
		{
			if(a[i]!=0)
				return false;
		}
		return true;
	}
	public void addMouse()
	{
		addMouseListener(this);	
	}
	public void removeMouse()
	{
		removeMouseListener(this);
	}
	public void delay(int d)
	{
		try 
		{
			Thread.sleep(d);
		} 
		catch (Exception e) 
		{
			System.out.println(e.toString());
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		Graphics g=getGraphics();
		g.setColor(Color.BLUE);
		int x=e.getX();
		int y=e.getY();
		if(state == 0)
		{
			prevX=x;
			prevY=y;
			state=1;
		}
		else 
		{
			if(count < noOfLines)
			{
				Line temp=new Line();
				temp.x1=prevX;
				temp.y1=prevY;
				temp.x2=x;
				temp.y2=y;
				lines.add(temp);
				g.drawLine(prevX,prevY,x,y);
				count++;
				state=0;
			}
		}
		if(count == noOfLines)
		{
			removeMouse();
			clip(g);
		}
	}
	@Override
	public void mousePressed(MouseEvent e){}
	@Override
	public void mouseReleased(MouseEvent e){}
	@Override
	public void mouseEntered(MouseEvent e){}
	@Override
	public void mouseExited(MouseEvent e){}
}
class Line
{
	int x1,y1,x2,y2;
	int dx,dy;
	public Line() 
	{
		x1=0;
		y1=0;
		x2=0;
		y2=0;
		dx=x2-x1;
		dy=y2-y1;
	}
}
