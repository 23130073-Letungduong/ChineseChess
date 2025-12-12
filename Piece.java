package GameLogic.Pieces;

import GameLogic.Move;

public abstract class Piece {
    protected String type;
    protected Side side;
    protected boolean captured;
    public enum Side { UP, DOWN }

    public Piece(Side side) {
        this.side = side;
    }

    // Phương thức checkPattern: chỉ kiểm tra hình học (geometry)
    public void checkPattern(Move move) {
        move.setValid(true);
    }
   
    public String getType() { return type; }
    public Side getSide() { return side; }
    
    public boolean isCaptured() {
        return captured;
    }
    
    public void setCaptured(boolean captured) {
        this.captured = captured;
    }
    
    @Override
    public String toString() { return this.type + " " + this.side; }
}
