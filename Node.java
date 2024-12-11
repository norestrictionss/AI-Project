public class Node {

    private int x,y,n,child_index;
    private int[] possibleMoves;
    public Node() {
        this.n = 0;
        this.child_index = 0;
        this.x = 0;
        this.y = 0;
    }



    //Getters and setters


    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int[] getPossibleMoves() {
        return possibleMoves;
    }

    public int getChild_index() {
        return child_index;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setChild_index(int child_index) {
        this.child_index = child_index;
    }

    public void incrementChild_index() {
        this.child_index++;
    }

    public void setPossibleMoves(int[] possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

}
