/*
 * To change this license header, choose License Headers in Projectcs Properties.
 * To change this templatecs file, choose Tools | Templates
 * and open the template in the editor.
 */
package TSPModel_PtiDeb;

import TSPModel_PtiDeb.point.Point;
import TSPModel_PtiDeb.point.PointList;
import TSPModel_PtiDeb.segment.Segment;
import TSPModel_PtiDeb.segment.SegmentList;
import TSPModel_PtiDeb.rollBack.RollBackList;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author cyril
 */
public class TSPModel_PtiDeb extends Observable  implements Cloneable,Runnable {
    
    //ces elements vous concerne meme si vous ne les utilisez pas directement
    
    // booleen de mise en pause, qui passe sur true quand on lance la fonction "pause()"
    private boolean pause;
    
    // identifiant du segment courant (ajoute ou supprime) ainsi que des points concernes
    private int segmentID;
    private int segmentP1;
    private int segmentP2;

        
    // type d'action possible
    public enum ActionType{Add,Remove,NewBest,Finish}
    // derniere action realisee, des qu'elle est mise a jour un notifyObservers() est lance 
    // (enfin, chaque fois que je la met a jour dans le code j'ai lance un 
    // setCHanged() + notifyObservers() ensuite, il n'y a pas de magie qui a tout fait automatiquement
    // bien entendu )
    // ce qui provoquera en reaction le lancement de la fonction "update()" de ses "Observer"
    //
    // voir fonction newBest() de cette classe ainsi que addSegmentToObservers et removeSegmentToObservers
    private ActionType action;
    
    //tout ces "private" ont des getter associes, tout en bas dans le code de cette classe
    // getAction() / getSegmentID() / getSegmentP1() / getSegmentP2()
    
    
    // ces elements ne vous concernent pas, 
    // mais je detaille rapidement a quoi ils servent
    private SegmentList segmentlist;//liste des segments existants (tous)
    private PointList pointlist;//liste des points
    private double distance_total;// distance total du chemin en cour
    private int nb_seg_actu; // nombre de segment dans le chemin courant
    private int nb_seg_total; // nb de segment a selectionner
    //ceux ci-dessous ne servent plus
    private TSPModel_PtiDeb BEST; // servaient a enregistre le meilleur chemin actuel
    private ArrayList<Segment> choix; // servaient a enregistre le meilleur chemin actuel
    private boolean trace; // ancien boolean qui servait a determiner si l'on souhaitais ou non tracer le graphe
    private boolean acloner; // servaient a enregistre le meilleur chemin actuel, par clonage du TSPModel_PtiDeb
    
    
    //constructeur par defaut, ne pas utiliser celui-ci, on est en MVC
    public TSPModel_PtiDeb(){
        BEST=null;
        pause=false;
        nb_seg_actu=0;
        nb_seg_total=-1;
        choix=new ArrayList();
        pointlist=new PointList();
        segmentlist=new SegmentList();
        distance_total=0;
        trace=false;
        acloner=false;
    }
    
    // contructeur qui permet le MVC, enregistre l'observateur (votre vue)
    public TSPModel_PtiDeb(Observer v){
        BEST=null;
        pause=false;
        nb_seg_actu=0; // pour le moment, aucun segment dans le chemin
        nb_seg_total=-1; // (0 point = -1 segment, 1 point = 0 segment), 2 points = 1 segment,...
        choix=new ArrayList();
        pointlist=new PointList();
        segmentlist=new SegmentList();
        distance_total=0;
        trace=true;
        acloner=false;
        
        ((Observable)this).addObserver(v);
    }
    
    /*private void endInitialisation(){
        segmentlist.affectID();
    }*/
    
    
    
    private void addSegment(Segment s){
        segmentlist.add(s);
    }
    
    private void addPoint(Point pAjoute){
        for(Point p : pointlist.liste){
            Segment seg = new Segment(p,pAjoute);
            addSegment(seg);
            pAjoute.appartientA.add(seg);
            pAjoute.possible++;
            p.appartientA.add(seg);
            p.possible++;
        };       
        pointlist.add(pAjoute);
        nb_seg_total++;
        
    }
    private void removePoint(Point p){
        
        ArrayList<Segment> adelete= new ArrayList();
        for(Segment seg : segmentlist.liste){
            ArrayList<Segment> adelete2= new ArrayList();
            for(Segment seg2 : seg.elimine)
                if(seg2.A==p || seg2.B == p)
                    adelete2.add(seg2);
            
            for(Segment seg2 : adelete2)
                seg.elimine.remove(seg2);  
            
            if(seg.A==p || seg.B == p)
                adelete.add(seg);
             
        }   
        for(Segment seg : adelete){
            segmentlist.liste.remove(seg); 
        }
        nb_seg_total--;
    }
    
