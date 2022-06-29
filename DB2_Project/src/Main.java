import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    static ArrayList<LogOpertion> log = new ArrayList<>();

    public static void main(String args[]) throws IOException {
        String filePath = "input.txt";
        //Instantiating the File class
        File file = new File(filePath);
        //Instantiating the StringBuffer
        StringBuffer buffer = new StringBuffer();
        //instantiating the RandomAccessFile
        RandomAccessFile raFile = new RandomAccessFile(file, "rw");
        //Reading each line using the readLine() method
        while (raFile.getFilePointer() < raFile.length()) {
            buffer.append(raFile.readLine() + System.lineSeparator());
        }
        String contents = buffer.toString();
        //System.out.println("Contents of the file: \n"+contents);
        seprateFile(contents);

        //IsCascadless();
        PinOrUnpin_Force();
        //DefferUpdate();
        //ImmediateUpdate();
        //Undo();
        Redo();
    }

    public static void seprateFile(String fileContent) {
        int count = 1;

        String[] lines = fileContent.split("\\r?\\n");
        for (String line : lines) {
            LogOpertion opertion = new LogOpertion();
            String[] contentOfLine = line.split(",");
            for (int i = 0; i < contentOfLine.length; i++) {
                if (contentOfLine[i].equals("W")) {
                    opertion.action = "W";
                    opertion.transactionName = contentOfLine[++i];
                    opertion.item = contentOfLine[++i];
                    opertion.BFIM = Integer.parseInt(contentOfLine[++i]);
                    opertion.AFIM = Integer.parseInt(contentOfLine[++i]);

                } else if ((contentOfLine[i].equals("S")) || (contentOfLine[i].equals("Commit"))) {
                    opertion.action = contentOfLine[i];
                    opertion.transactionName = contentOfLine[++i];
                } else {
                    opertion.action = contentOfLine[i];
                }
                log.add(opertion);
            }
        }
    }

    public static void IsCascadless() {
        boolean cascadless = true;
        for (int i = 0; i < log.size(); i++) {
            if (log.get(i).action.equals("W")) {
                for (int j = i + 1; j < log.size(); j++) {
                    if (!(log.get(i).transactionName.equals(log.get(j).transactionName)) && (log.get(i).item.equals(log.get(j).item))) {
                        for (int k = i + 1; k < j; k++) {
                            if (!(log.get(i).action.equals("Commit"))) {
                                cascadless = false;
                                break;
                            }
                        }
                    }
                }
            }
            if (cascadless == false) {
                System.out.println(" not Casscadless");
                break;
            }
        }
    }

    public static void PinOrUnpin_NoForce() {
        HashMap<String, Integer> lines = new HashMap();
        lines.put("A", 1);
        lines.put("B", 1);
        lines.put("C", 1);
        lines.put("D", 1);
        lines.put("E", 1);
        for (int i = 0; i < log.size(); i++) {
            if (log.get(i).action.equals("Commit")) {
                String transaction = log.get(i).transactionName;
                for (int j = 0; j < i; j++) {
                    if (log.get(j).action.equals("W") && log.get(j).transactionName.equals(transaction)) {
                        lines.put(log.get(j).item, 0);
                    }
                }
            } else if (log.get(i).action.length() > 1 && log.get(i).action.substring(0, 10).equals("Checkpoint")) {
                System.out.println(log.get(i).action.substring(0, 10));
                lines.put("A", 1);
                lines.put("B", 1);
                lines.put("C", 1);
                lines.put("D", 1);
                lines.put("E", 1);
            }
            System.out.println(i + " " + lines);
        }
    }

    //not done yet
    public static void PinOrUnpin_Force() {
        HashMap<String, Integer> lines = new HashMap();
        lines.put("A", 1);
        lines.put("B", 1);
        lines.put("C", 1);
        lines.put("D", 1);
        lines.put("E", 1);
        for (int i = 0; i < log.size(); i++) {
            if (log.get(i).action.equals("W")) {
                lines.put(log.get(i).item, 0);
            } else if (log.get(i).action.length() > 1) {
                lines.put("A", 1);
                lines.put("B", 1);
                lines.put("C", 1);
                lines.put("D", 1);
                lines.put("E", 1);
            }
            System.out.println(i + " " + lines);
        }
    }

    public static void DefferUpdate() {
        HashMap<String, Integer> lines = new HashMap();
        lines.put("A", 2);
        lines.put("B", 10);
        lines.put("C", 5);
        lines.put("D", 1);
        lines.put("E", 1);
        for (int i = 0; i < log.size(); i++) {
            if (log.get(i).action.equals("W")) {
                lines.put(log.get(i).item, log.get(i).BFIM);
            }
            if (log.get(i).action.equals("Commit")) {
                String transaction = log.get(i).transactionName;
                for (int j = 0; j < i; j++) {
                    if (log.get(j).action.equals("W") && log.get(j).transactionName.equals(transaction)) {
                        lines.put(log.get(j).item, log.get(j).AFIM);
                    }
                }
            } else if (log.get(i).action.length() > 1 && log.get(i).action.substring(0, 10).equals("Checkpoint")) {
                for (int j = 0; j < i; j++) {
                    if (log.get(j).action.equals("W")) {
                        lines.put(log.get(j).item, log.get(j).AFIM);
                    }
                }
            }
            System.out.println(i + " " + lines);
        }

    }

    public static void ImmediateUpdate() {
        HashMap<String, Integer> lines = new HashMap();
        lines.put("A", 2);
        lines.put("B", 10);
        lines.put("C", 5);
        lines.put("D", 1);
        lines.put("E", 1);
        for (int i = 0; i < log.size(); i++) {
            if (log.get(i).action.equals("W")) {
                lines.put(log.get(i).item, log.get(i).AFIM);
            }

            System.out.println(i + " " + lines);
        }

    }

    public static void Undo() {
        HashMap<String, Integer> lines = new HashMap();
        lines.put("T1", 0);
        lines.put("T2", 0);
        lines.put("T3", 0);
        lines.put("T4", 0);
        for (int i = 0; i < log.size(); i++) {
            if (log.get(i).action.equals("Commit")) {
                lines.put(log.get(i).transactionName, 1);
            }
        }
        for (Map.Entry me : lines.entrySet()) {
            if (me.getValue().equals(0)) {
                System.out.println("Key: " + me.getKey() + " & Value: " + me.getValue());
            }
        }
    }

    public static void Redo() {
        ArrayList<String> trans= new ArrayList();
        for (int i =log.size()-1; i > 0; i--) {
            if (log.get(i).action.equals("Commit")) {
                trans.add(log.get(i).transactionName);
            }

            if (log.get(i).action.length() > 7 && log.get(i).action.substring(0, 10).equals("Checkpoint")) {
                break;
            }
        }
        System.out.println(trans);
    }

    public static void CascadingRollBack(){

    }
}
