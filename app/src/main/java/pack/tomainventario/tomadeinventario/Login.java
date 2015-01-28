package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pack.tomainventario.tomadeinventario.DataBase.SBN090D;


public class Login extends Activity {
    private Button bIngresar;
    private EditText eUser,ePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bIngresar = (Button)findViewById(R.id.btn1);
        eUser = (EditText)findViewById(R.id.user);
        ePassword = (EditText)findViewById(R.id.password);

        //--- Configuraciones- Inicio
        ePassword.setTypeface(Typeface.DEFAULT);
        ePassword.setTransformationMethod(new PasswordTransformationMethod());
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        //--- Configuraciones- Fin

        bIngresar.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {

                SBN090D regUsuario = SBN090D.getLog(eUser.getText(), ePassword.getText());
                if(regUsuario == null)
                {
                    Toast.makeText(getApplicationContext(),"Usuario o clave incorrecto",Toast.LENGTH_LONG).show();
                    eUser.setText("");
                    ePassword.setText("");
                }
                else
                {
                    SharedPreferences prefs = getSharedPreferences("invPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putInt("Login", 1);
                    edit.apply();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings)return true;
        return super.onOptionsItemSelected(item);
    }
}
