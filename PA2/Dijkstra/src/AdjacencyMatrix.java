import java.util.ArrayList;
import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class AdjacencyMatrix {
    static ArrayList<Integer> vertices;
    static boolean[][] edges;
    static int[][] edgeWeight;
    static int[] visitedNodes;
    static int[] distance;
    static int[] parentNodes;

    public AdjacencyMatrix()
    {
        vertices = new ArrayList<>();
        edges = new boolean[0][0];
    }

    public int getIndex(int vertex)
    {
        for(int i = 0; i < vertices.size(); i++)
        {
            if(vertices.get(i).equals(vertex)) {
                return i;
            }
        }
        return -1;
    }

    public void addVertex(int vertex)
    {
        boolean[][] newEdges = new boolean[edges.length + 1][edges.length + 1];
        for(int i = 0; i < edges.length; i++)
            for(int j = 0; j < edges.length; j++)
                newEdges[i][j] = edges[i][j];
        edges = newEdges;
        vertices.add(vertex);
    }

    public void addEdge(int from, int to)
    {
        int fromIndex = getIndex(from);
        int toIndex = getIndex(to);

        if(fromIndex == -1 || toIndex == -1) return;

        edges[fromIndex][toIndex] = true;
        edges[toIndex][fromIndex] = true;
    }

    public void removeEdge(int from, int to)
    {
        int fromIndex = getIndex(from);
        int toIndex = getIndex(to);

        if(fromIndex == -1 || toIndex == -1) return;

        edges[fromIndex][toIndex] = false;
        edges[toIndex][fromIndex] = false;
    }

    public void removeVertex(int vertex)
    {
        int index = getIndex(vertex);
        if(index == -1) return;

        boolean[][] newEdges = new boolean[edges.length - 1][edges.length - 1];
        for(int i = 0; i < edges.length; i++)
        {
            for(int j = 0; j < edges.length; j++)
            {
                if(i < index && j < index)
                {
                    newEdges[i][j] = edges[i][j];
                } else if(i > index && j < index)
                {
                    newEdges[i - 1][j] = edges[i][j];
                } else if(i < index && j > index)
                {
                    newEdges[i][j - 1] = edges[i][j];
                } else if(i > index && j > index)
                {
                    newEdges[i - 1][j - 1] = edges[i][j];
                }
            }
        }

        edges = newEdges;
        vertices.remove(vertex);
    }

    public void print() {
        for (int i = 0; i < vertices.size(); i++) {
            System.out.print(vertices.get(i) + " -> ");
            for (int j = 0; j < vertices.size(); j++) {
                if (edges[i][j]) {
                    System.out.print(vertices.get(j) + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
        //printing the edgeWeight graph
        for(int i=0; i < vertices.size(); i++){
            for(int j=0; j < vertices.size(); j++){
                System.out.print(edgeWeight[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();
        for (int i = 0; i < vertices.size(); i++) {
            System.out.print(vertices.get(i) + " -> ");
            for (int j = 0; j < vertices.size(); j++) {
                if (edges[i][j]) {
                    System.out.print(vertices.get(j) + " (" + edgeWeight[i][j] + ") ");
                }
            }
            System.out.println();
        }
    }

    public static int nextNode(int numVerticies){
        int low, save;
        low = 999999;// Hopefully this is high enough where its an unrealistic input for a node
        save = -1;

        for(int i=0; i < numVerticies; i++){
            if(visitedNodes[i] == 0 && distance[i] < low){
                save = i;
                low = distance[i];
            }
        }

        return save;
    }

    public static void main(String[] args) {

        int numVertices = 0, numEdges = 0, source = 0;
        AdjacencyMatrix graph = new AdjacencyMatrix();
        File file = new File("src/cop3503-dijkstra-input.txt");//current working directory is the Project file
        // if the current working directory is src, use the one below
        //File file = new File("cop3503-dijkstra-input.txt");

        try{
            Scanner scan = new Scanner(file);

            // scan in number of vertices, edges and source Vertex
            for(int i = 0; i < 3; i++){
                if(i == 0)
                    numVertices = scan.nextInt();
                else if(i == 1)
                    source = scan.nextInt();
                else
                    numEdges = scan.nextInt();
            }

            edgeWeight = new int[numVertices][numVertices];

            System.out.println("Number of Verticies: " + numVertices);
            System.out.println("Number of Edges: " + numEdges);
            System.out.println("Source Vertex: " + source);

            // add the vertices
            vertices = new ArrayList<>();
            for(int i = 1; i <= numVertices; i++){
                graph.addVertex(i);
            }

            // add the edges
            for(int i = 0; i< numEdges; i++){
                // next ints in file will be an edge from 1 vertex to another vertex
                int from, to, weight;

                from = scan.nextInt();
                to = scan.nextInt();
                int fromIndex = graph.getIndex(from);
                int toIndex = graph.getIndex(to);

                graph.addEdge(from, to);

                // this is the weight of the edge
                weight = scan.nextInt();
                edgeWeight[fromIndex][toIndex] = weight;
                edgeWeight[toIndex][fromIndex] = weight;

            }

            // Now that the graph has been made, we implement Dijkstra's Algorithm
            //start: source variable
            //check neighboring nodes and their weights
            distance = new int[numVertices];
            visitedNodes = new int[numVertices];
            parentNodes = new int[numVertices];
            // initialize distance array are parentNodes array to impossible values
            for(int i =0; i < numVertices; i++){
                distance[i] = 9999999;
                parentNodes[i] = -1;
            }
            int current = graph.getIndex(source);
            int newDistance;

            // set source index to
            distance[current] = 0;
            visitedNodes[current] = -1;
            // minus 1 because of the source node
            for(int i=0; i < numVertices - 1; i++){
                //loop through all edges
                for(int j = 0; j < numVertices; j++){
                    // we want to visit any unvisited nodes along the current node we are at
                    if(edges[current][j] && visitedNodes[j] == 0){
                        //find smallest distance and update it in the distance array
                        newDistance = distance[current] + edgeWeight[current][j];
                        if(newDistance < distance[j]){
                            distance[j] = distance[current] + edgeWeight[current][j];
                            // + 1 because we don't want to save the index here
                            parentNodes[j] = current + 1;
                        }
                    }
                }
                // we've now visited current
                visitedNodes[current] = 1;

                //
                current = nextNode(numVertices);

            }
            distance[graph.getIndex(source)] = -1;

            /*
            */
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }

        //Time for printing :D
        try
        {
            BufferedWriter outFile = new BufferedWriter(new FileWriter("cop3503-dijkstra-output-Wilson-Reece.txt", false));

            outFile.append(String.format("%d", numVertices));
            outFile.newLine();

            //for each vertex
            for (int i = 1; i <= numVertices; i++)
            {
                outFile.append(String.format("%d %d %d", i, distance[i-1], parentNodes[i-1]));
                outFile.newLine();
            }

            outFile.close();
        }
        catch (IOException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}