    @Override
    protected TSPModel_PtiDeb clone(){
        
        TSPModel_PtiDeb o = new TSPModel_PtiDeb();
        o.segmentlist=segmentlist.clone();
        o.pointlist=pointlist.clone();
        o.distance_total=distance_total;
        o.nb_seg_actu=nb_seg_actu;
        o.nb_seg_total=nb_seg_total;
        o.BEST=BEST;
        o.acloner=false;
        o.choix=new ArrayList(); 
        for(Segment s : choix){
            o.choix.add(s);
        };
	return o;
    }
    // fonction pour desactiver plein d'arete apres ajout d'une arete
    private void check(Segment seg,RollBackList rollback){ 
        ////////////////
        pointlist.check(seg.A,seg.B,rollback);
        segmentlist.check(seg,rollback);
    }
    
    
    //lancement de l'algo, fonction recursive
    // le graph renvoye par cette fonction ne vous est pas du tout utile,
    // cette sortie retournee servait a l'ancien fonctionnement, non MVC
    private TSPModel_PtiDeb recursiveAlgo(int firstID,double record) {
        
            synchronized(this){
                while(pause){
                    try{wait();}catch(InterruptedException e){}
                }
            }
        
        RollBackList rollback = new RollBackList();
        if(nb_seg_actu==nb_seg_total){            
            if(trace)
                newBest();
            acloner=true;
            return this;
        }
        else
        {
            double distanceRestante=record-distance_total;
            int nbSegmentRestant=nb_seg_total-nb_seg_actu;
            ArrayList<Segment> a_tester = segmentlist.querestetil(distanceRestante,nbSegmentRestant,firstID);
            for (Segment seg : a_tester) {
                if(distanceRestante<seg.distance*nbSegmentRestant){
                   break;
                }
                
                    choix.add(seg);
                    check(seg,rollback);
                    
                    if(trace){
                        addSegmentToObservers(seg.ID,seg.A.ID,seg.B.ID);
                        
                    }
                    distance_total+=seg.distance;
                    nb_seg_actu++;
                    TSPModel_PtiDeb test =recursiveAlgo((seg.ID+1),record);
                    if(test!=null && test.acloner){
                        if(test.distance_total<record){
                            BEST=test.clone();
                            record=BEST.distance_total;
                            distanceRestante=BEST.distance_total-distance_total;
                        }
                    }
                    else if (test!=null)
                        if(test.distance_total<record){
                            BEST=test;
                            record=BEST.distance_total;
                            distanceRestante=BEST.distance_total-distance_total;
                        }
                    
                    distance_total-=seg.distance;
                    nb_seg_actu--;
                    choix.remove(seg);
                    removeSegmentToObservers(seg.ID,seg.A.ID,seg.B.ID);
                    
                    rollback.back();
            }             
        }
        return BEST;
    }
    


    /////////////////////
    // seuls les fonctions a partir d ici peuvent vous etre utiles
    // vous ne devez par contre rien modifier
    /////////////////
    ////////////////////
    // ici vous pouvez voir comment les donnees utiles a la vue sont modifiees
    // pour y acceder correctement dans le update
    private void newBest(){
        action=ActionType.NewBest;
        segmentID = -1;
        segmentP1 = -1;
        segmentP2 = -1;
        setChanged();
        notifyObservers();
    }
    private void addSegmentToObservers(int ID, int ID1, int ID2){
        action= ActionType.Add;
        segmentID = ID;
        segmentP1 = ID1;
        segmentP2 = ID2;
        setChanged();
        notifyObservers();
    }
    private void removeSegmentToObservers(int ID, int ID1, int ID2){
        action= ActionType.Remove;
        segmentID = ID;
        segmentP1 = ID1;
        segmentP2 = ID2;
        setChanged();
        notifyObservers();
    }
    //
    ////////////////////
    
    ////////////////////
    // et ici sont les fonctions qui vont etre appelees par votre "controleur"
    // (oui enfin... par la vue quand on clique sur les boutons quoi)
    
    // Cette classe implements Runnable, lorsqu'elle est threadee
    // la fonction run() se lance dès qu'on lance l'execution du thread (start()).
    @Override
    public synchronized void run(){
        segmentlist.affectID();
        recursiveAlgo(0,Double.MAX_VALUE);
        action=ActionType.Finish;
        setChanged();
        notifyObservers();
    }
    
    // le "pause" met en pause le déroulement de l'algorithme
    // quand le booleen passe sur "true", ça enclenche la pause (par un wait() )
    // la sortie de pause se fera par :
    //
    //             *   un passage du booleen de pause sur false 
    //             *   ensuite un notify() sur le Thread du model
    //
    //                       DANS CET ORDRE
    //
    // Si vous avez du mal avec ca, voyez l'utilisation wait/notify de java
    // (c'est un wait qui est utilise pour la mise en pause dans le modele
    // donc c'est un notify() qui dois etre utilise sur le thread pour 
    // reveiller le modele depuis la vue. )
    // 
    public void setPause(boolean b){pause=b;}
    public boolean getPause(){return pause;}
    
    // creation de point
    public void addPoint(int ID, int x, int y){ 
        Point p = new Point(ID,x,y);
        addPoint(p);
    }
    
    // suppression de point par identifiant
    public void removePoint(int ID){ 
        for(Point p : pointlist.liste)
            if(p.ID==ID)
                removePoint(p);
    }
    
    //fonction qui clear tout pour repartir d'un graphe vide sans aucun point
    public void clear(){ 
        BEST=null;
        pause=false;
        nb_seg_actu=0;
        nb_seg_total=-1;
        choix=new ArrayList();
        pointlist=new PointList();
        segmentlist=new SegmentList();
        distance_total=0;
        trace=true;
        acloner=false;
    }
    //
    /////////////
    
    public void premierPoint(int ID){
        pointlist.setPremier(ID);        
    }
    
    public void deletePremierPoint(){
        pointlist.setPremier(-1);
    }
    ////////////
    // pour finir, les getter/setter a utiliser depuis la vue
    public ActionType getAction(){return action;}
    public int getSegmentID(){return segmentID;}
    public int getSegmentP1(){return segmentP1;}
    public int getSegmentP2(){return segmentP2;}
    //
    /////////////
    
    
    
    

        
}

