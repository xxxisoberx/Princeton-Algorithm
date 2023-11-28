import java.util.HashMap;
import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;

public class WordNet {
   private final ArrayList<String> map; // orginal string of i th line
   private final HashMap<String, ArrayList<Integer>> reverseMap; // noun to set of int
   private final SAP sapIns;
   
   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms) {
      if (synsets == null || hypernyms == null) throw new IllegalArgumentException();

      // convert from string to file
      In in1 = new In(synsets);
      In in2 = new In(hypernyms);
      
      // construct vertices from synsets
      map = new ArrayList<>();
      reverseMap = new HashMap<>();
      int vertexNum = 0;
      while (in1.hasNextLine()) {
         String[] line = in1.readLine().split(",");
         int id = Integer.parseInt(line[0]);
         String[] synonymSet = line[1].split(" ");
         for (int i = 0; i < synonymSet.length; i++) {
            String cur = synonymSet[i];
            if (reverseMap.containsKey(cur)) {
               reverseMap.get(cur).add(id);
            } else {
               ArrayList<Integer> newIntSet = new ArrayList<>();
               newIntSet.add(id);
               reverseMap.put(cur, newIntSet);
            }
         }
         map.add(line[1]);
         vertexNum++;
      }

      // construct edges from hypernyms
      Digraph digraph = new Digraph(vertexNum);
      while (in2.hasNextLine()) {
         String[] line = in2.readLine().split(",");
         int idHyponym = Integer.parseInt(line[0]);
         for (int i = 1; i < line.length; i++) {
            int idHypernym = Integer.parseInt(line[i]);
            digraph.addEdge(idHyponym, idHypernym);
         }
      }

      // if digraph is a rooted DAG - rooted, acyclic
      // if acyclic
      DirectedCycle diCycle = new DirectedCycle(digraph);
      if (diCycle.hasCycle()) throw new IllegalArgumentException();
      // if rooted
      int roots = 0;
      for (int i = 0; i < vertexNum; i++) {
         if (digraph.outdegree(i) == 0) roots++;
         if (roots > 1) throw new IllegalArgumentException();
      }

      sapIns = new SAP(digraph);
   }

   // returns all WordNet nouns
   public Iterable<String> nouns() {
      return reverseMap.keySet();
   }

   // is the word a WordNet noun?
   public boolean isNoun(String word) {
      if (word == null) throw new IllegalArgumentException();

      return reverseMap.containsKey(word);
   }

   // distance between nounA and nounB (defined below)
   public int distance(String nounA, String nounB) {
      if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

      return sapIns.length(reverseMap.get(nounA), reverseMap.get(nounB));
   }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
   public String sap(String nounA, String nounB) {
      if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

      return map.get(sapIns.ancestor(reverseMap.get(nounA), reverseMap.get(nounB)));
   }

   // do unit testing of this class
   public static void main(String[] args) {
      // for test code
   }
}