///////////////////////
// Vous n'avez pas a utiliser cette classe
// Ce code n'est donc pas commente pour vous, les seuls commentaires
// presents sont ceux qui m'etait utile
// au moment du codage
////////////////////////

package TSPModel_PtiDeb.segment;

import TSPModel_PtiDeb.rollBack.RollBack;
import TSPModel_PtiDeb.rollBack.RollBackList;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author cyril
 */
public class SegmentList {

    public ArrayList<Segment> liste;
    
    
    public SegmentList(){
        liste=new ArrayList();
    }
    
    public void add(Segment segAjoute){
        
        ArrayList<Segment> listesegCroise = new ArrayList();
        for(Segment s : liste){
            if(segAjoute.croise(s))listesegCroise.add(s);
        }
        
        liste.add(segAjoute);
        
        for(Segment seg : listesegCroise){
            seg.elimine.add(segAjoute);
            segAjoute.elimine.add(seg);
        }
    }
    
    
    @Override
    public SegmentList clone(){
        
        SegmentList SL = new SegmentList();
        for (Segment s : liste){
            SL.liste.add(s.clone());
        }
	return SL;
    }
    
    
    public ArrayList<Segment> querestetil(double pointrestant,int sommet_restant,int firstID) {
        
        ArrayList<Segment> listtest= new ArrayList();
        ArrayList<Segment> list= new ArrayList();
        
        double dist=0;
        double arrete=0;
        for(Segment s :liste){
                if(s.ID>=firstID && s.disponible)
                {
                    dist+=s.distance;
                    if(dist>pointrestant){
                        return list;
                    }
                    
                    arrete++;
                    listtest.add(s);
                    if(arrete==sommet_restant){
                        dist-=listtest.get(0).distance;
                        list.add(listtest.remove(0));
                        arrete--;
                    }
                }
        }
        return list; 
    }


    public void check(Segment seg,RollBackList rollback) {
        seg.disponible=false;
        
        
                
        
        for(Segment s : seg.elimine)
        {
            if(s.disponible==true){
                /////////////////
                // RollBack
                RollBack rb=new RollBack();
                rb.type=RollBack.Type.SegDisp;
                rb.segment=s;
                rb.value=1;
                rollback.liste.add(rb);
                //
                ////////////////////
                s.disponible=false;
            }
        }
    }

    
    public void desactivSegment(Segment s) {
        if(s.disponible==true)
        {
            s.disponible = false;
        }
    }

    public void affectID() {
        Collections.sort(liste);
        int IDcpt=0;
        for(Segment s: liste)
        {
            s.ID=IDcpt;
            //System.out.println("Afectation ID "+IDcpt+" pour l'arete "+s);
            IDcpt++;
        }
        
    }
    
}
