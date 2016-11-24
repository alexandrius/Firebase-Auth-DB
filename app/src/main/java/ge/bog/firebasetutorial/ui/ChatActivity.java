package ge.bog.firebasetutorial.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ge.bog.firebasetutorial.K;
import ge.bog.firebasetutorial.R;
import ge.bog.firebasetutorial.Utils;
import ge.bog.firebasetutorial.bean.Message;

/**
 * Created by alex on 11/24/2016
 */

public class ChatActivity extends BaseActivity {

    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().getRoot();
    private String userName;

    @BindView(R.id.message_et)
    protected EditText messageET;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userName = getIntent().getStringExtra(K.USER_NAME);

//        dbRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot d: dataSnapshot.getChildren()) {
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                Utils.log(dataSnapshot.getValue(Message.class).getMessageText());
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        FirebaseDatabase.getInstance().getReference().child(dataSnapshot.getKey()).removeValue();
//                    }
//                }, 3000);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_chat;
    }


    @OnClick(R.id.send_button)
    protected void onSendClick() {
        Message message = new Message(messageET.getText().toString(), userName);
        Map<String, Object> map = new ArrayMap<>();
        String key = dbRef.push().getKey();
        map.put(K.MESSAGE + key, message);
        dbRef.updateChildren(map);
        messageET.setText("");
    }
}
