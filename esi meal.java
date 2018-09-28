package ESIMeal;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class EsiMeal implements Serializable{
    static private int nombrePlaceExt=0;
    static private int nombrePlaceInt=0;
    private LinkedList<Met> metsListe;
    private PriorityQueue<Commande> commandesListe;
    private ArrayList<Commande> commandesEffecListe;
    private TreeSet<Integer> agenda;
    private HashSet<Client> clientsListe;
    private HashSet<ClientFidele> clientsFidelesListe;
    public static int getNombrePlaceExt() {
        return nombrePlaceExt;
    }

    public static int getNombrePlaceInt() {
        return nombrePlaceInt;
    }

    public static int getChaisesParTable() {
        return chaisesParTable;
    }
    //------------------------------ Methodes-------------------------//
    //-----Mets Methodes-----//
    public EsiMeal(){
    }
    public EsiMeal(LinkedList<Met> metsListe) {
        this.metsListe = metsListe;
    }

    public boolean ajouterMet(Met met) {
        return metsListe.add(met);
    }

    public boolean supprimerMet(Object o) {
        return metsListe.remove(o);
    }

    public Met supprimerMet(int index) {
        return metsListe.remove(index);
    }
    public void modifierMet(int index){
        Met met = metsListe.get(index);
        //modification de met
        metsListe.set(index,met);
    }
    public void afficherMetListe(){
        Iterator <Met> it = metsListe.iterator();
        System.out.println("Met Liste");
        while(it.hasNext()){
            it.next().afficherMet();
        }
    }
    //---- Clients Liste ----//
    public boolean ajouterClient(Client client ){
        return clientsListe.add(client);
    }


    //---- Clients Fideles Liste ----//
    public boolean ajouterClientFidele(ClientFidele client ){
        return clientsFidelesListe.add(client);
    }
    public ClientFidele recupererClient(String codeFedelite){
        Iterator<ClientFidele> it = clientsFidelesListe.iterator();
        while(it.hasNext()){
            if(it.next().getCodeFidelite().equals(codeFedelite)) return it.next();
        }
        return null;
    }
    //---- commande File ---//
    public boolean ajouterCommandeFile(Commande commande){
        return commandesListe.add(commande);
    }
    public int numberCommandeinFile(){ return commandesListe.size();}
    public Commande supprimerCommandeFile(){ return commandesListe.remove();}
    public void afficherCommandFile(){
        Iterator<Commande> it = commandesListe.iterator();
        while(it.hasNext()){
            //it.next().afficherCommande;
        }
    }
    //---- Commande Liste ---//
    public boolean ajouterCommandeListe(Commande commande){return commandesEffecListe.add(commande);}
    public void afficherCommandeListe() {
        Iterator<Commande> it = commandesEffecListe.iterator();
        while(it.hasNext()){
            //it.next().afficherCommande;
        }
    }
    //---Agenda de la disponibilite---//
    public void reserverRestau(LocalDateTime date){
        if(date.getDayOfYear()-LocalDateTime.now().getDayOfYear() > 15) agenda.add(date.getDayOfYear());
        //else throw Exception;
    }

    //---Gestion des tables---//
    final int nbrTableInterieure=20;
    final int nbrTableExterieure=20;
    static private final int chaisesParTable=4;
    private Map<nombreTable,Float> tableInterieures= new HashMap <nombreTable,Float> ();
    private Map<nombreTable,Float>tableExterieures= new HashMap <nombreTable,Float> ();

    public EsiMeal(Map<nombreTable, Float > tableInterieures, Map<nombreTable, Float> tableExterieures) {
        this.tableInterieures = tableInterieures;
        this.tableExterieures = tableExterieures;
    }

   public void nbrPlaceIn()
    {
        for (int i=1; i<21;i++)
         {
            String key= "table"+i;
            if ((tableInterieures.get(nombreTable.valueOf(key))>0)) tableInterieures.replace(nombreTable.valueOf(key), (float) (tableInterieures.get(nombreTable.valueOf(key))-0.5));
            if((tableInterieures.get(nombreTable.valueOf(key))== 0)) nombrePlaceInt++;
         }
    }

    public void nbrPlaceEx(Commande cmd)
    {
        for (int i=1; i<21;i++)
        {
            String key= "table"+i;
            if ((tableExterieures.get(nombreTable.valueOf(key))>0)) tableExterieures.replace(nombreTable.valueOf(key), (float) (tableExterieures.get(nombreTable.valueOf(key))-0.5));
            if((tableExterieures.get(nombreTable.valueOf(key))== 0)) nombrePlaceExt++;
        }
       // return ((nombrePlaceExt*chaisesParTable>=cmd.getNbrPersonnes()));
    }

    public int nbrCmdEffectuees(LocalDateTime d1, LocalDateTime d2)
    {
        int cpt=0;
        Iterator <Commande> it =  commandesEffecListe.iterator();
        while (it.hasNext())
        {
            Commande cmd= it.next();
            if(cmd.isBetween(d1, d2)) cpt++;
        }
        return cpt;
    }
    public double montantCmdEffectuees(LocalDateTime d1, LocalDateTime d2)
    {
        double somme=0;
        Iterator <Commande> it =  commandesEffecListe.iterator();
        while (it.hasNext())
        {
            Commande cmd= it.next();
            if(cmd.isBetween(d1, d2)) somme= it.next().prixCMD();
        }
        return somme;
    }

    public int nbrCmdSurPlaceEffectuees(LocalDateTime d1, LocalDateTime d2)
    {
        int cpt=0;
        Iterator <Commande> it =  commandesEffecListe.iterator();
        while (it.hasNext())
        {
            Commande cmd= it.next();
            if(cmd.isBetween(d1, d2) && (cmd instanceof SurPlace)) cpt++;
        }
        return cpt;
    }
    public double montantCmdSurplaceEffectuees(LocalDateTime d1, LocalDateTime d2)
    {
        double somme=0;
        Iterator <Commande> it =  commandesEffecListe.iterator();
        while (it.hasNext())
        {
            Commande cmd= it.next();
            if(cmd.isBetween(d1, d2)&& (cmd instanceof SurPlace)) somme= it.next().prixCMD();
        }
        return somme;
    }

    public int nbrCmdLivreeEffectuees(LocalDateTime d1, LocalDateTime d2)
    {
        int cpt=0;
        Iterator <Commande> it =  commandesEffecListe.iterator();
        while (it.hasNext())
        {
            Commande cmd= it.next();
            if(cmd.isBetween(d1, d2) && (cmd instanceof Livraison)) cpt++;
        }
        return cpt;
    }
    public double montantCmdLivreeEffectuees(LocalDateTime d1, LocalDateTime d2)
    {
        double somme=0;
        Iterator <Commande> it =  commandesEffecListe.iterator();
        while (it.hasNext())
        {
            Commande cmd= it.next();
            if(cmd.isBetween(d1, d2)&& (cmd instanceof Livraison)) somme= it.next().prixCMD();
        }
        return somme;
    }

    public int nbrCmdEventEffectuees(LocalDateTime d1, LocalDateTime d2)
    {
        int cpt=0;
        Iterator <Commande> it =  commandesEffecListe.iterator();
        while (it.hasNext())
        {
            Commande cmd= it.next();
            if(cmd.isBetween(d1, d2) && (cmd instanceof Evenement)) cpt++;
        }
        return cpt;
    }
    public double montantCmdEventEffectuees(LocalDateTime d1, LocalDateTime d2)
    {
        double somme=0;
        Iterator <Commande> it =  commandesEffecListe.iterator();
        while (it.hasNext())
        {
            Commande cmd= it.next();
            if(cmd.isBetween(d1, d2)&& (cmd instanceof Evenement)) somme= it.next().prixCMD();
        }
        return somme;
    }
    //------- Partie Ficher ------//
    public void sauvegarderListeMet(){
        ObjectOutputStream out;
        File file = new File("metListe.dat");
        try{
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file,false)));
            out.writeObject(metsListe);
            out.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void recupererListeMet(){
        ObjectInputStream in;
        File file = new File("metListe.dat");
        try{
            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            try{
                metsListe = (LinkedList<Met>)in.readObject();

            }
            catch(ClassNotFoundException e){
                e.printStackTrace();
            }
            in.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void sauvegarderCommandeListe(){
        ObjectOutputStream out;
        File file = new File("cmdListe.dat");
        try{
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file,false)));
            out.writeObject(commandesListe);
            out.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void recupererCommandeListe(){

        ObjectInputStream in;
        File file = new File("cmdListe.dat");
        try{
            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            try{
                commandesListe = (PriorityQueue<Commande>) in.readObject();

            }
            catch(ClassNotFoundException e){
                e.printStackTrace();
            }
            in.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void sauvegarderCommandeEffectueListe(){
        ObjectOutputStream out;
        File file = new File("cmdEffecListe.dat");
        try{
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file,false)));
            out.writeObject(commandesEffecListe);
            out.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void recupererCommandeEffectueListe(){
        ObjectInputStream in;
        File file = new File("cmdEffecListe.dat");
        try{
            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            try{
                commandesEffecListe = (ArrayList<Commande>)in.readObject();

            }
            catch(ClassNotFoundException e){
                e.printStackTrace();
            }
            in.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
//---- sauvegarder Esi Meal
public void sauvegarderEsiMeal(){
    ObjectOutputStream out;
    File file = new File("EsiMeal.dat");
    try{
        out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file,false)));
        out.writeObject(this);
        out.close();
    }
    catch(FileNotFoundException e){
        e.printStackTrace();
    }
    catch(IOException e){
        e.printStackTrace();
    }
}
    public EsiMeal recupererEsiMeal(){
        ObjectInputStream in;
        EsiMeal esiMeal = new EsiMeal();
        File file = new File("EsiMeal.dat");
        try{
            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            try{
                esiMeal = (EsiMeal) in.readObject();
            }
            catch(ClassNotFoundException e){
                e.printStackTrace();
            }
            in.close();
            return esiMeal;
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return esiMeal;
    }

}
