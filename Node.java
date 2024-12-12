import java.util.ArrayList;
import java.util.List;

public class Node {

    private int x,y,n, posSequenceIndex;
    private int[] possibleMoves;
    private List<int[]> path;
    public Node() {
        this.n = 0;
        this.posSequenceIndex = 0;
        this.x = 0;
        this.y = 0;
        this.path = new ArrayList<>();
    }



    //Getters and setters



    public List<int[]> getPath() {
        return path;
    }
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

    public void addToPath(int[] pos) {
        this.path.add(pos);
    }

    public void setPath(List<int[]> path) {

        List<int[]> copiedPath = new ArrayList<>();
        for (int[] array : path) {

            int[] arrayCopy = new int[array.length];
            System.arraycopy(array, 0, arrayCopy, 0, array.length);


            copiedPath.add(arrayCopy);
        }

        this.path = copiedPath;
    }
}
