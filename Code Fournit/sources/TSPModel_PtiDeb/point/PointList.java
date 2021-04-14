///////////////////////
// Vous n'avez pas a utiliser cette classe
// Ce code n'est donc pas commente pour vous, les seuls commentaires
// presents sont ceux qui m'etait utile
// au moment du codage
////////////////////////

package TSPModel_PtiDeb.point;

import TSPModel_PtiDeb.segment.Segment;
import TSPModel_PtiDeb.rollBack.RollBack;
import TSPModel_PtiDeb.rollBack.RollBackList;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author cyril
 */
public class PointList implements Cloneable{
    public ArrayList<Point> liste;
    
    //////////////
    // modification 17/02/20 pour choisir un point de debut
    public Point premier;
    ///////////////
    
    public PointList(){
        liste=new ArrayList();
    //////////////
    // modification 17/02/20 pour choisir un point de debut
        premier=null;
    ///////////////
        
    }
    
    public void add(Point p){
        int IDP=liste.size();
        liste.add(p);
        //System.out.println("situation.addPoint(new Point("+p.x+","+p.y+"));");
    }
    
    //////////////
    // modification 17/02/20 pour choisir un point de debut
    public Point get(int i){
        for(Point p: liste)
            if(p.ID==i)
                return p;
        return null;
    }
    ///////////////
    
    
    @Override
    public PointList clone(){
        
        PointList PL = new PointList();
      		// On récupère l'instance à renvoyer par l'appel de la 
      		// méthode super.clone()
            Iterator itr = liste.iterator();
            while(itr.hasNext()) {
                PL.liste.add(((Point)itr.next()).clone());
            }
	    // on renvoie le clone
	    return PL;
    }

    public void check(Point p1, Point p2, RollBackList rollback) {
        
                /////////////////
                // RollBack
                RollBack rb1=new RollBack();
                rb1.type=RollBack.Type.PointUse;
                rb1.p1=p1;
                rb1.value=p1.utilise;
                RollBack rb2=new RollBack();
                rb2.type=RollBack.Type.PointUse;
                rb2.p1=p2;
                rb2.value=p2.utilise;
                rollback.liste.add(rb1);
                rollback.liste.add(rb2);
                //
                ////////////////////
                
        p1.utilise++;
        p2.utilise++;
        
        //////////////
        // modification 17/02/20 pour choisir un point de debut
        //if(p1.utilise==2)
        if(p1.utilise==2 || p1==premier)
        ///////////////
            for(Segment s : p1.appartientA)
            {
                if(s.disponible==true){
                    /////////////////
                    // RollBack
                    RollBack rb3=new RollBack();
                    rb3.type=RollBack.Type.SegDisp;
                    rb3.segment=s;
                    rb3.value=1;
                    rollback.liste.add(rb3);

                    //
                    ////////////////////
                    s.disponible=false;
                }
            }
        //////////////
        // modification 17/02/20 pour choisir un point de debut
        //if(p2.utilise==2)
        if(p2.utilise==2 || p2==premier)
        ///////////////
            for(Segment s : p2.appartientA)
            {
                if(s.disponible==true){
                    /////////////////
                    // RollBack
                    RollBack rb4=new RollBack();
                    rb4.type=RollBack.Type.SegDisp;
                    rb4.segment=s;
                    rb4.value=1;
                    rollback.liste.add(rb4);

                    //
                    ////////////////////
                    s.disponible=false;
                }
            }
        Point p1i=p1.point_interdit;
        Point p2i=p2.point_interdit;
                    /////////////////
                    // RollBack
                    RollBack rb5=new RollBack();
                    rb5.type=RollBack.Type.PointInterdit;
                    rb5.p1=p2.point_interdit;
                    rb5.p2=p2.point_interdit.point_interdit;
                    RollBack rb6=new RollBack();
                    rb6.type=RollBack.Type.PointInterdit;
                    rb6.p1=p1.point_interdit;
                    rb6.p2=p1.point_interdit.point_interdit;
                    rollback.liste.add(rb5);
                    rollback.liste.add(rb6);
                    //
                    ////////////////////
        p2.point_interdit.point_interdit=p1i;
        p1.point_interdit.point_interdit=p2i;
        for(Segment s : p1i.appartientA)
            if(p2i.appartientA.contains(s)){
                if(s.disponible==true){
                    /////////////////
                    // RollBack
                    RollBack rb7=new RollBack();
                    rb7.type=RollBack.Type.SegDisp;
                    rb7.segment=s;
                    rb7.value=1;
                    rollback.liste.add(rb7);

                    //
                    ////////////////////
                    s.disponible=false;
                }
            }
    }
    
    //////////////
    // modification 17/02/20 pour choisir un point de debut
    public void setPremier(int i){
        premier=get(i);
    }
    ///////////////
    


}
