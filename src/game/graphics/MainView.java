package game.graphics;

import game.field.Field;
import game.main.Game;
import game.tank.AbstractShell;
import game.tank.AbstractTank;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainView extends View
{
	final int k = 2;
	float x=0, y=0;
	float curx=0, cury=0;
	float oldx = 0, oldy=0;
	float width, height;
	int length = 10; 
	
	Game game;
	
	Bitmap js; // битмэп для джойстика
	
//	Field f; // поле для отрисовки
	
//	AbstractTank t; // танк
	
	int control, cw = 200, ch = 200; // собственно джойстик, его ширина и высота
	
	public MainView(Context c, Field df, int control)
	{
		super(c);
		game = new Game(df, 5, 5, "Normal", this);
		this.control = control;
	}	
	
	public void view()
	{
		invalidate();
	}
	public void onDraw(Canvas c)
	{
		render(c);
	}
	
	public void render(Canvas c)
	{
		renderField(c);	
//		renderTank(game.getMainTank(), c);
		renderTanks(c);
//		renderShells(c);
		renderJoystick(c);
	}	
	
	public void renderField(Canvas s) // рисуем поле
	{
		Paint p = new Paint();
		for(int i = 0; i < game.getField().height; i++)
		{
			for(int j = 0; j < game.getField().width; j++)
			{
				p.setColor(revers(i, j, game.getField()));				
				s.drawRect(j*length*k + x, i*length*k + y, (j+1)*length*k + x, (i+1)*length*k + y, p);
			}
		}
	}
	public void renderTanks(Canvas s)
	{
		Paint p = new Paint();
		p.setColor(Color.GREEN);
		
		for(int q=0;q<game.getField().getHeight();q++)
		{
			for(int w=0;w<game.getField().getWidth();w++)
			{
				if(game.getTField().get(w, q) != null) s.drawRect(w*length*k + x, q*length*k + y, (w+1)*length*k + x, (q+1)*length*k + y, p);
			}
		}
	}
	public void renderShells(Canvas s)
	{
		Paint p = new Paint();
		p.setColor(Color.RED);
		Log.d("000000000", "Rendering");
		Log.d("000000000", "x, y: "+game.getShells().get(0).getGX()+", "+game.getShells().get(0).getGY());
		for(int q=0;q<game.getShells().size();q++)
		{
			Log.d("000000000", "Rendering..");
			float xs = x+game.getShells().get(q).getGX()*length*k+(game.getShells().get(q).getLX()*length*k)/game.getShells().get(q).getSize();
			float ys = y+game.getShells().get(q).getGY()*length*k+(game.getShells().get(q).getLY()*length*k)/game.getShells().get(q).getSize();
			s.drawRect(xs, ys, xs+length*k/game.getShells().get(q).getSize(), ys+length*k/game.getShells().get(q).getSize(), p);
			Log.d("000000000", "Rendered");
		}
	}
	
	
	public void renderJoystick(Canvas s) // добавляем джойстик
	{		
		js = BitmapFactory.decodeResource(getResources(), control);
		s.drawBitmap(js, null, new Rect(0, (int)height - ch, cw, (int)height), null);
	}
	
//	public void renderTank(AbstractTank at, Canvas s) // рисуем танк
//	{
//		Paint p = new Paint();
//		p.setColor(Color.GREEN);
//		s.drawRect(at.getX()*k*length+x, at.getY()*k*length+y, (at.getX()+at.getWidth())*k*length+x, (at.getY()+at.getLength())*k*length+y, p);
//	}
	
	public int revers(int i, int g, Field f) // преобразование числа в соответствующий цвет
	{
		switch(f.get(g, i))
		{
			case 0 : return Color.BLACK;	
			case 1 : return Color.DKGRAY;
			case 2 : return Color.rgb(151, 76, 24);
		}
		return Color.RED;
	}
	public int getLength()
	{
		return length;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) 
	{		
		if(ev.getAction() == MotionEvent.ACTION_MOVE) // сложные потуги с промоткой
		{	
			x = oldx + ev.getX() - curx;
			y = oldy + ev.getY() - cury;
			if(x > 0) x = 0;
			if(y > 0) y = 0;	
			int w = game.getField().width * k * length;
			int h = game.getField().height * k * length;
			if(x < (width - w)) x = width - w;
			if(y < (height - h)) y = height - h;
			invalidate();			
		}
		if(ev.getAction() == MotionEvent.ACTION_DOWN)
		{
			curx = ev.getX();
			cury = ev.getY();
			if(curx < cw && cury > height - ch) // если тыкнул на джойстик
			{
				replaceTank(); // перемещение танка
			}
			
			invalidate();
		}
		if(ev.getAction() == MotionEvent.ACTION_UP)
		{
			oldx = x;
			oldy = y;
		}
		return true;
	}
	
	private void replaceTank()
	{
		int turn = controlAction(curx, cury);
		if(turn == 1)
		{
			game.getMainTank().goUp();
		}
		if(turn == 2)
		{
			game.getMainTank().goRight();
		}
		if(turn == 3)
		{
			game.getMainTank().goDown();
		}
		if(turn == 4)
		{
			game.getMainTank().goLeft();
		}
	}
	
	private int controlAction(float curx, float cury) // проверяем куда именно нажали на джойстике
	{
		int x1 = 0; int x2 = cw;
		int y1 = (int)height - ch; int y2 = (int)height;
		int mainDiagonal = (int)((curx - x1) * (y2 - y1) - (cury - y1) * (x2 - x1));
		int pobDiagonal = (int)((curx - x1) * (y1 - y2) - (cury - y2) * (x2 - x1));
		if(mainDiagonal > 0 && pobDiagonal > 0)
			return 1;
		if(mainDiagonal > 0 && pobDiagonal < 0)
			return 2;
		if(mainDiagonal < 0 && pobDiagonal < 0)
			return 3;
		return 4;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) // формируем размеры экрана
	{		
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = h;
	}
	
}
