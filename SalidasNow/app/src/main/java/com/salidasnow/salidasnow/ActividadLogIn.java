package com.salidasnow.salidasnow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
//import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class ActividadLogIn extends Activity implements LoaderCallbacks<Cursor> {

    boolean seObtuvoUnUsuario;
    //ManejadorDeBaseDeDatos accesoBD;
    SQLiteDatabase baseDatos;
    Usuarios usuarioTraido;
    Generics generics;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private View mPleaseWait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        generics=new Generics(ActividadLogIn.this);

        Usuarios usuarioResultado= new Usuarios();



        usuarioResultado=verificoUsuarioBD();
        if ( usuarioResultado != null)
        {
            setContentView(R.layout.pagina_principal);

            String url = "http://salidasnow.hol.es/obtener_usuario_byUsername.php?username=" + usuarioResultado.get_Username();
            Log.d("urlLogin", url);

            mAuthTask = new UserLoginTask(usuarioResultado.get_Username(), usuarioResultado.get_Password(), true);
            mAuthTask.execute(url);
        }
        else
        {
            setContentView(R.layout.actividad_log_in);



            // Set up the login form.
            mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);

            mPasswordView = (EditText) findViewById(R.id.password);


            Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
            Button mSignUpButton = (Button)findViewById(R.id.sign_up_button);


            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);
            mPleaseWait = findViewById(R.id.txPleaseWait);

            //mUsernameView
            //populateAutoComplete();

            //mPasswordView
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            //button
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                    InputMethodManager imm = (InputMethodManager) ActividadLogIn.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mUsernameView.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
                }
            });

            mSignUpButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent actividadSignUp= new Intent(ActividadLogIn.this,SignUpActivity.class);
                    startActivity(actividadSignUp);
                    finish();
                }
            });

            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);
        }
    }

    private Usuarios verificoUsuarioBD() {

        baseDatos=generics.AbroBaseDatos();
        if (baseDatos!=null) {
            Cursor conjuntoDeRegistros;
            conjuntoDeRegistros = baseDatos.rawQuery("select username, password from usuarios", null);

            if (!conjuntoDeRegistros.moveToFirst()) {

                return null;
            } else {
                Usuarios UsuarioEnBD = new Usuarios();

                UsuarioEnBD.set_Username(conjuntoDeRegistros.getString(0));
                UsuarioEnBD.set_Password(conjuntoDeRegistros.getString(1));

                return UsuarioEnBD;

            }

        }
        else
        {
            return null;
        }
    }
    /*private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }*/

   /* private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUsernameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }*/

    /**
     * Callback received when a permissions request has been completed.
     */
  /*  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }*/


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            String url = "http://salidasnow.hol.es/obtener_usuario_byUsername.php?username=" + username;
            Log.d("urlLogin", url);

            mAuthTask = new UserLoginTask(username, password,false);
            mAuthTask.execute(url);
        }
    }

    private boolean isUsernameValid(String username) {
        //TODO: Replace this with your own logic
        return username.length() > 2;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            mPleaseWait.setVisibility(show ? View.VISIBLE : View.GONE);
            mPleaseWait.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mPleaseWait.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mPleaseWait.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(ActividadLogIn.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUsernameView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<String, Void, ArrayList<Boolean>> {

        // private ProgressDialog dialog = new ProgressDialog(ActividadLogIn.this);
        private OkHttpClient client = new OkHttpClient();

        private final String mUsername;
        private final String mPassword;
        private final boolean mUsuarioLogueado;

        UserLoginTask(String email, String password, boolean usuarioLogueado) {
            mUsername = email;
            mPassword = password;
            mUsuarioLogueado = usuarioLogueado;
        }

        @Override
        protected ArrayList<Boolean> doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            ArrayList<Boolean> Resultado = new ArrayList<>(); /*Posicion 0 si hubo excepcion*
                                                            *Posicion 1 si esta bien el mail*
                                                            *Posicion 2 si está bien la contraseña**/

            Resultado.add(0, false);
            Resultado.add(1, false);
            Resultado.add(2, false);

            String url = params[0];

            usuarioTraido = new Usuarios();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                usuarioTraido = parsearResultado(response.body().string());
            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());         // Error de Network o al parsear JSON

                Resultado.set(0, true);
                Resultado.set(1, false);
                Resultado.set(2, false);
            }


         /*   try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {

            }*/

            if (seObtuvoUnUsuario) {
                if (usuarioTraido.get_Username().compareTo(mUsername)==0) {
                    // Account exists, return true if the password matches.
                    if (usuarioTraido.get_Password().compareTo(mPassword)==0) {
                        Resultado.set(0, false);
                        Resultado.set(1, true);
                        Resultado.set(2, true);
                    } else {
                        Resultado.set(0, false);
                        Resultado.set(1, true);
                        Resultado.set(2, false);
                    }
                }
            }
            else
            {
                Resultado.set(0, false);
                Resultado.set(1, false);
                Resultado.set(2, false);
            }

            // TODO: register the new account here.
            return Resultado;
        }


        @Override
        protected void onPostExecute(final ArrayList<Boolean> success) {
            mAuthTask = null;


            if (mUsuarioLogueado) {
                if (!success.get(0) && success.get(1) && success.get(2)) {
                    Intent ActividadDestino;
                    ActividadDestino = new Intent(ActividadLogIn.this, ActividadPrincipal.class);
                    ActividadDestino.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle paquete = new Bundle();
                    paquete.putSerializable("usuario", usuarioTraido);
                    ActividadDestino.putExtras(paquete);
                    startActivity(ActividadDestino);
                    finish(); // Call once you redirect to another activity
                }
                else
                {

                    setContentView(R.layout.actividad_log_in);

                    Toast.makeText(ActividadLogIn.this, "Ocurrió un error, ingrese nuevamente", Toast.LENGTH_SHORT).show();

                    //mUsernameView
                    //populateAutoComplete();

                    //mPasswordView
                    mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                                attemptLogin();
                                return true;
                            }
                            return false;
                        }
                    });
                }
            } else {
                showProgress(false);
                Log.d("resultados", "excepcion: " + success.get(0) + " email: " + success.get(1) + " pass: " + success.get(2));
                if (!success.get(0) && success.get(1) && success.get(2)) {

                    //PASAR DE ACTIVITY y GUARDAR EN BASE DE DATOS
                    guardoUsuarioBD(usuarioTraido.get_Username(), usuarioTraido.get_Password());

                    Intent ActividadDestino;
                    ActividadDestino = new Intent(ActividadLogIn.this, ActividadPrincipal.class);
                    ActividadDestino.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle paquete = new Bundle();
                    paquete.putSerializable("usuario", usuarioTraido);
                    ActividadDestino.putExtras(paquete);
                    startActivity(ActividadDestino);
                    finish();
                } else if (!success.get(0) && success.get(1) && !success.get(2)) {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                } else if (!success.get(0) && !success.get(1)) {
                    mUsernameView.setError(getString(R.string.error_invalid_username));
                    mUsernameView.requestFocus();
                }
            }
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        private Usuarios parsearResultado(String JSONstr) throws JSONException {
            Usuarios u = new Usuarios();
            JSONObject json = new JSONObject(JSONstr);                 // Convierto el String recibido a JSONObject
            //JSONArray jsonUsuarios;   // Array - una busqueda puede retornar varios resultados

            int estado = json.getInt("estado");

            Log.d("estado",estado+"");

            if (estado==2)
            {
               seObtuvoUnUsuario = false;
            }
            else {
                seObtuvoUnUsuario=true;
                //jsonUsuarios = json.getJSONArray("usuario");
                JSONObject jsonResultado = json.getJSONObject("usuario");
                String jsonUsuario = jsonResultado.getString("Username");
                String jsonPassword = jsonResultado.getString("Password");
                String jsonNombre = jsonResultado.getString("Nombre");
                String jsonApellido = jsonResultado.getString("Apellido");
                int jsonIdUsuario = jsonResultado.getInt("idUsuario");
                String jsonNombreImg = jsonResultado.getString("NombreImg");
                String jsonEmail = jsonResultado.getString("Email");
               // int jsonAutorizado = jsonResultado.getInt("Autorizado");

                Log.d("parsearResultado", "Usuario: " + jsonUsuario + " Contraseña: " + jsonPassword);

                u.set_Nombre(jsonNombre);
                u.set_Apellido(jsonApellido);
                u.set_idUsuario(jsonIdUsuario);
                u.set_NombreIMG(jsonNombreImg);
                u.set_Username(jsonUsuario);
                u.set_Password(jsonPassword);
                u.set_Email(jsonEmail);
              /*  if (jsonAutorizado == 1) {
                    u.set_Autorizado(true);
                } else {
                    u.set_Autorizado(false);
                }*/

            }
            return u;
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



