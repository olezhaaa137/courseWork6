package barbershop.web.admin;

import barbershop.entities.Timetable;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduleForm {

    public ScheduleForm(){
        this.timetables = new ArrayList<>();
        Timetable timetable1 = new Timetable();
        Timetable timetable2 = new Timetable();
        Timetable timetable3 = new Timetable();
        Timetable timetable4 = new Timetable();
        Timetable timetable5 = new Timetable();
        timetable1.setDay("Понедельник");
        timetable2.setDay("Вторник");
        timetable3.setDay("Среда");
        timetable4.setDay("Четверг");
        timetable5.setDay("Пятница");
        this.timetables.add(timetable1);
        this.timetables.add(timetable2);
        this.timetables.add(timetable3);
        this.timetables.add(timetable4);
        this.timetables.add(timetable5);
    }
    private List<Timetable> timetables;


}
