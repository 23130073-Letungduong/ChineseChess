package Test;
import GameLogic.*;
import GameLogic.Pieces.*;
import java.util.Scanner;

public class MainConsole {
    public static void main(String[] args) {
        // 1. Khởi tạo Bàn cờ và AI
        Board board = new Board();
        AI ai = new AI();
        Scanner scanner = new Scanner(System.in);
        
        // 2. Thiết lập Phe
        // Người chơi là ĐỎ (DOWN - Chữ Hoa - Đi trước)
        // Máy là ĐEN (UP - chữ thường)
        Piece.Side humanSide = Piece.Side.DOWN;
        Piece.Side aiSide = Piece.Side.UP;
        
        boolean isHumanTurn = true; // Đỏ đi trước

        System.out.println("=== CỜ TƯỚNG CONSOLE (JAVA) ===");
        System.out.println("Luật: Nhập tọa độ x1 y1 x2 y2 để đi.");
        System.out.println("Ví dụ: '1 9 2 7' (Mã đỏ nhảy lên).");

        while (true) {
            printBoard(board);
            
            // --- KIỂM TRA HẾT CỜ (Game Over) ---
            // Nếu không còn Tướng trên bàn cờ thì End Game
            if (!hasGeneral(board, humanSide)) {
                System.out.println(">> BẠN THUA! Tướng của bạn đã bị ăn.");
                break;
            }
            if (!hasGeneral(board, aiSide)) {
                System.out.println(">> BẠN THẮNG! Tướng địch đã bị tiêu diệt.");
                break;
            }

            if (isHumanTurn) {
                // ================= LƯỢT NGƯỜI =================
                System.out.print(">> Lượt BẠN (Đỏ - Hoa). Nhập nước đi (x1 y1 x2 y2): ");
                String input = scanner.nextLine();
                
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Đã thoát game.");
                    break;
                }
                
                try {
                    String[] parts = input.trim().split("\\s+");
                    if (parts.length != 4) {
                        System.out.println("Lỗi: Vui lòng nhập đủ 4 số!");
                        continue;
                    }

                    int x1 = Integer.parseInt(parts[0]);
                    int y1 = Integer.parseInt(parts[1]);
                    int x2 = Integer.parseInt(parts[2]);
                    int y2 = Integer.parseInt(parts[3]);
                    Move move = new Move(x1, y1, x2, y2);

                    // Kiểm tra cơ bản: Có quân không? Có phải quân mình không?
                    Piece p = board.getPiece(x1, y1);
                    if (p == null) {
                        System.out.println("Lỗi: Ô này không có quân cờ!");
                        continue;
                    }
                    if (p.getSide() != humanSide) {
                        System.out.println("Lỗi: Bạn không được đi quân của đối phương!");
                        continue;
                    }

                    // Kiểm tra luật di chuyển
                    if (board.isValidMove(move)) {
                        // Thực hiện nước đi (Hàm này trả về quân bị ăn, nhưng ở Main ta không cần dùng)
                        board.executeMove(move);
                        System.out.println(">> Bạn đã đi: " + move.toString());
                        isHumanTurn = false; // Chuyển lượt sang Máy
                    } else {
                        System.out.println("Lỗi: Nước đi không hợp lệ (Sai luật hoặc bị cản)!");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Lỗi: Tọa độ phải là số nguyên!");
                } catch (Exception e) {
                    System.out.println("Lỗi: " + e.getMessage());
                }

            } else {
                // ================= LƯỢT AI =================
                System.out.println(">> AI (Đen) đang suy nghĩ...");
                long startTime = System.currentTimeMillis();
                
                Move bestMove = ai.findBestMove(board, aiSide);
                
                long endTime = System.currentTimeMillis();
                System.out.println(">> AI suy nghĩ trong " + (endTime - startTime) + "ms");
                
                if (bestMove != null) {
                    board.executeMove(bestMove);
                    System.out.println(">> AI đã đi: " + bestMove.toString());
                    isHumanTurn = true; // Chuyển lượt lại cho Người
                } else {
                    System.out.println(">> AI không còn nước đi hợp lệ! BẠN THẮNG!");
                    break;
                }
            }
        }
        scanner.close();
    }
    
    // Hàm kiểm tra xem Tướng còn sống không
    private static boolean hasGeneral(Board board, Piece.Side side) {
        for(Piece p : board.getAllPieces()) {
            if (p.getType().equals("General") && p.getSide() == side) {
                return true;
            }
        }
        return false;
    }

    // Hàm vẽ bàn cờ ra màn hình console
    public static void printBoard(Board board) {
        System.out.println("\n   0 1 2 3 4 5 6 7 8  (X)");
        System.out.println("  -------------------");
        for (int y = 0; y < Board.BOARD_HEIGHT; y++) {
            System.out.print(y + "| ");
            for (int x = 0; x < Board.BOARD_WIDTH; x++) {
                Piece p = board.getPiece(x, y);
                System.out.print(getPieceChar(p) + " ");
            }
            System.out.println("|" + y);
        }
        System.out.println("  -------------------");
    }

    private static String getPieceChar(Piece p) {
        if (p == null) return "."; 
        
        String symbol = "?";
        switch (p.getType()) {
            case "Chariot": symbol = "R"; break; // Rook - Xe
            case "Horse":   symbol = "H"; break; // Horse - Mã
            case "Elephant":symbol = "E"; break; // Elephant - Tượng
            case "Guard":   symbol = "A"; break; // Advisor - Sĩ
            case "General": symbol = "K"; break; // King - Tướng
            case "Cannon":  symbol = "C"; break; // Cannon - Pháo
            case "Soldier": symbol = "S"; break; // Soldier - Tốt
        }
        
        // Đỏ (Người) viết Hoa, Đen (Máy) viết thường
        return (p.getSide() == Piece.Side.DOWN) ? symbol.toUpperCase() : symbol.toLowerCase();
    }
}