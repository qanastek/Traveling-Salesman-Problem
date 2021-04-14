///////////////////////
// Vous n'avez pas a utiliser cette classe
// Ce code n'est donc pas commente pour vous, les seuls commentaires
// presents sont ceux qui m'etait utile
// au moment du codage
////////////////////////

package TSPModel_PtiDeb.rollBack;

import java.util.ArrayList;

/**
 *
 * @author cyril
 */
public class RollBackList {
    public ArrayList<RollBack> liste;
    // a faire en FILO
    
    public RollBackList(){
        liste= new ArrayList();
    }
    public void back(){
        for(RollBack rb : liste)
        {
            switch(rb.type){
                case SegDisp :
                    rb.segment.disponible=(rb.value==1);
                    break;
                case PointUse:
                    rb.p1.utilise=rb.value;
                    break;
                case PointInterdit:
                    rb.p1.point_interdit=rb.p2;
                    break;
                default:
                    break;
            }
        }
        liste.removeAll(liste);
        
    }
    
}
