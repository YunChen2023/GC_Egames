/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gc_egames_549;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;




public class GC_EGames_549_GUI extends javax.swing.JFrame {

    private DefaultTableModel competitionResultsTableModel;
    private DefaultTableModel leaderBoardsSelectedEventTableModel;
    private DefaultTableModel leaderBoardsAllEventsTableModel;
    
    private DB_Read dbRead;
    private DB_Write dbWrite;
    
    private String chosenEvent;
    private String chosenTeam;
    
    private String sql;
    
    private String [] teamsCSVStrArray;
    private String [] gamesCSVStrArray;
    private String [] eventsCSVStrArray;
    
    private Object [][] leaderBoardArray;
    private boolean comboBoxStatus;
    
    
    
    
    public GC_EGames_549_GUI() {
        String [] columnNames_CompResults = new String [] {"Game", "Team 1", "Pt", "Team 2", "Pt"};
        competitionResultsTableModel = new DefaultTableModel ();
        competitionResultsTableModel.setColumnIdentifiers(columnNames_CompResults);
        
        String [] columnNames_ChosenEventLeaderBoard = new String [] {"Team", "Total points - chosen event"};
        leaderBoardsSelectedEventTableModel = new DefaultTableModel ();
        leaderBoardsSelectedEventTableModel.setColumnIdentifiers(columnNames_ChosenEventLeaderBoard);
        
        String [] columnNames_AllEventsLeaderBoard = new String [] {"Team", "Total points - all events"};
        leaderBoardsAllEventsTableModel = new DefaultTableModel ();
        leaderBoardsAllEventsTableModel.setColumnIdentifiers(columnNames_AllEventsLeaderBoard);
        
        dbRead = null;
        //dbWrite = null;
        
        chosenEvent = "All events";
        chosenTeam = "All teams";
        sql = "";
        
        teamsCSVStrArray = null;
        gamesCSVStrArray = null;
        eventsCSVStrArray = null;
        leaderBoardArray = null;
        
        comboBoxStatus = false;
        
        
        initComponents();
        
        resizeTableColumnsForCompResults();
        resizeTableColumnsForSelectedEventLeaderBoard();
        resizeTableColumnsForAllEventsLeaderBoard();
        
        displayCompResults();
        
        displayEventListing();
        
        displayTeamListing();
        
        displayGameListing();
        
        displayAllEventsLeaderBoard();

        
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todaysDate = dateObj.format(formatter);
        
        date_jTextField.setText(todaysDate);
        location_jTextField.setText("TAFE Coomera");
        
        comboBoxStatus = true;
    }
    
    private void resizeTableColumnsForCompResults()
    {
        float[] columnWidthPercentage = {0.3f, 0.3f, 0.05f, 0.3f, 0.05f};
        int tW = competitionResults_jTable.getWidth();
        javax.swing.table.TableColumn column;
        javax.swing.table.TableColumnModel jTableColumnModel = competitionResults_jTable.getColumnModel();
        int cantCols = jTableColumnModel.getColumnCount();
        for (int i = 0; i < cantCols; i++)
        {
            column = jTableColumnModel.getColumn(i);
            int pWidth = Math.round(columnWidthPercentage[i] * tW);
            column.setPreferredWidth(pWidth);
        }
    }
    
    private void resizeTableColumnsForSelectedEventLeaderBoard()
    {
        float [] columnWidthPercentage = {0.4f, 0.6f};
        int tW = leaderBoardsSelectedEvent_jTable.getWidth();
        javax.swing.table.TableColumn column;
        javax.swing.table.TableColumnModel jTableColumnModel = leaderBoardsSelectedEvent_jTable.getColumnModel();
        int cantCols = jTableColumnModel.getColumnCount();
        for (int i = 0; i < cantCols; i++)
        {
            column = jTableColumnModel.getColumn(i);
            int pWidth = Math.round(columnWidthPercentage[i] * tW);
            column.setPreferredWidth(pWidth);
        }
    }
    
    private void resizeTableColumnsForAllEventsLeaderBoard()
    {
        float [] columnWidthPercentage = {0.4f, 0.6f};
        int tW = leaderBoardsAllEvents_jTable.getWidth();
        javax.swing.table.TableColumn column;
        javax.swing.table.TableColumnModel jTableColumnModel = leaderBoardsAllEvents_jTable.getColumnModel();
        int cantCols = jTableColumnModel.getColumnCount();
        for (int i = 0; i < cantCols; i++)
        {
            column = jTableColumnModel.getColumn(i);
            int pWidth = Math.round(columnWidthPercentage[i] * tW);
            column.setPreferredWidth(pWidth);
        }
    }
    
    private void displayCompResults()
    {
        sql = "SELECT gameName, team1, team1Points, team2, team2Points "
                + "FROM goldcoast_esports.competition ";
        
        if (! chosenEvent.equals("All events"))
        {
            sql += "WHERE eventName = '" + chosenEvent + "'";
            
            if (! chosenTeam.equals("All teams"))
            {
                sql += "AND (team1 = '" + chosenTeam + "' OR team2 = '" + chosenTeam + "')";
            }
        }
        else
        {
            if (! chosenTeam.equals("All teams"))
            {
                sql += "WHERE (team1 = '" + chosenTeam + "' OR team2 = '" + chosenTeam + "')";
            }

        }
        
        System.out.println("SQL used for competition display: " + sql);
        
        dbRead = new DB_Read(sql, "competition");
        
        if (dbRead.getErrorMessage().isEmpty() == false)
        {
            System.out.println("ERROR: " + dbRead.getErrorMessage());
        }
        
        System.out.println("Number of competition results from SQL: " + dbRead.getRecordCount());
        
    
       
       if (dbRead.getRecordCount() > 0)
       {
           if (competitionResultsTableModel.getRowCount() > 0)
           {
               for (int i = competitionResultsTableModel.getRowCount() - 1; i > -1; i--)
               {
                   competitionResultsTableModel.removeRow(i);
               }
           }
           
           if (dbRead.getObjDataSet() != null)
           {
               for (int row = 0; row < dbRead.getObjDataSet().length; row++)
               {
                   competitionResultsTableModel.addRow(dbRead.getObjDataSet()[row]);
               }
               
               competitionResultsTableModel.fireTableDataChanged();
           }
           
       }
       else
       {
           if (competitionResultsTableModel.getRowCount() > 0)
           {
               for (int i = competitionResultsTableModel.getRowCount() - 1; i > -1; i--)
               {
                   competitionResultsTableModel.removeRow(i);
               }
           }
       }
       
       nbrRecordsFound_jTextField.setText(dbRead.getRecordCount() + " records found");
        
    }
    
