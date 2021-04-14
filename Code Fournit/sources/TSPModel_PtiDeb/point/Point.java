///////////////////////
// Vous n'avez pas a utiliser cette classe
// Ce code n'est donc pas commente pour vous, les seuls commentaires
// presents sont ceux qui m'etait utile
// au moment du codage
////////////////////////

package TSPModel_PtiDeb.point;

import TSPModel_PtiDeb.segment.Segment;
import java.util.ArrayList;

/**
 *
 * @author cyril
 */
public class Point implements Cloneable {
    
    public double x;//coord x
    public double y;//coord y
    public Integer ID;
    public ArrayList<Segment> appartientA; // segment appartient
    
    public int utilise; // utilise 
    public int possible;
    public Point point_interdit; //cle anti-cycle
    
    
    public Point(){
        possible=0;
    }
            
    public Point(double xx, double yy){
        x=xx;
        y=yy;
        appartientA=new ArrayList();
        point_interdit=this;
        possible=0;
    };
    
    public Point(int IDD, double xx, double yy){
        ID=IDD;
        x=xx;
        y=yy;
        appartientA=new ArrayList();
        point_interdit=this;
        possible=0;
    };
    
                
    @Override
    public Point clone(){
        Point o = new Point();
        o.x=x;
        o.y=y;
        o.ID=ID;
        o.appartientA=appartientA;// appartientA pas copie, juste pointeur
        o.utilise=utilise;
        o.point_interdit=point_interdit;
	return o;
    }

    
    
    @Override
    public String toString(){
        return "P"+ID+"( "+x+" , "+y+" )";
    }
}
