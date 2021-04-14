///////////////////////
// Vous n'avez pas a utiliser cette classe
// Ce code n'est donc pas commente pour vous, les seuls commentaires
// presents sont ceux qui m'etait utile
// au moment du codage
////////////////////////

package TSPModel_PtiDeb.segment;

import TSPModel_PtiDeb.point.Point;
import java.util.ArrayList;

/**
 *
 * @author cyril
 */
public class Segment implements Comparable<Segment>, Cloneable{
    
    public Point A; // pointeur sur un sommet
    public Point B; // pointeur sur le deuxieme sommet
    public int ID; // pointeur sur le deuxieme sommet
    public double distance; // distance precalcule entre les deux points
    public ArrayList<Segment> elimine; // simplification en cas de selection, elimination de segment
            
    public boolean disponible; // disponible ou non (fonction par clonage pou rtest)
    
    public Segment(){}
        
    public Segment(Point a, Point b){
        A=a;
        B=b;
        distance=Math.sqrt(
            Math.pow(A.x - B.x,2.0)
            +
            Math.pow(A.y - B.y, 2.0 )
        );
        elimine=new ArrayList();
        disponible = true;
    }
    

    @Override
    public int compareTo(Segment o) { // comparaison de distance
        Integer min = Math.min(A.possible,B.possible);
        Integer min2 = Math.min(o.A.possible,o.B.possible);
        
        return (min>min2)?1:(min<min2?-1:((distance >o.distance)?1:(distance ==o.distance)?0:-1));
    }
        
    public boolean croise(Segment S){
        
        double S0 = distance + S.distance;
        double S1 = Math.sqrt(Math.pow(S.A.x-A.x,2.0)+Math.pow(S.A.y-A.y,2.0))
                +
                Math.sqrt(Math.pow(S.B.x-B.x,2.0)+Math.pow(S.B.y-B.y,2.0));
        
        
        double S2 = Math.sqrt(Math.pow(S.A.x-B.x,2.0)+Math.pow(S.A.y-B.y,2.0))
                +
                Math.sqrt(Math.pow(S.B.x-A.x,2.0)+Math.pow(S.B.y-A.y,2.0));
        
        if(S0 > S1 && S0 > S2)
            return true;
        
        return false;
        
        ////////////////////////////
        // ancien fonctionnement , IE par croisement
        /*
        double Ax = A.x;
	double Ay = A.y;
	double Bx = B.x;
	double By = B.y;
	double Cx = S.A.x;
	double Cy = S.A.y;
	double Dx = S.B.x;
	double Dy = S.B.y;
 
	double Sx;
	double Sy;
	if(Ax==Bx)
	{
            if(Cx==Dx) return false;
            else
            {
                double pCD = (Cy-Dy)/(Cx-Dx);
                Sx = Ax;
                Sy = pCD*(Ax-Cx)+Cy;
            }
	}
	else
	{
            if(Cx==Dx)
            {
                double pAB = (Ay-By)/(Ax-Bx);
                Sx = Cx;
                Sy = pAB*(Cx-Ax)+Ay;
            }
            else
            {
                double pCD = (Cy-Dy)/(Cx-Dx);
                double pAB = (Ay-By)/(Ax-Bx);
                if(pCD==pAB)return false;
		double oCD = Cy-pCD*Cx;
		double oAB = Ay-pAB*Ax;
		Sx = (oAB-oCD)/(pCD-pAB);
		Sy = pCD*Sx+oCD;
            }
	}
                
	if((Sx<Ax && Sx<Bx)||(Sx>Ax && Sx>Bx) || (Sx<Cx && Sx<Dx)||(Sx>Cx && Sx>Dx)
            || (Sy<Ay && Sy<By)||(Sy>Ay && Sy>By) || (Sy<Cy && Sy<Dy)||(Sy>Cy && Sy>Dy)) return false;
		  
                  
        return true; //or :     return new Point2D.Float((float)Sx,(float)Sy)
    */
    //////////////////////////
    }
    
    
    @Override
    public Segment clone(){
        Segment o = new Segment();
        o.A=A;
        o.B=B;
        o.ID=ID;
        o.distance=distance;
        o.elimine=elimine; // elimine pas copie, pointeur suffisant
        o.disponible = disponible;
	return o;
    }
    
    @Override
    public String toString(){
        return "S"+ID+"( "+A+" , "+B+" )"+" distance:"+distance;
    }
}
