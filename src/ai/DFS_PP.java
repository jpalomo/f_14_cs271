package ai;

import java.io.File;

import model.Rule;
import model.Vector;

public class DFS_PP extends DFS {

    private Rules rules;

    public DFS_PP(int searchDepth, int numArrows) {
        super(searchDepth, numArrows);

        String ruleFileName = numArrows + "_arrow.txt";
        File file = new File(ruleFileName);
        if (!file.exists() || file.isDirectory()) {
            RuleGenerator.nArrowProblem(numArrows);
        }
        this.rules = new Rules(ruleFileName);
    }

    public DFS_PP() {
        this(1, 2);
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

        if (solveHelper(startState, goalState, 0, rules, null)) {
            System.out.println("Find goal state successfully, visited: " + nodeVisited + " nodes");
        } else {
            System.out.println("Fail to find goal state, visited: " + nodeVisited + " nodes");
        }
        return nodeVisited;
    }

    public boolean solveHelper(Vector currentState, Vector goalState, int currentDepth, int[] result, Vector parentState) {
        if (currentState.equals(goalState)) {
            printTransition(result, currentDepth);
            return true;
        }

        if (currentDepth >= searchDepth) {
            return false;
        }

        int numRules = rules.getNumRules();
        for (int i = 1; i < numRules; i++) {
            Rule rule = rules.getRuleById(i);

            if (Utility.satisfyPrecondition(rule.getPrecondition(), currentState)) {
                Vector nextState = Utility.apply(currentState, rule.getAction());

                if (!nextState.equals(parentState)) {
                    nodeVisited++;
                    result[currentDepth] = rule.getRuleId();
                    if (solveHelper(nextState, goalState, currentDepth + 1, result, currentState)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // a helper method for debug, print the transition from start state to goal state
    protected void printTransition(int[] result, int currentDepth) {
        Vector state = startState.copyOf();
        System.out.println(state);
        for (int i = 0; i < result.length && i < currentDepth; i++) {
            state = Utility.apply(state, rules.getRuleById(result[i]).getAction());
            System.out.println("Rule " + result[i] + ": " + state);
        }
    }

    public static void main(String[] args) {
        int d = 11;
        int n = 12;
        String line = "-----------------------------------------------";

        DFS_PP dfs_pp = new DFS_PP(d, n);

        // String[] starts = { "1,1,0,1,0,1,0,0,0,0,0,1" };
        // String[] ends = { "1,1,1,1,1,0,0,1,1,1,0,1" };
        //
        // for (int i = 0; i < starts.length; i++) {
        // Vector startState = new Vector(starts[i]);
        // Vector goalState = new Vector(ends[i]);
        //
        // dfs_pp.inputState(startState, goalState);
        // dfs_pp.solve();
        //
        // System.out.println(line);
        // }

        int num = 100;
        long nodes = 0;
        while (num > 0) {
            dfs_pp.generateStateRandomly();
            int result = dfs_pp.solve();
            if (result >= 0) {
                nodes += result;
                num--;
            }
            System.out.println(line);
        }
        System.out.println("Visited: " + nodes + " nodes in total!");
    }
}
