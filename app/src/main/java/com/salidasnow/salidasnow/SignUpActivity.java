package com.salidasnow.salidasnow;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    Usuarios usuarioARegistrar;
    SQLiteDatabase baseDatos;
    Generics generics;

    @InjectView(R.id.input_name)
    EditText _nameText;
    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_username)
    EditText _usernameText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.input_apellido)
    EditText _ApellidoText;
    @InjectView(R.id.btn_signup)
    Button _signupButton;
    @InjectView(R.id.link_login)
    TextView _loginLink;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        ButterKnife.inject(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent actividadLogin = new Intent(SignUpActivity.this, ActividadLogIn.class);
                startActivity(actividadLogin);
                finish();
            }
        });
        generics=new Generics(SignUpActivity.this);

    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        usuarioARegistrar = new Usuarios();
        _signupButton.setEnabled(false);

      /*  final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();*/

        String name = _nameText.getText().toString().trim();
        String apellido = _ApellidoText.getText().toString().trim();
        String email = _emailText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();
        String username = _usernameText.getText().toString().trim();

        usuarioARegistrar.set_Nombre(name);
        //usuarioARegistrar.set_Autorizado(false);
        usuarioARegistrar.set_Username(username);
        usuarioARegistrar.set_Password(password);
        usuarioARegistrar.set_Email(email);
        usuarioARegistrar.set_Apellido(apellido);
        usuarioARegistrar.set_NombreIMG("usuario1.png");

        // TODO: Implement your own signup logic here.
        senddatatoserver(usuarioARegistrar);
      /*  new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String username = _usernameText.getText().toString();
        String apellido = _ApellidoText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("Mas de 3 caracteres");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(getString(R.string.error_invalid_email));
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (username.isEmpty() || username.length() < 6 || username.length() > 18) {
            _usernameText.setError("6 y 18 caracteres");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (apellido.isEmpty() || apellido.length() < 1 || apellido.length()> 25 ) {
            _ApellidoText.setError("Mas de 6 caracteres");
            valid = false;
        } else {
            _ApellidoText.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
            _passwordText.setError("Entre 4 y 15 caracteres");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void senddatatoserver(Usuarios unUsuario) {
        //function in the activity that corresponds to the layout button
        JSONObject post_dict = new JSONObject();

        try {
            post_dict.put("Nombre", unUsuario.get_Nombre());
            post_dict.put("Apellido", unUsuario.get_Apellido());
            post_dict.put("Username", unUsuario.get_Username());
            post_dict.put("Password", unUsuario.get_Password());
            post_dict.put("NombreImg", unUsuario.get_NombreIMG());
            post_dict.put("Email", unUsuario.get_Email());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (post_dict.length() > 0) {
            new SendJsonDataToServer().execute(String.valueOf(post_dict));
        }
    }

     class SendJsonDataToServer extends AsyncTask<String, String, String>  {

         private ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
         @Override
         protected void onPreExecute() {
             this.dialog.setMessage("Please wait");
             this.dialog.show();
         }

        @Override
        protected String doInBackground(String... params){
            String JsonResponse = null;
            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://salidasnow.hol.es/insertar_usuario.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
//set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
// json data
                writer.close();
                InputStream inputStream = urlConnection.getInputStream();
//input stream
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                JsonResponse = buffer.toString();
//response data
                Log.i(TAG, JsonResponse);

//send to post execute

                    return JsonResponse;





            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            Log.d(TAG,s);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            int estado = 0; //Si es 1 se agregó bien, si ess 2 hubo error, si es 4 username repetido, 5 email repetido,6 ambos repetidos
            try
            {
                estado=parsearResultado(s);
            }
            catch (JSONException e)
            {
                Log.d("Error", e.getMessage());
                estado=10;
            }
            Log.d(TAG,"Estado: "+estado);
            switch (estado)
            {
                case 1:
                    guardoUsuarioBD(usuarioARegistrar.get_Username(),usuarioARegistrar.get_Password());
                    Intent ActividadDestino;
                    ActividadDestino = new Intent(SignUpActivity.this, ActividadPrincipal.class);
                    ActividadDestino.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle paquete = new Bundle();
                    paquete.putSerializable("usuario", usuarioARegistrar);
                    ActividadDestino.putExtras(paquete);
                    startActivity(ActividadDestino);
                    finish(); // Call once you redirect to another activity
                    break;
                case 2:
                    Toast.makeText(SignUpActivity.this, s, Toast.LENGTH_LONG).show();
                    _signupButton.setEnabled(true);
                    break;
                case 4:
                    _usernameText.setError("Username repetido");
                    _signupButton.setEnabled(true);
                    break;
                case 5:
                    _emailText.setError("Email repetido");
                    _signupButton.setEnabled(true);
                    break;
                case 6:
                    _usernameText.setError("Username repetido");
                    _emailText.setError("Email repetido");
                    _signupButton.setEnabled(true);
                    break;
                case 10:
                    Toast.makeText(SignUpActivity.this, s , Toast.LENGTH_SHORT).show();
                    _signupButton.setEnabled(true);
                    break;
                default:
                    Log.d(TAG, "Algo salio mal en el switch.");
            }


        }

         public int parsearResultado(String JSONstr) throws JSONException
         {
             JSONObject json=new JSONObject(JSONstr);

             return json.getInt("estado");
         }
    }
    private void guardoUsuarioBD(String username, String password)
    {
        baseDatos=generics.AbroBaseDatos();
        if (baseDatos!=null)
        {
            ContentValues nuevoUsuario;

            nuevoUsuario=new ContentValues();
            nuevoUsuario.put("username", username);
            nuevoUsuario.put("password",password);
            baseDatos.insert("usuarios",null,nuevoUsuario);

            baseDatos.close();
        }
    }
}


