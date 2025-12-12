package GameLogic.Pieces;
import GameLogic.Move;

public class Guard extends Piece {
    public Guard(Side side) {
        super(side);
        this.type = "Guard";
    }

    @Override
    public void checkPattern(Move move) {
        // Phải đi chéo
        if (!move.isDiagonal()) {
            move.setValid(false);
            return;
        }
        // Chỉ đi 1 ô
        if (Math.abs(move.getDx()) != 1) {
            move.setValid(false);
            return;
        }
        // GIỚI HẠN CUNG
        if (move.getFinalX() < 3 || move.getFinalX() > 5) {
            move.setValid(false);
            return;
        }
        if (side == Side.UP && move.getFinalY() > 2) move.setValid(false);
        if (side == Side.DOWN && move.getFinalY() < 7) move.setValid(false);
    }
}