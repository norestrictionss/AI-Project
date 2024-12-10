import java.util.*;

public class Main {

    static int x0;              //Starting positions
    static int y0;
    static int N;               //Size

    static int n = 0;
    static int x = 0;
    static int y = 0;
    
    static String searchMethod; //Search method

    static int[][] knightMoves = {      //Possible knight move set
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };

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
        //First knight placed
        int i = 0;
        board.setPosition(startPos[0], startPos[1], 1); //Make first move

        int [] posSequence;
        int [] newArray;

        List<int[]> posArray = new ArrayList<>();                   //Initialize Stack/Queue

        if(searchMethod.equals("a")) {
            posArray.add(new int[]{startPos[0], startPos[1]});          // Add first move
        }

        if(searchMethod.equals("b"))      {                         //Depth first initialize
            posArray.add(new int[]{startPos[0], startPos[1], 0, 1});      //Add first move
        }

        if(searchMethod.equals("c") || searchMethod.equals("d")){                               //Heuristic case
            posSequence = getPosSequenceHeuristic(new int[]{x, y});  //Get pos sequence for heuristic
            newArray = new int[4 + posSequence.length];

            //Add the original elements
            newArray[0] = x;
            newArray[1] = y;
            newArray[2] = 1;
            newArray[3] = 4;
            // Add elements from posSequence
            System.arraycopy(posSequence, 0, newArray, 4, posSequence.length);
            posArray.add(newArray);
        }
    
        //place remaining knights
        while(!posArray.isEmpty() && searchMethod.equals("a") || !posArray.isEmpty() && !searchMethod.equals("a")){
            
            if(searchMethod.equals("a")){
                BFS(posArray);
            }
            else if(searchMethod.equals("b")){
                DFS(posArray);
            }

            else if(searchMethod.equals("c") || searchMethod.equals("d")){
                heuristic(posArray);
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

    public static void BFS(List<int[]> pos){                  // BFS function with given stack/queue

        int[] path = pos.remove(0);                // Pop first position from stack/queue to process
        int lastPositionX = path[path.length - 2];      // Get last X position
        int lastPositionY = path[path.length - 1];      // Get last Y position

        for(int i = 0; i < N; i++){                    // Loop all cells, set unvisited
            for(int j = 0; j < N; j++){
                board.setPosition(i, j, -1);
                board.setVisited(i, j, false);
            }
        }

        // Set visited position move number
        for(int i = 0; i < path.length / 2; i++){
            int x = path[2 * i]; // X coordinate at even index
            int y = path[2 * i + 1]; // Y coordinate at odd index
            board.setPosition(x, y, i + 1);
        }

        // Check possible knight moves
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


    public static void DFS(List<int[]> pos){
        int [] currentPos = pos.get(pos.size()-1);
        int currentMove = currentPos[2];
        n = currentPos[3];
        // System.out.printf("%d %d %d %d \n", currentPos[0], currentPos[1], currentMove, n);
        if(currentPos[2]==knightMoves.length){
            board.setPosition(currentPos[0], currentPos[1], -1);
            board.setVisited(currentPos[0], currentPos[1], false);
            
            pos.remove(pos.size()-1);
            if(pos.isEmpty())
                return;
            ++pos.get(pos.size()-1)[2];
        }
        else if(board.isInRange(currentPos[0]+knightMoves[currentMove][0], currentPos[1]+knightMoves[currentMove][1]) 
            && !board.getCell(currentPos[0]+knightMoves[currentMove][0], currentPos[1]+knightMoves[currentMove][1]).isVisited()){
            // System.out.printf("%d %d %d %d 1\n", currentPos[0]+knightMoves[currentMove][0], currentPos[1]+knightMoves[currentMove][1], currentMove, pos.get(pos.size()-1)[3]+1);
            pos.add(new int[]{currentPos[0]+knightMoves[currentMove][0], currentPos[1]+knightMoves[currentMove][1], 0, n+1});
            board.setPosition(currentPos[0]+knightMoves[currentMove][0], currentPos[1]+knightMoves[currentMove][1], n+1);
        }
        else{
            ++pos.get(pos.size()-1)[2];
        }
        /*  return false; */
        
    }

    public static void heuristic(List<int[]> pos){

        int [] currentPos = new int[]{pos.get(pos.size()-1)[0], pos.get(pos.size()-1)[1]};
        int posSequenceIndex = pos.get(pos.size()-1)[3];
        n = pos.get(pos.size()-1)[2];
        board.setPosition(currentPos[0], currentPos[1], n);     //TODO bug check
        int[] newPos; 
        if(posSequenceIndex-4!=knightMoves.length){
            newPos = getNextPosition(currentPos, pos.get(pos.size()-1)[posSequenceIndex]);
            x = newPos[0];
            y = newPos[1];
        }
        // System.out.printf("%d %d %d %d \n", currentPos[0], currentPos[1], currentMove, n);
        if(posSequenceIndex-4==knightMoves.length){
            board.setPosition(currentPos[0], currentPos[1], -1);
            board.setVisited(currentPos[0], currentPos[1], false);
            
            pos.remove(pos.size()-1);
            if(pos.isEmpty())
                return;
            ++pos.get(pos.size()-1)[3];
        }
        else if(board.isInRange(x, y) 
            && !board.getCell(x, y).isVisited()){
            // System.out.printf("%d %d %d %d 1\n", currentPos[0]+knightMoves[currentMove][0], currentPos[1]+knightMoves[currentMove][1], currentMove, pos.get(pos.size()-1)[3]+1);
            
            int [] posSequence;

            //Get queue for heuristic
            posSequence = getPosSequenceHeuristic(new int[]{x, y});


            //Assuming posSequence is an int[] array
            int[] newArray = new int[4 + posSequence.length];
            // Add the original elements
            newArray[0] = x;  // The x coordinate that will be expanded
            newArray[1] = y; // The y coordinate that will be expanded
            newArray[2] = n + 1; // Move count of the next node
            newArray[3] = 4; // Current index of the position sequence
            // Add elements from posSequence
            System.arraycopy(posSequence, 0, newArray, 4, posSequence.length);
            pos.add(newArray);
        }
        else{
            ++pos.get(pos.size()-1)[3];
            
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