package model;

import java.util.StringTokenizer;

/**
 * This class represent a particular rule in game as dictated by the input
 * file. 
 * @author Palomo Feng
 *
 */
public class Rule {

    private int rid;
    private Vector precondition;
    private Vector action;
    private int cost;

    /**
     * 
     * @param line the string representation of a complete rule
     */
    public Rule(String line) {
        constructRule(line);
    }

    /**
     * 
     * Constructs a new rule given the preconditions, actions, and cost
     * and returns a new Rule object with rule id == rid
     * @param rid unique identifier for a game rule
     * @param precondition the precondition vector for the rule
     * @param action the resulting action vector (represents the application of the
     * rule on the precondition
     * @param cost associated cost of applying the rule (move)
     */
    public Rule(int rid, Vector precondition, Vector action, int cost) {
        this.rid = rid;
        this.precondition = precondition;
        this.action = action;
        this.cost = cost;
    }

    /**
     * Constructs a new rule from the given textual representation of the rule
     * A rule comes in the form of of ruleID|preconditionVector|actionVector|cost.
     * Note that each field of the rule is delimited by a pipe.
     * 
     * @param line the string representation of a rule
     */
    private void constructRule(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, "|");
        if (tokenizer.countTokens() == 4) {
            this.rid = Integer.parseInt(tokenizer.nextToken());
            this.precondition = new Vector(tokenizer.nextToken());
            this.action = new Vector(tokenizer.nextToken());
            this.cost = Integer.parseInt(tokenizer.nextToken());
        }
    }

    public int getRuleId() {
        return rid;
    }

    public boolean isMicroRule() {
        return rid == 0;
    }

    public Vector getPrecondition() {
        return precondition;
    }

    public Vector getAction() {
        return action;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return precondition + "->" + action + " : " + cost;
    }
}
