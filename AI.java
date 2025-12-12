package GameLogic;

import GameLogic.Pieces.Piece;
import java.util.List;

public class AI {
    private static final int MAX_DEPTH = 3;

    public Move findBestMove(Board board, Piece.Side aiSide) {
        int bestVal = Integer.MIN_VALUE;
        Move bestMove = null;
        List<Move> allMoves = board.generateMoves(aiSide);

        for (Move move : allMoves) {
            // [THAY ĐỔI] Nhận quân bị ăn từ Board
            Piece captured = board.executeMove(move); 
            
            int moveVal = minimax(board, MAX_DEPTH - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false, aiSide);
            
            // [THAY ĐỔI] Đưa lại quân bị ăn cho Board để hoàn tác
            board.undoMove(move, captured);    

            if (moveVal > bestVal) {
                bestVal = moveVal;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int minimax(Board board, int depth, int alpha, int beta, boolean isMaximizing, Piece.Side aiSide) {
        if (depth == 0) return evaluateBoard(board, aiSide);

        Piece.Side currentSide = isMaximizing ? aiSide : (aiSide == Piece.Side.UP ? Piece.Side.DOWN : Piece.Side.UP);
        List<Move> moves = board.generateMoves(currentSide);

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : moves) {
                // [THAY ĐỔI]
                Piece captured = board.executeMove(move);
                int eval = minimax(board, depth - 1, alpha, beta, false, aiSide);
                board.undoMove(move, captured); // [THAY ĐỔI]
                
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : moves) {
                // [THAY ĐỔI]
                Piece captured = board.executeMove(move);
                int eval = minimax(board, depth - 1, alpha, beta, true, aiSide);
                board.undoMove(move, captured); // [THAY ĐỔI]
                
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }

    private int evaluateBoard(Board board, Piece.Side aiSide) {
        int score = 0;
        for (Piece p : board.getAllPieces()) {
            int value = getPieceValue(p);
            if (p.getSide() == aiSide) score += value;
            else score -= value;
        }
        return score;
    }

    private int getPieceValue(Piece p) {
        switch (p.getType()) {
            case "General": return 10000;
            case "Chariot": return 90;
            case "Cannon":  return 45;
            case "Horse":   return 40;
            case "Elephant":return 20;
            case "Guard":   return 20;
            case "Soldier": return 10;
            default: return 0;
        }
    }

	@Override
	public String toString() {
		return "AI [toString()=" + super.toString() + "]";
	}
}