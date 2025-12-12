package GameLogic.Pieces;
import GameLogic.Move;

public class Horse extends Piece {
    public Horse(Side side) {
        super(side);
        this.type = "Horse";
    }

    @Override
    public void checkPattern(Move move) {
        int dx = Math.abs(move.getDx());
        int dy = Math.abs(move.getDy());
        // Hình chữ L: (2 ngang, 1 dọc) hoặc (1 ngang, 2 dọc)
        if (!((dx == 1 && dy == 2) || (dx == 2 && dy == 1))) {
            move.setValid(false);
        }
    }
}
