//Each board square is represented by a cell
class Cell {
    private int moveNumber;
    private boolean visited;

    //Constructor
    public Cell(int moveNumber, boolean visited) {
        this.moveNumber = moveNumber;
        this.visited = visited;
    }

    //Getters
    public int getMoveNumber() {
        return moveNumber;
    }

    public boolean isVisited() {
        return visited;
    }

    //Setters
    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}