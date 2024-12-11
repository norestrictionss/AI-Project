public class Node {
    private int n;

    private int child_index;
    public Node() {
        this.n = 0;
        this.child_index = 0;
    }



    //Getters and setters

    public int getChild_index() {
        return child_index;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setChild_index(int child_index) {
        this.child_index = child_index;
    }

    public void incrementChild_index() {
        this.child_index++;
    }
}