    private void displayEventListing()
    {
        dbRead = new DB_Read("SELECT name, date, location FROM goldcoast_esports.event ORDER BY date", "event");
        
        if (dbRead.getRecordCount() > 0)
        {
            eventsCSVStrArray = dbRead.getStringCSVData();
            event_jComboBox.removeAllItems();
            event_jComboBox.addItem("All events");
            
            addNewCompResultEvent_ComboBox.removeAllItems();
            
            for (int i = 0; i < eventsCSVStrArray.length; i++)
            {
                String [] splitEventStr = eventsCSVStrArray[i].split(",");
                String eventNameStr = splitEventStr[0] + " (" + splitEventStr[1]
                        + " " + splitEventStr[2] + ")";
                event_jComboBox.addItem(eventNameStr);
                addNewCompResultEvent_ComboBox.addItem(splitEventStr[0]);
            }
        }
        
        //event_jComboBox.setSelectedIndex(0);
    }
    
    private void displayTeamListing()
    {
        dbRead = new DB_Read ("SELECT name, contact, phone, email FROM goldcoast_esports.team ORDER BY name", "team");
        
        if (dbRead.getRecordCount() > 0)
        {
            teamsCSVStrArray = dbRead.getStringCSVData();
            
            team_jComboBox.removeAllItems();
            addNewCompResultTeam1_ComboBox.removeAllItems();
            addNewCompResultTeam2_ComboBox.removeAllItems();
            updateTeamName_jComboBox.removeAllItems();
            
            team_jComboBox.addItem("All teams");
            
            for (int i = 0; i < teamsCSVStrArray.length; i++)
            {
                String[] splitTeamStr = teamsCSVStrArray[i].split(",");
                team_jComboBox.addItem(splitTeamStr[0]);
                addNewCompResultTeam1_ComboBox.addItem(splitTeamStr[0]);
                addNewCompResultTeam2_ComboBox.addItem(splitTeamStr[0]);
                updateTeamName_jComboBox.addItem(splitTeamStr[0]);
                
                if (i == 0)
                {
                    updateContactName_jTextField.setText(splitTeamStr[1]);
                    updatePhoneNumber_jTextField.setText(splitTeamStr[2]);
                    updateEmailAddress_jTextField.setText(splitTeamStr[3]);
                }
            }
            
        }
    }
    
 private void displayGameListing()
 {
     dbRead = new DB_Read("SELECT name FROM goldcoast_esports.game ORDER BY name", "game");
     
     if (dbRead.getRecordCount() > 0)
     {
         gamesCSVStrArray = dbRead.getStringCSVData();
         
         addNewCompResultGame_ComboBox.removeAllItems();
         for (int i = 0; i < dbRead.getStringCSVData().length; i++)
         {
             addNewCompResultGame_ComboBox.addItem(dbRead.getStringCSVData()[i]);
         }
     }
 }
 
