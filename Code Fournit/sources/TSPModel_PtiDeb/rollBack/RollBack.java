///////////////////////
// Vous n'avez pas a utiliser cette classe
// Ce code n'est donc pas commente pour vous, les seuls commentaires
// presents sont ceux qui m'etait utile
// au moment du codage
////////////////////////

package TSPModel_PtiDeb.rollBack;

import TSPModel_PtiDeb.point.Point;
import TSPModel_PtiDeb.segment.Segment;

/**
 *
 * @author cyril
 */
public class RollBack {
    public enum Type{SegDisp,PointUse,PointInterdit}
    
    public Type type;
    public Segment segment;
    public Point p1,p2;
    public int value;
    
    @Override
    public String toString(){
       
        return "["+type+"]";
    }
}
