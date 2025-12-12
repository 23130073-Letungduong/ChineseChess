package GameLogic;

import java.util.ArrayList;
import java.util.List;

import GameLogic.Pieces.*;

public class Board {
    public static final int BOARD_WIDTH = 9;
    public static final int BOARD_HEIGHT = 10;
    private Piece[][] pieces;

    public Board() {
        pieces = new Piece[BOARD_WIDTH][BOARD_HEIGHT];
        initBoard();
    }

    //Ham lay quan co tai toa do
    public Piece getPiece(int x, int y) {
        if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) return null;
        return pieces[x][y];
    }

    // Khoi tao ban co
    private void initBoard() {
        //UP la phe mau den o tren
        pieces[0][0] = new Chariot(Piece.Side.UP); pieces[8][0] = new Chariot(Piece.Side.UP);
        pieces[1][0] = new Horse(Piece.Side.UP);   pieces[7][0] = new Horse(Piece.Side.UP);
        pieces[2][0] = new Elephant(Piece.Side.UP); pieces[6][0] = new Elephant(Piece.Side.UP);
        pieces[3][0] = new Guard(Piece.Side.UP);    pieces[5][0] = new Guard(Piece.Side.UP);
        pieces[4][0] = new General(Piece.Side.UP);
        pieces[1][2] = new Cannon(Piece.Side.UP);   pieces[7][2] = new Cannon(Piece.Side.UP);
        for(int i=0; i<9; i+=2) pieces[i][3] = new Soldier(Piece.Side.UP);

        //DOWN la phe do o duoi
        pieces[0][9] = new Chariot(Piece.Side.DOWN); pieces[8][9] = new Chariot(Piece.Side.DOWN);
        pieces[1][9] = new Horse(Piece.Side.DOWN);   pieces[7][9] = new Horse(Piece.Side.DOWN);
        pieces[2][9] = new Elephant(Piece.Side.DOWN); pieces[6][9] = new Elephant(Piece.Side.DOWN);
        pieces[3][9] = new Guard(Piece.Side.DOWN);    pieces[5][9] = new Guard(Piece.Side.DOWN);
        pieces[4][9] = new General(Piece.Side.DOWN);
        pieces[1][7] = new Cannon(Piece.Side.DOWN);   pieces[7][7] = new Cannon(Piece.Side.DOWN);
        for(int i=0; i<9; i+=2) pieces[i][6] = new Soldier(Piece.Side.DOWN);
    }

    /**
     *Kiem tra tinh hop le cua huong di 
     */
    public boolean isValidMove(Move move) {
        // 1.Kiem tra xem co outrange khong
        if (getPiece(move.getOriginX(), move.getOriginY()) == null) return false;
        if (move.getFinalX() < 0 || move.getFinalX() >= BOARD_WIDTH || 
            move.getFinalY() < 0 || move.getFinalY() >= BOARD_HEIGHT) return false;

        Piece movingPiece = getPiece(move.getOriginX(), move.getOriginY());
        Piece targetPiece = getPiece(move.getFinalX(), move.getFinalY());

        // 2.khong duoc an dong doi 
        if (targetPiece != null && targetPiece.getSide() == movingPiece.getSide()) {
            return false;
        }

        // 3.kieu di chuyen
        move.setValid(true);
        movingPiece.checkPattern(move);
        if (!move.isValid()) return false;

        // 4.vat can(can ma, can tuong, phao long)
        return isPathClear(movingPiece, move, targetPiece);
    }

    // Logic kiểm tra đường đi có bị chặn không
    private boolean isPathClear(Piece piece, Move move, Piece target) {
        String type = piece.getType();
        int dx = move.getDx();
        int dy = move.getDy();

        // LOGIC CẢN MÃ
        if (type.equals("Horse")) {
            if (Math.abs(dx) == 2) { // Đi ngang 2 ô -> check chân mã ở (x+1, y)
                if (getPiece(move.getOriginX() + dx/2, move.getOriginY()) != null) return false;
            } else { // Đi dọc 2 ô -> check chân mã ở (x, y+1)
                if (getPiece(move.getOriginX(), move.getOriginY() + dy/2) != null) return false;
            }
            return true;
        }

        // LOGIC CẢN TƯỢNG (Mắt tượng)
        if (type.equals("Elephant")) {
            if (getPiece(move.getOriginX() + dx/2, move.getOriginY() + dy/2) != null) return false;
            return true;
        }

        // LOGIC XE (Không được có vật cản)
        if (type.equals("Chariot")) {
            return countObstacles(move) == 0;
        }

        // LOGIC PHÁO (Đi thường 0 cản, Ăn quân 1 cản)
        if (type.equals("Cannon")) {
            int obs = countObstacles(move);
            if (target == null) return obs == 0; // Đi thường
            else return obs == 1; // Ăn quân
        }

        return true; // Các quân khác (Tướng, Sĩ, Tốt) đi gần nên không xét cản
    }

    // Đếm số quân cờ nằm giữa điểm đầu và điểm cuối
    private int countObstacles(Move move) {
        int count = 0;
        int x = move.getOriginX();
        int y = move.getOriginY();
        int xDir = Integer.compare(move.getFinalX(), x);
        int yDir = Integer.compare(move.getFinalY(), y);

        x += xDir; y += yDir;
        while (x != move.getFinalX() || y != move.getFinalY()) {
            if (getPiece(x, y) != null) count++;
            x += xDir; y += yDir;
        }
        return count;
    }
    
    public Piece executeMove(Move move) {
        Piece movingPiece = pieces[move.getOriginX()][move.getOriginY()];
        Piece capturedPiece = pieces[move.getFinalX()][move.getFinalY()];
        
        // Di chuyển quân
        pieces[move.getFinalX()][move.getFinalY()] = movingPiece;
        pieces[move.getOriginX()][move.getOriginY()] = null;
        
        // Trả về quân bị ăn (để AI giữ hộ)
        return capturedPiece;
    }

    // --- CẬP NHẬT: undoMove nhận quân cần hồi sinh ---
    public void undoMove(Move move, Piece capturedPiece) {
        Piece movingPiece = pieces[move.getFinalX()][move.getFinalY()];
        
        // Trả quân đi về chỗ cũ
        pieces[move.getOriginX()][move.getOriginY()] = movingPiece;
        
        // Hồi sinh quân bị ăn vào ô đích
        pieces[move.getFinalX()][move.getFinalY()] = capturedPiece;
    }
    
    public List<Move> generateMoves(Piece.Side side) {
        List<Move> validMoves = new ArrayList<>();
        for (int x = 0; x < BOARD_WIDTH; x++) {
            for (int y = 0; y < BOARD_HEIGHT; y++) {
                Piece p = pieces[x][y];
                if (p != null && p.getSide() == side) {
                    for (int i = 0; i < BOARD_WIDTH; i++) {
                        for (int j = 0; j < BOARD_HEIGHT; j++) {
                             Move tryMove = new Move(x, y, i, j);
                             if (isValidMove(tryMove)) {
                                 validMoves.add(tryMove);
                             }
                        }
                    }
                }
            }
        }
        return validMoves;
    }
    
    public List<Piece> getAllPieces() {
        List<Piece> list = new ArrayList<>();
        for (int x = 0; x < BOARD_WIDTH; x++) {
            for (int y = 0; y < BOARD_HEIGHT; y++) {
                if (pieces[x][y] != null) list.add(pieces[x][y]);
            }
        }
        return list;
    }
}
