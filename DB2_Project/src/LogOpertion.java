public class LogOpertion {
    public String transactionName;
    public String item;
    public String action;
    public int BFIM;
    public int AFIM;
    public String toString(){//overriding the toString() method
        return transactionName+" "+item+" "+action+" "+BFIM+" "+AFIM;
    }
}
