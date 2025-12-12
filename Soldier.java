package GameLogic.Pieces;
import GameLogic.Move;

public class Soldier extends Piece {
    public Soldier(Side side) {
        super(side);
        this.type = "Soldier";
    }

    @Override
    public void checkPattern(Move move) {
        int dy = move.getDy();
        int dx = Math.abs(move.getDx());

        // Khong di lui
        if (side == Side.UP && dy < 0) { move.setValid(false); return; }
        if (side == Side.DOWN && dy > 0) { move.setValid(false); return; }

        //Kiem tra qua song chua
        boolean crossedRiver = false;
        if (side == Side.UP && move.getOriginY() > 4) crossedRiver = true;
        if (side == Side.DOWN && move.getOriginY() < 5) crossedRiver = true;

        // If else qua song 
        if (!crossedRiver) {
            //Chua qua song thi van di chuyen 1 o
            if (dx != 0 || Math.abs(dy) != 1) move.setValid(false);
        } else {
            //Da qua song thi duoc di chuyen ngang 1 o
            if (!((dx == 1 && dy == 0) || (dx == 0 && Math.abs(dy) == 1))) {
                move.setValid(false);
            }
        }
    }
}
