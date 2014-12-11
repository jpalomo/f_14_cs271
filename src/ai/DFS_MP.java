package ai;

import java.io.File;
import java.util.List;

import model.Vector;
import ai.MovePruningTree.MPTreeNode;

public class DFS_MP extends DFS {

    private MovePruningTree mpTree;

    public DFS_MP(int movePruningTreeLevel, int searchDepth, int numArrows) {
        super(searchDepth, numArrows);

        String ruleFileName = numArrows + "_arrow.txt";
        File file = new File(ruleFileName);
        if (!file.exists() || file.isDirectory()) {
            RuleGenerator.nArrowProblem(numArrows);
        }
        mpTree = new MovePruningTree(movePruningTreeLevel, ruleFileName);
    }

    public DFS_MP() {
        this(1, 1, 2);
    }

    @Override
    public int solve() {

        int startStatus = checkState(startState);
        int goalStatus = checkState(goalState);

        if (startStatus == -1 || goalStatus == -1) {
            System.out.println("Invalid Input");
            return -1;
        }

        if ((startStatus ^ goalStatus) != 0) {
            System.out.println("Mismatch of start and goal state, no Solution!");
            return -1;
        }

        nodeVisited = 0;
        int[] rules = new int[searchDepth];

        if (solveHelper(startState, goalState, mpTree.getRoot(), 0, rules)) {
            System.out.println("Find goal state successfully, visited: " + nodeVisited + " nodes");
        } else {
            System.out.println("Fail to find goal state, visited: " + nodeVisited + " nodes");
        }
        return nodeVisited;
    }

    private boolean solveHelper(Vector currentState, Vector goalState, MPTreeNode currentNode, int currentDepth,
        int[] rules) {
        if (currentState.equals(goalState)) {
            printTransition(rules, currentDepth);
            return true;
        }

        if (currentDepth >= searchDepth) {
            return false;
        }

        int nodeLevel = currentNode.getLevel();
        // leaf node, go back to root node
        if (nodeLevel == mpTree.getLevel()) {
            return solveHelper(currentState, goalState, mpTree.getRoot(), currentDepth, rules);
        } else {
            List<MPTreeNode> children = currentNode.getChildren();
            for (MPTreeNode nextNode : children) {
                if (Utility.satisfyPrecondition(nextNode.getRule().getPrecondition(), currentState)) {
                    Vector nextState = Utility.apply(currentState, nextNode.getRule().getAction());
                    nodeVisited++;
                    rules[currentDepth] = nextNode.getRule().getRuleId();
                    if (solveHelper(nextState, goalState, nextNode, currentDepth + 1, rules)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    // a helper method for debug, print the transition from start state to goal state
    protected void printTransition(int[] rules, int currentDepth) {
        Vector state = startState.copyOf();
        System.out.println(state);
        for (int i = 0; i < rules.length && i < currentDepth; i++) {
            state = Utility.apply(state, mpTree.getRules().getRuleById(rules[i]).getAction());
            System.out.println("Rule " + rules[i] + ": " + state);
        }
    }

    public static void main(String[] args) {
        int d = 11;
        int n = 12;
        int l = 2;
        String line = "-----------------------------------------------";

        DFS_MP dfs_mp = new DFS_MP(l, d, n);

        // String[] starts = { "1,1,0,1,0,1,0,0,0,0,0,1" };
        // String[] ends = { "1,1,1,1,1,0,0,1,1,1,0,1" };

        // for (int i = 0; i < starts.length; i++) {
        // Vector startState = new Vector(starts[i]);
        // Vector goalState = new Vector(ends[i]);
        //
        // dfs_mp.inputState(startState, goalState);
        // dfs_mp.solve();
        //
        // System.out.println(line);
        // }

        int num = 100;
        long nodes = 0;
        while (num > 0) {
            dfs_mp.generateStateRandomly();
            int result = dfs_mp.solve();
            if (result >= 0) {
                nodes += result;
                num--;
            }
            System.out.println(line);
        }
        System.out.println("Visited: " + nodes + " nodes in total!");
    }
}
