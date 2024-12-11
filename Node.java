public class Node {

    private int x,y,n, posSequenceIndex;
    private int[] possibleMoves;
    public Node() {
        this.n = 0;
        this.posSequenceIndex = 0;
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

    public int getPosSequenceIndex() {
        return posSequenceIndex;
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

    public void setPosSequenceIndex(int posSequenceIndex) {
        this.posSequenceIndex = posSequenceIndex;
    }

    public void incrementPosSequenceIndex() {
        this.posSequenceIndex++;
    }

    public void setPossibleMoves(int[] possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

}
