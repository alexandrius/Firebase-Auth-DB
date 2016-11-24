package ge.bog.firebasetutorial.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.OnClick;
import ge.bog.firebasetutorial.K;
import ge.bog.firebasetutorial.R;

import static ge.bog.firebasetutorial.Utils.log;
import static ge.bog.firebasetutorial.Utils.showToast;

public class MainActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, FirebaseAuth.AuthStateListener {

    private static final int SIGN_IN_REQUEST_CODE = 11;

    @BindView(R.id.status_tv)
    protected TextView statusTV;
    @BindView(R.id.sign_in_button)
    protected Button signInButton;
    @BindView(R.id.sign_out_button)
    protected Button signOutButton;
    @BindView(R.id.start_chat_button)
    protected Button startChatButton;

    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildApiClient();
        initAuth();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    private void buildApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void initAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        log("onConnectionFailed:" + connectionResult);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_REQUEST_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        showProgress();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        log("signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            log("signInWithCredential " + task.getException());
                            showToast("ავტორიზაცია ვერ შესრულდა.");
                        }
                        hideProgress();
                    }
                });
    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            log("onAuthStateChanged: Signed In: " + user.getEmail());
        } else {
            log("onAuthStateChanged: Signed Out");
        }

        updateUI(user);
    }

    @OnClick(R.id.sign_in_button)
    protected void onSignInClick() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, SIGN_IN_REQUEST_CODE);
    }

    @OnClick(R.id.sign_out_button)
    protected void onSignOutClick() {
        firebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    @OnClick(R.id.start_chat_button)
    protected void onStartChatClick() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(K.USER_NAME, userName);
        startActivity(intent);
    }

    private void updateUI(FirebaseUser user) {
        hideProgress();

        if (user != null) {
            userName = user.getDisplayName();
            statusTV.setText("მომხმარებელი შესულია " + user.getEmail() + "\n" + userName);
            signInButton.setVisibility(View.GONE);
            startChatButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.VISIBLE);

        } else {
            statusTV.setText("");
            signInButton.setVisibility(View.VISIBLE);
            startChatButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.GONE);
        }
    }
}
