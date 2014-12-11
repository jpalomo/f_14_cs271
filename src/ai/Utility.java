package ai;

import model.*;
import model.Constant.Value;

public class Utility {

    /**
     * this is the implementation of the algorithm in page 3, which is used to combine a micro rule and a normal rule to
     * get a new micro rule
     * 
     * @param r1
     *            old mirco rule
     * @param r2
     *            normal rule
     * @return micro rule obtained from applying r1 to r2. null if the combination is invalid
     */
    public static Rule combineRules(Rule r1, Rule r2) {
        Vector p1 = r1.getPrecondition().copyOf();
        Vector a1 = r1.getAction().copyOf();
        Vector p2 = r2.getPrecondition();
        Vector a2 = r2.getAction();
        int length = p2.getLength();

        for (int i = 1; i <= length; i++) {
            Variable a1i = a1.getVariable(i);
            Variable p2i = p2.getVariable(i);

            if (a1i.isConstant()) {
                Value val = ((Constant) a1i).getValue();
                if (p2i.isConstant()) {
                    if (val != ((Constant) p2i).getValue()) {
                        return null;
                    }
                } else {
                    int j = ((Symbol) p2i).getPosition();
                    Variable a1j = a1.getVariable(j);
                    if (a1j.isConstant()) {
                        if (val != ((Constant) a1j).getValue()) {
                            return null;
                        }
                    } else {
                        int k = ((Symbol) a1j).getPosition();
                        replace(p1, k, val);
                        replace(a1, k, val);
                    }
                }
            } else {
                int j = ((Symbol) a1i).getPosition();
                if (p2i.isConstant()) {
                    Value val = ((Constant) p2i).getValue();
                    replace(p1, j, val);
                    replace(a1, j, val);
                } else {
                    int k = ((Symbol) p2i).getPosition();
                    if (k != j) {
                        Variable a1k = a1.getVariable(k);

                        if (a1k.isConstant()) {
                            Value val = ((Constant) a1k).getValue();
                            replace(p1, j, val);
                            replace(a1, j, val);
                        } else {
                            int t = ((Symbol) a1k).getPosition();
                            int y = Math.min(j, t), z = Math.max(j, t);
                            replace(p1, z, y);
                            replace(a1, z, y);
                        }
                    }
                }
            }
        }

        a1 = apply(a1, a2);
        return new Rule(0, p1, a1, r1.getCost() + r2.getCost()); // rid = 0 => micro rule
    }

    /**
     * a helper method used to replace all occurrence of x_pos in vec with val
     * 
     * @param vec
     *            vector
     * @param pos
     *            x_pos
     * @param val
     *            value used to replace x_pos
     */
    protected static void replace(Vector vec, int pos, Value val) {
        for (int i = 1; i <= vec.getLength(); i++) {
            Variable var = vec.getVariable(i);
            if (var != null) {
                if (!var.isConstant() && ((Symbol) var).getPosition() == pos) {
                    vec.setVariable(i, new Constant(val));
                }
            } else {
                break;
            }
        }
    }

    /**
     * a helper method used to replace all occurrence of x_posi with x_posj
     * 
     * @param vec
     *            vector
     * @param posi
     *            x_i
     * @param posj
     *            x_j
     */
    protected static void replace(Vector vec, int posi, int posj) {
        for (int i = 1; i <= vec.getLength(); i++) {
            Variable var = vec.getVariable(i);
            if (var != null) {
                if (!var.isConstant() && ((Symbol) var).getPosition() == posi) {
                    vec.setVariable(i, new Symbol(posj));
                }
            } else {
                break;
            }
        }
    }

    /**
     * implementation of the algorithm o n page 3. apply vec2 to vec1 and return a result. vec1 and vec2 are left
     * untouched
     * 
     * @param vec1
     * @param vec2
     * @return
     */
    public static Vector apply(Vector vec1, Vector vec2) {
        Vector result = vec1.copyOf();
        for (int i = 1; i <= vec1.getLength(); i++) {
            Variable v2i = vec2.getVariable(i);
            if (v2i.isConstant()) {
                result.setVariable(i, new Constant(((Constant) v2i).getValue()));
            } else {
                int j = ((Symbol) v2i).getPosition();
                Variable copyj = vec1.getVariable(j);
                if (copyj.isConstant()) {
                    result.setVariable(i, new Constant(((Constant) copyj).getValue()));
                } else {
                    result.setVariable(i, new Symbol(((Symbol) copyj).getPosition()));
                }
            }
        }
        return result;
    }

    /**
     * this method is used to judge if rule A can be used to prune rule B. it is the implementation of the algorithm on
     * page 4
     * 
     * @param ruleA
     * @param ruleB
     * @return
     */
    public static boolean canPrune(Rule ruleA, Rule ruleB) {
        if (ruleA.getCost() > ruleB.getCost()) {
            return false;
        }

        Vector pa = ruleA.getPrecondition();
        Vector pb = ruleB.getPrecondition();
        for (int i = 1; i <= pa.getLength(); i++) {
            Variable pai = pa.getVariable(i);
            Variable pbi = pb.getVariable(i);

            if (pai.isConstant()) {
                if (!pbi.isConstant() || !pbi.equals(pai)) {
                    return false;
                }
            } else {
                int j = ((Symbol) pai).getPosition();
                if (j != i) {
                    if (!pbi.equals(pb.getVariable(j))) {
                        return false;
                    }
                }
            }
        }

        return ruleB.getAction().equals(apply(ruleB.getPrecondition(), ruleA.getAction()));
    }

    /**
     * judge if a game state satisfy a rule's precondition
     * 
     * @param precondition
     *            could be micro rule or rule's precondition
     * @param state
     *            a game state, all variables should be Constant
     * @return
     */
    public static boolean satisfyPrecondition(Vector precondition, Vector state) {
        if (precondition.getLength() != state.getLength()) {
            return false;
        }
        int length = precondition.getLength();
        for (int i = 1; i <= length; i++) {
            Variable var = precondition.getVariable(i);
            if (var.isConstant()) {
                if (!var.equals(state.getVariable(i))) {
                    return false;
                }
            } else {
                int j = ((Symbol) var).getPosition();
                if (!state.getVariable(j).isConstant() || !state.getVariable(j).equals(state.getVariable(i))) {
                    return false;
                }
            }
        }

        return true;
    }
}
