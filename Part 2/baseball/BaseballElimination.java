import java.util.HashMap;
import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

public class BaseballElimination {
    private final int numberOfTeams;
    private final HashMap<String, Integer> nameToIndex;
    private final HashMap<Integer, String> indexToName;
    private final HashMap<String, ArrayList<String>> nameToEliminatedByTeams;
    private final int[] wins;
    private final int[] loses;
    private final int[] remaining;
    private final int[][] leftInDiv;
    private final boolean[] solved;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        numberOfTeams = in.readInt();
        // games initialization
        wins = new int[numberOfTeams];
        loses = new int[numberOfTeams];
        remaining = new int[numberOfTeams];
        leftInDiv = new int[numberOfTeams][numberOfTeams];
        solved = new boolean[numberOfTeams];
        // teams initialization
        nameToIndex = new HashMap<>();
        indexToName = new HashMap<>();
        nameToEliminatedByTeams = new HashMap<>();
        for (int i = 0; i < numberOfTeams; i++) {
            String name = in.readString();
            wins[i] = in.readInt(); // wins
            loses[i] = in.readInt(); // loses
            remaining[i] = in.readInt(); // remaining
            for (int j = 0; j < numberOfTeams; j++) {
                leftInDiv[i][j] = in.readInt();
            }
            nameToIndex.put(name, i);
            indexToName.put(i, name);
            nameToEliminatedByTeams.put(name, new ArrayList<>());
            solved[i] = false;
        }

    }
    
    // number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return nameToIndex.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (!nameToIndex.containsKey(team)) throw new IllegalArgumentException();

        return wins[nameToIndex.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!nameToIndex.containsKey(team)) throw new IllegalArgumentException();

        return loses[nameToIndex.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!nameToIndex.containsKey(team)) throw new IllegalArgumentException();

        return remaining[nameToIndex.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!nameToIndex.containsKey(team1) || !nameToIndex.containsKey(team2)) throw new IllegalArgumentException();

        int indexOfTeam1 = nameToIndex.get(team1);
        int indexOfTeam2 = nameToIndex.get(team2);
        return leftInDiv[indexOfTeam1][indexOfTeam2];
    }

    // construct a FlowNetWork of "team"
    private FlowNetwork createFlowNetWork(String team) {
        int gameVertices = (numberOfTeams - 1) * (numberOfTeams - 2) / 2;
        int teamVertices = numberOfTeams - 1;
        int numberOfVertices = gameVertices + teamVertices + 2;

        int teamIdx = nameToIndex.get(team);
        FlowNetwork teamFnw = new FlowNetwork(numberOfVertices);
        // convert other teams' teamRow to vertexIndex
        int[] rowToIdx = new int[numberOfTeams];
        int teamStartIdx = gameVertices + 1;
        for (int j = 0; j < numberOfTeams; j++) {
            if (j == teamIdx) continue;
            rowToIdx[j] = teamStartIdx++;
        }
        // addEdge "0 - GameVertices" & "GameVertices - TeamVertices"
        int gameIdx = 1;
        for (int teamRow1 = 0; teamRow1 < numberOfTeams; teamRow1++) {
            if (teamRow1 == teamIdx) continue;
            for (int teamRow2 = teamRow1 + 1; teamRow2 < numberOfTeams; teamRow2++) {
                if (teamRow2 == teamIdx) continue;
                teamFnw.addEdge(new FlowEdge(0, gameIdx, leftInDiv[teamRow1][teamRow2]));
                int idxOfTeam1 = rowToIdx[teamRow1];
                int idxOfTeam2 = rowToIdx[teamRow2];
                teamFnw.addEdge(new FlowEdge(gameIdx, idxOfTeam1, Double.POSITIVE_INFINITY));
                teamFnw.addEdge(new FlowEdge(gameIdx, idxOfTeam2, Double.POSITIVE_INFINITY));
                gameIdx++;
            }
        }
        // addEdge "TeamVertices - endIndex"
        int endIdx = numberOfVertices - 1;
        for (int teamRow = 0; teamRow < numberOfTeams; teamRow++) {
            if (teamRow == teamIdx) continue;
            double cap = wins[teamIdx] + remaining[teamIdx] - wins[teamRow];
            teamFnw.addEdge(new FlowEdge(rowToIdx[teamRow], endIdx, cap));
        }
        return teamFnw;
    }

    // if the team of "flowNetWork" and "fordFulkerson" eliminated ("flowNetWork" is created from "flowNetWork")
    private boolean isOut(FlowNetwork flowNetWork, FordFulkerson fordFulkerson) {
        double maxFlow = fordFulkerson.value();
        double fullCapacity = 0;
        for (FlowEdge edge : flowNetWork.adj(0)) {
            fullCapacity += edge.capacity();
        }
        return maxFlow < fullCapacity;
    }

    // add teams eliminating "team" to the ArrayList of "team"
    private void solve(String team) {
        int teamIdx = nameToIndex.get(team);
        if (solved[teamIdx]) return;
        solved[teamIdx] = true;
        ArrayList<String> eliminatedByTeams = new ArrayList<>();
        // trivial elimination
        int maxWins = wins[teamIdx] + remaining[teamIdx];
        for (String teamName : teams()) {
            int curTeamIdx = nameToIndex.get(teamName);
            if (maxWins < wins[curTeamIdx]) {
                eliminatedByTeams.add(teamName);
                nameToEliminatedByTeams.put(team, eliminatedByTeams);
                return;
            }
        }
        // nontrivial elimination
        FlowNetwork flowNetWork = createFlowNetWork(team);
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetWork, 0, flowNetWork.V() - 1);
        if (!isOut(flowNetWork, fordFulkerson)) return;
        int teamVertexIdx = (numberOfTeams - 1) * (numberOfTeams - 2) / 2 + 1;
        for (int teamRow = 0; teamRow < numberOfTeams; teamRow++) {
            if (teamRow == teamIdx) continue;
            if (fordFulkerson.inCut(teamVertexIdx)) {
                eliminatedByTeams.add(indexToName.get(teamRow));
            }
            teamVertexIdx++;
        }
        nameToEliminatedByTeams.put(team, eliminatedByTeams);
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!nameToIndex.containsKey(team)) throw new IllegalArgumentException();
        
        if (!solved[nameToIndex.get(team)]) solve(team);
        return !nameToEliminatedByTeams.get(team).isEmpty();
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!nameToIndex.containsKey(team)) throw new IllegalArgumentException();

        if (!solved[nameToIndex.get(team)]) solve(team);
        if (nameToEliminatedByTeams.get(team).isEmpty()) return null;
        return nameToEliminatedByTeams.get(team);
    }

    public static void main(String[] args) {
        HashMap<String, Integer> a = new HashMap<>();
        a.put("a", 1);
        System.out.println(a.containsKey("a"));
    }
}
