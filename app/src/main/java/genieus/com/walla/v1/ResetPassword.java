package genieus.com.walla.v1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import genieus.com.walla.R;

public class ResetPassword extends AppCompatActivity {

    Button reset;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        reset = (Button) findViewById(R.id.reset);
        email = (EditText) findViewById(R.id.enter_email);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em = email.getText().toString();

                if(isValid(em)) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(em);
                    onResetSend(em);
                }
            }
        });
    }

    private boolean isValid(String em){
        if(em == null)
            return false;

        if(em.equals(""))
            return false;

        if(!em.toLowerCase().endsWith("@duke.edu"))
            return false;

        return true;
    }

    private void onResetSend(String em){
        Toast.makeText(this, "Password reset link send to " + em, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
