class Board {
    private Cell[][] board; //Array of cells as chess board

    //Constructor
    public Board(int size) {
        board = new Cell[size][size];
        //Initialize move number to -1 and visited false
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new Cell(-1, false);
            }
        }
    }

    //Set position
    public void setPosition(int x, int y, int moveNumber) {
        board[x][y].setMoveNumber(moveNumber);
        board[x][y].setVisited(true);
    }

    public void setVisited(int x, int y, boolean n) {
        board[x][y].setVisited(n);
    }

    //Getter
    public Cell getCell(int x, int y) {
        return board[x][y];
    }

    //Is inside chess board?
    public boolean isInRange(int x, int y) {
        return x >= 0 && x < board.length && y >= 0 && y < board[0].length;
    }

    //Prints board state
    public void printBoard() {
        for (Cell[] cells : board) {
            for (Cell cell : cells) {
                System.out.print(cell.getMoveNumber() + "\t");
            }
            System.out.println();
        }
        System.out.println();
        for (Cell[] cells : board) {
            for (Cell cell : cells) {
                System.out.print(cell.isVisited() + "\t");
            }
            System.out.println();
        }
    }
}