package sn.ucad.restou.exception;



public class ResourceNotFoundException extends RuntimeException {
    private String ressource;
    private String champ;
    private Object valeur;

    public ResourceNotFoundException(String ressource, String champ, Object valeur) {
        super(String.format("%s non trouvé avec %s : '%s'", ressource, champ, valeur));
        this.ressource = ressource;
        this.champ = champ;
        this.valeur = valeur;
    }

    public String getRessource() {
        return ressource;
    }

    public String getChamp() {
        return champ;
    }

    public Object getValeur() {
        return valeur;
    }
    
}
