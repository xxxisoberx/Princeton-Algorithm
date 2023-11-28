public class Outcast {
    private final WordNet wordnet;
    
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int length = nouns.length;
        int maxDis = Integer.MIN_VALUE;
        String maxDisNoun = nouns[0];
        for (int i = 0; i < length; i++) {
            int dis = 0;
            for (int j = 0; j < length; j++) {
                dis += wordnet.distance(nouns[i], nouns[j]);
            }
            if (dis > maxDis) {
                maxDis = dis;
                maxDisNoun = nouns[i];
            }
        }
        return maxDisNoun;
    }

    // see test client below
    public static void main(String[] args) {
        // for code test
    }
}