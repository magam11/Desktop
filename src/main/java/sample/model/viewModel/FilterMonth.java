package sample.model.viewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterMonth {
    public static final String ALL = "All";
    public static final String JAN = "JAN";
    public static final String FEB = "FEB" ;
    public static final String MAR ="MAR";
    public static final String APR = "APR";
    public static final String MAY  = "MAY";
    public static final String JUNE = "JUNE";
    public static final String JULY = "JULY";
    public static final String AUG  = "AUG";
    public static final String SEPT = "SEPT";
    public static final String OCT = "OCT";
    public static final String NOV = "NOV";
    public static final String DEC = "DEC";

    public static List<String > list (){
        return new ArrayList<>(Arrays.asList(ALL,JAN,FEB,MAR,APR,MAY,JUNE,JULY,AUG,SEPT,OCT,NOV,DEC));
    }
}
