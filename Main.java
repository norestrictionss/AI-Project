import java.util.*;

public class Main {

    static int x0, y0, N;              //Starting positions

    static int n = 0;
    static int x = 0;
    static int y = 0;
    
    static String searchMethod; //Search method

    static int[][] knightMoves = {      //Possible knight move set
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };

    static List<Node> nodes;

    static Board board;
    static boolean solutionFound = false;
    static int[] startPosition = new int[1];    //Starting position to be set

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //Input taking
        System.out.print("Enter the size for the chessboard n:= ");
        N = scanner.nextInt();

        x0 = N - 1;
        y0 = 0;

        x = x0;
        y = y0;

        startPosition = new int[] {x0, y0};

        System.out.print("Enter the search method preferred (a,b,c,d):= ");
        searchMethod = scanner.next();

        //Initialize board
        board = new Board(N);

        //Search
        solutionFound = TREE_SEARCH(startPosition);


        if (solutionFound) {
            System.out.println("Solution found!");
            board.printBoard();
        }
        else if (N < 5) {       //TODO make it so functions called actually detect this!
            System.out.println("No Solution Exists");
        }
        else {
            System.out.println("No solution found.");
            board.printBoard();
        }

        scanner.close();
    }

    public static boolean TREE_SEARCH(int[] startPos) {
        //First knight placed frontier initialization
        int i = 0;
        board.setPosition(startPos[0], startPos[1], 1); //Make first move

        nodes = new ArrayList<>();
        nodes.add(new Node());                                      //Add first node

        List<int[]> posArray = new ArrayList<>();                   //Initialize Stack/Queue

        nodes.get(0).setPossibleMoves(getPosSequenceHeuristic(new int[]{x, y}));        //initialize forintier
        nodes.get(0).setN(1);
        nodes.get(0).setX(x);
        nodes.get(0).setY(y);
        nodes.get(0).setChild_index(0);

        posArray.add(new int[]{startPos[0], startPos[1]});
    
        //place remaining knights
        while(!posArray.isEmpty()){

            switch (searchMethod) {
                case "a" -> BFS(posArray);
                case "b" -> DFS(posArray);
                case "c", "d" -> heuristic(posArray);
            }

            if(n==N*N  && !searchMethod.equals("a")) return solutionFound = true;
        }

        for(int k = 0;i<N;i++){                                 //Check for unvisited cell and return false
            for(int j = 0;j<N;j++){
                if(board.getCell(k, j).getMoveNumber()==-1)
                    return solutionFound=false;
            }
        }

        return solutionFound = true;                            //If all visited return true
    }

    public static void search(List<int[]> pos) {

    }

    public static void BFS(List<int[]> pos){                  // BFS function with given stack/queue

        int[] path = pos.remove(0);                // Pop first position from stack/queue to process
        int lastPositionX = path[path.length - 2];      // Get last X position
        int lastPositionY = path[path.length - 1];      // Get last Y position

        clearBoard();

        // Set visited position move number
        for(int i = 0; i < path.length / 2; i++){
            int x = path[2 * i]; // X coordinate at even index
            int y = path[2 * i + 1]; // Y coordinate at odd index
            board.setPosition(x, y, i + 1);
        }


        // Check possible knight moves, make  move
        for(int i = 0; i < knightMoves.length; i++){
            int[] newPos = getNextPosition(new int[] {lastPositionX, lastPositionY}, i);
            int newX = newPos[0];
            int newY = newPos[1];

            // If unvisited and in range
            if(board.isInRange(newX, newY) && !board.getCell(newX, newY).isVisited()){

                int[] nextPos = new int[]{newX, newY}; // Make a move, put into nextPos[]
                int[] newPath = new int[path.length + 2]; // Add next position to the path

                System.arraycopy(path, 0, newPath, 0, path.length); // Copy existing path
                newPath[path.length] = nextPos[0];
                newPath[path.length + 1] = nextPos[1];

                pos.add(newPath); // Add the new path to the queue
            }
        }
    }


    public static void DFS(List<int[]> pos) {
        int[] currentPos = pos.get(pos.size() - 1);
        Node currentNode = nodes.get(nodes.size() - 1);

        int currentMove = currentNode.getChild_index();
        n = currentNode.getN();

        if (currentNode.getChild_index() == knightMoves.length) {  // Set unvisited if last move is played, backtrack cleanup
            board.setPosition(currentPos[0], currentPos[1], -1);
            board.setVisited(currentPos[0], currentPos[1], false);

            pos.remove(pos.size() - 1);
            nodes.remove(nodes.size() - 1);

            if (pos.isEmpty()) {                        //No position left, TODO print no solution!
                return;
            }
            nodes.get(nodes.size() - 1).incrementChild_index();
        } else {
            int[] nextPos = getNextPosition(currentPos, currentMove);       //Get next position

            if (board.isInRange(nextPos[0], nextPos[1]) &&
                    !board.getCell(nextPos[0], nextPos[1]).isVisited()) {

                pos.add(new int[]{nextPos[0], nextPos[1]});

                Node newNode = new Node();
                newNode.setChild_index(0);
                newNode.setN(n + 1);
                nodes.add(newNode);

                board.setPosition(nextPos[0], nextPos[1], n + 1);
            } else {
                nodes.get(nodes.size() - 1).incrementChild_index();
            }
        }
    }


    public static void heuristic(List<int[]> pos){

        Node currentNode = nodes.get(nodes.size() - 1);
        int [] currentPos = new int[]{currentNode.getX(), currentNode.getY()};

        int posSequenceIndex = currentNode.getChild_index();
        n = currentNode.getN();

        board.setPosition(currentPos[0], currentPos[1], n);
        int[] newPos;

        if(posSequenceIndex!=knightMoves.length){
            newPos = getNextPosition(currentPos, nodes.get(nodes.size() - 1).getPossibleMoves()[posSequenceIndex]);
            x = newPos[0];
            y = newPos[1];
        }

        if(posSequenceIndex==knightMoves.length){
            board.setPosition(currentPos[0], currentPos[1], -1);
            board.setVisited(currentPos[0], currentPos[1], false);
            
            //pos.remove(pos.size()-1);
            nodes.remove(nodes.size() - 1);

            if(nodes.isEmpty())               //No more nodes to explore
                return;

            nodes.get(nodes.size() - 1).incrementChild_index();
        }

        else if(board.isInRange(x, y)
            && !board.getCell(x, y).isVisited()){
            
            int [] posSequence;

            //Get queue for heuristic
            posSequence = getPosSequenceHeuristic(new int[]{x, y});

            Node newNode = new Node();
            newNode.setX(x);
            newNode.setY(y);
            newNode.setN(n + 1);
            newNode.setChild_index(0);
            newNode.setPossibleMoves(posSequence);
            nodes.add(newNode);
        }
        else{
            nodes.get(nodes.size() - 1).incrementChild_index();
            
        }
    }

    public static void DFSHeuristic(List<int[]> pos) {
        int[] currentPos = pos.get(pos.size() - 1);  // Current position in the path
        Node currentNode = nodes.get(nodes.size() - 1);  // Last node in the search tree

        int currentMove = currentNode.getChild_index();  // Current move sequence
        n = currentNode.getN();  // Current step number

        // If last move has been played, backtrack cleanup
        if (currentMove == knightMoves.length) {
            board.setPosition(currentPos[0], currentPos[1], -1);  // Reset position
            board.setVisited(currentPos[0], currentPos[1], false);  // Mark as unvisited

            pos.remove(pos.size() - 1);  // Remove last position
            nodes.remove(nodes.size() - 1);  // Remove last node

            // If no position left to explore, exit
            if (pos.isEmpty()) {
                return;  // No solution found (not handled explicitly here)
            }

            // Increment child index for the parent node
            nodes.get(nodes.size() - 1).incrementChild_index();
        } else {
            // Get the next position based on the current move index
            int[] nextPos = getNextPosition(currentPos, currentMove);

            // If valid next position
            if (board.isInRange(nextPos[0], nextPos[1]) &&
                    !board.getCell(nextPos[0], nextPos[1]).isVisited()) {

                pos.add(new int[]{nextPos[0], nextPos[1]});  // Add new position to the path

                Node newNode = new Node();
                newNode.setChild_index(0);  // Start at first child
                newNode.setN(n + 1);  // Increment step number
                nodes.add(newNode);  // Add new node

                board.setPosition(nextPos[0], nextPos[1], n + 1);  // Mark position with step number
            } else {
                // If move isn't valid, increment child index to try next move
                nodes.get(nodes.size() - 1).incrementChild_index();
            }
        }

        // Perform heuristic exploration if not at the last move yet
        if (currentMove != knightMoves.length) {
            // Compute next position based on heuristic
            int[] newPos = getNextPosition(currentPos, nodes.get(nodes.size() - 1).getPossibleMoves()[currentMove]);
            x = newPos[0];
            y = newPos[1];

            // If valid move, perform heuristic-based exploration
            if (board.isInRange(x, y) && !board.getCell(x, y).isVisited()) {
                int[] posSequence = getPosSequenceHeuristic(new int[]{x, y});  // Get sequence for heuristic

                Node newNode = new Node();
                newNode.setX(x);
                newNode.setY(y);
                newNode.setN(n + 1);  // Increment step number
                newNode.setChild_index(0);  // Start at first child
                newNode.setPossibleMoves(posSequence);  // Set possible moves based on heuristic
                nodes.add(newNode);  // Add new node for heuristic path

                // Update the board
                board.setPosition(x, y, n + 1);  // Mark position on the board
            }
        } else {
            // If we've reached the last possible move, perform backtracking
            board.setPosition(currentPos[0], currentPos[1], -1);  // Reset position
            board.setVisited(currentPos[0], currentPos[1], false);  // Mark as unvisited

            nodes.remove(nodes.size() - 1);  // Remove the last node

            // If no nodes left to explore, exit
            if (nodes.isEmpty()) {
                return;  // No more nodes to explore (no solution found)
            }

            // Increment the child index of the parent node for the next exploration step
            nodes.get(nodes.size() - 1).incrementChild_index();
        }
    }

    public static void clearBoard() {
        for(int i = 0; i < N; i++){                    // Loop all cells, set unvisited
            for(int j = 0; j < N; j++){
                board.setPosition(i, j, -1);
                board.setVisited(i, j, false);
            }
        }
    }

    public static int[] getPosSequenceHeuristic(int[] pos) {
        double[] moveProxies = new double[8];
        int[] moveCounts = new int[8];

        for(int i = 0; i < knightMoves.length; i++) {
            int[] newPos = getNextPosition(pos, i);
            int x = newPos[0];
            int y = newPos[1];
            if(board.isInRange(x, y) && !board.getCell(x, y).isVisited())
                if(searchMethod.equals("d"))
                    moveCounts[i] = getPossibleMoveCount(new int[] {x, y});
                else if(searchMethod.equals("c"))
                    moveCounts[i] = -getPossibleMoveCount(new int[] {x, y});
        }

        Integer[] indices = new Integer[moveCounts.length]; //indices to be sorted

        for(int i = 0; i < moveCounts.length; i++) {    //Initialize indices
            indices[i] = i;
        }

        if(searchMethod.equals("d")) {
            for(int i = 0; i < knightMoves.length; i++) {
                int[] newPos = getNextPosition(pos, i);
                int x = newPos[0];
                int y = newPos[1];
                if(board.isInRange(x, y) && !board.getCell(x, y).isVisited())
                    moveProxies[i] = getMoveCornerProximity(new int[] {x, y});
            }

            return heuristic2Sort(moveCounts, indices, moveProxies);
        }
        else {
            Arrays.sort(indices, (i1, i2) -> Integer.compare(moveCounts[(int) i2], moveCounts[(int) i1]));
            return Arrays.stream(indices).mapToInt(Integer::intValue).toArray();
        }
    }

    public static int[] heuristic2Sort(int[] moveCounts, Integer[] indices, double[] moveProxies) {
        for (int i = 0; i < knightMoves.length - 1; i++) {
            int min = i;

            //find Min element
            for (int j = i + 1; j < knightMoves.length; j++) {
                if (moveCounts[j] < moveCounts[min]) {
                    min = j;
                }
            }

            //swap min
            if (min != i) {
                int temp = moveCounts[i];
                int tempIndex = indices[i];
                double tempProxy = moveProxies[i];

                moveCounts[i] = moveCounts[min];
                indices[i] = indices[min];
                moveProxies[i] = moveProxies[min];

                moveCounts[min] = temp;
                indices[min] = tempIndex;
                moveProxies[min] = tempProxy;
            }
        }

        for (int i = 0; i < moveCounts.length - 1; i++) {
            //compare proximity if equal move counts
            if (moveCounts[i] == moveCounts[i + 1]) {
                //swap if closer to corner
                if (moveProxies[i] > moveProxies[i + 1]) {
                    int tempMove = moveCounts[i];
                    moveCounts[i] = moveCounts[i+1];
                    moveCounts[i+1] = tempMove;

                    // Swap indices
                    int tempIndex = indices[i];
                    indices[i] = indices[i+1];
                    indices[i+1] = tempIndex;

                    // Swap proximity
                    double tempProximity = moveProxies[i];
                    moveProxies[i] = moveProxies[i+1];
                    moveProxies[i+1] = tempProximity;
                }
            }
        }

        return Arrays.stream(indices).mapToInt(Integer::intValue).toArray();
    }

    public static double getMoveCornerProximity(int[] pos) {        //Returns proximity value for h2
        return Math.min(
                Math.min(Math.abs(Math.pow(pos[0], 2) + Math.pow(pos[1], 2)), Math.abs(Math.pow(pos[0] - (N - 1), 2) + Math.pow(pos[1] - (N - 1), 2))),
                Math.min(Math.abs(Math.pow(pos[0] - (N - 1), 2) + Math.pow(pos[1], 2)), Math.abs(Math.pow(pos[0], 2) + Math.pow(pos[1] - (N - 1), 2)))
        );
    }

    public static int getPossibleMoveCount(int[] pos) {     //Returns possible legal moves for h1 and h2
        int possibleMoves = 0;

        for(int i = 0; i < knightMoves.length; i++) {
            int[] newPos = getNextPosition(pos, i);
            int x = newPos[0];
            int y = newPos[1];

            if (board.isInRange(x, y) && !board.getCell(x, y).isVisited()) {    //if move is possible
                possibleMoves++;
            }
        }
        return possibleMoves;
    }

    public static int[] getNextPosition(int[] pos, int i) {     //Returns next position with given knight move
        int x = pos[0] + knightMoves[i][0];
        int y = pos[1] + knightMoves[i][1];

        return new int[] {x, y};
    }
}