package ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.Rule;

public class MovePruningTree {

    private Rules rules;
    private MPTreeNode root;
    private int level;
    private List<Rule> visited;

    /**
     * Creates a new tree with moves that result in duplicate nodes pruned.
     * 
     * @param level
     *            the number of moves to apply on the tree
     */
    public MovePruningTree(int level, String ruleFileName) {
        rules = new Rules(ruleFileName);
        this.level = level;
        visited = new ArrayList<Rule>();
        constructTree();
    }

    private void constructTree() {
        root = new MPTreeNode(null, rules.getEmptyMove(), 0);
        LinkedList<MPTreeNode> queue = new LinkedList<MPTreeNode>();
        queue.add(root);
        visited.add(root.getMicroRule());

        while (!queue.isEmpty()) {
            MPTreeNode head = queue.removeFirst();
            if (head.level >= level) {
                break;
            }

            int numRules = rules.getNumRules();
            OUTER: for (int i = 1; i < numRules; i++) {
                Rule rule = rules.getRuleById(i);
                Rule microRule = Utility.combineRules(head.getMicroRule(), rule);

                // check if this is a valid move
                if (microRule != null) {
                    // use existing rules to check if this new micro rule can be pruned
                    for (Rule existRule : visited) {
                        if (Utility.canPrune(existRule, microRule)) {
                            continue OUTER;
                        }
                    }

                    MPTreeNode child = new MPTreeNode(rule, microRule, head.level + 1);
                    head.addChild(child);
                    queue.add(child);
                    visited.add(microRule);
                }
            }
        }
    }

    public MPTreeNode getRoot() {
        return root;
    }

    public int getLevel() {
        return level;
    }

    public Rules getRules() {
        return rules;
    }

    // helper method for debugging
    protected void printByLevel() {
        List<List<MPTreeNode>> result = new ArrayList<List<MPTreeNode>>();
        LinkedList<MPTreeNode> queue = new LinkedList<MPTreeNode>();
        queue.add(root);

        while (!queue.isEmpty()) {
            MPTreeNode head = queue.removeFirst();

            if (result.size() <= head.level) {
                result.add(new ArrayList<MPTreeNode>());
            }

            List<MPTreeNode> levelList = result.get(head.level);
            levelList.add(head);

            for (MPTreeNode child : head.getChildren()) {
                queue.add(child);
            }
        }

        for (List<MPTreeNode> level : result) {
            System.out.println(level);
        }
    }

    public static class MPTreeNode {
        private Rule rule;
        private Rule microRule;
        private List<MPTreeNode> children;
        private int level;

        public MPTreeNode(Rule rule, Rule microRule, int level) {
            this.rule = rule;
            this.microRule = microRule;
            children = new ArrayList<MPTreeNode>();
            this.level = level;
        }

        public void addChild(MPTreeNode node) {
            children.add(node);
        }

        public List<MPTreeNode> getChildren() {
            return children;
        }

        public Rule getRule() {
            return rule;
        }

        public Rule getMicroRule() {
            return microRule;
        }

        public int getLevel() {
            return level;
        }

        @Override
        public String toString() {
            return microRule.toString();
        }
    }
}
