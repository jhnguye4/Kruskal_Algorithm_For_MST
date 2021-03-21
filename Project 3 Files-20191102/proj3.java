import java.util.*;
import java.io.*;

public class proj3 {
    public static final int CONST9 = 9;
    public static final int MAX_EDGES = 5000;
    public static final int MAX_VERTICES = 1000;
    public ArrayList<edge> arr = new ArrayList<edge>();
    public edge[] minHeap;
    public edge[] cpyHeap;
    public int[][] adjList;
    public int[] upTree;

    public proj3() {
        Scanner console = new Scanner(System.in);
        System.out.print("Enter a filename or Q to quit: ");
        String filename = console.next().toLowerCase();

        Scanner input = null;
        PrintStream output = null;
        while (!(filename.equals("q"))) {
            if (filename.endsWith("input.txt")) {
                input = getInputScanner(filename);
                if (input != null) {
                    output = getOutputPrintStream(console, filename);
                    if (output != null) {
                        process(input);
                        Heap heap = new Heap();
                        Adjacency adj = new Adjacency();
                        for (int i = 0; i < arr.size(); i++) {
                            heap.insert(arr.get(i));
                        }

                        int s = heap.size();
                        adj.cpyHeap(s);
                        heap.print(output);

                        // output.println("Union: ");
                        // up.printUnion(output);

                        UpTree up = new UpTree(adj, heap, s);
                        up.kruskalMST(heap);
                        up.printMST(s, output);
                        adj.adjacencyList(s, output);
                    }
                }
            } else {
                System.out.println("Invalid filename");
            }
            System.out.print("Enter a filename or Q to quit: ");
            filename = console.next().toLowerCase();
        }
    }

    public static void main(String[] args) {
        new proj3();
    }

