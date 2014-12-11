package ai;

import model.Constant;
import model.Constant.Value;
import model.Vector;

public abstract class DFS {

    protected Vector startState;
    protected Vector goalState;
    protected int searchDepth;
    protected int numArrows;
    protected int nodeVisited;

    public DFS(int searchDepth, int numArrows) {
        this.startState = null;
        this.goalState = null;
        this.searchDepth = searchDepth;
        this.numArrows = numArrows;
        this.nodeVisited = 0;
    }

    public DFS() {
        this(1, 2);
    }

    public void inputState(Vector startState, Vector goalState) {
        System.out.println("Start state: " + startState);
        System.out.println("Goal state: " + goalState);
        this.startState = startState;
        this.goalState = goalState;
        this.nodeVisited = 0;
    }

    protected int checkState(Vector state) {
        int numUp = 0;
        if (state == null || state.getLength() != numArrows) {
            return -1;
        }
        for (int i = 1; i <= numArrows; i++) {
            if (state.getVariable(i) == null || !state.getVariable(i).isConstant()) {
                return -1;
            } else {
                if (((Constant) state.getVariable(i)).getValue().equals(Value.UP)) {
                    numUp++;
                }
            }
        }
        return numUp % 2 == 0 ? 0 : 1;
    }

    public void generateStateRandomly() {
        startState = new Vector(numArrows);
        for (int i = 0; i < numArrows; i++) {
            int rand = (int) (Math.random() * 2);
            startState.appendVariable(new Constant(rand + ""));
        }
        System.out.println("Start state: " + startState);

        goalState = new Vector(numArrows);
        for (int i = 0; i < numArrows; i++) {
            int rand = (int) (Math.random() * 2);
            goalState.appendVariable(new Constant(rand + ""));
        }
        System.out.println("Goal state: " + goalState);
    }

    // a helper method for debug
    protected void printRules(int[] rules, int currentDepth) {
        System.out.print("Rules: ");
        for (int i = 0; i < rules.length && i < currentDepth; i++) {
            System.out.print(rules[i] + " ");
        }
        System.out.println();
    }

    public abstract int solve();
}
