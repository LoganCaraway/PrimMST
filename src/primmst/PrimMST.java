/*
 * Author: Logan Caraway
 * Date Created: 6/3/2018
 * Purpose: PrimMST takes an adjacency matrix and returns the edges of the MST (in the order that they were added to the MST)
 */
package primmst;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class PrimMST {
    //--------------------Nested-Edge-Class--------------------//
    //---------------------------------------------------------//
    private class Edge implements Comparable{
        private final int first_vertex;
        private final int second_vertex;
        private final int weight;
        
        /*Edge constructor*/
        public Edge(int first_vertex, int second_vertex, int weight) {
            this.first_vertex = first_vertex;
            this.second_vertex = second_vertex;
            this.weight = weight;
        }
        
        /*Methods to get the vertexs and weight for this Edge*/
        public int getFirstVertex() {return first_vertex;}
        public int getSecondVertex() {return second_vertex;}
        public int getWeight() {return weight;}

        @Override
        public int compareTo(Object o) {
            //if they are different classes, return -1
            if (o.getClass() != this.getClass())
                return -1;
            //compare Edges based upon weight +/- 2 depending on which is greater, 0 if same
            int cmp = this.weight - ((Edge)o).getWeight();
            
            if (cmp > 0)
                return 2;
            else if (cmp < 0)
                return -2;
            else return 0;
        }    
    }
    //---------------------------------------------------------//
    //---------------------------------------------------------//
    ArrayList<Character> headers;
    Scanner fin = null;
    
    public PrimMST(File f) {
        headers = new ArrayList<>();
        
        try {
            fin = new Scanner(f);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File Not Found: PrimMST not correctly initialized. Exiting program.");
            System.exit(1);
        }
        
        if (fin.hasNextLine())
            for (char c : fin.nextLine().toCharArray())
                if (c != ',')
                    headers.add(c);
    }
    
    public String[] makeMST() {
        PriorityQueue<Edge> edges = new PriorityQueue();
        
        //there is one less edge than vertex
        String mst[] = new String[headers.size() - 1];
        int ind = 0;
        
        boolean visited[] = new boolean[headers.size()];
        ArrayList<Edge> temp = new ArrayList<>();
        
        visited[0] = true;
        importGraph(edges);
        
        //continue until all vertex in the original graph have been visited and added to the mst
        while (!allVisited(visited)) {
            Edge e = edges.poll();
            
            //if the first vertex is in mst and the second vertex isn't
            if (visited[e.getFirstVertex()] && !visited[e.getSecondVertex()]) {
                mst[ind] = headers.get(e.getFirstVertex())+""+headers.get(e.getSecondVertex());
                ind++;
                
                //mark second vertex as visited
                visited[e.getSecondVertex()] = true;
                
                //move the stored edges back from temp into the queue
                for (int i = temp.size() - 1; i >= 0; i--) {
                    edges.add(temp.get(i));
                    temp.remove(i);
                }
            }
            else
                //just because the next lower weighted edge won't be the next to the mst doesn't mean that it
                //won't be added later, so I store it here for the time being
                temp.add(e);
        }
        
        return mst;
    }
    
    private boolean allVisited(boolean arr[]) {
        for (boolean visited : arr)
            if (!visited)
                return false;
        return true;
    }
    
    /*Imports a graph from an advacency matrix into the given PriorityQueue*/
    private void importGraph(PriorityQueue<Edge> edges) {
        if (!fin.hasNextLine())
            return;
        
        String line[] = null;
        for (int row = 0; fin.hasNextLine(); row++) {
            line = fin.nextLine().split(",");
            for (int col = 0; col < line.length; col++)
                if ((line[col].charAt(0) - '0') != 0)
                    try {
                        edges.add(new Edge(row, col, Integer.parseInt(line[col])));
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing for integer: "+line[col]);
                        System.out.println(e.getMessage());
                        System.exit(1);
                    }
        }
    }
    /*
     * MAIN CLASS
     */
    public static void main(String[] args) {
        File f = new File("input.txt");
        PrimMST prim = new PrimMST(f);
        String tree[] = prim.makeMST();
        for (String s : tree)
            System.out.print(s+"   ");
        System.out.println();
    }
    
}
