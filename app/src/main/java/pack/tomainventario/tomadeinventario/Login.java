package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pack.tomainventario.tomadeinventario.DataBase.SBN053D;
import pack.tomainventario.tomadeinventario.DataBase.SBN090D;


public class Login extends Activity {
    private Button bIngresar;
    private EditText eUser,ePassword;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;

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

                //SBN090D regUsuario = SBN090D.getLog(eUser.getText(), ePassword.getText());
                Log.e("FICHA", "El user es "+ eUser.getText() + " y el passw es "+ ePassword.getText());
                if(SBN090D.getLog(eUser.getText(),ePassword.getText()) == null)
                {
                    Toast.makeText(getApplicationContext(),"Usuario o clave incorrecto",Toast.LENGTH_LONG).show();
                    eUser.setText("");
                    ePassword.setText("");
                }
                else
                {
                    prefs = getSharedPreferences("invPreferences", Context.MODE_PRIVATE);
                    edit = prefs.edit();


                    if(SBN090D.getUser(eUser.getText()).ficha.equals(SBN053D.getAll().get(0).fichaUa)){
                        edit.putInt("Login", 2);
                        Log.e("FICHA", "Dice q es la misma ficha de 53 "+ prefs.getInt("Login",0));
                    }
                    else{
                        edit.putInt("Login", 1);
                        Toast.makeText(getApplicationContext(),"Usuario invitado",Toast.LENGTH_LONG).show();
                        Log.e("FICHA", "Dice q NO es la misma ficha de 53 "+ prefs.getInt("Login",0));
                    }

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
