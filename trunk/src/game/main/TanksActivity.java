package game.main;
import game.graphics.*;
import game.tanks.R;
import android.app.Activity;
import android.os.Bundle;
import java.io.*;
public class TanksActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
    }
}