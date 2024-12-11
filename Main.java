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

        nodes.get(0).setPossibleMoves(getPosSequenceHeuristic(new int[]{x, y}));        //initialize frontier
        nodes.get(0).setN(1);
        nodes.get(0).setX(x);
        nodes.get(0).setY(y);
        nodes.get(0).setPosSequenceIndex(0);

        posArray.add(new int[]{startPos[0], startPos[1]});
    
        //place remaining knights
        while(!posArray.isEmpty()){

            switch (searchMethod) {
                case "a", "b", "c", "d" -> search();
            }

            if(n==N*N) return solutionFound = true;
        }

        for(int k = 0;i<N;i++){                                 //Check for unvisited cell and return false
            for(int j = 0;j<N;j++){
                if(board.getCell(k, j).getMoveNumber()==-1)
                    return solutionFound=false;
            }
        }

        return solutionFound = true;                            //If all visited return true
    }

    public static void search() {
        Node currentNode;
        if(searchMethod.equals("a")) {
            currentNode = nodes.get(0);
        }
        else {
            currentNode = nodes.get(nodes.size() - 1);                         //Initialize current node, current position
        }
        int [] currentPos = new int[]{currentNode.getX(), currentNode.getY()};  //

        int posSequenceIndex = currentNode.getPosSequenceIndex();
        n = currentNode.getN();

        board.setPosition(currentPos[0], currentPos[1], n);
        int[] newPos;

        if(posSequenceIndex!=knightMoves.length){                               //Try moves
            newPos = getNextPosition(currentPos, currentNode.getPossibleMoves()[posSequenceIndex]);
            x = newPos[0];
            y = newPos[1];
        }

        if(posSequenceIndex==knightMoves.length){                               //Last move tried, backtrack cleanup
            board.setPosition(currentPos[0], currentPos[1], -1);
            board.setVisited(currentPos[0], currentPos[1], false);

            if(searchMethod.equals("a"))
                nodes.remove(0);
            else
                nodes.remove(nodes.size() - 1);

            if(nodes.isEmpty())               //No more nodes to explore, failed to find solution
                return;

            if(searchMethod.equals("a"))
                nodes.get(0).incrementPosSequenceIndex();
            else
                nodes.get(nodes.size() - 1).incrementPosSequenceIndex();
        }
        else if(board.isInRange(x, y)                                                //if on board and not visited
                && !board.getCell(x, y).isVisited()){

            int [] posSequence;

            //Get queue for heuristic
            if(searchMethod.equals("c") || searchMethod.equals("d"))
                posSequence = getPosSequenceHeuristic(new int[]{x, y});
            else if(searchMethod.equals("b"))
                posSequence = new int[] {0, 1, 2, 3, 4, 5, 6, 7};
            else {
                nodes.get(0).incrementPosSequenceIndex();
                posSequence = new int[] {0, 1, 2, 3, 4, 5, 6, 7};
            }


            Node newNode = new Node();
            newNode.setX(x);
            newNode.setY(y);
            newNode.setN(n + 1);
            newNode.setPosSequenceIndex(0);
            newNode.setPossibleMoves(posSequence);
            nodes.add(newNode);
        }

        else{                               //Skip this move
            if(searchMethod.equals("a"))
                nodes.get(0).incrementPosSequenceIndex();
            else
                nodes.get(nodes.size() - 1).incrementPosSequenceIndex();

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