package GameLogic.Pieces;
import GameLogic.Move;

public class Elephant extends Piece {
    public Elephant(Side side) {
        super(side);
        this.type = "Elephant";
    }

    @Override
    public void checkPattern(Move move) {
        // Đi chéo đúng 2 ô
        if (!move.isDiagonal() || Math.abs(move.getDx()) != 2) {
            move.setValid(false);
            return;
        }
        // KHÔNG ĐƯỢC QUA SÔNG
        if (side == Side.UP && move.getFinalY() > 4) move.setValid(false);
        if (side == Side.DOWN && move.getFinalY() < 5) move.setValid(false);
    }
}
