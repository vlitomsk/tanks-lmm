package game.field;
public class Field 
{
	private byte [][] pole; // собственно поле
	public int width, height; // ширина и высота
	
	public Field(byte[][] array)
	{
		pole = array;
		width = array[0].length;
		height = array.length;		
	}
	
	public int get(int x, int y) // возвращает содержимое определенной ячейки
	{
		return pole[y][x];
	}	
	public void explode(int x, int y, int power) // взрыв
	{
//		if(power>)
		pole[y][x] = 0;
		return;
	}
	public boolean isExplodable(int x, int y)
	{
		return pole[y][x] != 0;
	}
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
}
