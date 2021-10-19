package engine.ihm.net;

public class ConsoleHistorique {

    private String[] commandHistorique;
    private int commandHistoriqueCurrentLvl;

    public ConsoleHistorique() {
        commandHistorique = new String[10];
        commandHistoriqueCurrentLvl = -1;
    }

    public String getUpCommand() {
        if (commandHistoriqueCurrentLvl < 9) {
            commandHistoriqueCurrentLvl++;
            return commandHistorique[commandHistoriqueCurrentLvl];
        } else {
            return null;
        }
    }

    public String getDownCommand() {
        if (commandHistoriqueCurrentLvl > 0) {
            commandHistoriqueCurrentLvl--;
            String command = commandHistorique[commandHistoriqueCurrentLvl];
            return command;
        } else if (commandHistoriqueCurrentLvl == 0) {
            commandHistoriqueCurrentLvl--;
            return "";
        } else {
            return null;
        }
    }

    public void executeCommand(String command) {
        if (command != null && !command.equals("")) {
            //Remove \n
            command = command.replaceAll("\n", "");

            //Add the texte in the historique and decals every commands
            String[] newCommandhistorique = new String[10];
            for (int i = 0; i < commandHistorique.length; i++) {
                if (i == 0) {
                    newCommandhistorique[i] = command;
                } else {
                    newCommandhistorique[i] = commandHistorique[i - 1];
                }
            }
            commandHistorique = newCommandhistorique;
            commandHistoriqueCurrentLvl = -1;
        }
    }

}
