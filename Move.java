package GameLogic;
import GameLogic.Pieces.Piece;

public class Move {
    private int originX, originY; //bat dau tu diem A
    private int finalX, finalY; //toi diem B
    private boolean valid; //di dung la true, sai theo luat hinh hoc la false

    public Move(int originX, int originY, int finalX, int finalY) {
        this.originX = originX;
        this.originY = originY;
        this.finalX = finalX;
        this.finalY = finalY;
        this.valid = true; //ban dau mac dinh la true
    }

	public int getOriginX() { return originX; }
    public int getOriginY() { return originY; }
    public int getFinalX() { return finalX; }
    public int getFinalY() { return finalY; }

    // Tinh khoang cach theo truc ngang doc
    public int getDx() { return finalX - originX; }
    public int getDy() { return finalY - originY; }

    //Ham kiem tra di chuyen ngang, doc, cheo
    public boolean isHorizontal() { return getDy() == 0; }
    public boolean isVertical() { return getDx() == 0; }
    public boolean isDiagonal() { return Math.abs(getDx()) == Math.abs(getDy()); }
    
	public void setValid(boolean valid) { this.valid = valid; }
    public boolean isValid() { return valid; }

	@Override
	public String toString() {
		return originX + " " + originY + " " + finalX + " " + finalY;
	}
}