    // Returns Scanner for an input file
    // Returns null if the file does not exist
    /**
     * getInputScanner method is used to create a scanner of the input file the user
     * entered. It will return null and a FileNotFoundException if the user enters a
     * file that does not exist.
     *
     *
     * @param filename The string is the filename that you prompted the user to
     *                 enter
     * @return returns Scanner of the filename or null if there is no such file.
     */
    public Scanner getInputScanner(String filename) {
        Scanner fileScanner = null;
        try {
            fileScanner = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return fileScanner;
    }

    // Returns PrintStream for an output file
    // If the output file already exists, asks the user if it is OK to overwrite the
    // file
    // If it is not OK to overwrite the file or a FileNotFoundException occurs, null
    // is
    // returned instead of a PrintStream
    /**
     * getOutputPrintStream method is used to create a output files with the
     * opposite extentions. If the file already exists then it will prompt the user
     * if they want to overwrite the file If they respond no then the output will
     * return null. If file is unable to be written then it will prompt the user of
     * the error
     *
     * @param console  scanner that is used to prompt user to answer if they want to
     *                 overwrite file
     * @param filename file that the user inputted and depending extention, it is
     *                 used to strip and put on the opposite extention for the
     *                 output file.
     * @return returns PrintStream of the filename with the opposite extention or
     *         null if file could not be written
     */
    public PrintStream getOutputPrintStream(Scanner console, String filename) {
        PrintStream output = null;
        if (filename.endsWith("input.txt")) {
            filename = filename.substring(0, filename.length() - CONST9);
            filename = filename + "output.txt";

        }
        File file = new File(filename);
        try {
            if (!file.exists()) {
                output = new PrintStream(file);
            } else {
                System.out.print(filename + " exists - OK to overwrite(y,n)?: ");
                String reply = console.next().toLowerCase();
                if (reply.startsWith("y")) {
                    output = new PrintStream(file);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File unable to be written " + e);
        }
        return output;
    }

    public void process(Scanner input) {
        int first = 0;
        int second = 0;
        double weight = 0;
        while (input.hasNextLine()) {
            String line = input.nextLine();
            Scanner lineScan = new Scanner(line);
            if (lineScan.hasNextInt()) {
                first = lineScan.nextInt();
                // if integer is -1 it will exit while loop
                if (first == -1) {
                    break;
                }
            }
            if (lineScan.hasNextInt()) {
                second = lineScan.nextInt();
            }
            if (lineScan.hasNextDouble()) {
                weight = lineScan.nextDouble();
            }
            if (first > second) {
                int temp = first;
                first = second;
                second = temp;
            }
            edge edge = new edge(first, second, weight);
            arr.add(edge);
            lineScan.close();
        }

    }

    public class edge {
        public int vert1;
        public int vert2;
        public double weight;
        edge next;

        public edge(int vert1, int vert2, double weight) {
            this.vert1 = vert1;
            this.vert2 = vert2;
            this.weight = weight;
            next = null;
        }
    }

    public class Heap {
        private int size;

        /**
         * Creating default constructor for creating a heap
         */
        public Heap() {
            minHeap = new edge[MAX_EDGES];
            this.size = 0;
        }

        public void swap(int first, int second) {
            edge temp;
            temp = minHeap[first];
            minHeap[first] = minHeap[second];
            minHeap[second] = temp;
        }

        /**
         * inserting a new value into the heap
         * 
         * @param num
         */
        public void insert(edge edge) {
            if (size >= MAX_EDGES) {
                return;
            }
            minHeap[size] = edge;
            size++;
            upHeap(size - 1);
        }

        public void upHeap(int index) {
            if (index > 0) {
                edge edge1 = minHeap[(index - 1) / 2];
                edge edge2 = minHeap[index];
                if (edge1.weight > edge2.weight) {
                    swap((index - 1) / 2, index);
                    upHeap((index - 1) / 2);
                }
            }
        }

        public int size() {
            return size;
        }

        public void reduceSize() {
            size--;
        }

        public void print(PrintStream out) {
            for (int i = 0; i < size; i++) {
                edge edge = minHeap[i];
                out.printf("%4d %4d\n", edge.vert1, edge.vert2);
            }
        }
    }

    public class Adjacency {
        int maxNode = 0;
        int col;
        edge[] cpyHeap;

        public void adjacencyList(int size, PrintStream out) {

            // Finding max node
            for (int i = 0; i < size; i++) {
                if (cpyHeap[i].vert2 > cpyHeap[i].vert1) {
                    if (cpyHeap[i].vert2 > maxNode) {
                        maxNode = cpyHeap[i].vert2;
                    }
                }
            }
            // creating 2D array with maxNode + 1 for rows and maxNode for cols
            adjList = new int[maxNode + 1][maxNode];
            for (int i = 0; i <= maxNode; i++) {
                ArrayList<Integer> arr = new ArrayList<Integer>();
                col = 0;
                for (int j = 0; j < size; j++) {
                    int vert1 = cpyHeap[j].vert1;
                    int vert2 = cpyHeap[j].vert2;
                    if (vert1 == i) {
                        adjList[i][col] = vert2;
                        arr.add(vert2);
                        col++;
                    }
                    if (vert2 == i) {
                        adjList[i][col] = vert1;
                        arr.add(vert1);
                        col++;
                    }
                }
                Collections.sort(arr);
                for (int k = 0; k < arr.size() - 1; k++) {
                    out.printf("%4s ", arr.get(k));
                }
                out.printf("%4s", arr.get(arr.size() - 1));
                out.println();
            }
            order();
        }

        public int maxNode() {
            return maxNode;
        }

        public void order() {
            for (int i = 0; i <= maxNode; i++) {
                for (int j = 0; j < maxNode; j++) {
                    if (j < maxNode - 1) {
                        if (adjList[i][j] > adjList[i][j + 1]) {
                            change(i, j, j + 1);
                            order();
                        }
                    }
                }
            }
        }

        public void change(int row, int col1, int col2) {
            int temp;
            temp = adjList[row][col1];
            adjList[row][col1] = adjList[row][col2];
            adjList[row][col2] = temp;
        }

        public void cpyHeap(int s) {
            cpyHeap = new edge[MAX_EDGES];
            for (int i = 0; i < s; i++) {
                cpyHeap[i] = minHeap[i];
            }
        }

    }

    public class UpTree {
        LinkedList<edge> MST;
        int maxNode = 0;
        int size;

        public UpTree(Adjacency a, Heap h, int s) {
            // Finding max node
            for (int i = 0; i < s; i++) {
                if (minHeap[i].vert2 > minHeap[i].vert1) {
                    if (minHeap[i].vert2 > maxNode) {
                        maxNode = minHeap[i].vert2;
                    }
                }
            }
            size = maxNode + 1;
            upTree = new int[size];
            for (int i = 0; i < size; i++) {
                upTree[i] = -1;
            }

        }

        public int union(int vert1, int vert2) {
            if (upTree[vert1] >= upTree[vert2]) {
                upTree[vert1] = upTree[vert1] + upTree[vert2];
                upTree[vert2] = vert1;
                return vert1;
            } else {
                upTree[vert2] = upTree[vert1] + upTree[vert2];
                upTree[vert1] = vert2;
                return vert2;
            }
        }

        public int find(int node) {
            if (upTree[node] < 0) {
                return node;
            } else {
                return find(upTree[node]);
            }
        }

        public int pathCompressionFind(int node) {
            int r = find(node);
            int q = node;
            while (q != r) {
                int s = q;
                q = upTree[node];
                upTree[s] = r;
            }
            return r;
        }

        /**
         * Deleting minium value in that heap
         */
        public edge deleteMin(Heap h) {
            edge x = minHeap[0];
            h.reduceSize();
            h.swap(0, h.size());
            downHeap(h, 0);
            return x;
        }

        public void downHeap(Heap h, int index) {
            int child = 0;
            // both children exist and cheching which child is larger to store into child
            if ((2 * index + 2) < h.size()) {
                edge index1 = minHeap[2 * index + 2];
                edge index2 = minHeap[2 * index + 1];
                if (index1.weight < index2.weight) {
                    child = 2 * index + 2;
                } else {
                    child = 2 * index + 1;
                }
            }
            // only left child exists
            else if ((2 * index + 1) < h.size()) {
                child = 2 * index + 1;
            }

            edge weight1 = minHeap[index];
            edge weight2 = minHeap[child];
            if ((child > 0) && (weight1.weight > weight2.weight)) {
                h.swap(index, child);
                downHeap(h, child);
            }
        }

        public void kruskalMST(Heap h) {
            // Creating linked list for MST
            MST = new LinkedList<edge>();
            // This is the size of the upTree array is # of vertices
            int component = size;
            while (component > 1) {
                // Deleting off heap
                edge min = deleteMin(h);
                // finding index of vert1
                int u = pathCompressionFind(min.vert1);
                // System.out.printf("u: %d", u);
                // finding index of vert2
                int v = pathCompressionFind(min.vert2);
                // System.out.printf(" v: %d\n", v);
                if (u != v) {
                    union(u, v);
                    int i = 0;
                    if (MST.size() > 0) {
                        edge previous = null;
                        edge current = MST.get(i);
                        while (current.next != null || current.weight > min.weight) {
                            current = current.next;
                        }
                        previous = current;
                        previous.next = min;
                        MST.add(i, min);
                        component--;
                        continue;
                    }
                    MST.add(min);
                    component--;
                }
            }

        }

        public void printMST(int size, PrintStream out) {

            for (int i = 0; i < maxNode; i++) {
                for (int j = maxNode - 1; j >= 0; j--) {
                    edge current = MST.get(j);
                    if (current.vert1 == i) {
                        out.printf("%4d %4d\n", i, current.vert2);
                        continue;
                    }
                }
            }
        }
    }
}