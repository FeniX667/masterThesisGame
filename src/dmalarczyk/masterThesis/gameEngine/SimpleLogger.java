package dmalarczyk.masterThesis.gameEngine;

public class SimpleLogger {
    public StringBuilder log;
    public String eol;
    public boolean printToConsole;

    SimpleLogger(){
        log = new StringBuilder();
        eol = System.getProperty("line.separator");
        printToConsole = false;
    }

    public void append(String string){
        log.append(string);
        if( printToConsole )
            System.out.print(string);
    }

    public void appendln(String string){
        log.append(string + eol);
        if( printToConsole )
            System.out.println(string);
    }
}
