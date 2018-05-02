import org.primefaces.context.RequestContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.sound.midi.SysexMessage;
import javax.xml.stream.events.StartDocument;

@ManagedBean
@RequestScoped

public class ListTrainerBean {

    //private ArrayList<TrainerTemp> trainerNames = new ArrayList<TrainerTemp>();

    public DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    public ArrayList<Trainer> loadTrainers(String courseId) {

        String courseDate = (String)dtf.format(LocalDateTime.now());

        ArrayList<Trainer> trainers = new ArrayList<Trainer>();
        ArrayList<LinkedHashMap<String,Object>> result = null;
        ArrayList<ArrayList<Object>> trainer_times = new ArrayList<ArrayList<Object>>();
        try {
            DatabaseBean database = new DatabaseBean();

            // Fetch unique trainers for given course id and course_date.
            result = database.execute_fetch_all("SELECT DISTINCT T_ID FROM GeneralSchedule WHERE C_ID=? AND C_DATE=? ORDER BY T_ID ASC",-1,courseId,courseDate);
            // Fetch course hours for these trainers.
            for(LinkedHashMap<String,Object> row_map:result){
                String trainerId = row_map.get("T_ID").toString();
                ArrayList<LinkedHashMap<String,Object>> result_times = null;
                result_times = database.execute_fetch_all("SELECT T_NAME,C_TIME FROM Trainer NATURAL JOIN GeneralSchedule WHERE C_ID=? AND T_ID=? AND C_DATE=? ORDER BY C_TIME ASC",-1,courseId,trainerId,courseDate);
                // Trainer's course times for given c_id. It will store in Trainer's object.
                // Store in an array for storing Trainer's object.
                ArrayList<String> times = new ArrayList<String>();
                for (LinkedHashMap<String, Object> result_time : result_times) {
                    times.add(result_time.get("C_TIME").toString());
                }
                ArrayList<Object> names_times = new ArrayList<Object>();
                names_times.add(trainerId); // 0
                names_times.add(result_times.get(0).get("T_NAME"));// 1
                names_times.add(times); // 2
                trainer_times.add(names_times);

            }
            database.destruct_connection();
        } catch (SQLException e) {
            System.out.println("ERROR OCCURED WHILE PULLING COURSES " + e.getMessage());
        }

        if(result == null){
            System.out.println("LOAD TRAINERS RESULT LIST IS EMPTY\n");
        }else{
            int len = trainer_times.size();
            int x = 0;
            for(ArrayList<Object> times:trainer_times){
                Trainer trainer = new Trainer(times.get(1).toString(),(ArrayList<String>) times.get(2),courseDate);
                trainers.add(trainer);
                System.out.println((ArrayList<String>) times.get(2));
            }
        }

        return trainers;
    }

}


//
//    public ArrayList<TrainerTemp> getTrainerNames() {
//        trainerNames.add(new TrainerTemp("Ege Uçak", "egeu", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Bahadır Adak", "baho", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Eyüpcan Bodur", "konyalı", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Berk Can Özen", "brkczn", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Serhat Sağlık", "mavi", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Sean Green", "seangreen", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Keegan Alvarado", "keeganalvarado", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Nguyen","oscarnguyen", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Carrillo","reginaldcarrillo", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Isabelle","Strickland", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Alaina","Willis", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Tiffani","Phillips", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Eugene","Miranda", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Brenna","Chandler", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Ryan","Wolfe", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Caitlyn","Clarke", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Caitlin","Lloyd", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Graham","Zimmerman", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Nathanael","Mclaughlin", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Dalton","Sanchez", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Esmeralda","Morales", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Blake","Lyons", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//        trainerNames.add(new TrainerTemp("Vincent","Garcia", "Lorem ipsum dolor sit amet, cetero commodo cum et, nam elit gubergren ex, cetero euripidis definitiones has"));
//
//        return trainerNames;
//    }