 private void displayAllEventsLeaderBoard()
 {
     sql = "SELECT team.name, SUM(competition.team1Points) AS points "
             + "FROM team INNER JOIN competition ON team.name = competition.team1 "
             + "GROUP BY team.name "
             + "UNION "
             + "SELECT team.name, SUM(competition.team2Points) AS points "
             + "FROM team INNER JOIN competition ON team.name = competition.team2 "
             + "GROUP BY team.name";
     
     dbRead = new DB_Read(sql, "leaderBoard");
     
     ArrayList<LeaderboardResult> resultArrayList = new ArrayList<LeaderboardResult>();
     
     if (dbRead.getRecordCount() > 0)
     {
         for (int i = 0; i < dbRead.getObjDataSet().length; i++)
         {
             String teamName = (String) dbRead.getObjDataSet()[i][0];
             int points = (int) dbRead.getObjDataSet()[i][1];
             
             LeaderboardResult result = new LeaderboardResult (teamName, points);
             
             boolean duplicateStatus = false;
             
             for (int j = 0; j < resultArrayList.size(); j++)
             {
                 if (resultArrayList.get(j).getTeamName().equals(result.getTeamName()))
                 {
                     duplicateStatus = true;
                     resultArrayList.get(j).setPoints(resultArrayList.get(j).getPoints() + result.getPoints());
                     System.out.println(resultArrayList.get(j).getTeamName() + " - " + resultArrayList.get(j).getPoints());
                 }
                 
             }
             
             if (duplicateStatus == false)
             {
                 resultArrayList.add(result);
             }
         }
         
         if (resultArrayList.size() > 0)
         {
             Collections.sort(resultArrayList);
             Collections.reverse(resultArrayList);
             
             leaderBoardArray = new Object[resultArrayList.size()][];
             for (int i = 0; i < leaderBoardArray.length; i++)
             {
                 Object[] rowData = new Object [2];
                 rowData[0] = resultArrayList.get(i).getTeamName();
                 rowData[1] = resultArrayList.get(i).getPoints();
                 leaderBoardArray[i] = rowData;
                 resultArrayList.get(i).toString();
             }
             
             if (leaderBoardsAllEventsTableModel.getRowCount() > 0)
             {
                 for (int j = leaderBoardsAllEventsTableModel.getRowCount() - 1; j > -1; j--)
                 {
                     leaderBoardsAllEventsTableModel.removeRow(j);
                 }
             }
             
             for (int row = 0; row < leaderBoardArray.length; row++)
             {
                 leaderBoardsAllEventsTableModel.addRow(leaderBoardArray[row]);
             }
             
             leaderBoardsAllEventsTableModel.fireTableDataChanged();
         }
     }
     
 }
 
 
private void displaySelectedEventLeaderBoard() {
    
    sql = "SELECT team.name, SUM(competition.team1Points) AS points "
            + "FROM team INNER JOIN competition ON team.name = competition.team1 "
            + "WHERE competition.eventName = '" + chosenEvent + "' "
            + "GROUP BY team.name "
            + "UNION "
            + "SELECT team.name, SUM(competition.team2Points) AS points "
            + "FROM team INNER JOIN competition ON team.name = competition.team2 "
            + "WHERE competition.eventName = '" + chosenEvent + "' "
            + "GROUP BY team.name";
     
    dbRead = new DB_Read(sql, "leaderBoard");
    
    System.out.println("ERROR: " + dbRead.getErrorMessage());
     
    ArrayList<LeaderboardResult> resultArrayList = new ArrayList<LeaderboardResult>();
     
    if (dbRead.getRecordCount() > 0) {
        for (int i = 0; i < dbRead.getObjDataSet().length; i++) {
            String teamName = (String) dbRead.getObjDataSet()[i][0];
            int points = (int) dbRead.getObjDataSet()[i][1];
             
            LeaderboardResult result = new LeaderboardResult(teamName, points);
             
            boolean duplicateStatus = false;
             
            for (int j = 0; j < resultArrayList.size(); j++) {
                if (resultArrayList.get(j).getTeamName().equals(result.getTeamName())) {
                    duplicateStatus = true;
                    resultArrayList.get(j).setPoints(resultArrayList.get(j).getPoints() + result.getPoints());
                    System.out.println(resultArrayList.get(j).getTeamName() + " - " + resultArrayList.get(j).getPoints());
                }  
            }
             
            if (!duplicateStatus) {
                resultArrayList.add(result);
            }
        }
         
        if (resultArrayList.size() > 0) {
            Collections.sort(resultArrayList);
            Collections.reverse(resultArrayList);
             
            Object[][] leaderBoardArray = new Object[resultArrayList.size()][];
            for (int i = 0; i < leaderBoardArray.length; i++) {
                Object[] rowData = new Object[2];
                rowData[0] = resultArrayList.get(i).getTeamName();
                rowData[1] = resultArrayList.get(i).getPoints();
                leaderBoardArray[i] = rowData;
                resultArrayList.get(i).toString();
            }
             
            if (leaderBoardsSelectedEventTableModel.getRowCount() > 0) {
                for (int j = leaderBoardsSelectedEventTableModel.getRowCount() - 1; j > -1; j--) {
                    leaderBoardsSelectedEventTableModel.removeRow(j);
                }
            }
             
            for (int row = 0; row < leaderBoardArray.length; row++) {
                leaderBoardsSelectedEventTableModel.addRow(leaderBoardArray[row]);
            }
             
            leaderBoardsSelectedEventTableModel.fireTableDataChanged();
        }
    }
}

private void exportCompResults() { 
    
    if (competitionResultsTableModel.getRowCount() > 0) {
        try {
            
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String timeStamp = currentDateTime.format(formatter);
            String fileName = "competitionTeamScores_" + timeStamp + ".csv";

            FileOutputStream outputStream = new FileOutputStream(fileName, false);

            // create outputStream and designate the external file
            //FileOutputStream outputStream = new FileOutputStream("competitionTeamScores.csv", false);

            // create outputStreamWriter and designate the character set used by the outputStream object
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");

            // create bufferedWriter which uses the outputStreamWriter to write to the external file
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            // write header row to CSV file
            bufferedWriter.write("Game Name, Team 1, Team 1 Points, Team 2, Team 2 Points");
            bufferedWriter.newLine();

            // loop through competition results table and write each row to the CSV file
            for (int i = 0; i < competitionResultsTableModel.getRowCount(); i++) {
                String gameName = competitionResultsTableModel.getValueAt(i, 0).toString();
                String team1 = competitionResultsTableModel.getValueAt(i, 1).toString();
                String team1Points = competitionResultsTableModel.getValueAt(i, 2).toString();
                String team2 = competitionResultsTableModel.getValueAt(i, 3).toString();
                String team2Points = competitionResultsTableModel.getValueAt(i, 4).toString();

                bufferedWriter.write(gameName + "," + team1 + "," + team1Points + "," + team2 + "," + team2Points);
                bufferedWriter.newLine();
            }

            // close the bufferedWriter object
            bufferedWriter.close();

            // display pop-up message indicating successful write to file
            JOptionPane.showMessageDialog(null, "CSV Data successfully written to file: competitionTeamScores[TIME].csv", "CSV Data Exported!", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    } else {
        // display pop-up message indicating no data to export
        JOptionPane.showMessageDialog(null, "No data to export.", "Export Failed", JOptionPane.WARNING_MESSAGE);
    }
}

private void exportSelectedEventLeaderBoard() {

    if (leaderBoardsSelectedEventTableModel.getRowCount() > 0) {
        try {

            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String timeStamp = currentDateTime.format(formatter);
            String fileName = "selectedEventLeaderBoard_" + timeStamp + ".csv";

            FileOutputStream outputStream = new FileOutputStream(fileName, false);

            // create outputStreamWriter and designate the character set used by the outputStream object
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");

            // create bufferedWriter which uses the outputStreamWriter to write to the external file
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            // write header row to CSV file
            bufferedWriter.write("Team Name, Points");
            bufferedWriter.newLine();

            // loop through leaderboard table and write each row to the CSV file
            for (int i = 0; i < leaderBoardsSelectedEventTableModel.getRowCount(); i++) {
                String teamName = leaderBoardsSelectedEventTableModel.getValueAt(i, 0).toString();
                String points = leaderBoardsSelectedEventTableModel.getValueAt(i, 1).toString();

                bufferedWriter.write(teamName + "," + points);
                bufferedWriter.newLine();
            }

            // close the bufferedWriter object
            bufferedWriter.close();

            // display pop-up message indicating successful write to file
            JOptionPane.showMessageDialog(null, "CSV Data successfully written to file: selectedEventLeaderBoard[TIME].csv", "CSV Data Exported!", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    } else {
        // display pop-up message indicating no data to export
        JOptionPane.showMessageDialog(null, "No data to export.", "Export Failed", JOptionPane.WARNING_MESSAGE);
    }
}

private void exportAllEventsLeaderBoard() {

    if (leaderBoardsAllEventsTableModel.getRowCount() > 0) {
        try {

            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String timeStamp = currentDateTime.format(formatter);
            String fileName = "allEventLeaderBoard_" + timeStamp + ".csv";

            FileOutputStream outputStream = new FileOutputStream(fileName, false);

            // create outputStreamWriter and designate the character set used by the outputStream object
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");

            // create bufferedWriter which uses the outputStreamWriter to write to the external file
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            // write header row to CSV file
            bufferedWriter.write("Team Name, Points");
            bufferedWriter.newLine();

            // loop through leaderboard table and write each row to the CSV file
            for (int i = 0; i < leaderBoardsAllEventsTableModel.getRowCount(); i++) {
                String teamName = leaderBoardsAllEventsTableModel.getValueAt(i, 0).toString();
                String points = leaderBoardsAllEventsTableModel.getValueAt(i, 1).toString();

                bufferedWriter.write(teamName + "," + points);
                bufferedWriter.newLine();
            }

            // close the bufferedWriter object
            bufferedWriter.close();

            // display pop-up message indicating successful write to file
            JOptionPane.showMessageDialog(null, "CSV Data successfully written to file: allEventLeaderBoard[TIME].csv", "CSV Data Exported!", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    } else {
        // display pop-up message indicating no data to export
        JOptionPane.showMessageDialog(null, "No data to export.", "Export Failed", JOptionPane.WARNING_MESSAGE);
    }
}

private boolean validateCompResults ()
{
    // boolean validation
        boolean validation = true;
        // String errorMsg
        String errorMsg = "ERROR(S) DETECTED!\n";
        
        String team1Chosen = addNewCompResultTeam1_ComboBox.getSelectedItem().toString();
        String team2Chosen = addNewCompResultTeam2_ComboBox.getSelectedItem().toString();
        String eventChosen = addNewCompResultEvent_ComboBox.getSelectedItem().toString();
        String gameChosen = addNewCompResultGame_ComboBox.getSelectedItem().toString();
        
        String team1Points = addNewCompResultT1Pt_jTextField.getText();
        String team2Points = addNewCompResultT2Pt_jTextField.getText();
        
        if (addNewCompResultT1Pt_jTextField.getText().isEmpty())
        {
            errorMsg += "New competion points required\n";
            validation = false;
        }
        
        if (addNewCompResultT2Pt_jTextField.getText().isEmpty())
        {
            errorMsg += "New competion points required\n";
            validation = false;
        }
        
        if (team1Chosen == team2Chosen)
        {
            errorMsg += "Team 1 and Team 2 must be different teams!\n";
            validation = false;
        }
        
        
        if (!team1Points.matches("[0-2]")) 
        {
            errorMsg += "Team 1 points must be 0, 1, or 2\n";
            validation = false;
        }

        
        if (!team2Points.matches("[0-2]")) 
        {
            errorMsg += "Team 2 points must be 0, 1, or 2\n";
            validation = false;
        }

        if (!addNewCompResultT1Pt_jTextField.getText().isEmpty() && !addNewCompResultT2Pt_jTextField.getText().isEmpty())
        {
            int team1PointsN = Integer.parseInt(team1Points);
            int team2PointsN = Integer.parseInt(team2Points);

            int totalPoints = team1PointsN + team2PointsN;
            
            if (totalPoints != 2) 
            {
            errorMsg += "Team 1 points and Team 2 points must add to 2\n";
            validation = false;
            }
        }
        
        
        String sql = "SELECT * FROM goldcoast_esports.competition " +
                     "WHERE ((team1 = '" + team1Chosen + "' AND team2 = '" + team2Chosen + "') " +
                     "OR (team1 = '" + team2Chosen + "' AND team2 = '" + team1Chosen + "')) " +
                     "AND gameName = '" + gameChosen + "' AND eventName = '" + eventChosen + "'";
        
        DB_Read dbRead = new DB_Read(sql, "competition");
        Object[][] dataSet = dbRead.getObjDataSet();
        if (dataSet != null && dataSet.length > 0 && dataSet[0] != null && dataSet[0].length > 0) 
        {
           String countString = dataSet[0][0].toString();
           if (countString != null) 
              {
               errorMsg += "existing record in database (same teams playing same event and same game)\n";
               validation = false;
              }
        } 
        else
        {
    
        }

        
        if  (validation == false)
        {
            JOptionPane.showMessageDialog(null, errorMsg, "ERRORS DETECTED!", JOptionPane.ERROR_MESSAGE);
        }

        return validation; 

}

private boolean validateUpdateTeam()
{
    boolean validation = true;
        // String errorMsg
        String errorMsg = "ERROR(S) DETECTED:\n";

        if (updateContactName_jTextField.getText().isEmpty())
         {
            errorMsg += "- contact person's name required\n";
            validation = false;
        }

        // chexk contact phone 
        if (updatePhoneNumber_jTextField.getText().isEmpty())
         {
            errorMsg += "- contact phone number required\n";
            validation = false;
        }

        // check the email
        if (updateEmailAddress_jTextField.getText().isEmpty())
         {
            errorMsg += "- contact email address required\n";
            validation = false;
        }
        
        if  (validation == false)
        {
            JOptionPane.showMessageDialog(null, errorMsg, "ERRORS DETECTED!", JOptionPane.ERROR_MESSAGE);
        }

        return validation; 

}


  
        
       
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header_jPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        body_jPanel = new javax.swing.JPanel();
        body_jTabbedPane = new javax.swing.JTabbedPane();
        eventCompetitionResults_jPanel = new javax.swing.JPanel();
        event_jLabel = new javax.swing.JLabel();
        team_jLabel = new javax.swing.JLabel();
        event_jComboBox = new javax.swing.JComboBox<>();
        team_jComboBox = new javax.swing.JComboBox<>();
        competitionResults_jLabel = new javax.swing.JLabel();
        competitionResults_jScrollPane = new javax.swing.JScrollPane();
        competitionResults_jTable = new javax.swing.JTable();
        nbrRecordsFound_jTextField = new javax.swing.JTextField();
        exportCompResults_jButton = new javax.swing.JButton();
        leaderBoards_jLabel = new javax.swing.JLabel();
        leaderBoardsSelectedEvent_jScrollPane1 = new javax.swing.JScrollPane();
        leaderBoardsSelectedEvent_jTable = new javax.swing.JTable();
        leaderBoardsAllEvents_jScrollPane = new javax.swing.JScrollPane();
        leaderBoardsAllEvents_jTable = new javax.swing.JTable();
        exportLeaderBoards_jButton = new javax.swing.JButton();
        addNewCompetition_jPanel = new javax.swing.JPanel();
        addNewCompResult_event_jLabel = new javax.swing.JLabel();
        addNewCompResult_game_jLabel = new javax.swing.JLabel();
        addNewCompResult_team1_jLabel = new javax.swing.JLabel();
        addNewCompResult_team2_jLabel = new javax.swing.JLabel();
        addNewCompResultEvent_ComboBox = new javax.swing.JComboBox<>();
        addNewCompResultGame_ComboBox = new javax.swing.JComboBox<>();
        addNewCompResultTeam1_ComboBox = new javax.swing.JComboBox<>();
        addNewCompResultTeam2_ComboBox = new javax.swing.JComboBox<>();
        addNewCompResults_jButton = new javax.swing.JButton();
        addNewCompResult_team1Point_jLabel = new javax.swing.JLabel();
        addNewCompResult_team2Point_jLabel = new javax.swing.JLabel();
        addNewCompResultT1Pt_jTextField = new javax.swing.JTextField();
        addNewCompResultT2Pt_jTextField = new javax.swing.JTextField();
        addNewTeam_jPanel = new javax.swing.JPanel();
        addNewTeamName_jLabel = new javax.swing.JLabel();
        contactName_jLabel = new javax.swing.JLabel();
        phoneNumber_jLabel = new javax.swing.JLabel();
        emailAddress_jLabel = new javax.swing.JLabel();
        addNewTeam_jButton = new javax.swing.JButton();
        newTeamName_jTextField = new javax.swing.JTextField();
        contactName_jTextField = new javax.swing.JTextField();
        phoneNumber_jTextField = new javax.swing.JTextField();
        emailAddress_jTextField = new javax.swing.JTextField();
        updateTeam_jPanel = new javax.swing.JPanel();
        updateTeamName_jLabel = new javax.swing.JLabel();
        updateContactName_jLabel = new javax.swing.JLabel();
        updateContactName_jTextField = new javax.swing.JTextField();
        updatePhoneNumber_jLabel = new javax.swing.JLabel();
        updatePhoneNumber_jTextField = new javax.swing.JTextField();
        updateEmailAddress_jLabel = new javax.swing.JLabel();
        updateEmailAddress_jTextField = new javax.swing.JTextField();
        updateTeam_jButton = new javax.swing.JButton();
        updateTeamName_jComboBox = new javax.swing.JComboBox<>();
        addNewEvent_jPanel = new javax.swing.JPanel();
        NewEventName_jLabel = new javax.swing.JLabel();
        newEventName_jTextField = new javax.swing.JTextField();
        date_jLabel = new javax.swing.JLabel();
        date_jTextField = new javax.swing.JTextField();
        location_jLabel = new javax.swing.JLabel();
        location_jTextField = new javax.swing.JTextField();
        addNewEvent_jButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("GC_EGames_GUI");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/header_img/goldcoast_esports_v2.jpg"))); // NOI18N

        javax.swing.GroupLayout header_jPanelLayout = new javax.swing.GroupLayout(header_jPanel);
        header_jPanel.setLayout(header_jPanelLayout);
        header_jPanelLayout.setHorizontalGroup(
            header_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        header_jPanelLayout.setVerticalGroup(
            header_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        body_jTabbedPane.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N

        event_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        event_jLabel.setText("Event:");

        team_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        team_jLabel.setText("Team:");

        event_jComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                event_jComboBoxItemStateChanged(evt);
            }
        });

        team_jComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        team_jComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                team_jComboBoxItemStateChanged(evt);
            }
        });

        competitionResults_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        competitionResults_jLabel.setText("Competition Result:");

        competitionResults_jTable.setModel(competitionResultsTableModel);
        competitionResults_jScrollPane.setViewportView(competitionResults_jTable);

        exportCompResults_jButton.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        exportCompResults_jButton.setText("Export Competition Results as CSV File");
        exportCompResults_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportCompResults_jButtonActionPerformed(evt);
            }
        });

        leaderBoards_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        leaderBoards_jLabel.setText("Event Leader Boards");

        leaderBoardsSelectedEvent_jTable.setModel(leaderBoardsSelectedEventTableModel);
        leaderBoardsSelectedEvent_jScrollPane1.setViewportView(leaderBoardsSelectedEvent_jTable);

        leaderBoardsAllEvents_jScrollPane.setSize(new java.awt.Dimension(300, 80));

        leaderBoardsAllEvents_jTable.setModel(leaderBoardsAllEventsTableModel);
        leaderBoardsAllEvents_jScrollPane.setViewportView(leaderBoardsAllEvents_jTable);

        exportLeaderBoards_jButton.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        exportLeaderBoards_jButton.setText("Export Leader Board as CSV File");
        exportLeaderBoards_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportLeaderBoards_jButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout eventCompetitionResults_jPanelLayout = new javax.swing.GroupLayout(eventCompetitionResults_jPanel);
        eventCompetitionResults_jPanel.setLayout(eventCompetitionResults_jPanelLayout);
        eventCompetitionResults_jPanelLayout.setHorizontalGroup(
            eventCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, eventCompetitionResults_jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(eventCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(eventCompetitionResults_jPanelLayout.createSequentialGroup()
                        .addComponent(nbrRecordsFound_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(exportCompResults_jButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(eventCompetitionResults_jPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(eventCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(competitionResults_jScrollPane)
                            .addGroup(eventCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(competitionResults_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(eventCompetitionResults_jPanelLayout.createSequentialGroup()
                                    .addComponent(event_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(event_jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(eventCompetitionResults_jPanelLayout.createSequentialGroup()
                                    .addComponent(team_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(team_jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(eventCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(leaderBoards_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(eventCompetitionResults_jPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(eventCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(leaderBoardsAllEvents_jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(leaderBoardsSelectedEvent_jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(exportLeaderBoards_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(45, 45, 45))
        );
        eventCompetitionResults_jPanelLayout.setVerticalGroup(
            eventCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(eventCompetitionResults_jPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(eventCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(event_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(eventCompetitionResults_jPanelLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(event_jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(eventCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(team_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(team_jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(eventCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(competitionResults_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(leaderBoards_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(eventCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(competitionResults_jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(eventCompetitionResults_jPanelLayout.createSequentialGroup()
                        .addComponent(leaderBoardsSelectedEvent_jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(leaderBoardsAllEvents_jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(eventCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nbrRecordsFound_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exportCompResults_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exportLeaderBoards_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        leaderBoards_jLabel.getAccessibleContext().setAccessibleName("Event Leader Boards:");

        body_jTabbedPane.addTab("Event Cometition Results", eventCompetitionResults_jPanel);

        addNewCompResult_event_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        addNewCompResult_event_jLabel.setText("Event:");

        addNewCompResult_game_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        addNewCompResult_game_jLabel.setText("Game:");

        addNewCompResult_team1_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        addNewCompResult_team1_jLabel.setText("Team1:");

        addNewCompResult_team2_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        addNewCompResult_team2_jLabel.setText("Team2:");

        addNewCompResultEvent_ComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        addNewCompResultGame_ComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        addNewCompResultTeam1_ComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        addNewCompResultTeam2_ComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        addNewCompResults_jButton.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        addNewCompResults_jButton.setLabel("ADD NEW COMPETITION RESULTS");
        addNewCompResults_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewCompResults_jButtonActionPerformed(evt);
            }
        });

        addNewCompResult_team1Point_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        addNewCompResult_team1Point_jLabel.setText("Team1 Points:");

        addNewCompResult_team2Point_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        addNewCompResult_team2Point_jLabel.setText("Team2 Points:");

        addNewCompResultT1Pt_jTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewCompResultT1Pt_jTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addNewCompetition_jPanelLayout = new javax.swing.GroupLayout(addNewCompetition_jPanel);
        addNewCompetition_jPanel.setLayout(addNewCompetition_jPanelLayout);
        addNewCompetition_jPanelLayout.setHorizontalGroup(
            addNewCompetition_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addNewCompetition_jPanelLayout.createSequentialGroup()
                .addGroup(addNewCompetition_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addNewCompetition_jPanelLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(addNewCompetition_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(addNewCompetition_jPanelLayout.createSequentialGroup()
                                .addComponent(addNewCompResult_event_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addNewCompResultEvent_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(addNewCompetition_jPanelLayout.createSequentialGroup()
                                .addComponent(addNewCompResult_game_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addNewCompResultGame_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(addNewCompetition_jPanelLayout.createSequentialGroup()
                                .addGroup(addNewCompetition_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(addNewCompetition_jPanelLayout.createSequentialGroup()
                                        .addComponent(addNewCompResult_team1_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(addNewCompResultTeam1_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(addNewCompetition_jPanelLayout.createSequentialGroup()
                                        .addComponent(addNewCompResult_team2_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(addNewCompResultTeam2_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(30, 30, 30)
                                .addGroup(addNewCompetition_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(addNewCompetition_jPanelLayout.createSequentialGroup()
                                        .addComponent(addNewCompResult_team2Point_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(addNewCompResultT2Pt_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(addNewCompetition_jPanelLayout.createSequentialGroup()
                                        .addComponent(addNewCompResult_team1Point_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(addNewCompResultT1Pt_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(addNewCompetition_jPanelLayout.createSequentialGroup()
                        .addGap(224, 224, 224)
                        .addComponent(addNewCompResults_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(99, Short.MAX_VALUE))
        );
        addNewCompetition_jPanelLayout.setVerticalGroup(
            addNewCompetition_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addNewCompetition_jPanelLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(addNewCompetition_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addNewCompResultEvent_ComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(addNewCompResult_event_jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(addNewCompetition_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewCompResult_game_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewCompResultGame_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addNewCompetition_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewCompResult_team1_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewCompResultTeam1_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewCompResult_team1Point_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewCompResultT1Pt_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addNewCompetition_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewCompResult_team2_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewCompResultTeam2_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewCompResult_team2Point_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewCompResultT2Pt_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56)
                .addComponent(addNewCompResults_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(110, Short.MAX_VALUE))
        );

        body_jTabbedPane.addTab("Add New Competition Result", addNewCompetition_jPanel);

        addNewTeamName_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        addNewTeamName_jLabel.setText("New Team Name:");

        contactName_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        contactName_jLabel.setText("Contact Name:");

        phoneNumber_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        phoneNumber_jLabel.setText("Phone Number:");

        emailAddress_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        emailAddress_jLabel.setText("Email Address:");

        addNewTeam_jButton.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        addNewTeam_jButton.setText("ADD NEW TEAM");
        addNewTeam_jButton.setActionCommand("ADD NEW TEAM");
        addNewTeam_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewTeam_jButtonActionPerformed(evt);
            }
        });

        phoneNumber_jTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneNumber_jTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addNewTeam_jPanelLayout = new javax.swing.GroupLayout(addNewTeam_jPanel);
        addNewTeam_jPanel.setLayout(addNewTeam_jPanelLayout);
        addNewTeam_jPanelLayout.setHorizontalGroup(
            addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addNewTeam_jPanelLayout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addGroup(addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addNewTeam_jPanelLayout.createSequentialGroup()
                        .addGroup(addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(contactName_jLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addNewTeamName_jLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(newTeamName_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(contactName_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(addNewTeam_jPanelLayout.createSequentialGroup()
                        .addGroup(addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(emailAddress_jLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                            .addComponent(phoneNumber_jLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(phoneNumber_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(emailAddress_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(addNewTeam_jPanelLayout.createSequentialGroup()
                                .addGap(77, 77, 77)
                                .addComponent(addNewTeam_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(224, Short.MAX_VALUE))
        );
        addNewTeam_jPanelLayout.setVerticalGroup(
            addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addNewTeam_jPanelLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewTeamName_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newTeamName_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contactName_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactName_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneNumber_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneNumber_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailAddress_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailAddress_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(64, 64, 64)
                .addComponent(addNewTeam_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(100, Short.MAX_VALUE))
        );

        body_jTabbedPane.addTab("Add New Team", addNewTeam_jPanel);

        updateTeamName_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        updateTeamName_jLabel.setText("Team Name:");

        updateContactName_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        updateContactName_jLabel.setText("Contact Name:");

        updatePhoneNumber_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        updatePhoneNumber_jLabel.setText("Phone Number:");

        updatePhoneNumber_jTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatePhoneNumber_jTextFieldActionPerformed(evt);
            }
        });

        updateEmailAddress_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        updateEmailAddress_jLabel.setText("Email Address:");

        updateTeam_jButton.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        updateTeam_jButton.setText("UPDATE EXISTING TEAM");
        updateTeam_jButton.setActionCommand("ADD NEW TEAM");
        updateTeam_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateTeam_jButtonActionPerformed(evt);
            }
        });

        updateTeamName_jComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout updateTeam_jPanelLayout = new javax.swing.GroupLayout(updateTeam_jPanel);
        updateTeam_jPanel.setLayout(updateTeam_jPanelLayout);
        updateTeam_jPanelLayout.setHorizontalGroup(
            updateTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateTeam_jPanelLayout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addGroup(updateTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(updateTeam_jPanelLayout.createSequentialGroup()
                        .addGroup(updateTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(updateContactName_jLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(updateTeamName_jLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(updateTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(updateContactName_jTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                            .addComponent(updateTeamName_jComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(updateTeam_jPanelLayout.createSequentialGroup()
                        .addGroup(updateTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(updateEmailAddress_jLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                            .addComponent(updatePhoneNumber_jLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(updateTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(updatePhoneNumber_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(updateEmailAddress_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(updateTeam_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(224, Short.MAX_VALUE))
        );
        updateTeam_jPanelLayout.setVerticalGroup(
            updateTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateTeam_jPanelLayout.createSequentialGroup()
                .addGroup(updateTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(updateTeam_jPanelLayout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(updateTeamName_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updateTeam_jPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(updateTeamName_jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(updateTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateContactName_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateContactName_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(updateTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updatePhoneNumber_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updatePhoneNumber_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(updateTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateEmailAddress_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateEmailAddress_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65)
                .addComponent(updateTeam_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(97, Short.MAX_VALUE))
        );

        body_jTabbedPane.addTab("Update Existing Team", updateTeam_jPanel);

        NewEventName_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        NewEventName_jLabel.setText("New Event Name:");

        date_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        date_jLabel.setText("Date:");

        location_jLabel.setFont(new java.awt.Font("American Typewriter", 1, 13)); // NOI18N
        location_jLabel.setText("Location:");

        location_jTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                location_jTextFieldActionPerformed(evt);
            }
        });

        addNewEvent_jButton.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        addNewEvent_jButton.setText("ADD NEW EVENT");
        addNewEvent_jButton.setActionCommand("ADD NEW TEAM");
        addNewEvent_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewEvent_jButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addNewEvent_jPanelLayout = new javax.swing.GroupLayout(addNewEvent_jPanel);
        addNewEvent_jPanel.setLayout(addNewEvent_jPanelLayout);
        addNewEvent_jPanelLayout.setHorizontalGroup(
            addNewEvent_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addNewEvent_jPanelLayout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addGroup(addNewEvent_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addNewEvent_jPanelLayout.createSequentialGroup()
                        .addGroup(addNewEvent_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(date_jLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(NewEventName_jLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addNewEvent_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(newEventName_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(date_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(addNewEvent_jPanelLayout.createSequentialGroup()
                        .addComponent(location_jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(location_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(213, 213, 213))
            .addGroup(addNewEvent_jPanelLayout.createSequentialGroup()
                .addGap(281, 281, 281)
                .addComponent(addNewEvent_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        addNewEvent_jPanelLayout.setVerticalGroup(
            addNewEvent_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addNewEvent_jPanelLayout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addGroup(addNewEvent_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NewEventName_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newEventName_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(addNewEvent_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(date_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(date_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addNewEvent_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(location_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(location_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(70, 70, 70)
                .addComponent(addNewEvent_jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(140, Short.MAX_VALUE))
        );

        body_jTabbedPane.addTab("Add New Event", addNewEvent_jPanel);

        javax.swing.GroupLayout body_jPanelLayout = new javax.swing.GroupLayout(body_jPanel);
        body_jPanel.setLayout(body_jPanelLayout);
        body_jPanelLayout.setHorizontalGroup(
            body_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, body_jPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(body_jTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 837, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        body_jPanelLayout.setVerticalGroup(
            body_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(body_jTabbedPane)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(body_jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(header_jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(header_jPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(body_jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exportCompResults_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportCompResults_jButtonActionPerformed
        // TODO add your handling code here:
        exportCompResults();
    }//GEN-LAST:event_exportCompResults_jButtonActionPerformed

    private void exportLeaderBoards_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportLeaderBoards_jButtonActionPerformed
        // TODO add your handling code here:
        if (leaderBoardsAllEventsTableModel.getRowCount() > 0) {
        exportAllEventsLeaderBoard();
        } 
        else {
        exportSelectedEventLeaderBoard();
        }
        
    }//GEN-LAST:event_exportLeaderBoards_jButtonActionPerformed

    private void phoneNumber_jTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneNumber_jTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phoneNumber_jTextFieldActionPerformed

    private void updatePhoneNumber_jTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatePhoneNumber_jTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updatePhoneNumber_jTextFieldActionPerformed

    private void location_jTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_location_jTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_location_jTextFieldActionPerformed

    private void addNewTeam_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewTeam_jButtonActionPerformed
        // TODO add your handling code here:
        String newTeamName = newTeamName_jTextField.getText();
        String newContactPerson = contactName_jTextField.getText();
        String newContactPhone = phoneNumber_jTextField.getText();
        String newContactEmail = emailAddress_jTextField.getText();
        
        boolean errorStatus = false;
        String errorMessage = "ERROR(S) DETECTED:\n";
        
        if (newTeamName.isEmpty())
        {
            errorStatus = true;
            errorMessage += "- team name required\n";
        }
        else
        {
           for (int i = 0; i < teamsCSVStrArray.length; i++)
           {
               String [] splitTeamsStr = teamsCSVStrArray[i].split(",");
               if (newTeamName.equals(splitTeamsStr[0]))
               {
                   errorStatus = true;
                   errorMessage += "- team name already exists (must be unique)\n";
                   break;
               }
           }
        }
        
        if (newContactPerson.isEmpty())
        {
            errorStatus = true;
            errorMessage += "- contact person's name required\n";              
        }
        if (newContactPhone.isEmpty())
        {
            errorStatus = true;
            errorMessage += "- contact phone number required\n";  
        }
        
        if (newContactEmail.isEmpty())
        {
            errorStatus = true;
            errorMessage += "- contact email address required\n";  
        }
        
        if (errorStatus == true)
        {
            javax.swing.JOptionPane.showMessageDialog(null, errorMessage, "ERRORS DETECTED!", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int yesOrNo = javax.swing.JOptionPane.showConfirmDialog(null, "You are about to save a new team for: " + newTeamName + "\nDo you wish to continue?", "ADD NEW TEAM", javax.swing.JOptionPane.YES_NO_OPTION);
        if (yesOrNo == javax.swing.JOptionPane.NO_OPTION)
        {
            System.out.println("ADD NEW TEAM: " + newTeamName + " cancelled");
        }
        else 
        {
            System.out.println("ADD NEW TEAM: " + newTeamName + " proceeding");
            
            sql = "INSERT INTO team (name, contact, phone, email) VALUES ('" + newTeamName + "', '" + newContactPerson + "', '" + newContactPhone + "', '" + newContactEmail + "')";
            
            System.out.println(sql);
            dbWrite = new DB_Write(sql);
            
            if (dbWrite.getErrorMessage().equals(""))
            {
                System.out.println("Successful write operation to database");
                
                ArrayList<String> arrayListTeams = new ArrayList<String>(Arrays.asList(teamsCSVStrArray));
                String newTeamStr = newTeamName + "," + newContactPerson + "," + newContactPhone + "," + newContactEmail;
                arrayListTeams.add(newTeamStr);
                
                teamsCSVStrArray = arrayListTeams.toArray(new String[arrayListTeams.size()]);
                
                team_jComboBox.addItem(newTeamName);
                addNewCompResultTeam1_ComboBox.addItem(newTeamName);
                addNewCompResultTeam2_ComboBox.addItem(newTeamName);
                updateTeamName_jComboBox.addItem(newTeamName);
                
                if (event_jComboBox.getSelectedItem().toString().equals("All events"))
                {
                    displayAllEventsLeaderBoard();
                    
                    if (leaderBoardsSelectedEventTableModel.getRowCount() > 0)
                    {
                        for (int i = leaderBoardsSelectedEventTableModel.getRowCount() - 1; i > -1; i--)
                        {
                            leaderBoardsSelectedEventTableModel.removeRow(i);
                        }
                    }
                }
                else
                
                    {
                        displaySelectedEventLeaderBoard();
                        
                        if (leaderBoardsAllEventsTableModel.getRowCount() > 0)
                        {
                            for (int i = leaderBoardsAllEventsTableModel.getRowCount() - 1; i > -1; i--)
                            {
                                leaderBoardsAllEventsTableModel.removeRow(i);
                            }
                        }
                    }
                
            }
            else
                {
                   System.out.println(dbWrite.getErrorMessage());
                }
        }
            
    }//GEN-LAST:event_addNewTeam_jButtonActionPerformed

    private void event_jComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_event_jComboBoxItemStateChanged
        // TODO add your handling code here:
        
        if (comboBoxStatus)
        {
            
            chosenEvent = event_jComboBox.getSelectedItem().toString();
            chosenTeam = team_jComboBox.getSelectedItem().toString();
            
            if (chosenEvent.equals("All events"))
            {
                
                if (leaderBoardsSelectedEventTableModel.getColumnCount() > 0)
                {
                    for (int i = leaderBoardsSelectedEventTableModel.getColumnCount() - 1; i > -1; i--)
                    {
                    leaderBoardsSelectedEventTableModel.removeRow(i);
                    }            
                }
                
            displayAllEventsLeaderBoard();
            
           }
    
           else
           {
               
            chosenEvent = chosenEvent.substring(0, chosenEvent.indexOf(" ("));
           
            displaySelectedEventLeaderBoard();
            
            if (leaderBoardsAllEventsTableModel.getRowCount() > 0)
            {
                for (int i = leaderBoardsAllEventsTableModel.getRowCount() - 1; i > -1; i--)
                {
                    leaderBoardsAllEventsTableModel.removeRow(i);
                }
            }
            }
        
        displayCompResults();
        }
    }//GEN-LAST:event_event_jComboBoxItemStateChanged

    private void team_jComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_team_jComboBoxItemStateChanged
        // TODO add your handling code here:
        if (comboBoxStatus)
        {
            //chosenEvent = event_jComboBox.getSelectedItem().toString();
            chosenTeam = team_jComboBox.getSelectedItem().toString();
            displayCompResults();
            //displaySelectedEventLeaderBoard();
        }
    }//GEN-LAST:event_team_jComboBoxItemStateChanged

    private void addNewCompResults_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewCompResults_jButtonActionPerformed
        // TODO add your handling code here:
        if (validateCompResults())
        {
            String team1Chosen = addNewCompResultTeam1_ComboBox.getSelectedItem().toString();
            String team2Chosen = addNewCompResultTeam2_ComboBox.getSelectedItem().toString();
            String eventChosen = addNewCompResultEvent_ComboBox.getSelectedItem().toString();
            String gameChosen = addNewCompResultGame_ComboBox.getSelectedItem().toString();
        
            String team1Points = addNewCompResultT1Pt_jTextField.getText();
            String team2Points = addNewCompResultT2Pt_jTextField.getText();
            
            int team1PointsN = Integer.parseInt(team1Points);
            int team2PointsN = Integer.parseInt(team2Points);
                        
            
            int yesOrNo = JOptionPane.showConfirmDialog
                                                        (null, "You are about to save a new competition result for: "
                                                        + team1Chosen + " and " + team2Chosen + "\nDo you wish to continue?", 
                                                        "NEW COMPETITION RESULT", JOptionPane.YES_NO_OPTION);
            if (yesOrNo == JOptionPane.YES_OPTION)
            {      
                
            }
            else
            {
                // no option
            }    
        }
    }//GEN-LAST:event_addNewCompResults_jButtonActionPerformed

    private void addNewCompResultT1Pt_jTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewCompResultT1Pt_jTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addNewCompResultT1Pt_jTextFieldActionPerformed

    private void updateTeam_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateTeam_jButtonActionPerformed
        // TODO add your handling code here:
        if(validateUpdateTeam())
        {
            String updateTeamName = updateTeamName_jComboBox.getSelectedItem().toString();
            String updateContactName = updateContactName_jTextField.getText();
            String updatePhoneNumber = updatePhoneNumber_jTextField.getText(); 
            String updateEmailAddress = updateEmailAddress_jTextField.getText();
            
            int yesOrNo = JOptionPane.showConfirmDialog
                                                        (null, "You are about to update the team details for: " + updateTeamName + "\n Do you wish to continue?", 
                                                        "UPDATE EXISTING TEAM", JOptionPane.YES_NO_OPTION);
            if (yesOrNo == JOptionPane.YES_OPTION) 
            {
                //System.out.println(updateTeamName);
                //System.out.println(updatePhoneNumber);
                String sql = "UPDATE goldcoast_esports.team " + 
                             "SET contact = '" + updateContactName + "', " +
                             "phone = '" + updatePhoneNumber + "', " +
                             "email = '" + updateEmailAddress + "' " +
                             "WHERE name = '" + updateTeamName + "'" ;
                
                dbWrite = new DB_Write(sql);
                
                if (dbWrite.getErrorMessage().isEmpty() == false) 
                {
                   System.out.println("ERROR: " + dbWrite.getErrorMessage());
                }
                
            }
            else
            {
                // no option
            }    

            }
    }//GEN-LAST:event_updateTeam_jButtonActionPerformed

    private void addNewEvent_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewEvent_jButtonActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_addNewEvent_jButtonActionPerformed
        
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GC_EGames_549_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GC_EGames_549_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GC_EGames_549_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GC_EGames_549_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GC_EGames_549_GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel NewEventName_jLabel;
    private javax.swing.JComboBox<String> addNewCompResultEvent_ComboBox;
    private javax.swing.JComboBox<String> addNewCompResultGame_ComboBox;
    private javax.swing.JTextField addNewCompResultT1Pt_jTextField;
    private javax.swing.JTextField addNewCompResultT2Pt_jTextField;
    private javax.swing.JComboBox<String> addNewCompResultTeam1_ComboBox;
    private javax.swing.JComboBox<String> addNewCompResultTeam2_ComboBox;
    private javax.swing.JLabel addNewCompResult_event_jLabel;
    private javax.swing.JLabel addNewCompResult_game_jLabel;
    private javax.swing.JLabel addNewCompResult_team1Point_jLabel;
    private javax.swing.JLabel addNewCompResult_team1_jLabel;
    private javax.swing.JLabel addNewCompResult_team2Point_jLabel;
    private javax.swing.JLabel addNewCompResult_team2_jLabel;
    private javax.swing.JButton addNewCompResults_jButton;
    private javax.swing.JPanel addNewCompetition_jPanel;
    private javax.swing.JButton addNewEvent_jButton;
    private javax.swing.JPanel addNewEvent_jPanel;
    private javax.swing.JLabel addNewTeamName_jLabel;
    private javax.swing.JButton addNewTeam_jButton;
    private javax.swing.JPanel addNewTeam_jPanel;
    private javax.swing.JPanel body_jPanel;
    private javax.swing.JTabbedPane body_jTabbedPane;
    private javax.swing.JLabel competitionResults_jLabel;
    private javax.swing.JScrollPane competitionResults_jScrollPane;
    private javax.swing.JTable competitionResults_jTable;
    private javax.swing.JLabel contactName_jLabel;
    private javax.swing.JTextField contactName_jTextField;
    private javax.swing.JLabel date_jLabel;
    private javax.swing.JTextField date_jTextField;
    private javax.swing.JLabel emailAddress_jLabel;
    private javax.swing.JTextField emailAddress_jTextField;
    private javax.swing.JPanel eventCompetitionResults_jPanel;
    private javax.swing.JComboBox<String> event_jComboBox;
    private javax.swing.JLabel event_jLabel;
    private javax.swing.JButton exportCompResults_jButton;
    private javax.swing.JButton exportLeaderBoards_jButton;
    private javax.swing.JPanel header_jPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane leaderBoardsAllEvents_jScrollPane;
    private javax.swing.JTable leaderBoardsAllEvents_jTable;
    private javax.swing.JScrollPane leaderBoardsSelectedEvent_jScrollPane1;
    private javax.swing.JTable leaderBoardsSelectedEvent_jTable;
    private javax.swing.JLabel leaderBoards_jLabel;
    private javax.swing.JLabel location_jLabel;
    private javax.swing.JTextField location_jTextField;
    private javax.swing.JTextField nbrRecordsFound_jTextField;
    private javax.swing.JTextField newEventName_jTextField;
    private javax.swing.JTextField newTeamName_jTextField;
    private javax.swing.JLabel phoneNumber_jLabel;
    private javax.swing.JTextField phoneNumber_jTextField;
    private javax.swing.JComboBox<String> team_jComboBox;
    private javax.swing.JLabel team_jLabel;
    private javax.swing.JLabel updateContactName_jLabel;
    private javax.swing.JTextField updateContactName_jTextField;
    private javax.swing.JLabel updateEmailAddress_jLabel;
    private javax.swing.JTextField updateEmailAddress_jTextField;
    private javax.swing.JLabel updatePhoneNumber_jLabel;
    private javax.swing.JTextField updatePhoneNumber_jTextField;
    private javax.swing.JComboBox<String> updateTeamName_jComboBox;
    private javax.swing.JLabel updateTeamName_jLabel;
    private javax.swing.JButton updateTeam_jButton;
    private javax.swing.JPanel updateTeam_jPanel;
    // End of variables declaration//GEN-END:variables
}
