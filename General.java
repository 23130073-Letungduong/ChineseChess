package GameLogic.Pieces;
import GameLogic.Move;

public class General extends Piece {
    public General(Side side) {
        super(side);
        this.type = "General";
    }

    @Override
    public void checkPattern(Move move) {
        // Chỉ đi ngang hoặc dọc
        if (!move.isHorizontal() && !move.isVertical()) {
            move.setValid(false);
            return;
        }
        // Chỉ đi 1 ô
        if (Math.abs(move.getDx()) > 1 || Math.abs(move.getDy()) > 1) {
            move.setValid(false);
            return;
        }
        // GIỚI HẠN CUNG TƯỚNG (X: 3->5)
        if (move.getFinalX() < 3 || move.getFinalX() > 5) {
            move.setValid(false);
            return;
        }
        // Giới hạn Y theo phe
        if (side == Side.UP && move.getFinalY() > 2) move.setValid(false);
        if (side == Side.DOWN && move.getFinalY() < 7) move.setValid(false);
    }
}
