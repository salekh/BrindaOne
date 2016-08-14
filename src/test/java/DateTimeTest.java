import java.time.*;

/**
 * Created by Brindavan on 8/14/2016.
 */
public class DateTimeTest {

    public static void main(String[] args){

        LocalDate nowDate = LocalDate.now();
        LocalDate compareDate = nowDate.plusMonths(2);
        System.out.println(compareDate.toString());
        LocalDate otherDate = LocalDate.of(2017,Month.APRIL,01);
        /*
        LocalDate localDate1 = LocalDate.of(2016,Month.AUGUST,14);
        String lDate1 = localDate1.toString();
        System.out.println(lDate1);
        */
    }

}
